/**
 * Copyright    : Copyright (c) 2006. Wintim Corp. All rights reserved
 * File Summary : 
 * Create time  : 2013-5-5
 */

package Rsme.recommender.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import Rsme.cluster.Category;
import Rsme.cluster.HACluster;
import Rsme.model.DataModel;
import Rsme.model.Folksonomy;
import Rsme.model.TagPreference;
import Rsme.model.loader.MovielensTagDataLoader;

/**
 * Personalized Recommendation in Socail Tagging using hierarchical
 * Cluster
 * 
 * @author wang yuliang
 *
 */
public class PRBC_Hierarchy extends ItemRecommender {
	final public static int DEFAULT_CLUSTER_SIZE = 10;
	
	private Folksonomy folksonomy;//folk sonony
	private Map<Integer, Category> clusters;//聚类结果
	private Map<Integer, Map<Integer, Double>> rc_ws;//资源对应于类别的权重	
	private Map<Integer, Map<Integer, Double>> rt_ws;//资源对应于tag的权重向量
	
	@Override
	public void buildRecommender(DataModel dataModel) throws Exception {
		folksonomy = (Folksonomy) dataModel;
		//层次聚类
		System.out.println("层次聚类开始");
		clusters = HACluster.clusterForTag(folksonomy, DEFAULT_CLUSTER_SIZE);
		System.out.println("层次聚类结束");
		//初始化rc_ws列表
		System.out.println("创建Rc_w（资源与类的权重）列表开始");
		initRc_ws();
		System.out.println("创建Rc_w（资源与类的权重）列表结束");
		//初始化rt_ws列表
		System.out.println("创建rt_w（资源与标签的权重）列表开始");
		initRt_ws();
		System.out.println("创建rt_w(资源与标签的权重)列表结束");
	}

	@Override
	public RatingItem[] ratingsForItems(int userId) throws Exception {
		//用户的Tag向量
		System.out.println("创建userVector开始");
		Map<Integer, Double> userVector = buildUserVector(userId);
		System.out.println("创建Uservector 结束");
		
		//用户对某一资源的兴趣度（标准推荐算法）
		System.out.println("创建sur_w（标准算法用户与资源的权重）列表开始");
		Map<Integer, Double> sur_ws = buildSur_ws(userVector);
		System.out.println("创建sur_w(标准算法用户与资源的权重)列表结束");
	
		//用户对某一类别的兴趣度
		System.out.println("创建uc_w（用户与类的权重）列表开始");
		Map<Integer, Double> uc_ws = buildUc_ws(userId);
		System.out.println("创建uc_w（用户与类的权重）列表结束");

		//用户对某一资源的兴趣度(层次聚类产生的)
		System.out.println("创建ur_w（用户与资源的权重）列表开始");
		Map<Integer, Double> ur_ws = buildUr_ws(folksonomy, uc_ws);
		System.out.println("创建ur_w（用户与资源的权重）列表开始");

		//形成results集合
		RatingItem[] results = new RatingItem[ur_ws.size()];
		int i = 0;
		for (Entry<Integer, Double> entry : ur_ws.entrySet()) {
			double weight = entry.getValue() * sur_ws.get(entry.getKey());
			results[i ++] = new RatingItem(entry.getKey(), weight);
		}
		return results;
	}
	
	private Map<Integer, Double> buildUr_ws(Folksonomy folksonomy, Map<Integer, Double> uc_ws) {
		Map<Integer, Double> ur_ws = new HashMap<Integer, Double>();
		for (Entry<Integer, Map<Integer, Double>> entry1 : rc_ws.entrySet()) {
			double weight = 0.0;
			for (Entry<Integer, Double> entry2 : entry1.getValue().entrySet()) {
				if (uc_ws.containsKey(entry2.getKey())) {
					weight += entry2.getValue() * uc_ws.get(entry2.getKey());
				}
			}
			ur_ws.put(entry1.getKey(), weight);
		}
		return ur_ws;
	}
	
	/**
	 * 初始化rc_ws列表
	 * @param folksonomy
	 */
	private void initRc_ws() {
		assert (folksonomy != null);
		//资源对应的类别的计数器
		Map<Integer, Map<Integer, Integer>> rc_cs = new HashMap<Integer, Map<Integer,Integer>>();
		//开始统计
		rc_ws = new HashMap<Integer, Map<Integer,Double>>();
		for (TagPreference tagPreference : folksonomy.getPreferences()) {
			int resourcId = tagPreference.getItemId();
			if (! rc_cs.containsKey(resourcId)) {
				rc_cs.put(resourcId, new HashMap<Integer, Integer>());
			}
			int tagId = tagPreference.getTagId();
			int categoryId = getCategoryIndex(tagId);
			Map<Integer, Integer> map = rc_cs.get(resourcId);
			if (! map.containsKey(categoryId)) {
				map.put(categoryId, 1);
			} else {
				map.put(categoryId, map.get(categoryId) + 1);
			}
		}
		
		//开始计算
		for (Entry<Integer, Map<Integer, Integer>> entry : rc_cs.entrySet()) {
			Map<Integer, Double> map = new HashMap<Integer, Double>();
			Map<Integer, Integer> counterMap = entry.getValue();
			for (Entry<Integer, Integer> entry2 : counterMap.entrySet()) {
				map.put(entry2.getKey(), entry2.getValue() / (double) folksonomy.getPreferences().size());
			}
			rc_ws.put(entry.getKey(), map);
		}
	}
	
