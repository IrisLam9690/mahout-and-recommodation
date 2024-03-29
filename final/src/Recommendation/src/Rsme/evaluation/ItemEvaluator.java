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
 *    ItemEvaluator.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.evaluation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import Rsme.evaluation.measure.FMesure;
import Rsme.model.DataModel;
import Rsme.model.Folksonomy;
import Rsme.model.UserTagProfile;
import Rsme.model.loader.MovielensTagDataLoader;
import Rsme.recommender.item.ItemRecommender;
import Rsme.recommender.item.PRBC_Hierarchy;
import Rsme.recommender.item.RatingItem;
import Rsme.recommender.tag.AbstractTagRecommender;
import Rsme.recommender.tag.TagRecommender;
import Rsme.utils.Utils;

/**
 * <code>ItemEvaluator</code>is used for evaluate for item recommend alogorithme
 * @author wang yuliang
 *
 */
public class ItemEvaluator
{
	
	/*---------------------------members---------------------------------------*/
	/** seed for reproduction of cross-validation results */
	private int m_seed = 1;
	
	
	/*---------------------------public functions------------------------------*/
	/**
	 * Sets the seed for reproduction of cross-validation results
	 * 
	 * @param seed
	 *          seed for reproduction of cross-validation results
	 */
	public void setSeed(int seed)
	{
		m_seed = seed;
	}
	
	
	/**
	 * check the recommender is valid or not
	 * 
	 * @param recommender
	 *          the recommender need to be evaluated
	 */
	protected void checkLearner(ItemRecommender recommender)
	{
		if (recommender == null)
		{
			throw new IllegalArgumentException("Learner to be evaluated is null.");
		}
	}
	
	
	/**
	 * check the data used to evaluate recommender is valid or not
	 * 
	 * @param data
	 */
	protected void checkData(DataModel data)
	{
		if (data == null)
		{
			throw new IllegalArgumentException("Evaluation data object is null.");
		}
	}
	
	
	/**
	 * prepare the measures used to evaluate the algorithms
	 * 
	 * @return
	 */
	protected List<TopNBasedRecommenderMeasure> prepareMeasures()
	{
		List<TopNBasedRecommenderMeasure> measures = new ArrayList<TopNBasedRecommenderMeasure>();
		measures.add(new FMeasureForItem("fmeasure for item alogrithem"));
		//add precision for item
		measures.add(new PrecisionForItem("precision for item algorithem"));
		//add recall for item
		measures.add(new RecallForItem("recall for item algorithem"));
		return measures;
	}
	
