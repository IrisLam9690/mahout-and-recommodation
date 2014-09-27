package Rsme.evaluation;

public class FMeasureForItem extends TopNBasedRecommenderMeasure{

	public FMeasureForItem(String name) {
		super("FMeasure");
	}

	@Override
	public void update(int[] truthItems, int[] predictedItems) {
		int numTruths = truthItems.length;
		int numPredictions = predictedItems.length;

		double sum = 0;
		for (int i = 0; i < numTruths; i++) {
			int trueTag = truthItems[i];
			for (int j = 0; j < numPredictions; j++) {
				if (predictedItems[j] == trueTag) {
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
	public double getValue() {
		return m_sums / m_counts;
	}

}
