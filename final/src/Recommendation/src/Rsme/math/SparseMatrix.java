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
 *    SparseMatrix.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.math;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * this is a class that implement the sparse matrix
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$
 */


public class SparseMatrix
													extends Matrix
{
	/*---------------------------members---------------------------------------*/
	/** store all the values in the matrix */
	protected LinkedHashMap<Integer, LinkedHashMap<Integer, Double>> m_values;
	
	
	/*------------------------constructor--------------------------------------*/
	public SparseMatrix()
	{
		super();
		m_values = new LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>();
	}
	
	
	public SparseMatrix(int numRows, int numColumns)
	{
		super(numRows, numColumns);
		m_values = new LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>();
		
	}
	
	
	/*------------------------public functions---------------------------------*/
	/**
	 * set the values at the give position
	 */
	@Override
	public void set(int row, int col, double value) throws Exception
	{
		if (row >= m_numRows || col >= m_numColumns)
			throw new Exception("out of the bounds of the matrix");
		else
		{
			if (!m_values.containsKey(row))
			{
				LinkedHashMap<Integer, Double> vector = new LinkedHashMap<Integer, Double>();
				vector.put(col, value);
				m_values.put(row, vector);
			}
			else
				m_values.get(row).put(col, value);
		}
		
	}
	
	
	/**
	 * get the values at the give position
	 */
	@Override
	public double at(int row, int col) throws Exception
	{
		if (row >= m_numRows || col >= m_numColumns)
			throw new Exception("out of the bounds of the matrix");
		
		if (!m_values.containsKey(row))
			return 0;
		else
		{
			LinkedHashMap<Integer, Double> vector = m_values.get(row);
			if (!vector.containsKey(col))
				return 0;
			else
				return vector.get(col);
		}
	}
	
	
	/**
	 * examine if SparseMatrix has the value with a specific row and a specific
	 * column
	 * 
	 * @param row
	 *          a specific row
	 * @param col
	 *          a specific column
	 * @return
	 */
	public boolean containRowCol(int row, int col) throws Exception
	{
		if (row >= m_numRows || col >= m_numColumns)
			throw new Exception("out of the bounds of the matrix");
		
		if (!m_values.containsKey(row))
			return false;
		else
		{
			LinkedHashMap<Integer, Double> vector = m_values.get(row);
			if (!vector.containsKey(col))
				return false;
			else
				return true;
		}
	}
	
	
	/**
	 * left multiply a vector
	 */
	public DoubleVector leftMultiplyVector(DoubleVector vector) throws Exception
	{
		if (m_numColumns != vector.size())
		{
			throw new Exception("the matrix and the vector are noe compatible");
		}
		
		double[] result = new double[m_numRows];
		
		for (int i = 0; i < this.m_numRows; i++)
		{
			
			if (!m_values.containsKey(i))
			{
				result[i] = 0;
			}
			
			else
			{
				double sums = 0;
				LinkedHashMap<Integer, Double> currentRow = m_values.get(i);
				Set<Integer> keys = currentRow.keySet();
				Iterator<Integer> iterator = keys.iterator();
				while (iterator.hasNext())
				{
					int key = iterator.next().intValue();
					sums += currentRow.get(key) * vector.at(key);
				}
				result[i] = sums;
			}
		}
		
		return new DoubleVector(result);
	}
	
	
	@Override
	public void rowNormalization()
	{
		Set<Integer> keys = m_values.keySet();
		Iterator<Integer> rowIterator = keys.iterator();
		while (rowIterator.hasNext())
		{
			/* current row */
			Integer key = rowIterator.next();
			
			double sum = 0;
			
			/* the non-zero values in current row */
			Map<Integer, Double> row = m_values.get(key);
			Set<Integer> colKeys = row.keySet();
			Iterator<Integer> colIterator = colKeys.iterator();
			while (colIterator.hasNext())
			{
				sum += row.get(colIterator.next()).doubleValue();
			}
			
			if (sum != 0)
			{
				Iterator<Integer> iterator = colKeys.iterator();
				while (iterator.hasNext())
				{
					Integer colkey = iterator.next();
					double value = row.get(colkey);
					row.put(colkey, value / sum);
				}
			}
		}
	}
	
	
	/* for test */
	public static void main(String[] args) throws Exception
	{
		SparseMatrix mar = new SparseMatrix();
		mar.set(3, 4, 12.3);
		double value = mar.at(3, 8);
		System.out.println(value);
	}
}