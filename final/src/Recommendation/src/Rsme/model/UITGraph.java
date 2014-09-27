package Rsme.model;

import java.util.List;

import Rsme.math.SparseMatrix;

public class UITGraph extends TripartiteGraph
{
	public UITGraph(Folksonomy data) throws Exception
	{
		
		List<TagPreference> preferences =data.getPreferences();
		int numPreferences = preferences.size();
		int user_num = data.numUsers();
		int item_num = data.numItems();
		TagPreference preference;
		for (int i = 0; i < numPreferences; i++)
		{
			preference = (TagPreference) preferences.get(i);
			int userIndex = data.getUserIndex(preference.getUerId());
			int itemIndex = data.getItemIndex(preference.getItemId())+ user_num;
			int tagIndex = data.getTagIndex(preference.getTagId())+ user_num
					+ item_num;
			
			result.set(userIndex, tagIndex, 1);
			result.set(tagIndex, userIndex, 1);
			result.set(itemIndex, tagIndex, 1);
			result.set(tagIndex, itemIndex, 1);
		}
	}
}