	/**
	 * 计算uc_ws列表
	 */
	private Map<Integer, Double> buildUc_ws(int userId) {
		//用户对某一类的关注次数,计数器
		Map<Integer, Integer> uc_cs = new HashMap<Integer, Integer>();
		Map<Integer, Double> uc_ws = new HashMap<Integer, Double>();
		
		//开始统计
		int count = 0;
		for (TagPreference tagPreference : folksonomy.getPreferences()) {
			if (tagPreference.getUerId() == userId) {
				int categoryId = getCategoryIndex(tagPreference.getTagId());
				if (!uc_cs.containsKey(categoryId)) {
					uc_cs.put(categoryId, 1);
				} else {
					uc_cs.put(categoryId, uc_cs.get(categoryId) + 1);
				}
				
				count ++;
			}
		}
		
		//计算权重
		for (Entry<Integer, Integer> entry : uc_cs.entrySet()) {
			uc_ws.put(entry.getKey(), entry.getValue() / (double) count);
		}
		return uc_ws;
	}
	
	/**
	 * 获取关于user的向量（我们使用的就是，user评价的Tag的次数）,和倍数无关
	 * @param userId
	 * @return
	 */
	private Map<Integer, Double> buildUserVector(int userId) {
		Map<Integer, Double> userVector = new HashMap<Integer, Double>();
		for (TagPreference preference : folksonomy.getPreferences()) {
			if (preference.getUerId() == userId) {
				if (! userVector.containsKey(preference.getTagId())) {
					userVector.put(preference.getTagId(), 1.0);
				} else {
					userVector.put(preference.getTagId(), userVector.get(preference.getTagId()) + 1);
				}
			}
		}
		return userVector;
	}
	
	private Map<Integer, Double> buildSur_ws(Map<Integer, Double> userVector) {
		Map<Integer, Double> sur_ws = new HashMap<Integer, Double>();
		
		for (Entry<Integer, Map<Integer, Double>> entry : rt_ws.entrySet()) {
			double weight = HACluster.mutiplyVector(entry.getValue(), userVector);
			weight = weight / (HACluster.normVector(userVector) * HACluster.normVector(entry.getValue()));
			sur_ws.put(entry.getKey(), weight);
		}
		return sur_ws;
	}
	
	/**
	 * 创建Rt_ws(资源标签的权重)列表
	 */
	private void initRt_ws() {
		rt_ws = new HashMap<Integer, Map<Integer,Double>>();
		Map<Integer, Map<Integer, Integer>> rt_cs = new HashMap<Integer, Map<Integer, Integer>>();

		//开始统计
		for (TagPreference preference : folksonomy.getPreferences()) {
			if (! rt_cs.containsKey(preference.getItemId())) {
				rt_cs.put(preference.getItemId(), new HashMap<Integer, Integer>());	
			} 
			Map<Integer, Integer> map = rt_cs.get(preference.getItemId());
			map.put(preference.getTagId(), 1);
		}
		
		//开始计算 (使用频率作为权重值)
		for (Entry<Integer, Map<Integer, Integer>> entry1 : rt_cs.entrySet()) {
			Map<Integer, Double> map = new HashMap<Integer, Double>();
			rt_ws.put(entry1.getKey(), map);
			for (Entry<Integer, Integer> entry2 : entry1.getValue().entrySet()) {
				double weight = entry2.getValue() / (double) entry1.getValue().size();
				map.put(entry2.getKey(), weight);
			}
		}
		
	}
	
	/**
	 * 获取Tag所对应的类别索引号
	 * @param tagId
	 * @return
	 */
	private int getCategoryIndex(int tagId) {
		for (Map.Entry<Integer, Category> entry : clusters.entrySet()) {
			Category category = entry.getValue();
			if (category.contains(tagId)) {
				return entry.getKey();
			}
		}
		
		throw new IllegalArgumentException("tagId is not in clusters");
	}
	
	public static void main(String[] args) throws Exception {
		MovielensTagDataLoader loader = new MovielensTagDataLoader(
				"C:\\Users\\wuyadong\\Desktop\\数据挖掘扩展\\Recommendation\\1.dat", "C:\\Users\\wuyadong\\Desktop\\数据挖掘扩展\\Recommendation\\tags.dat");
		Folksonomy data = loader.getFolksonomy();
		
		PRBC_Hierarchy prbc = new PRBC_Hierarchy();
		prbc.buildRecommender(data); 
		int[] values = prbc.recommendItems(127);
	}
}
