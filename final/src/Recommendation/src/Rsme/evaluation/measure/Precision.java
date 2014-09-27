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
 *    Precision.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.evaluation.measure;

/**
 * ����ʵ����precision���۱�׼
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public class Precision extends TagRecommenderMeasure
{
	/*------------------------constructor--------------------------------------*/
	public Precision()
	{
		super("precision");
	}


	/*------------------------public functions----------------------------------*/
	@Override
	/**
	 * ���ظ����۱�׼���ܵ�����ֵ
	 * 
	 * @return
	 */
	public double getIdealValue()
	{
		return 1;
	}


	@Override
	/**
	 * ���ݶԵ�ǰitem��Ԥ��tag,�������۱�׼��ȡֵ
	 * 
	 * @param truths
	 *          item����ʵtag����
	 * @param predictedrank
	 *          �Ƽ��㷨Ԥ���tag��������
	 */
	public void update(int[] truths, int[] predictedrank)
	{
		int numTruths = truths.length;
		int numPredictions = predictedrank.length;

		double sum = 0;
		for (int i = 0; i < numPredictions; i++)
		{
			int id = predictedrank[i];
			for (int j = 0; j < numTruths; j++) {
				if (id == truths[j]) {
					sum += 1;
				}
			}
		}

		m_counts++;
		m_sums = m_sums + sum / numPredictions;
	}


	@Override
	/**
	 * ���ظ����۱�׼��ȡֵ
	 */
	public double getValue()
	{
		return m_sums / m_counts;
	}
}
