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
 *    TagEvaluation.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.evaluation;

/**
 * �����ʾ�Ա�ǩ�Ƽ��㷨һ�����۵Ľ�����ɶ�����۱�׼��ֵ��ɣ���recall, precision F1��
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Rsme.evaluation.measure.TagRecommenderMeasure;

public class TagEvaluation
{
	/** a list of measures for tag recommendation algorithms */
	private List<TagRecommenderMeasure> m_measures;
	
	
	/**
	 * ȱʡ���캯�������Ϊÿ�����۱�׼�����ڴ棬����δָ�����۱�׼
	 */
	public TagEvaluation()
	{
		m_measures = new ArrayList<TagRecommenderMeasure>();
	}
	
	
	/**
	 * ���캯����ָ���˾�������۱�׼
	 * @param measures
	 */
	public TagEvaluation(List<TagRecommenderMeasure> measures)
	{
		m_measures = new ArrayList<TagRecommenderMeasure>();
		Iterator<TagRecommenderMeasure> iterator = measures.iterator();
		while (iterator.hasNext())
		{
			m_measures.add(iterator.next());
		}
	}
	
	
	/**
	 * ���ַ�������ʽ�������۽�� 
	 * @return a string with the results of the evaluation
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (TagRecommenderMeasure measure : m_measures)
		{
			sb.append(measure);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	/**
	 * ��CSV�ĸ�ʽ�������۽��
	 * 
	 * @return the CSV representation of the calculated measures
	 */
	public String toCSV()
	{
		StringBuilder sb = new StringBuilder();
		for (TagRecommenderMeasure measure : m_measures)
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
	 * ���ظ����۽������������measures
	 * @return the evaluation measures
	 */
	public List<TagRecommenderMeasure> getMeasures()
	{
		return m_measures;
	}
}
