/**
 * Copyright    : Copyright (c) 2006. Wintim Corp. All rights reserved
 * File Summary : 
 * Create time  : 2013-5-13
 */

package Rsme.evaluation;

/**
 * <code>ItemRecommenderMeasure</code> is used to evaluate item recommend algorithem
 * @author wang yuliang
 *
 */
public class ItemRecommenderMeasure extends TopNBasedRecommenderMeasure {

	public ItemRecommenderMeasure(String name) {
		super(name);
	}

	@Override
	public double getValue() {
		return 0;
	}

	@Override
	public void update(int[] truthItems, int[] predictedItems) {
		
	}
}
