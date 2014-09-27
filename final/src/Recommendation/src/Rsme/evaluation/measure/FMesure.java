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
 *    FMesure.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.evaluation.measure;

/**
 * 该类实现了fmeasure评价标准
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public class FMesure extends TagRecommenderMeasure
{
	/*------------------------constructor--------------------------------------*/
	public FMesure()
	{
		super("FMesure");
	}


	/*------------------------public functions----------------------------------*/
	@Override
	/**
	 * 返回该评价标准可能的最优值
	 * 
	 * @return
	 */
	public double getIdealValue()
	{
		return 1;
	}


	@Override
	/**
	 * 根据对当前item的预测tag,更新评价标准的取值
	 * 
	 * @param truths
	 *          item的真实tag集合
	 * @param predictedrank
	 *          推荐算法预测的tag有序排列
	 */
	public void update(int[] truths, int[] predictedrank)
	{
		int numTruths = truths.length;
		int numPredictions = predictedrank.length;

		double sum = 0;
		for (int i = 0; i < numTruths; i++) {
			int trueTag = truths[i];
			for (int j = 0; j < numPredictions; j++) {
				if (predictedrank[j] == trueTag) {
					sum += 1.0;
					break;
				}
			}
		}

		double precision = sum / numPredictions;
		double recall = sum / numTruths;
		m_counts++;
		if ((recall + precision) != 0) {
			m_sums = m_sums + 2 * recall * precision / (recall + precision);
		}
	}


	@Override
	/**
	 * 返回评价标准的当前取值
	 * 
	 * @return
	 */
	public double getValue()
	{
		return m_sums / m_counts;
	}
}
