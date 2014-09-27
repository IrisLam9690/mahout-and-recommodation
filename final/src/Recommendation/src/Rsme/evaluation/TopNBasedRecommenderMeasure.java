package Rsme.evaluation;

public abstract class TopNBasedRecommenderMeasure
{
	/*---------------------------members---------------------------------------*/
	/** ���۱�׼������ */
	protected String m_name;

	/** ��ǰ�ۼӵ�ֵ����ΪҪ�ڶ������ʵ���Ͻ����ۼӣ� */
	protected double m_sums;

	/** ��ǰ�Ѿ�������Ĵ�������Ӧ�Ѿ�Ԥ���tag�ģ�user, item����Եĸ��� */
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
	 * �������۱�׼������
	 * 
	 * @return
	 */
	public String getName()
	{
		return m_name;
	}


	/**
	 * �������۱�׼��ʹ���䴦�ڳ�ʼ״̬
	 */
	public void reset()
	{
		m_sums = 0;
		m_counts = 0;
	}


	/**
	 * ��������۱�׼�����ƺ�ֵ
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
	 * �������۱�׼�ĵ�ǰȡֵ
	 * 
	 * @return
	 */
	public abstract double getValue();

}
