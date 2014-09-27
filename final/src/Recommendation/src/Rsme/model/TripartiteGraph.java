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
 *    TripartiteGraph.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */
package Rsme.model;

import Rsme.math.Matrix;
import Rsme.math.SparseMatrix;

/**
 * 该类将folksonomy数据表示成为一个图模型
 * @author bin fu
 *
 */
public class TripartiteGraph
{
	protected Folksonomy m_data;
	protected int numNodes;
	protected Matrix result;


	public TripartiteGraph()
	{
		
	}
	/**
	 * constructor
	 * @param data
	 * @throws Exception
	 */
	public TripartiteGraph(Folksonomy data) throws Exception
	{
		createGraph(data);
	}

	
	/**
	 * create the graph using a specific folksonomy data
	 * @param data
	 * @throws Exception
	 */
	public void createGraph(Folksonomy data) throws Exception
	{
		m_data=data;
		numNodes = data.numItems() + data.numTags() + data.numUsers();
		result = new SparseMatrix(numNodes, numNodes);

		int numPreferences = data.m_preferences.size();
		for (int i = 0; i < numPreferences; i++)
		{
			Preference preference = (TagPreference) data.m_preferences.get(i);

			int userIndex = data.m_userIndex.get(preference.getUerId());
			int itemIndex = data.m_itemIndex.get(preference.getItemId()) + data.numUsers();
			int tagIndex = data.m_tagIndex.get(((TagPreference) preference).getTagId())
					+ data.numUsers() + data.numItems();

			result.set(userIndex, itemIndex, result.at(userIndex, itemIndex) + 1);
			result.set(itemIndex, userIndex, result.at(itemIndex, userIndex) + 1);
			result.set(userIndex, tagIndex, result.at(userIndex, tagIndex) + 1);
			result.set(tagIndex, userIndex, result.at(tagIndex, userIndex) + 1);
			result.set(itemIndex, tagIndex, result.at(itemIndex, tagIndex) + 1);
			result.set(tagIndex, itemIndex, result.at(tagIndex, itemIndex) + 1);
		}
	}
	
	/**
	 * return the matrix representaion of the gragh
	 * @return
	 */
	public Matrix matrix()
	{
		return result;
	}


	public int numNodes()
	{
		return numNodes;
	}


	public int getUserIndex(int id)
	{
		return m_data.getUserIndex(id);
	}


	public int getItemIndex(int id)
	{
		if (m_data.getItemIndex(id) == -1)
			return -1;
		else
		{
			return m_data.getItemIndex(id) +m_data.numUsers();
		}
	}


	public int getTagIndex(int id)
	{
		if (m_data.getItemIndex(id) == -1)
			return -1;
		else
		{
			return m_data.getItemIndex(id) + m_data.numUsers() + m_data.numTags();
		}
	}
}