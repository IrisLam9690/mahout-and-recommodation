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
 *    Folksonomy.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Rsme.math.Matrix;
import Rsme.math.SparseMatrix;

/**
 * class that represents users' tag preference for items.
 * 
 * author Bin Fu (0911@bjtu.edu.cn)
 * version $Revision: 0.1$
 */

public class Folksonomy
												extends DataModel
{
	/*---------------------------members---------------------------------------*/
	/** all the tags appears in the folksonomy */
	protected List<Tag> m_tags;
	
	/**
	 * a map that store every tag's index when it's loaded in memory, key of the
	 * map is tag's id, and the value is its index
	 */
	protected Map<Integer, Integer> m_tagIndex;
	
	/**
	 * the specific preferences of all users, each preference indicates a uses
	 * uses which tag to denote a item. So the format of preference is<userId,
	 * itemId, tagId>
	 */
	protected List<TagPreference> m_preferences;
	
	/**
	 * store the preference as users' profiles
	 */
	protected UserTagProfiles m_userProfiles;
	
	
	/*------------------------constructor--------------------------------------*/
	/**
	 * default constructor
	 */
	public Folksonomy()
	{
		super();
		m_tags = new ArrayList<Tag>();
		m_tagIndex = new LinkedHashMap<Integer, Integer>();
		m_preferences = new ArrayList<TagPreference>();
		m_userProfiles = new UserTagProfiles();
	}
	
	
	/**
	 * copy the values of another data model, without the preferences
	 * 
	 * @param model
	 */
	public Folksonomy(Folksonomy model)
	{
		this(model, 0);
	}
	
	
	/**
	 * copy the values of another data model, and certain number of its preference
	 * 
	 * @param model
	 * @param number
	 */
	public Folksonomy(Folksonomy model, int number)
	{
		super(model);
		
		/* copy the tags */
		m_tags = new ArrayList<Tag>();
		int size = model.m_tags.size();
		for (int i = 0; i < size; i++)
		{
			m_tags.add(new Tag(model.m_tags.get(i)));
		}
		
		/* copy the correlation of tag's id and index */
		m_tagIndex = new LinkedHashMap<Integer, Integer>();
		Set<Integer> keys = model.m_tagIndex.keySet();
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext())
		{
			Integer key = iterator.next();
			m_tagIndex.put(key.intValue(), model.m_tagIndex.get(key).intValue());
		}
		
		/* copy the preferences */
		m_preferences = new ArrayList<TagPreference>();
		m_userProfiles = new UserTagProfiles();
		
		List<TagPreference> preferences = model.getPreferences();
		Iterator<TagPreference> preferenceIterator = preferences.iterator();
		
		while (preferenceIterator.hasNext() && number > 0)
		{
			TagPreference preference = preferenceIterator.next();
			addPreference(preference.getUerId(), preference.getItemId(),
					preference.getTagId());
			number--;
		}
	}
	
	
	/*------------------------public functions----------------------------------*/
	/**
	 * add a preference into memory
	 * 
	 * @param userId
	 *          user's id
	 * @param itemId
	 *          item's id
	 * @param tagId
	 *          tag's id
	 */
	public void addPreference(int userId, int itemId, int tagId)
	{
		
		TagPreference newPreference = new TagPreference(userId, itemId, tagId);
		m_preferences.add(newPreference);
		m_userProfiles.addPreference(userId, itemId, tagId);
	}
	
	
	/**
	 * add a tag in this data model
	 * 
	 * @param id
	 *          tag's id
	 * @param name
	 *          tag
	 */
	public void addTag(Tag tag)
	{
		if (m_tagIndex.containsKey(tag.getId()))
			return;
		else
		{
			m_tags.add(tag);
			m_tagIndex.put(tag.getId(), m_tags.size() - 1);
		}
	}
	
	
	/**
	 * return a specific preference, given its index in memory
	 * 
	 * @param index
	 *          the preference's index
	 * @return a specific preference
	 */
	public TagPreference getPreference(int index)
	{
		return m_preferences.get(index);
	}
	
	
	/**
	 * get all the preferences
	 * 
	 * @return
	 */
	public List<TagPreference> getPreferences()
	{
		return this.m_preferences;
	}
	
	
	/**
	 * return a tag's id, given its index used in memory
	 * 
	 * @param index
	 *          the tag's index in memory
	 * @return tag's id store in exteranl file
	 */
	public int getTagByIndex(int index)
	{
		if (index < 0 || index >= numTags())
			return -1;
		else
			return m_tags.get(index).getId();
	}
	
	
	public int getTagIndex(int tagId)
	{
		if (!m_tagIndex.containsKey(tagId))
			return -1;
		else
			return m_tagIndex.get(tagId).intValue();
	}
	
	
	/**
	 * return all of the users'id
	 * 
	 * @return
	 */
	public List<Integer> getUsers()
	{
		return this.m_users;
	}
	
	
	/**
	 * return a user's profile
	 * 
	 * @param userId
	 *          user's Id
	 * @return the uers's profile
	 */
	public UserTagProfile getUserProfile(int userId)
	{
		return m_userProfiles.getUserProfile(userId);
	}
	
	
	/**
	 * return the number of tags
	 * 
	 * @return number of tags
	 */
	public int numTags()
	{
		return m_tags.size();
	}
	
	
	/**
	 * 得到交叉验证的训练数据
	 * 
	 * @param numFolds
	 * @param numFold
	 * @return
	 */
	public Folksonomy trainCV(int numFolds, int numFold, Random random)
	{
		/* create a new folksonomy, but the users' profile is set to be empty */
		Folksonomy train = new Folksonomy(this);
		
		/* numfold must be no more less 2 */
		if (numFolds < 2)
		{
			throw new IllegalArgumentException("Number of folds must be at least 2!");
		}
		
		/* the number of each user's items shoudl be more than numFolds */
		if (numFolds > m_userProfiles.minNumItems())
		{
			throw new IllegalArgumentException(
					"Can't have more folds than tagged items!");
		}
		
		/* for each user, split its annotated items into numfolds subset */
		Iterator<Integer> iterator = m_users.iterator();
		int userId;
		int numItems;
		int numItemsForFold, first, offset;
		while (iterator.hasNext())
		{
			/* get current user'id and his profile */
			userId = iterator.next().intValue();
			UserTagProfile profile = m_userProfiles.getUserProfile(userId);
			
			/* randomize the annotated items */
			profile.randomize(random);
			
			/* determine the first item and the number of items to be copied */
			numItems = profile.numItems();
			numItemsForFold = numItems / numFolds;
			if (numFold < (numItems % numFolds))
			{
				numItemsForFold++;
				offset = numFold;
			}
			else
				offset = numItems % numFolds;
			first = numFold * (numItems / numFolds) + offset;
			
			/* copy the specific profiles to create new folksonomy */
			AnnotatedItem item;
			for (int i = 0; i < first; i++)
			{
				item = profile.getAnnotatedItem(i);
				int itemId = item.getItemId();
				ArrayList<Integer> tags = item.getTags();
				for (int j = 0; j < tags.size(); j++)
				{
					int tagId = tags.get(j);
					train.addPreference(userId, itemId, tagId);
				}
			}
			
			for (int i = first + numItemsForFold; i < numItems; i++)
			{
				item = profile.getAnnotatedItem(i);
				int itemId = item.getItemId();
				ArrayList<Integer> tags = item.getTags();
				for (int j = 0; j < tags.size(); j++)
				{
					int tagId = tags.get(j);
					train.addPreference(userId, itemId, tagId);
				}
			}
		}
		
		return train;
	}
	
	
	/**
	 * 得到交叉验证的测试数据
	 * 
	 * @param numFolds
	 * @param numFold
	 * @param random
	 * @return
	 */
	public Folksonomy testCV(int numFolds, int numFold, Random random)
	{
		
		/* create a new folksonomy, but the users' profile is set to be empty */
		Folksonomy test = new Folksonomy(this);
		
		/* numfold must be no more less 2 */
		if (numFolds < 2)
		{
			throw new IllegalArgumentException("Number of folds must be at least 2!");
		}
		
		/* the number of each user's items shoudl be more than numFolds */
		if (numFolds > m_userProfiles.minNumItems())
		{
			throw new IllegalArgumentException(
					"Can't have more folds than tagged items!");
		}
		
		/* for each user, split its annotated items into numfolds subset */
		Iterator<Integer> iterator = m_users.iterator();
		int userId;
		int numItems;
		int numItemsForFold, first, offset;
		while (iterator.hasNext())
		{
			/* get current user'id and his profile */
			userId = iterator.next().intValue();
			UserTagProfile profile = m_userProfiles.getUserProfile(userId);
			
			/* randomize the annotated items */
			profile.randomize(random);
			
			/* determine the first item and the number of items to be copied */
			numItems = profile.numItems();
			numItemsForFold = numItems / numFolds;
			if (numFold < (numItems % numFolds))
			{
				numItemsForFold++;
				offset = numFold;
			}
			else
				offset = numItems % numFolds;
			first = numFold * (numItems / numFolds) + offset;
			
			/* copy the specific profiles to create new folksonomy */
			AnnotatedItem item;
			for (int i = first; i < first + numItemsForFold; i++)
			{
				item = profile.getAnnotatedItem(i);
				int itemId = item.getItemId();
				ArrayList<Integer> tags = item.getTags();
				for (int j = 0; j < tags.size(); j++)
				{
					int tagId = tags.get(j);
					test.addPreference(userId, itemId, tagId);
				}
			}
		}
		
		return test;
	}
	
	
	/*------------------------unimplemented functions--------------------------*/
	@Override
	public Matrix userItemCorrelation()
	{
		/* create a matrix to store the cooccurences of different users and items */
		Matrix UI_Matrix = new SparseMatrix(numUsers(), numTags());
		
		// Set<Integer> userKeys=this.m_userProfiles.
		
		return UI_Matrix;
	}
	
	
	@Override
	public Matrix itemsCorrelation()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public UserTagProfiles getUserprofiles()
	{
		// TODO Auto-generated method stub
		return m_userProfiles;
	}
}