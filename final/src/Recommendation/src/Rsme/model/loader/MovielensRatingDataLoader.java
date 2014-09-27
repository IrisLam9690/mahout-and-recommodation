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
 * MovielensRatingDataLoader.java Copyright (C) 2012 Beijing jiaotong University, Beijing,
 * China
 */

package Rsme.model.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import Rsme.model.RatingDataModel;

public class MovielensRatingDataLoader
																			extends RatingFileLoader
{
	
	/*------------------------constructor--------------------------------------*/
	public MovielensRatingDataLoader() {
		super();
	}
	
	@Override
	public RatingDataModel getRatingData() throws Exception
	{
		if (m_preferencesFile == null)
			throw new Exception("file not find!");
		
		RatingDataModel result = new RatingDataModel();
		
		/*-----------------read the preference information-------------------*/
		BufferedReader ratingFileread = new BufferedReader(new FileReader(
				m_preferencesFile));
		
		String[] attributes = null;
		int userId;
		int itemId;
		double rating;
		
		String preference;
		while ((preference = ratingFileread.readLine()) != null)
		{
			attributes = preference.split(m_delimiter);
			userId = Integer.parseInt(attributes[0]);
			itemId = Integer.parseInt(attributes[1]);
			rating = Double.parseDouble(attributes[2]);
			result.addUser(userId);
			result.addItem(itemId);
			result.addPreference(userId, itemId, rating);
			
		}
		ratingFileread.close();
		return result;
	}
	
	
	/* for test */
	public static void main(String[] args) throws Exception
	{
		MovielensRatingDataLoader loader = new MovielensRatingDataLoader();
		loader.setSource("D:\\temps\\u.data");
		RatingDataModel folk = loader.getRatingData();
		System.out.println(folk.numUsers());
		System.out.println(folk.numItems());
	}
}
