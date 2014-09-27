/**
 * Copyright    : Copyright (c) 2006. Wintim Corp. All rights reserved
 * File Summary : 
 * Create time  : 2013-5-13
 */

package Rsme.evaluation;

/**
 * 
 * <code>RecallForItem</code> is used to calculate recall for item recommend algorithem
 * @author wang yuliang
 *
 */
public class RecallForItem extends TopNBasedRecommenderMeasure {

	public RecallForItem(String name) {
		super(name);
	}

	@Override
	public double getValue() {
		return m_sums / m_counts;
	}

	@Override
	public void update(int[] truthItems, int[] predictedItems) {
		int numTruths = truthItems.length;
		int numPredictions = predictedItems.length;

		double sum = 0;
		for (int i = 0; i < numTruths; i++) {
			int trueItem = truthItems[i];
			for (int j = 0; j < numPredictions; j++) {
				if (predictedItems[j] == trueItem) {
					sum += 1.0;
					break;
				}
			}
		}

		m_counts++;
		m_sums = m_sums + sum / numTruths;
	}
}
