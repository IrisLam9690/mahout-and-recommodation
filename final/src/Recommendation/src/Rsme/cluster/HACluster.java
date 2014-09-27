/**
 * Copyright    : Copyright (c) 2006. BJTU. All rights reserved
 * File Summary : 
 * Create time  : 2013-5-5
 */

package Rsme.cluster;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import Rsme.model.Folksonomy;
import Rsme.model.TagPreference;
import Rsme.model.loader.MovielensTagDataLoader;

/**
 * 层级聚类
 * @author wang yuliang
 *
 */
public class HACluster {
	
	/**
	 * 层级聚类，将<code>preferences</code>聚合成<code>threshold</code>个类别
	 * @param preferences
	 * @param threshold
	 * @return
	 */
	final public static Map<Integer, Category> clusterForTag(Folksonomy folksonomy, int threshold) {
		if (folksonomy == null) {
			throw new NullPointerException("folk sonomy");
		}
		if (threshold > folksonomy.numTags()) {
			System.err.println("numTags: " + folksonomy.numTags());
			throw new IllegalArgumentException("threshold must not be more than num tags");
		}
		//初始化
		Map<Integer, Category> clusters = buildClustersForTag(folksonomy);
		
		//开始聚类，直到类个数小于等于threshold
		while(clusters.size() > threshold) {
			//找出最近的两个Set，并且合并,重新计算center值
		//	System.err.println(clusters.size());
			findAndMerge(clusters);
		}
		return clusters;
	}
	
	final public static Map<Integer, Category> clusterForItem(Folksonomy folksonomy, int threshold) {
		if (folksonomy == null) {
			throw new NullPointerException("folk sonomy");
		}
		if (threshold > folksonomy.numTags()) {
			throw new IllegalArgumentException("threshold must not be more than num tags");
		}
		//初始化
		Map<Integer, Category> clusters = buildClustersForItem(folksonomy);
		
		//开始聚类，直到类个数小于等于threshold
		while(clusters.size() > threshold) {
			//找出最近的两个Set，并且合并,重新计算center值
			findAndMerge(clusters);
		}
		return clusters;
	}
	
	static private Map<Integer, Category> buildClustersForTag(Folksonomy folksonomy) {
		Map<Integer, Category> clusters = new HashMap<Integer, Category>();

		for (TagPreference tagPreference : folksonomy.getPreferences()) {
			Category category = null;
			if (clusters.containsKey(tagPreference.getTagId())) {
				category = clusters.get(tagPreference.getTagId());
			} else {
				category = new Category();
			}
			Map<Integer, Double> centerPointerMap = category.getCenterPointer();
			int itemId = tagPreference.getItemId();
			if (!centerPointerMap.containsKey(itemId)) {
				centerPointerMap.put(itemId, 0.0);
			}
			centerPointerMap.put(itemId, centerPointerMap.get(itemId) + 1);
			category.setCenter(centerPointerMap);
			category.addTag(tagPreference.getTagId());
			clusters.put(tagPreference.getTagId(), category);
		}

		return clusters;
	}
	
	static private Map<Integer, Category> buildClustersForItem(Folksonomy folksonomy) {
		Map<Integer, Category> clusters = new HashMap<Integer, Category>();

		for (TagPreference tagPreference : folksonomy.getPreferences()) {
			Category category = null;
			if (clusters.containsKey(tagPreference.getItemId())) {
				category = clusters.get(tagPreference.getItemId());
			} else {
				category = new Category();
			}
			Map<Integer, Double> centerPointerMap = category.getCenterPointer();
			int tagId = tagPreference.getTagId();
			if (!centerPointerMap.containsKey(tagId)) {
				centerPointerMap.put(tagId, 0.0);
			}
			centerPointerMap.put(tagId, centerPointerMap.get(tagId) + 1);
			category.setCenter(centerPointerMap);
			category.addTag(tagPreference.getItemId());
			clusters.put(tagPreference.getItemId(), category);
		}

		return clusters;
	}
	
	/**
	 * 找出最相近的两个Set并进行合并
	 */
	static private void findAndMerge(Map<Integer, Category> clusters) {
		if (clusters == null) {
			throw new NullPointerException("clusterss");
		}
		if (clusters.size() < 2) {
			throw new IllegalArgumentException("cluster size must not be less than 2");
		}
		
		//寻找最近的两个Set
		double maxValue = Double.MAX_VALUE * -1;
		int minI, minJ;
		minI = minJ = -1;
//		System.err.println("begin:" + clusters.size());
		for (Map.Entry<Integer, Category> entry1 : clusters.entrySet()) {
			for (Map.Entry<Integer, Category> entry2 : clusters.entrySet()) {
				int i = entry1.getKey();
				int j = entry2.getKey();
//				System.out.println("check:" + i + "," + j);
				if (i < j) {
					double value = cosVector(entry1.getValue().getCenterPointer(), entry2.getValue().getCenterPointer());
//					System.out.println("value: " + value + ",maxValue:" + maxValue + "," + (value > maxValue));
//					System.err.println(Double.MIN_VALUE);
					if (value > maxValue) {
						maxValue = value;
						minI = i;
						minJ = j;
//						System.out.println("更行" + minI + ":" + minJ);
					}
					
				}
			}
		}
		
		//合并两个Set
		Category category = clusters.get(minI);
		//System.err.println(clusters);
		System.out.println(minJ + "," + minI);
		category.merge(clusters.get(minJ));
		clusters.remove(minJ);
	}
	
	/**
	 * 求两个向量的余玄值
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	static public double cosVector(Map<Integer, Double> vector1, Map<Integer, Double> vector2) {
		if (vector1 == null) {
			throw new NullPointerException("vector 1");
		}
		if (vector2 == null) {
			throw new NullPointerException("vector 2");
		}
		double normVector1 = normVector(vector1);
		double normVector2 = normVector(vector2);
		double mutiplyOfVector = mutiplyVector(vector1, vector2);
		return mutiplyOfVector / (normVector1 * normVector2);
	}
	
	/**
	 * 求向量的模
	 * @param vector
	 * @return
	 */
	static public double normVector(Map<Integer, Double> vector) {
		if (vector == null) {
			throw new NullPointerException("vector");
		}
		//vector的模
		double normVector = 0.0;
		for (Double d : vector.values()) {
			normVector += d * d;
		}
		normVector = Math.sqrt(normVector);
		return normVector;
	}
	
	/**
	 * 求向量的内积
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static double mutiplyVector(Map<Integer, Double> vector1, Map<Integer, Double> vector2) {
		if (vector1 == null) {
			throw new NullPointerException("vector");
		}
		if (vector2 == null) {
			throw new NullPointerException("vector 2");
		}
		double sum = 0.0;
		for (Integer key : vector1.keySet()) {
			if (vector2.containsKey(key)) {
				sum += vector1.get(key) * vector2.get(key);
			}
		}
		return sum;
	}
	
	public static void main(String[] args) throws Exception {
		MovielensTagDataLoader loader = new MovielensTagDataLoader(
				"C:\\Users\\wuyadong\\Desktop\\数据挖掘扩展\\Recommendation\\新建文件夹 (4)\\new.txt", "C:\\Users\\wuyadong\\Desktop\\数据挖掘扩展\\Recommendation\\新建文件夹 (4)\\tags.txt");
		Folksonomy data = loader.getFolksonomy();
		Map<Integer, Category> map = HACluster.clusterForItem(data, 20);
		for (Entry<Integer, Category> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue().size());
		}
	}
}
