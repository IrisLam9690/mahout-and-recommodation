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
	private Map<Integer, Category> clusters;//������
	private Map<Integer, Map<Integer, Double>> rc_ws;//��Դ��Ӧ������Ȩ��	
	private Map<Integer, Map<Integer, Double>> rt_ws;//��Դ��Ӧ��tag��Ȩ������
	
	@Override
	public void buildRecommender(DataModel dataModel) throws Exception {
		folksonomy = (Folksonomy) dataModel;
		//��ξ���
		System.out.println("��ξ��࿪ʼ");
		clusters = HACluster.clusterForTag(folksonomy, DEFAULT_CLUSTER_SIZE);
		System.out.println("��ξ������");
		//��ʼ��rc_ws�б�
		System.out.println("����Rc_w����Դ�����Ȩ�أ��б�ʼ");
		initRc_ws();
		System.out.println("����Rc_w����Դ�����Ȩ�أ��б����");
		//��ʼ��rt_ws�б�
		System.out.println("����rt_w����Դ���ǩ��Ȩ�أ��б�ʼ");
		initRt_ws();
		System.out.println("����rt_w(��Դ���ǩ��Ȩ��)�б����");
	}

	@Override
	public RatingItem[] ratingsForItems(int userId) throws Exception {
		//�û���Tag����
		System.out.println("����userVector��ʼ");
		Map<Integer, Double> userVector = buildUserVector(userId);
		System.out.println("����Uservector ����");
		
		//�û���ĳһ��Դ����Ȥ�ȣ���׼�Ƽ��㷨��
		System.out.println("����sur_w����׼�㷨�û�����Դ��Ȩ�أ��б�ʼ");
		Map<Integer, Double> sur_ws = buildSur_ws(userVector);
		System.out.println("����sur_w(��׼�㷨�û�����Դ��Ȩ��)�б����");
	
		//�û���ĳһ������Ȥ��
		System.out.println("����uc_w���û������Ȩ�أ��б�ʼ");
		Map<Integer, Double> uc_ws = buildUc_ws(userId);
		System.out.println("����uc_w���û������Ȩ�أ��б����");

		//�û���ĳһ��Դ����Ȥ��(��ξ��������)
		System.out.println("����ur_w���û�����Դ��Ȩ�أ��б�ʼ");
		Map<Integer, Double> ur_ws = buildUr_ws(folksonomy, uc_ws);
		System.out.println("����ur_w���û�����Դ��Ȩ�أ��б�ʼ");

		//�γ�results����
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
	 * ��ʼ��rc_ws�б�
	 * @param folksonomy
	 */
	private void initRc_ws() {
		assert (folksonomy != null);
		//��Դ��Ӧ�����ļ�����
		Map<Integer, Map<Integer, Integer>> rc_cs = new HashMap<Integer, Map<Integer,Integer>>();
		//��ʼͳ��
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
		
		//��ʼ����
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
	 * ����uc_ws�б�
	 */
	private Map<Integer, Double> buildUc_ws(int userId) {
		//�û���ĳһ��Ĺ�ע����,������
		Map<Integer, Integer> uc_cs = new HashMap<Integer, Integer>();
		Map<Integer, Double> uc_ws = new HashMap<Integer, Double>();
		
		//��ʼͳ��
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
		
		//����Ȩ��
		for (Entry<Integer, Integer> entry : uc_cs.entrySet()) {
			uc_ws.put(entry.getKey(), entry.getValue() / (double) count);
		}
		return uc_ws;
	}
	
	/**
	 * ��ȡ����user������������ʹ�õľ��ǣ�user���۵�Tag�Ĵ�����,�ͱ����޹�
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
	 * ����Rt_ws(��Դ��ǩ��Ȩ��)�б�
	 */
	private void initRt_ws() {
		rt_ws = new HashMap<Integer, Map<Integer,Double>>();
		Map<Integer, Map<Integer, Integer>> rt_cs = new HashMap<Integer, Map<Integer, Integer>>();

		//��ʼͳ��
		for (TagPreference preference : folksonomy.getPreferences()) {
			if (! rt_cs.containsKey(preference.getItemId())) {
				rt_cs.put(preference.getItemId(), new HashMap<Integer, Integer>());	
			} 
			Map<Integer, Integer> map = rt_cs.get(preference.getItemId());
			map.put(preference.getTagId(), 1);
		}
		
		//��ʼ���� (ʹ��Ƶ����ΪȨ��ֵ)
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
	 * ��ȡTag����Ӧ�����������
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
				"C:\\Users\\wuyadong\\Desktop\\�����ھ���չ\\Recommendation\\1.dat", "C:\\Users\\wuyadong\\Desktop\\�����ھ���չ\\Recommendation\\tags.dat");
		Folksonomy data = loader.getFolksonomy();
		
		PRBC_Hierarchy prbc = new PRBC_Hierarchy();
		prbc.buildRecommender(data); 
		int[] values = prbc.recommendItems(127);
	}
}
