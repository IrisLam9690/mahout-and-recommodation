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
 *    TagPreference.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

/**
 * class that represent a user's preference on a item. the format of the
 * preference is a triple<userId, itemId, tagId>
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public class TagPreference
													extends Preference
{
	/*---------------------------members---------------------------------------*/
	/** tag's id */
	protected int m_tagId;
	
	
	/*------------------------constructor--------------------------------------*/
	/**
	 * default constructor, all the ids are set to 0
	 */
	public TagPreference()
	{
		super();
		m_tagId = 0;
	}
	
	
	/**
	 * constructor, set the ids with specific values
	 * 
	 * @param userId
	 *          use's id
	 * @param itemId
	 *          item's id
	 * @param tagId
	 *          tag's id
	 */
	public TagPreference(int userId, int itemId, int tagId)
	{
		super(userId, itemId);
		m_tagId = tagId;
	}
	
	
	/*------------------------public functions---------------------------------*/
	/**
	 * get the tag's id
	 * 
	 * @return
	 */
	public int getTagId()
	{
		return m_tagId;
	}
	
	
	/**
	 * set the tag's id
	 * 
	 * @param tagId
	 *          given tag's id
	 */
	public void setTagId(int tagId)
	{
		m_tagId = tagId;
	}
	
	
	/**
	 * print the preference in the format like "user's id, item's id, tag's id"
	 */
	public String toString()
	{
		String result = "user:" + " " + m_userId + " " + "item:" + "" + m_itemId
				+ " " + "tag:" + " " + m_tagId;
		
		return result;
	}
}
