/**
 * Copyright    : Copyright (c) 2006. Wintim Corp. All rights reserved
 * File Summary : 
 * Create time  : 2013-5-13
 */

package Rsme.evaluation;

/**
 * <code>PrecisionForItem</code> is used to caculate precison for item algorithem
 * @author wang yuliang
 *
 */
public class PrecisionForItem extends TopNBasedRecommenderMeasure {

	public PrecisionForItem(String name) {
		super(name);
	}

	@Override
	public double getValue() {
		return m_sums / m_counts;
	}

	@Override
	public void update(int[] truthItems, int[] predictedItems) {
//		System.err.println("truth:" + truthItems);
//		System.err.println("predicted: " + predictedItems);
		int numTruths = truthItems.length;
		int numPredictions = predictedItems.length;

		double sum = 0;
		for (int i = 0; i < numTruths; i++)
		{
			int trueItem = truthItems[i];
			for (int j = 0; j < numPredictions; j++)
			{
				if (predictedItems[j] == trueItem)
				{
					sum += 1;
					break;
				}
			}
		}

		m_counts++;
		m_sums = m_sums + (double)sum / numPredictions;
	}
}
