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
 *    CF_tag.java
 *    Copyright (C) 2012 Beijing Jiaotong University, Beijing, China
 *
 */

package Rsme.recommender.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Rsme.model.DataModel;
import Rsme.model.Folksonomy;
import Rsme.model.TagPreference;
import Rsme.utils.Utils;

public class CFTAG
									extends ItemRecommender
{
	private Folksonomy folk;
	private double weight = 0.85;
	private int neighbor_num = 5;
	private int[][] u_e;
	private int[][] i_e;
	private int[][] u_i;
	
	
	@Override
	public void buildRecommender(DataModel dataModel) throws Exception
	{
		// TODO Auto-generated method stub
		folk = (Folksonomy) dataModel;
		int user_num = folk.numUsers();
		int item_num = folk.numItems();
		int tag_num = folk.numTags();
		u_e = new int[user_num][item_num + tag_num];
		i_e = new int[user_num + tag_num][item_num];
		u_i = new int[user_num][item_num];
		int preference_num = folk.getPreferences().size();
		List<TagPreference> temp = folk.getPreferences();
		for (int i = 0; i < preference_num; i++)
		{
			u_i[folk.getUserIndex(temp.get(i).getUerId())][folk.getItemIndex(temp
					.get(i).getItemId())] = 1;
			u_e[folk.getUserIndex(temp.get(i).getUerId())][folk.getItemIndex(temp
					.get(i).getItemId())] = 1;
			u_e[folk.getUserIndex(temp.get(i).getUerId())][item_num
					+ folk.getTagIndex(temp.get(i).getTagId())] = 1;
			i_e[folk.getUserIndex(temp.get(i).getUerId())][folk.getItemIndex(temp
					.get(i).getItemId())] = 1;
			i_e[user_num + folk.getTagIndex(temp.get(i).getTagId())][folk
					.getItemIndex(temp.get(i).getItemId())] = 1;
		}
	}
	
	
	/**
	 * 计算两个用户间的相似度
	 * 
	 * @param id1
	 *          用户1的id
	 * @param id2
	 *          用户2的id
	 * @return 两个用户的相似度
	 * @throws IOException
	 */
	private double U_Similarity(int id1, int id2) throws IOException
	{
		int number = u_e[0].length;
		int first_position = folk.getUserIndex(id1);                                       // 第一个用户位置
		int second_position = folk.getUserIndex(id2);                                        // 第二个用户位置
		int f_user[] = new int[number];
		int s_user[] = new int[number];
		for (int i = 0; i < number; i++)
		{
			f_user[i] = u_e[first_position][i];
		}
		for (int i = 0; i < number; i++)
		{
			s_user[i] = u_e[second_position][i];
		}
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		for (int i = 0; i < number; i++)
		{
			list1.add(new Integer(f_user[i]));
			list2.add(new Integer(s_user[i]));
		}
		
		return Utils.Correlation(list1, list2);
	}
	
	
	/**
	 * 计算两个item间的相似度
	 * 
	 * @param id1
	 *          item1的id
	 * @param id2
	 *          item2的id
	 * @return 两个item的相似度
	 * @throws IOException
	 */
	private double I_Similarity(int id1, int id2) throws IOException
	{
		int first_position = folk.getItemIndex(id1);                              // 第一个item位置
		int second_position = folk.getItemIndex(id2);                              // 第二个item位置
		int number = i_e.length;
		int f_item[] = new int[number];
		int s_item[] = new int[number];
		for (int i = 0; i < number; i++)
		{
			f_item[i] = i_e[i][first_position];
		}
		for (int i = 0; i < number; i++)
		{
			s_item[i] = i_e[i][second_position];
		}
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		for (int i = 0; i < number; i++)
		{
			list1.add(new Integer(f_item[i]));
			list2.add(new Integer(s_item[i]));
		}
		
		return Utils.Correlation(list1, list2);
	}
	
	
	/**
	 * 找出给定用户的n个最近邻
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	private int[] U_k_neighbor(int id) throws IOException
	{
		int number = folk.numUsers();
		int result[] = new int[number];
		double pre_result[] = new double[number];
		for (int i = 0; i < number; i++)
		{
			if ((i == folk.getUserIndex(id)) && (i != number - 1))
			{
				pre_result[i] = -1;
				i++;
			}
			if (i == number - 1)
			{
				pre_result[i] = -1;
			}
			pre_result[i] = U_Similarity(id, folk.getUserByIndex(i));
			
		}
		
		double p_p[][] = new double[number][2];
		for (int i = 0; i < number; i++)
		{
			p_p[i][0] = i;
		}
		
		for (int i = 0; i < number; i++)
		{
			p_p[i][1] = pre_result[i];
		}
		
		double temp1, temp2;
		for (int i = 0; i < number; i++)
		{
			for (int j = 0; j < number - 1; j++)
			{
				
				if (p_p[j][1] < p_p[j + 1][1])
				{
					temp1 = p_p[j][1];
					p_p[j][1] = p_p[j + 1][1];
					p_p[j + 1][1] = temp1;
					temp2 = p_p[j][0];
					p_p[j][0] = p_p[j + 1][0];
					p_p[j + 1][0] = temp2;
				}
			}
		}
		
		for (int i = 0; i < number; i++)
		{
			result[i] = folk.getUserByIndex((int) p_p[i][0]);
		}
		
		int[] final_result = new int[neighbor_num];
		for (int i = 0; i < neighbor_num; i++)
		{
			final_result[i] = result[i];
		}
		
		return final_result;
	}
	
	
	/**
	 * 找出给定资源的n个最近邻
	 * 
	 * @param item_id
	 *          item's id
	 * @return
	 * @throws IOException
	 */
	private int[] I_k_neighbor(int item_id) throws IOException
	{
		int number = folk.numItems();
		int result[] = new int[number];
		double pre_result[] = new double[number];
		for (int i = 0; i < number; i++)
		{
			if ((i == folk.getItemIndex(item_id)) && (i != number - 1))
			{
				pre_result[i] = -1;
				i++;
			}
			if (i == number - 1)
			{
				pre_result[i] = -1;
			}
			pre_result[i] = I_Similarity(item_id, folk.getItemByIndex(i));
			
		}
		double i_p[][] = new double[number][2];
		for (int i = 0; i < number; i++)
		{
			i_p[i][0] = i;
		}
		for (int i = 0; i < number; i++)
		{
			i_p[i][1] = pre_result[i];
		}
		double temp1, temp2;
		for (int i = 0; i < number; i++)
		{
			for (int j = 0; j < number - 1; j++)
			{
				
				if (i_p[j][1] < i_p[j + 1][1])
				{
					temp1 = i_p[j][1];
					i_p[j][1] = i_p[j + 1][1];
					i_p[j + 1][1] = temp1;
					temp2 = i_p[j][0];
					i_p[j][0] = i_p[j + 1][0];
					i_p[j + 1][0] = temp2;
				}
			}
		}
		for (int i = 0; i < number; i++)
		{
			result[i] = folk.getItemByIndex((int) i_p[i][0]);
		}
		
		int[] final_result = new int[neighbor_num];
		for (int i = 0; i < neighbor_num; i++)
		{
			final_result[i] = result[i];
		}
		
		return final_result;
	}
	
	
	private double p_ucf(int id_user, int id_item, int[] n_u) throws IOException
	{
		// double result= 0 ;
		int number_NU = n_u.length;
		int count = 0;
		for (int i = 0; i < number_NU; i++)
		{
			if (u_i[folk.getUserIndex(n_u[i])][folk.getItemIndex(id_item)] == 1)
			{
				count++;
			}
			
		}
		return ((double) count / (double) number_NU);
	}
	
	
	private double p_icf(int id_user, int id_item, int[] n_i) throws IOException
	{
		double result = 0;
		for (int j = 0; j < n_i.length; j++)
		{
			if (u_i[folk.getUserIndex(id_user)][folk.getItemIndex(n_i[j])] == 1)
			{
				result += I_Similarity(id_item, n_i[j]);
			}
		}
		return result;
	}
	
	
	private double p_uicf(int id_user, int id_item, int[] n_u, int[] n_i)
			throws IOException
	{
		double u_sum = 0;
		for (int i = 0; i < folk.numItems(); i++)
		{
			u_sum += p_ucf(id_user, folk.getItemByIndex(i), n_u);
		}
		double i_sum = 0;
		for (int i = 0; i < folk.numItems(); i++)
		{
			
			i_sum += p_icf(id_user, folk.getItemByIndex(i), n_i);
			
		}
		if (u_sum == 0)
		{
			u_sum = 1;
		}
		if (i_sum == 0)
		{
			i_sum = 1;
		}
		double pre_pucf = p_ucf(id_user, id_item, n_u);
		double pre_picf = p_icf(id_user, id_item, n_i);
		double result = 0;
		result = weight * pre_pucf / u_sum + (1 - weight) * pre_picf / i_sum;
		
		return result;
	}
	
	
	@Override
	public RatingItem[] ratingsForItems(int userId) throws Exception
	{
		int top_n[] = new int[m_numRecommendations];
		
		int[] n_u = U_k_neighbor(userId);
		
		double pre_top[][] = new double[folk.numItems()][2];
		for (int i = 0; i < folk.numItems(); i++)
		{
			pre_top[i][0] = i;
		}
		
		for (int i = 0; i < folk.numItems(); i++)
		{
			int[] n_i = I_k_neighbor(folk.getItemByIndex(i));
			pre_top[i][1] = p_uicf(userId, folk.getItemByIndex(i), n_u, n_i);
		}
		
		double temp1, temp2;
		for (int i = 0; i < folk.numItems(); i++)
		{
			for (int j = 0; j < folk.numItems() - 1; j++)
			{
				
				if (pre_top[j][1] < pre_top[j + 1][1])
				{
					temp1 = pre_top[j][1];
					pre_top[j][1] = pre_top[j + 1][1];
					pre_top[j + 1][1] = temp1;
					temp2 = pre_top[j][0];
					pre_top[j][0] = pre_top[j + 1][0];
					pre_top[j + 1][0] = temp2;
				}
			}
		}
		
		int m_numItems = folk.numItems();
		RatingItem[] results = new RatingItem[m_numItems];
		for (int i = 0; i < m_numItems; i++)
		{
			int itemId = folk.getItemByIndex((int) pre_top[i][0]);
			results[i] = new RatingItem(itemId, pre_top[i][1]);
		}
		
		return results;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		
	}
	
}
