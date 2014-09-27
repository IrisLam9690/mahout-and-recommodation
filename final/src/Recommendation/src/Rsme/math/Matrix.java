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
 *    Matrix.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.math;

/**
 * this is a abstract class, which define the common members and interfaces a
 * matrix should have. Any specific implementation of matrix should inherit this
 * class
 * 
 * @author Bin Fu (091112072@bjtu.edu.cn)
 * @version $Revision: 0.1$
 */
public abstract class Matrix
{
	/*---------------------------members---------------------------------------*/
	/** number of rows */
	protected int m_numRows;
	
	/** number of columns */
	protected int m_numColumns;
	
	
	/*------------------------constructor--------------------------------------*/
	/**
	 * default constructor
	 */
	public Matrix()
	{
		this(100, 100);
	}
	
	
	/**
	 * constructor, giving specific number of rows and columns
	 * 
	 * @param numRows
	 *          number of rows
	 * @param numCols
	 *          number of columns
	 */
	public Matrix(int numRows, int numCols)
	{
		m_numRows = numRows;
		m_numColumns = numCols;
	}
	
	
	/*------------------------public functions----------------------------------*/
	/**
	 * return the number of rows
	 * 
	 * @return
	 */
	public int numRows()
	{
		return m_numRows;
	}
	
	
	/**
	 * return the number of columns
	 * 
	 * @return
	 */
	public int numColumns()
	{
		return m_numColumns;
	}
	
	
	/*------------------------abstract functions----------------------------------*/
	/**
	 * set teh value of the specific position in the matrix
	 * 
	 * @param row
	 *          the row-coordinate
	 * @param col
	 *          the column-coordinate
	 * @param value
	 *          the value should be set
	 * @throws Exception
	 */
	public abstract void set(int row, int col, double value) throws Exception;
	
	
	/**
	 * return the value of a specific position in the matrix
	 * 
	 * @param row
	 *          the row-coordinate
	 * @param col
	 *          the column-coordinate
	 * @return the value of that position
	 * @throws Exception
	 */
	public abstract double at(int row, int col) throws Exception;
	
	
	/**
	 * normalize the rows, so the values of each row are added to be 1
	 */
	public abstract void rowNormalization();
	
	/**
	 * left multiply a vector
	 * @param vector the vector to be multiplied
	 * @return
	 * @throws Exception
	 */
	public abstract DoubleVector leftMultiplyVector(DoubleVector vector)
			throws Exception;
}
