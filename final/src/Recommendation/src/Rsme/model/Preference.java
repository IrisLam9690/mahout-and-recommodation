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
 *    Preference.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

/**
 * class that represent a user's preference on a item. It's a abstract class
 * that does not specify the format of preference, such as rating, like/dislike,
 * or tags.
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public abstract class Preference
{
	
	/*---------------------------members---------------------------------------*/
	/** user's id */
	protected int m_userId;
	
	/** item's id */
	protected int m_itemId;
	
	
	/*------------------------constructor--------------------------------------*/
	/**
	 * default constructor, all the ids are set to 0
	 */
	public Preference()
	{
		m_userId = 0;
		m_itemId = 0;
	}
	
	
	/**
	 * constructor, set the ids with specific values
	 * 
	 * @param userId
	 *          use's id
	 * @param itemId
	 *          item's id
	 */
	public Preference(int userId, int itemId)
	{
		m_userId = userId;
		m_itemId = itemId;
	}
	
	
	/*------------------------public functions----------------------------------*/
	/**
	 * get the user's id
	 * 
	 * @return user's id
	 */
	public int getUerId()
	{
		return m_userId;
	}
	
	
	/**
	 * get the user's id
	 * 
	 * @param userId
	 *          user's id
	 */
	public void setUserId(int userId)
	{
		m_userId = userId;
	}
	
	
	/**
	 * get the item's id
	 * 
	 * @return items'id
	 */
	public int getItemId()
	{
		return m_itemId;
	}
	
	
	/**
	 * set the items'id
	 * 
	 * @param itemId
	 *          item's id
	 */
	public void setItemId(int itemId)
	{
		m_itemId = itemId;
	}
	
	
	/**
	 * print the preference in the format like "user's id, item's id"
	 */
	public String toString()
	{
		String result = "user: " + m_userId + "item: " + m_itemId;
		return result;
	}
	/*------------------------end class-----------------------------------------*/
}
