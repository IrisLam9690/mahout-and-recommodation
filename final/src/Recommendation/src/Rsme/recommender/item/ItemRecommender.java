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
 *    ItemRecommender.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.recommender.item;

import java.util.Arrays;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import Rsme.evaluation.TagEvaluator;
import Rsme.model.DataModel;
import Rsme.recommender.tag.TagRecommender;
import Rsme.utils.OptionHandler;
import Rsme.utils.SerializedObject;
import Rsme.utils.Utils;

/**
 * this is a abstract class that define the common members and interfaces for
 * any particular class the implement certain item recommendation method
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$
 * @date 22/10/2012
 */
public abstract class ItemRecommender implements OptionHandler
{
	
	/** the number of itmes should be recommended to users */
	protected int m_numRecommendations =20;
	
	/** debug option */
	protected boolean m_debug = false;
	
	
	/**
	 * set the number of items need be recommended
	 * 
	 * @param num
	 *          the number of items be recommended
	 */
	public void setNumRecommendations(int num)
	{
		m_numRecommendations = num;
	}
	
	
	/**
	 * build the recommender using given training dataset
	 * 
	 * @param dataModel
	 *          training dataset
	 * @throws Exception
	 */
	public abstract void buildRecommender(DataModel dataModel) throws Exception;
	
	
	/**
	 * predict the ratings of all items assigned by the user
	 * 
	 * @param userId
	 *          user's Id
	 * @return
	 * @throws Exception
	 */
	public abstract RatingItem[] ratingsForItems(int userId) throws Exception;
	
	
	/**
	 * recommend items for given user!
	 */
	public int[] recommendItems(int userId) throws Exception
	{
		RatingItem[] results = ratingsForItems(userId);
		results = sortRatingItems(results);
		
		int[] recommended = new int[m_numRecommendations];
		for (int i = 0; i < m_numRecommendations ; i++)
		{
			if (results.length <= m_numRecommendations) {
				recommended[i] =  -1;
			} else {
				recommended[i] = results[i].getId();
				System.err.println(results[i].getId() + "," + results[i].getRating());
			}
		}
		
		return recommended;
	}
	
	
	/**
	 * sort the items in descending order, by their ratings
	 * 
	 * @param items
	 */
	protected RatingItem[] sortRatingItems(RatingItem[] items)
	{
		Arrays.sort(items);
		return items;
	}
	
	
	/**
	 * return the debug profile
	 * 
	 * @return
	 */
	public boolean getDebug()
	{
		return m_debug;
	}
	
	
	/**
	 * set the debug profile
	 * 
	 * @param debug
	 */
	public void setDebug(boolean debug)
	{
		m_debug = debug;
	}
	
	
	public String[] getOptions()
	{
		return null;
		
	}
	
	
	/**
	 * set the given options
	 */
	public void setOptions(String[] options) throws Exception
	{
		setDebug(Utils.getFlag("D", options));
		
		String numrecommendations = Utils.getOption("n", options);
		if (numrecommendations.length() != 0)
			setNumRecommendations(Integer.parseInt(numrecommendations));
	}
	
	
	/**
	 * make a copy of this object
	 */
	public ItemRecommender makecopy() throws Exception
	{
		return (ItemRecommender) new SerializedObject(this).getObject();
	}
	
	/**
	 * runs the recommender instance with the given options.
	 * 
	 * @param recommender
	 *          the recommender to run
	 * @param options
	 *          the commandline options
	 */
	public static void runRecommender(ItemRecommender recommender, String[] options)
	{
		try
		{
			//TagEvaluator evaluator = new TagEvaluator();
			//System.out.println(evaluator.evaluateModel(recommender, options));
		}
		catch (Exception e)
		{
			if (((e.getMessage() != null) && (e.getMessage().indexOf(
					"General options") == -1))
					|| (e.getMessage() == null))
				e.printStackTrace();
			else
				System.err.println(e.getMessage());
		}
	}
}
