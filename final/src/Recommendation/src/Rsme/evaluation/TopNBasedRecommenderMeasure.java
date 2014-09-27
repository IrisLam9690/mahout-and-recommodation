package Rsme.evaluation;

public abstract class TopNBasedRecommenderMeasure
{
	/*---------------------------members---------------------------------------*/
	/** 评价标准的名称 */
	protected String m_name;

	/** 当前累加的值（因为要在多个测试实例上进行累加） */
	protected double m_sums;

	/** 当前已经运算过的次数，对应已经预测过tag的（user, item）序对的个数 */
	protected double m_counts;


	/*---------------------------constructor-----------------------------------*/
	public TopNBasedRecommenderMeasure(String name)
	{
		m_name = name;
		m_counts = 0;
		m_sums = 0;
	}


	/*---------------------------public functions------------------------------*/
	/**
	 * 返回评价标准的名称
	 * 
	 * @return
	 */
	public String getName()
	{
		return m_name;
	}


	/**
	 * 重置评价标准，使得其处于初始状态
	 */
	public void reset()
	{
		m_sums = 0;
		m_counts = 0;
	}


	/**
	 * 输入该评价标准的名称和值
	 */
	public String toString()
	{
		double value = Double.NaN;
		try {
			value = getValue();
		}
		catch (Exception ex) {
		}
		return getName() + ": " + String.format("%.4f", value);
	}


	/*----------------abstract functions need implemented----------------------*/
	
	/**
	 * update the measures
	 * @param truthItems 
	 * @param predictedItems
	 */
	public abstract void update(int[] truthItems, int[] predictedItems);


	/**
	 * 返回评价标准的当前取值
	 * 
	 * @return
	 */
	public abstract double getValue();

}
