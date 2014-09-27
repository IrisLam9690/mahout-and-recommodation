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
 *    UserTagProfile.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

/**
 * a class represents a uer's profile. user's profile refer to which item he/she
 *  has annotated, and use which tags
 *  
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class UserTagProfile
{
	/*---------------------------members---------------------------------------*/
	/**
	 * user's id
	 */
	private int m_userId;
	
	/**
	 * all the items and their relevant tags, refereed by id
	 */
	private ArrayList<AnnotatedItem> m_items;
	
	/**
	 * store each item's index in m_items, the key is item' id, and the value is
	 * its index in m_items
	 */
	private HashMap<Integer, Integer> m_itemIndex;
	
	
	/*---------------------------constructor-----------------------------------*/
	/**
	 * constructor
	 * 
	 * @param userId
	 */
	public UserTagProfile(int userId)
	{
		m_userId = userId;
		m_items = new ArrayList<AnnotatedItem>();
		m_itemIndex = new HashMap<Integer, Integer>();
	}
	
	
	/*---------------------------public functions------------------------------*/
	/**
	 * add a preference
	 * 
	 * @param userId
	 *          user' id
	 * @param itemId
	 *          item's id
	 * @param tagId
	 *          tag's id
	 */
	public void addPreference(int itemId, int tagId)
	{
		
		/*
		 * 如果user还没有对这个item添加过tag, 则针对该item新生成一个AnnotatedItem类对象， 用以保存其相关的tag标签
		 */
		if (!containItem(itemId))
		{
			AnnotatedItem newItem = new AnnotatedItem(itemId);
			newItem.addTag(tagId);
			m_items.add(newItem);
			m_itemIndex.put(itemId, m_items.size() - 1);
		}
		
		/* 否则就找到和这个item相关的AnnotatedItem类对象,并将tag添加进去 */
		else
		{
			int index = m_itemIndex.get(itemId);
			m_items.get(index).addTag(tagId);
		}
	}
	
	
	/**
	 * check if user has tagged a specific item
	 * 
	 * @param itemId
	 *          item's id
	 * @return true if the user has assigned a tag to the item, false otherwise
	 */
	public boolean containItem(int itemId)
	{
		if (m_itemIndex.containsKey(itemId))
			return true;
		else
			return false;
	}
	
	
	/**
	 * get the item and it's tags by specifying its Id
	 * 
	 * @param itemId
	 *          item's id
	 * @return return a annotatedItem object, or return null if not find the item
	 */
	public AnnotatedItem getItem(int itemId)
	{
		
		if (containItem(itemId))
			return m_items.get(itemId);
		else
			return null;
	}
	
	
	/**
	 * get user's Id
	 * 
	 * @return user's Id
	 */
	public int getUserId()
	{
		return m_userId;
	}
	
	
	/**
	 * get the specific annotated items, given its index in memory
	 * 
	 * @param index
	 * @return
	 */
	public AnnotatedItem getAnnotatedItem(int index)
	{
		return m_items.get(index);
	}
	
	
	/**
	 * get all the items'Id to which the user has assigned tags
	 * 
	 * @return all the items' Id
	 */
	public ArrayList<Integer> getAnnotatedItems()
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		int size = this.m_items.size();
		for (int i = 0; i < size; i++)
		{
			result.add(m_items.get(i).getItemId());
		}
		
		return result;
	}
	
	
	public HashSet<Integer> getItemsId()
	{
		HashSet<Integer> set = new HashSet<Integer>();
		for (Integer index : m_itemIndex.keySet()) {
			set.add(index);
		}
		return set;
	}
	
	/**
	 * get the number of items the user has annotated already
	 * 
	 * @return the number of items the user has annotated already
	 */
	public int numItems()
	{
		return m_items.size();
	}
	
	
	/**
	 * randomize the items, mainly for cross validation
	 */
	public void randomize(Random random)
	{
		int size = this.numItems();
		for (int j = size - 1; j > 0; j--)
		{
			int swapped = random.nextInt(j + 1);
			AnnotatedItem current = m_items.get(j);
			AnnotatedItem target = m_items.get(swapped);
			
			/* select another item randomly, and swap them */
			m_items.set(j, target);
			m_items.set(swapped, current);
			
			/* swap the stored index */
			m_itemIndex.put(current.getItemId(), swapped);
			m_itemIndex.put(target.getItemId(), j);
		}
	}
	/*------------------------end class-----------------------------------------*/
}
