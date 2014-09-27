/**
 * Copyright    : Copyright (c) 2006. Wintim Corp. All rights reserved
 * File Summary : 
 * Create time  : 2013-5-13
 */

package Rsme.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author wang yuliang
 *
 */
public class ItemEvaluation {

	private List<TopNBasedRecommenderMeasure> m_measures;
	
	public ItemEvaluation() {
		m_measures = new ArrayList<TopNBasedRecommenderMeasure>();
	}
	
	public ItemEvaluation(List<TopNBasedRecommenderMeasure> measures) {
		if (measures == null) {
			throw new NullPointerException("measures");
		}
		m_measures = Collections.unmodifiableList(measures);
	}

	/**
	 * 以字符串的形式返回评价结果 
	 * @return a string with the results of the evaluation
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (TopNBasedRecommenderMeasure measure : m_measures)
		{
			sb.append(measure);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	/**
	 * 以CSV的格式返回评价结果
	 * 
	 * @return the CSV representation of the calculated measures
	 */
	public String toCSV()
	{
		StringBuilder sb = new StringBuilder();
		for (TopNBasedRecommenderMeasure measure : m_measures)
		{
			double value = Double.NaN;
			try
			{
				value = measure.getValue();
			}
			catch (Exception ex)
			{
			}
			sb.append(String.format("%.4f", value));
			sb.append(";");
		}
		return sb.toString();
	}
	
	
	/**
	 * 返回该评价结果包含的所有measures
	 * @return the evaluation measures
	 */
	public List<TopNBasedRecommenderMeasure> getMeasures()
	{
		return m_measures;
	}
}
