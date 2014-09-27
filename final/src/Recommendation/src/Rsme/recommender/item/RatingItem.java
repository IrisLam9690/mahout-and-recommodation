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
 *    RatingItem.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.recommender.item;

/**
 * this class reresent the output of the item recommender. the output's format
 * is <itemId, rating>
 * 
 * @author bin fu
 * 
 */

public class RatingItem implements Comparable<RatingItem>
{

	/**the item's Id*/
	protected int m_itemId;
	
	/**the predicted rating for the item*/
	protected double m_rating;


	/**
	 * constructor
	 * @param itemId
	 * @param rating
	 */
	public RatingItem(int itemId, double rating)
	{
		m_itemId = itemId;
		m_rating = rating;
	}
	
	public int getId()
	{
		return m_itemId;
	}
	
	
	public void setId(int id)
	{
		m_itemId=id;
	}
	
	
	public double getRating()
	{
		return m_rating;
	}
	
	
	public void setRating(double rating)
	{
		m_rating=rating;
	}
	
	
	public String toString()
	{
		String result=new String("itemId: "+m_itemId+"rating: "+m_rating);
		return result;
	}

	@Override
	public int compareTo(RatingItem o) {
		if (m_rating > o.getRating()) {
			return -1;
		} else if (m_rating < o.getRating()) {
			return 1;
		} else {
			return 0;
		}
	}

}
