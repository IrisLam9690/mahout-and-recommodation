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
 *    AnnotatedItem.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * class that represents a item's all tags give by a specific user
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public class AnnotatedItem
{
	/*---------------------------members---------------------------------------*/
	/** item's id */
	protected int m_itemId;
	
	/** all the tags(represented by its id) used to annotate this item by one user */
	protected HashSet<Integer> m_tags;
	
	
	/*------------------------constructor--------------------------------------*/
	/**
	 * constructor, allocate the space, given a specific item's id
	 * 
	 * @param itemId
	 */
	public AnnotatedItem(int itemId)
	{
		m_itemId = itemId;
		m_tags = new HashSet<Integer>();
	}
	
	
	/**
	 * get the item's id
	 * 
	 * @return
	 */
	public int getItemId()
	{
		return m_itemId;
	}
	
	
	/**
	 * annotate this item use a new tag
	 * 
	 * @param tagId
	 */
	public void addTag(int tagId)
	{
		m_tags.add(tagId);
	}
	
	
	/**
	 * return all the tags in a list
	 * 
	 * @return tag list
	 */
	public ArrayList<Integer> getTags()
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		Iterator<Integer> iterator = m_tags.iterator();
		while (iterator.hasNext())
		{
			result.add(iterator.next());
		}
		return result;
	}
	
	
	public int numTags()
	{
		return m_tags.size();
	}
}
