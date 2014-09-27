/**
 * Copyright    : Copyright (c) 2006. Wintim Corp. All rights reserved
 * File Summary : 
 * Create time  : 2013-5-20
 */

package Rsme.cluster;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Rsme.model.Folksonomy;
import Rsme.model.TagPreference;

/**
 * Kmeans ����
 * @author wang yuliang
 *
 */
public class KMeansCluster {

	/**
	 * @param folksonomy
	 * @param threshold
	 * @return
	 */
	final public static Map<Integer, Category> clusterForTag(Folksonomy folksonomy, int threshold) {
		if (folksonomy == null) {
			throw new NullPointerException("folk sonomy");
		}
		if (threshold > folksonomy.numTags()) {
			throw new IllegalArgumentException("threshold must not be more than num tags");
		}
		
		//��������tag������
		Map<Integer, Map<Integer, Double>> tagVectors = buildTagVector(folksonomy);
		
		//�����tag�б�
		List<Integer> tags = getTagsAndShuffle(tagVectors);
		
		//��ʼ�����
		Map<Integer, Category> clusters = buildClusterForTag(tags, threshold, tagVectors);
		//�����е����Ⱥ��
		for (int i = threshold; i < tags.size(); i ++) {
			mergeAndUpdate(clusters, tags.get(i), tagVectors);
		}
		
		return clusters;
	}
	
	/**
	 * ��tag���ۼ���clusters��
	 * @param clusters
	 * @param tagId
	 */
	final static private void mergeAndUpdate(Map<Integer, Category> clusters, int tagId, Map<Integer, Map<Integer, Double>> tagsVectors)
	{
		//�ҵ���С�ľ���
		double maxValue = Double.MIN_VALUE;
		int index=0;
		for (int i = 0; i < clusters.size(); i ++) {
			Category category = clusters.get(i);
			double value = HACluster.cosVector(category.getCenterPointer(), tagsVectors.get(tagId));
			if (value >= maxValue) {
				maxValue = value;
				index = i;
			}
		}
		
		//�������cluster������Tag
		Category mergeCategory = clusters.get(index);
		mergeCategory.addTagAndUpdate(tagId, tagsVectors.get(tagId));
	}
	/**
	 * ���tag�б�,����tag��ǩ���������
	 * @param folksonomy
	 * @return
	 */
	final static private List<Integer> getTagsAndShuffle(Map<Integer, Map<Integer, Double>> tagVectors) {
		assert (tagVectors != null);
		Set<Integer> tagSet = tagVectors.keySet();
		List<Integer> tagList = Arrays.asList(tagSet.toArray(new Integer[1]));
		Collections.shuffle(tagList);
		return tagList;
	}
	
	/**
	 * ��������Tag������
	 * @param folksonomy
	 * @return
	 */
	final static private Map<Integer, Map<Integer, Double>> buildTagVector(Folksonomy folksonomy) {
		Map<Integer, Map<Integer, Double>> tagVectors = new HashMap<Integer, Map<Integer,Double>>();
		for (TagPreference tagPreference : folksonomy.getPreferences()) {
			int tagId = tagPreference.getTagId();
			Map<Integer, Double> vector;
			if (! tagVectors.containsKey(tagId)) {
				tagVectors.put(tagId, new HashMap<Integer, Double>());
			} 
			vector = tagVectors.get(tagId);
			int itemId = tagPreference.getItemId();
			if (! vector.containsKey(itemId)) {
				vector.put(itemId, 1.0);
			} else {
				vector.put(itemId, 1.0 + vector.get(itemId));
			}
		}
		
		return tagVectors;
	}
	
	/**
	 * ���ѡ��<code>threhold</code>��Ԫ�أ���Ϊ��ʼ��category
	 */
	final private static Map<Integer, Category> buildClusterForTag(List<Integer> tags, int threshold, Map<Integer, Map<Integer, Double>> tagsVector) {
		if (threshold < 0 || threshold > tags.size()) {
			throw new IllegalArgumentException("threhold");
		}
		if (tags == null) {
			throw new NullPointerException("tags");
		}
		if (tagsVector == null) {
			throw new NullPointerException("tagsvector");
		}
		
		Map<Integer, Category> categorys = new HashMap<Integer, Category>();
		for (int i = 0; i < threshold; i ++) {
			Category category = new Category();
			category.addTag(tags.get(i));
			category.setCenter(tagsVector.get(tags.get(i)));
			categorys.put(i, category);
		}
		return categorys;
	}
}
