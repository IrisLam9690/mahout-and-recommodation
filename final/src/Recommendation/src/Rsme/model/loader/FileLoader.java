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
 *    FileLoader.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model.loader;

import java.io.FileNotFoundException;

public abstract class FileLoader
{
	/*---------------------------members---------------------------------------*/
	/**
	 * the source file that contains the user's information, such as id, name,
	 * gender etc.
	 */
	protected String m_userFile = null;
	
	/**
	 * the source file that contains the item's information, such as id, name,
	 * gender etc.
	 */
	protected String m_itemFile = null;
	
	/**
	 * the source file that contains the preferences data, each preference is a
	 * triple<user, item, tag> or <user, item, rating>
	 */
	protected String m_preferencesFile = null;
	
	/** the delimiter used for separate the elements in a preference */
	protected String m_delimiter;
	
	
	/*------------------------constructor--------------------------------------*/
	/**
	 * default constructor, all files are set to null
	 */
	public FileLoader()
	{
		m_userFile = null;
		m_itemFile = null;
		m_preferencesFile = null;
		m_delimiter = "\t";
	}
	
	
	public void setSource(String preferencesFile) throws FileNotFoundException
	{
		setSource(preferencesFile, null, null);
	}
	
	
	public void setSource(String preferencesFile, String userFile,
			String itemFile) throws FileNotFoundException
	{
		m_preferencesFile = preferencesFile;
		m_userFile = userFile;
		m_itemFile = itemFile;
	}
	
	
	public void setDelimiter(String delimiter)
	{
		m_delimiter = delimiter;
	}
}
