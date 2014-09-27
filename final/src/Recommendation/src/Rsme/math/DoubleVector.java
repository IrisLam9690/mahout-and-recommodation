/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    DoubleVector.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */
package Rsme.math;

/**
 * class that represents a vector of double values, implements the common operations
 */

import java.util.ArrayList;
import java.util.List;

public class DoubleVector
{
	
	/*---------------------------members---------------------------------------*/
	protected List<Double> m_values;
	protected int m_size;
	
	
	/*------------------------constructor--------------------------------------*/
	public DoubleVector()
	{
		this(0);
	}
	
	
	public DoubleVector(int size)
	{
		m_values = new ArrayList<Double>(size);
		m_size = size;
	}
	
	
	public DoubleVector(double[] values)
	{
		m_size = values.length;
		m_values = new ArrayList<Double>(m_size);
		for (int i = 0; i < m_size; i++)
			m_values.add(values[i]);
	}
	
	
	/*---------------------------public functions-------------------------------*/
	public int size()
	{
		return m_size;
	}
	
	
	public void add(double value)
	{
		m_values.add(value);
		m_size++;
	}
	
	
	public double at(int index) throws Exception
	{
		if (index >= m_size)
			throw new Exception("out of vector's bounds");
		
		return m_values.get(index).doubleValue();
	}
	
	
	public static int[] sortedIndex(double[] values)
	{
		int size = values.length;
		int[] index = new int[size];
		for (int i = 0; i < size; i++)
			index[i] = i;
		
		/*--------pop sort-----------------*/
		double swap;
		double left, right;
		for (int i = 1; i <= size - 1; i++)
		{
			for (int j = 0; j <= size - 1 - i; j++)
			{
				left = values[j];
				right = values[j + 1];
				if (left < right)
				{
					swap = left;
					values[j] = right;
					values[j + 1] = swap;
					
					int temp = index[j];
					index[j] = index[j + 1];
					index[j + 1] = temp;
				}
			}
		}
		
		return index;
	}
	
	
	public static void normalize(double[] doubles)
	{
		double sum = 0;
		for (int i = 0; i < doubles.length; i++)
		{
			sum += doubles[i];
		}
		if (Double.isNaN(sum))
		{
			throw new IllegalArgumentException("Can't normalize array. Sum is NaN.");
		}
		if (sum == 0)
		{
			throw new IllegalArgumentException("Can't normalize array. Sum is zero.");
		}
		for (int i = 0; i < doubles.length; i++)
		{
			doubles[i] /= sum;
		}
	}
	
	
	public double[] toDoubleArray()
	{
		double[] result = new double[m_size];
		for (int i = 0; i < m_size; i++)
		{
			result[i] = m_values.get(i).doubleValue();
		}
		
		return result;
	}
	
	/*---------------------------end of the class-------------------------------*/
}
