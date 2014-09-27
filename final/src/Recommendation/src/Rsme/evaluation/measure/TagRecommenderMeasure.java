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
 * 该类为针对标签推荐算法的评价标准的基类，规矩了通用的属性和函数
 * 
 * @author fubin (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$ modification: 22/10/2012
 */

public abstract class TagRecommenderMeasure
{
	/*---------------------------members---------------------------------------*/
	/** 评价标准的名称 */
	protected String m_name;

	/** 当前累加的值（因为要在多个测试实例上进行累加） */
	protected double m_sums;

	/** 当前已经运算过的次数，对应已经预测过tag的（user, item）序对的个数 */
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
	 * 返回该评价标准可能的最优值
	 * 
	 * @return
	 */
	public abstract double getIdealValue();


	/**
	 * 根据对当前item的预测tag,更新评价标准的取值
	 * 
	 * @param truths
	 *          item的真实tag集合
	 * @param predictedrank
	 *          推荐算法预测的tag有序排列
	 */
	public abstract void update(int[] truths, int[] predictedrank);


	/**
	 * 返回评价标准的当前取值
	 * 
	 * @return
	 */
	public abstract double getValue();

}