	/**
	 * evaluate a particular algorithm using the test data
	 * 
	 * @param recommender
	 * @param testData
	 * @param numRecommendations
	 * @return
	 * @throws Exception
	 */
	public ItemEvaluation evaluate(ItemRecommender recommender, Folksonomy testData)
			throws Exception
	{
		/* check the recommender and data is valid or not */
		checkLearner(recommender);
		checkData(testData);
		
		/* reset the measures */
		List<TopNBasedRecommenderMeasure> measures = prepareMeasures();
		for (TopNBasedRecommenderMeasure m : measures)
		{
			m.reset();
		}
		
		/* for each user and each item in the test data, calculate all them measures */
		int[] truths;
		Iterator<Integer> users = testData.getUsers().iterator();
		recommender.buildRecommender(testData);
		
		while (users.hasNext())
		{
			/* get the user's id and his profile */
			int userId = users.next().intValue();
			UserTagProfile profile = testData.getUserProfile(userId);

			//predict items
			int[] ratingItems = recommender.recommendItems(userId);
			int[] predictItem = new int[ratingItems.length];
			int j = 0;
			for (int ratingItem : ratingItems) {
				predictItem[j ++] = ratingItem;
			}
			
			//truth items
			Set<Integer> truth = profile.getItemsId();
			truths = new int[truth.size()];
			int i = 0;
			for (Integer id : truth) {
				truths[i ++] = id;
			}
			
			
			for (TopNBasedRecommenderMeasure measure : measures) {
				measure.update(truths, predictItem);
			}
		}
		
		/* return the result of the evaluation */
		ItemEvaluation evaluation = new ItemEvaluation(measures);
		return evaluation;
	}
	
	
	/**
	 * implement the n-cross validation on the recommender
	 * 
	 * @param recommender
	 *          the recommender need to be evaluated
	 * @param data
	 *          the data used for cross validation
	 * @param folds
	 *          the folds of the cross validation
	 * @return
	 * @throws Exception
	 */
	public MultipleTagEvaluation crossValidate(TagRecommender recommender,
			Folksonomy data, String[] options) throws Exception
	{
//		checkLearner(recommender);
//		checkData(data);
//		
//		
//		String seed = Utils.getOption("s", options);
//		if (seed.length() != 0)
//			m_seed = Integer.parseInt(seed);
//		
//		String foldsNum = Utils.getOption("x", options);
//		int folds;
//		if (foldsNum.length() != 0)
//			folds = Integer.parseInt(foldsNum);
//		else
//			folds = 3;
//		
//		/* n个评价结果，对应于n重交叉验证 */
//		TagEvaluation[] evaluation = new TagEvaluation[folds];
//		
//		/* 依次运行第i重交叉验证，每一个都分别训练recommender并在训练集上得到一个评价结果 */
//		for (int i = 0; i < folds; i++)
//		{
//			try
//			{
//				/* 第i重交叉验证的训练集合 */
//				Folksonomy train = data.trainCV(folds, i, new Random(m_seed));
//				
//				/* 第i重交叉验证的测试集合 */
//				Folksonomy test = data.testCV(folds, i, new Random(m_seed));
//				
//				/* 训练一个recommender并评价 */
//				AbstractTagRecommender model = (AbstractTagRecommender) recommender
//						.makecopy();
//				model.setOptions(options);
//				model.buildRecommender(train);
//				evaluation[i] = evaluate(model, test);
//			}
//			catch (Exception ex)
//			{
//				Logger.getLogger(TagEvaluator.class.getName()).log(Level.SEVERE, null, ex);
//			}
//		}
//		
//		MultipleTagEvaluation me = new MultipleTagEvaluation(evaluation);
//		return me;
		return null;
	}
	
	
	/**
	 * 提供了对推荐算法进行评价的功能
	 * 
	 * @param recommender
	 * @param options
	 * @return
	 * @throws Exception
	 */
	public String evaluateModel(TagRecommender recommender, String[] options)
			throws Exception
	{
//		StringBuilder result = new StringBuilder();
//		String preferenceFileName = Utils.getOption("p", options);
//		String tagFileName = Utils.getOption("t", options);
//		if (preferenceFileName.length() == 0 || tagFileName.length() == 0)
//			throw new Exception("file not found!");
//		MovielensTagDataLoader loader = new MovielensTagDataLoader(
//				preferenceFileName, tagFileName);
//		Folksonomy data = loader.getFolksonomy();
//		
//		String testFileName = Utils.getOption("T", options);
//		if (testFileName.length() == 0)
//		{
//			MultipleTagEvaluation evaluations = crossValidate(recommender, data, options);
//			result.append(evaluations.toString());
//		}
//		
//		else
//		{
//			AbstractTagRecommender model = (AbstractTagRecommender) recommender
//					.makecopy();
//			model.setOptions(options);
//			model.buildRecommender(data);
//			MovielensTagDataLoader testloader = new MovielensTagDataLoader(
//					testFileName, tagFileName);
//			Folksonomy testData = testloader.getFolksonomy();
//			TagEvaluation evaluation = evaluate(recommender, testData);
//			result.append(evaluation.toString());
//		}
//		return result.toString();
		return null;
	}
	/*---------------------------end of class-----------------------------------*/
}
