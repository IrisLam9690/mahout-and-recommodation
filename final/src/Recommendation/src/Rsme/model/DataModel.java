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
 *    DataModel.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Rsme.math.Matrix;

/**
 * class that represents the related data used for training recommender.
 * generally, the data consist of users' information, item's information, and
 * users' preferences for particular items.
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public abstract class DataModel
{
	/*---------------------------members---------------------------------------*/
	/** the sets of users */
	protected List<Integer> m_users;
	
	/** the sets of items */
	protected List<Integer> m_items;
	
	/**
	 * a map that store every user's index when it's loaded in memory, key of the
	 * map is user's id, and the value is its index
	 */
	protected Map<Integer, Integer> m_userIndex;
	
	/**
	 * a map that store every item's index when it's loaded in memory, key of the
	 * map is item's id, and the value is its index
	 */
	protected Map<Integer, Integer> m_itemIndex;
	
	
	/*------------------------constructor--------------------------------------*/
	/**
	 * default constructor
	 */
	public DataModel()
	{
		m_users = new ArrayList<Integer>();
		m_items = new ArrayList<Integer>();
		m_userIndex = new LinkedHashMap<Integer, Integer>();
		m_itemIndex = new LinkedHashMap<Integer, Integer>();
	}
	
	
	/**
	 * constructor, that copy the values of another data model
	 * 
	 * @param model
	 */
	public DataModel(DataModel model)
	{
		/*copy the users*/
		m_users = new ArrayList<Integer>();
		Iterator<Integer> iterator = model.m_users.iterator();
		while (iterator.hasNext())
		{
			m_users.add(iterator.next().intValue());
		}
		
		/*copy the items*/
		m_items = new ArrayList<Integer>();
		iterator = model.m_items.iterator();
		while (iterator.hasNext())
		{
			m_items.add(iterator.next().intValue());
		}
		
		/*copy the users' index*/
		m_userIndex = new LinkedHashMap<Integer, Integer>();
		Set<Integer> keys = model.m_userIndex.keySet();
		iterator = keys.iterator();
		while (iterator.hasNext())
		{
			Integer key = iterator.next();
			m_userIndex.put(key.intValue(), model.m_userIndex.get(key).intValue());
		}
		
		/*copy the items' index*/
		m_itemIndex = new LinkedHashMap<Integer, Integer>();
		keys = model.m_itemIndex.keySet();
		iterator = keys.iterator();
		while (iterator.hasNext())
		{
			Integer key = iterator.next();
			m_itemIndex.put(key.intValue(), model.m_itemIndex.get(key).intValue());
		}
	}
	
	
	/*------------------------public functions---------------------------------*/
	/**
	 * add a user into this data model
	 * 
	 * @param userId
	 *          the user's id
	 */
	public void addUser(int userId)
	{
		if (m_userIndex.containsKey(userId))
			return;
		else
		{
			m_users.add(userId);
			m_userIndex.put(userId, m_users.size() - 1);
		}
	}
	
	
	/**
	 * add a item into this data model
	 * 
	 * @param itemId
	 *          the item's id
	 */
	public void addItem(int itemId)
	{
		if (m_itemIndex.containsKey(itemId))
			return;
		else
		{
			m_items.add(itemId);
			m_itemIndex.put(itemId, m_items.size() - 1);
		}
	}
	
	
	/**
	 * return the user's index in memory
	 * 
	 * @param userId
	 *          user's id
	 * @return user's index in memory, if the user does not exist, return -1
	 */
	public int getUserIndex(int userId)
	{
		if (m_userIndex.containsKey(userId))
		{
			return m_userIndex.get(userId).intValue();
		}
		else
			return -1;
		
	}
	
	
	/**
	 * return the item's index in memory
	 * 
	 * @param item
	 *          id
	 * @return item's index in memory
	 */
	public int getItemIndex(int itemId)
	{
		if (m_itemIndex.containsKey(itemId))
		{
			return m_itemIndex.get(itemId).intValue();
		}
		else
			return -1;
	}
	
	
	/**
	 * return a user'Id, give its index used in memory
	 * 
	 * @param index
	 *          the item's index
	 * @return
	 */
	public int getUserByIndex(int index)
	{
		if (index < 0 || index >= numUsers())
			return -1;
		else
			return m_users.get(index).intValue();
	}
	
	
	/**
	 * return a item'Id, give its index used in memory
	 * 
	 * @param index
	 *          the item's index
	 * @return
	 */
	public int getItemByIndex(int index)
	{
		if (index < 0 || index >= numItems())
			return -1;
		return m_items.get(index).intValue();
	}
	
	
	/**
	 * return the total number of users
	 * 
	 * @return
	 */
	public int numUsers()
	{
		return m_users.size();
	}
	
	
	/**
	 * return the total number of items
	 * 
	 * @return
	 */
	public int numItems()
	{
		return m_items.size();
	}
	/*------------------------abstract functions need implemented--------------*/
	public abstract Matrix userItemCorrelation();
	
	public abstract Matrix itemsCorrelation() throws Exception;
}
