/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    ItemRanker.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.recommender.item;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Rsme.math.DoubleVector;
import Rsme.math.Matrix;
import Rsme.model.DataModel;
import Rsme.model.RatingDataModel;
import Rsme.model.loader.MovielensRatingDataLoader;

/**
 * this class implements the folkrank method for tag recommendation
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */
public class ItemRanker
												extends ItemRecommender
{
	/*------------------------------members------------------------------------*/
	/** the data set usd to train the recommender */
	protected RatingDataModel m_ratingData;
	
	/** the matrix used to denote correlation between items */
	private Matrix m_matrix;
	
	/** the weight of the matrix */
	private double m_a = 0.85;
	
	/** the number of total items */
	private int m_numItems;
	
	/** the weights for all items */
	private double[] m_weights;
	
	/** user's perference for all items */
	private double[] m_preference;
	
	private int m_numiteration = 200;
	
	
	/*------------------------constructor--------------------------------------*/
	public ItemRanker()
	{
		m_a = 0.85;
		m_numiteration = 200;
	}
	
	
	/*------------------------public functions----------------------------------*/
	@Override
	/**
	 * build the recommendation model, using given training dataset 
	 */
	public void buildRecommender(DataModel dataModel) throws Exception
	{
		m_ratingData = (RatingDataModel) dataModel;
		m_matrix = m_ratingData.itemsCorrelation();
		m_matrix.rowNormalization();
		
		m_numItems = m_ratingData.numItems();
		
		/* the initialize weights for each item */
		m_weights = new double[m_numItems];
		for (int i = 0; i < m_numItems; i++)
			m_weights[i] = 1 / m_numItems;
	}
	
	
	@Override
	/**
	 * for a specific user, predict its ratings for all items
	 */
	public RatingItem[] ratingsForItems(int userId) throws Exception
	{
		/* the specific ratings for all the items of the user */
		m_preference = new double[m_numItems];
		for (int i = 0; i < m_numItems; i++)
			m_preference[i] = 0;
		
		Map<Integer, Double> userRatings = m_ratingData.getUserRatings(userId);
		Set<Integer> keys = userRatings.keySet();
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext())
		{
			int itemId = iterator.next().intValue();
			double rating = userRatings.get(itemId).doubleValue();
			int itemIndex = m_ratingData.getItemIndex(itemId);
			m_preference[itemIndex] = rating;
		}
		DoubleVector.normalize(m_preference);
		
		for (int i = 0; i < m_numiteration; i++)
		{
			DoubleVector vector = new DoubleVector(m_weights);
			m_weights = m_matrix.leftMultiplyVector(vector).toDoubleArray();
			for (int j = 0; j < m_numItems; j++)
			{
				m_weights[j] = m_a * m_weights[j] + (1 - m_a) * m_preference[j];
			}
		}
		
		RatingItem[] results = new RatingItem[m_numItems];
		for (int i = 0; i < m_numItems; i++)
		{
			int itemId = m_ratingData.getItemByIndex(i);
			results[i] = new RatingItem(itemId, m_weights[i]);
		}
		
		return results;
	}
	
	/**
	 * set the number of iterations
	 * @param num  nuber of iterations
	 */
	public void setNumInteration(int num)
	{
		m_numiteration = num;
	}
	
	
	/**
	 *  main function 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		MovielensRatingDataLoader loader = new MovielensRatingDataLoader();
		loader.setSource("D:\\temps\\1.txt");
		RatingDataModel data = loader.getRatingData();
		
		ItemRanker ranker = new ItemRanker();
		ranker.buildRecommender(data);
		int[] values = ranker.recommendItems(6);
		
		for (int i = 0; i < values.length; i++)
			System.out.println(values[i]);
	}
	
}
