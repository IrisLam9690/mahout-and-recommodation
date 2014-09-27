/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 */

/*
 * MovielensTagDataLoader.java Copyright (C) 2012 Beijing jiaotong University, Beijing,
 * China
 */

package Rsme.model.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import Rsme.model.Folksonomy;
import Rsme.model.Tag;

/**
 * MovielensDataLoader.该类用于读取movielens格式的数据
 * 
 * @author fubin
 * 
 */
public class MovielensTagDataLoader
																		extends TagFileLoader
{
	
	/*------------------------constructor--------------------------------------*/
	public MovielensTagDataLoader()
	{
		super();
	}
	
	
	public MovielensTagDataLoader(String preferencesFile, String tagsFile)
			throws FileNotFoundException
	{
		this(preferencesFile, tagsFile, "\t");
	}
	
	
	public MovielensTagDataLoader(String preferencesFile, String tagsFile,
			String delimiter) throws FileNotFoundException
	{
		m_preferencesFile = preferencesFile;
		m_tagsFile = tagsFile;
		m_delimiter = delimiter;
	}
	
	
	/*------------------------public functions----------------------------------*/
	/**
	 * read the preferences from file, and create a data model to hold them
	 * 
	 * @throws Exception
	 */
	public Folksonomy getFolksonomy() throws Exception
	{
		Folksonomy result = new Folksonomy();
		
		/* if these files are not open successfully, throw a exception */
		if (m_preferencesFile == null || m_tagsFile == null)
			throw new FileNotFoundException("file open fail!");
		
		/* the data model will be generated */
		BufferedReader folksonomyReader = new BufferedReader(new FileReader(
				m_preferencesFile));
		
		String[] attributes = null;
		int userId;
		int itemId;
		int tagId;
		Set<Integer> tags = new TreeSet<Integer>();
		
		/* omit the first line */
		String preference = folksonomyReader.readLine();
		
		/* read the file line by line, until the end */
		while ((preference = folksonomyReader.readLine()) != null)
		{
			/* get the user's id, item's id, and tag's id */
			attributes = preference.split("\t");
			userId = Integer.parseInt(attributes[0]);
			itemId = Integer.parseInt(attributes[1]);
			tagId = Integer.parseInt(attributes[2]);
			
			/* add the user and item into the data model */
			result.addUser(userId);
			result.addItem(itemId);
			
			/* add the preference into the datt model */
			result.addPreference(userId, itemId, tagId);
			
			/* store the tag id for now */
			tags.add(tagId);
		}
		folksonomyReader.close();
		
		/*------------------读取tags相关信息-------------------*/
		BufferedReader tagsReader = new BufferedReader(new FileReader(m_tagsFile));
		
		/* omit the first line */
		String tag = tagsReader.readLine();
		
		int id;
		String name;
		
		/* read the file line by line, until the end */
		while ((tag = tagsReader.readLine()) != null)
		{
			/* get the tag's id and name */
			attributes = tag.split("\t");
			id = Integer.parseInt(attributes[0]);
			name = attributes[1];
			
			/*
			 * if the tag has appeared in any perference, than add it into the data
			 * model, otherwise ignore it
			 */
			if (tags.contains(id))
			{
				result.addTag(new Tag(id, name));
			}
		}
		tagsReader.close();
		
		return result;
	}
	
	
	/* for test */
	public static void main(String[] args) throws Exception
	{
		MovielensTagDataLoader loader = new MovielensTagDataLoader(
				"D:\\temps\\a.txt", "D:\\temps\\tags.txt");
		Folksonomy folk = loader.getFolksonomy();
		System.out.println(folk.numUsers());
		System.out.println(folk.numItems());
		System.out.println(folk.numTags());
		/*
		 * for (int i = 0; i < 1000; i++) System.out.println(folk.preference(i));
		 */
		Folksonomy train = folk.testCV(5, 4, new Random(1));
		for (int i = 0; i < 18; i++)
			System.out.println(train.getPreference(i));
	}
}
