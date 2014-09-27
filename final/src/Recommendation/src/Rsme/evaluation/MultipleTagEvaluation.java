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
 *    MultipleEvaluation.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Rsme.evaluation.measure.TagRecommenderMeasure;

/**
 * Simple class that includes an array, whose elements are lists of evaluation
 * evaluations. Used to compute means and standard deviations of multiple
 * evaluations (e.g. cross-validation)
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */
public class MultipleTagEvaluation
{
	protected ArrayList<TagEvaluation> m_evaluations;
	protected HashMap<String, Double> m_mean;
	protected HashMap<String, Double> m_standardDeviation;


	public MultipleTagEvaluation()
	{
		this.m_evaluations = new ArrayList<TagEvaluation>();
	}


	/**
	 * Constructs a new object with given array of evaluations
	 * 
	 * @param data
	 *          the evaluation data used for obtaining label names for per
	 *          outputting per label values of macro average measures
	 * @param someEvaluations
	 */
	public MultipleTagEvaluation(TagEvaluation[] someEvaluations)
	{
		m_evaluations = new ArrayList<TagEvaluation>();
		m_evaluations.addAll(Arrays.asList(someEvaluations));
	}


	/**
	 * Adds an evaluation results to the list of evaluations
	 * 
	 * @param evaluation
	 *          an evaluation result
	 */
	public void addEvaluation(TagEvaluation evaluation)
	{
		m_evaluations.add(evaluation);
	}


	/**
	 * Computes mean and standard deviation of all evaluation measures
	 */
	public void calculateStatistics()
	{
		int size = m_evaluations.size();
		HashMap<String, Double> sums = new HashMap<String, Double>();

		// calculate sums of measures
		for (int i = 0; i < m_evaluations.size(); i++)
		{
			for (TagRecommenderMeasure m : m_evaluations.get(i).getMeasures())
			{
				double value = Double.NaN;
				value = m.getValue();
				if (sums.containsKey(m.getName()))
				{
					sums.put(m.getName(), sums.get(m.getName()) + value);
				}
				else
				{
					sums.put(m.getName(), value);
				}

			}
		}
		m_mean = new HashMap<String, Double>();
		for (String measureName : sums.keySet())
		{
			m_mean.put(measureName, sums.get(measureName) / size);
		}

		// calculate sums of squared differences from mean
		sums = new HashMap<String, Double>();
		for (int i = 0; i < m_evaluations.size(); i++)
		{
			for (TagRecommenderMeasure m : m_evaluations.get(i).getMeasures())
			{
				double value = Double.NaN;
				value = m.getValue();
				if (sums.containsKey(m.getName()))
				{
					sums.put(
							m.getName(),
							sums.get(m.getName())
									+ Math.pow(value - m_mean.get(m.getName()), 2));
				}
				else
				{
					sums.put(m.getName(), Math.pow(value - m_mean.get(m.getName()), 2));
				}

			}
		}
		m_standardDeviation = new HashMap<String, Double>();
		for (String measureName : sums.keySet())
		{
			m_standardDeviation.put(measureName,
					Math.sqrt(sums.get(measureName) / size));
		}
	}


	/**
	 * Returns a string with the results of the evaluation
	 * 
	 * @return a string with the results of the evaluation
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (TagRecommenderMeasure m : m_evaluations.get(0).getMeasures())
		{
			String measureName = m.getName();
			sb.append(measureName);
			sb.append(": ");
			sb.append(String.format("%.4f", m_mean.get(measureName)));
			sb.append("\u00B1");
			sb.append(String.format("%.4f", m_standardDeviation.get(measureName)));
			sb.append("\n");
		}
		return sb.toString();
	}


	/**
	 * Returns a CSV string representation of the results
	 * 
	 * @return a CSV string representation of the results
	 */
	public String toCSV()
	{
		StringBuilder sb = new StringBuilder();
		for (TagRecommenderMeasure m : m_evaluations.get(0).getMeasures())
		{
			String measureName = m.getName();
			sb.append(String.format("%.4f", m_mean.get(measureName)));
			sb.append("\u00B1");
			sb.append(String.format("%.4f", m_standardDeviation.get(measureName)));
			sb.append(";");
		}
		return sb.toString();
	}

}