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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * class that represent all user's profile, user's profile is all the items he
 * has tagged,and all the relevant tags.
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */
public class UserTagProfiles
{
	/*---------------------------members---------------------------------------*/
	/** store all the users' profiles */
	protected LinkedHashMap<Integer, UserTagProfile> m_profiles;
	
	
	/*---------------------------constructor---------------------------------------*/
	/**
	 * default constructor
	 */
	public UserTagProfiles()
	{
		m_profiles = new LinkedHashMap<Integer, UserTagProfile>();
	}
	
	
	/*------------------------public functions----------------------------------*/
	/**
	 * add a new preference, a preference is a triple like <userid, itemid, tagId>
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
		/* if this user does not have any preference yet */
		if (!containUser(userId))
		{
			UserTagProfile profile = new UserTagProfile(userId);
			profile.addPreference(itemId, tagId);
			m_profiles.put(userId, profile);
		}
		else
		/* if this user already exists, get its profile */
		{
			m_profiles.get(userId).addPreference(itemId, tagId);
		}
		
	}
	
	
	/**
	 * examine if a specific user exists or not
	 * 
	 * @param userId
	 *          user's id
	 * @return true if the user exist, false otherwise
	 */
	public boolean containUser(int userId)
	{
		if (m_profiles.containsKey(userId))
			return true;
		else
			return false;
	}
	
	
	/**
	 * get a user's profile, given his id
	 * 
	 * @param userId
	 *          user's id
	 * @return
	 */
	public UserTagProfile getUserProfile(int userId)
	{
		if (containUser(userId))
			return m_profiles.get(userId);
		else
			return null;
	}
	
	
	/**
	 * return the list of items a user has tagged
	 * 
	 * @param userId
	 *          user's id
	 * @return a list of items'id, or null if user does not exist
	 */
	public ArrayList<Integer> getTaggedItems(int userId)
	{
		if (containUser(userId))
		{
			return getUserProfile(userId).getAnnotatedItems();
		}
		
		else
			return null;
	}
	
	
	/**
	 * return the smallest number of tagged items of all users
	 * 
	 * @return
	 */
	public int minNumItems()
	{
		int minNums = Integer.MAX_VALUE;
		int size;
		Set<Integer> keys = m_profiles.keySet();
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext())
		{
			Integer key = iterator.next();
			size = m_profiles.get(key).numItems();
			if (size < minNums)
				minNums = size;
		}
		
		return minNums;
	}
	
	
	/**
	 * return the number of items the user has tagged
	 * 
	 * @param userId
	 *          user's id
	 * @return
	 */
	public int numItems(int userId)
	{
		if (containUser(userId))
		{
			return m_profiles.get(userId).numItems();
		}
		else
			return 0;
	}
}
