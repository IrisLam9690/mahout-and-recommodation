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
 *    Recall.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.evaluation.measure;

/**
 * 该类实现了recall评价标准
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public class Recall extends TagRecommenderMeasure
{
	/*------------------------constructor--------------------------------------*/
	public Recall()
	{
		super("recall");
	}


	/*------------------------public functions----------------------------------*/
	@Override
	public double getIdealValue()
	{
		return 1;
	}


	@Override
	public void update(int[] truths, int[] predictedrank)
	{
		int numTruths = truths.length;
		int numPredictions = predictedrank.length;

		double sum = 0;
		for (int i = 0; i < numTruths; i++) {
			int trueTag = truths[i];
			for (int j = 0; j < numPredictions; j ++) {
				if (predictedrank[j] == trueTag) {
					sum += 1.0;
					break;
				}
			}
		}

		m_counts++;
		
		m_sums = m_sums + sum / numTruths;
	}


	@Override
	public double getValue()
	{
		return m_sums / m_counts;
	}
}
