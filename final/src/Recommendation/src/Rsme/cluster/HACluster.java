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
 * �㼶����
 * @author wang yuliang
 *
 */
public class HACluster {
	
	/**
	 * �㼶���࣬��<code>preferences</code>�ۺϳ�<code>threshold</code>�����
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
		//��ʼ��
		Map<Integer, Category> clusters = buildClustersForTag(folksonomy);
		
		//��ʼ���ֱ࣬�������С�ڵ���threshold
		while(clusters.size() > threshold) {
			//�ҳ����������Set�����Һϲ�,���¼���centerֵ
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
		//��ʼ��
		Map<Integer, Category> clusters = buildClustersForItem(folksonomy);
		
		//��ʼ���ֱ࣬�������С�ڵ���threshold
		while(clusters.size() > threshold) {
			//�ҳ����������Set�����Һϲ�,���¼���centerֵ
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
	 * �ҳ������������Set�����кϲ�
	 */
	static private void findAndMerge(Map<Integer, Category> clusters) {
		if (clusters == null) {
			throw new NullPointerException("clusterss");
		}
		if (clusters.size() < 2) {
			throw new IllegalArgumentException("cluster size must not be less than 2");
		}
		
		//Ѱ�����������Set
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
//						System.out.println("����" + minI + ":" + minJ);
					}
					
				}
			}
		}
		
		//�ϲ�����Set
		Category category = clusters.get(minI);
		//System.err.println(clusters);
		System.out.println(minJ + "," + minI);
		category.merge(clusters.get(minJ));
		clusters.remove(minJ);
	}
	
	/**
	 * ����������������ֵ
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
	 * ��������ģ
	 * @param vector
	 * @return
	 */
	static public double normVector(Map<Integer, Double> vector) {
		if (vector == null) {
			throw new NullPointerException("vector");
		}
		//vector��ģ
		double normVector = 0.0;
		for (Double d : vector.values()) {
			normVector += d * d;
		}
		normVector = Math.sqrt(normVector);
		return normVector;
	}
	
	/**
	 * ���������ڻ�
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
				"C:\\Users\\wuyadong\\Desktop\\�����ھ���չ\\Recommendation\\�½��ļ��� (4)\\new.txt", "C:\\Users\\wuyadong\\Desktop\\�����ھ���չ\\Recommendation\\�½��ļ��� (4)\\tags.txt");
		Folksonomy data = loader.getFolksonomy();
		Map<Integer, Category> map = HACluster.clusterForItem(data, 20);
		for (Entry<Integer, Category> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue().size());
		}
	}
}
