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
 *    RatingPreference.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

/**
 * class that represent a user's rating on a item. the format of the preference
 * is a triple<userId, itemId, rating>
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$
 * @date 22/10/2012
 */
public class RatingPreference
															extends Preference
{
	/*---------------------------members---------------------------------------*/
	/**a real value, indicating the user's preference for that item*/
	protected double m_rating;
	
	
	/*------------------------constructor--------------------------------------*/
	/**
	 * default constructor, no specific value is set
	 */
	public RatingPreference()
	{
		super();
		m_rating = 0;
	}
	
	/**
	 * constructor, set the user's and item's id and rating with specific values
	 * 
	 * @param userId
	 *          use's id
	 * @param itemId
	 *          item's id
	 * @param rating
	 *          a real value represents the preference degree
	 */
	public RatingPreference(int userId, int itemId, double rating)
	{
		super(userId, itemId);
		m_rating = rating;
	}
	
	
	/*------------------------public functions---------------------------------*/
	/**
	 * get the rating
	 * @return
	 */
	public double getRating()
	{
		return m_rating;
	}
	
	/**
	 * 
	 * @param rating 
	 */
	public void setRating(double rating)
	{
		m_rating = rating;
	}
	
	/**
	 * print the prefernce
	 */
	public String toString()
	{
		String result = "user:" + " " + m_userId + " " + "item:" + "" + m_itemId
				+ " " + "rating:" + " " + m_rating;
		
		return result;
	}
}
