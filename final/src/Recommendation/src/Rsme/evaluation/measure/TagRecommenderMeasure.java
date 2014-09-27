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
 *    TagRecommenderMeasure.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.evaluation.measure;

/**
 * ����Ϊ��Ա�ǩ�Ƽ��㷨�����۱�׼�Ļ��࣬�����ͨ�õ����Ժͺ���
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public abstract class TagRecommenderMeasure
{
	/*---------------------------members---------------------------------------*/
	/** ���۱�׼������ */
	protected String m_name;

	/** ��ǰ�ۼӵ�ֵ����ΪҪ�ڶ������ʵ���Ͻ����ۼӣ� */
	protected double m_sums;

	/** ��ǰ�Ѿ�������Ĵ�������Ӧ�Ѿ�Ԥ���tag�ģ�user, item����Եĸ��� */
	protected double m_counts;


	/*---------------------------constructor-----------------------------------*/
	public TagRecommenderMeasure(String name)
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
	 * ���ظ����۱�׼���ܵ�����ֵ
	 * 
	 * @return
	 */
	public abstract double getIdealValue();


	/**
	 * ���ݶԵ�ǰitem��Ԥ��tag,�������۱�׼��ȡֵ
	 * 
	 * @param truths
	 *          item����ʵtag����
	 * @param predictedrank
	 *          �Ƽ��㷨Ԥ���tag��������
	 */
	public abstract void update(int[] truths, int[] predictedrank);


	/**
	 * �������۱�׼�ĵ�ǰȡֵ
	 * 
	 * @return
	 */
	public abstract double getValue();

}
