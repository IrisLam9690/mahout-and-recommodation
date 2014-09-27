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
 *    RatingDataModel.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import Rsme.math.Matrix;
import Rsme.math.SparseMatrix;

/**
 * class that represents users' rating preference for items.
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$
 */

public class RatingDataModel extends DataModel
{
	/*---------------------------members---------------------------------------*/
	/*
	 * the specific preferences of all users, each preference indicates a rating
	 * that a user gives to a item. So the format of preference is<userId, itemId,
	 * rating>
	 */

	/** 从item的角度记录其preference */
	protected LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> m_itemsRating;

	/** 从user的角度记录其preference */
	protected LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> m_usersRating;


	/*------------------------constructor--------------------------------------*/
	/**
	 * default constructor
	 */
	public RatingDataModel()
	{
		super();
		m_itemsRating = new LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>();
		m_usersRating = new LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>();
	}


	/*------------------------public functions----------------------------------*/
	/**
	 * add a new preference of a user
	 * 
	 * @param userId
	 *          user's Id
	 * @param itemId
	 *          item's Id
	 * @param rating
	 *          the rating value
	 */
	public void addPreference(int userId, int itemId, double rating)
	{
		/* add into the itemsRating */
		if (m_itemsRating.containsKey(itemId))
		{
			m_itemsRating.get(itemId).put(userId, rating);
		}
		else
		{
			LinkedHashMap<Integer, Double> ratings = new LinkedHashMap<Integer, Double>();
			ratings.put(userId, rating);
			m_itemsRating.put(itemId, ratings);
		}

		/* add into the userssRating */
		if (m_usersRating.containsKey(userId))
		{
			m_usersRating.get(userId).put(itemId, rating);
		}
		else
		{
			LinkedHashMap<Integer, Double> ratings = new LinkedHashMap<Integer, Double>();
			ratings.put(itemId, rating);
			m_usersRating.put(userId, ratings);
		}
	}


	/**
	 * get the user's ratings for a set of items
	 * 
	 * @param id
	 *          user's Id
	 * @return
	 */
	public LinkedHashMap<Integer, Double> getUserRatings(int id)
	{
		return m_usersRating.get(id);
	}


	/**
	 * get the all the ratings a item get from users
	 * 
	 * @param id
	 *          item's Id
	 * @return
	 */
	public LinkedHashMap<Integer, Double> getItemRatings(int id)
	{
		return m_itemsRating.get(id);
	}


	@Override
	/**
	 * compute the relationship between items
	 */
	public Matrix itemsCorrelation() throws Exception
	{
		int numNodes = numItems();
		Matrix result = new SparseMatrix(numNodes, numNodes);

		for (int i = 0; i < numNodes - 1; i++)
		{
			/* 得到当前的item和对其评过分的user集合 */
			int leftItemId = getItemByIndex(i);
			Set<Integer> leftkeys = m_itemsRating.get(leftItemId).keySet();
			result.set(i, i, leftkeys.size());

			/* 循环，依次计算当前item的user集合与后面的item的user集合的交集的大小 */
			for (int j = i + 1; j < numNodes; j++)
			{
				int rightItemId = getItemByIndex(j);
				Set<Integer> rightkeys = m_itemsRating.get(rightItemId).keySet();

				/* 计算交集大小并保持在矩阵的相应位置中 */
				int num = numshared(leftkeys, rightkeys);
				result.set(i, j, num);
				result.set(j, i, num);
			}
		}

		return result;
	}


	/*------------------------protected functions------------------------------*/
	/**
	 * return the number of elements that contained in both of the two sets
	 * 
	 * @param left
	 *          the first set of elements
	 * @param right
	 *          the second set of elements
	 * @return the number of shared elements
	 */
	protected int numshared(Set<Integer> left, Set<Integer> right)
	{
		int result = 0;
		Iterator<Integer> leftIterator = left.iterator();
		int leftValue;
		while (leftIterator.hasNext())
		{
			leftValue = leftIterator.next().intValue();
			Iterator<Integer> rightIterator = right.iterator();
			int rightValue;
			while (rightIterator.hasNext())
			{
				rightValue = rightIterator.next().intValue();
				if (leftValue == rightValue)
				{
					result++;
					break;
				}
			}
		}

		return result;
	}


	/*------------------------unimplemented functions--------------------------*/
	@Override
	public Matrix userItemCorrelation()
	{
		return null;
	}
}
