/**
 * Copyright    : Copyright (c) 2006. Wintim Corp. All rights reserved
 * File Summary : 
 * Create time  : 2013-5-5
 */

package Rsme.cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * �������������������,���е����ĵ㣬����ʹ�õ���Average distance
 * @author wang yuliang
 *
 */
public class Category {
	final private Set<Integer> tagSet;
	private Map<Integer, Double> centerPointer;
	
	public Category() {
		tagSet = new HashSet<Integer>();
		centerPointer = new HashMap<Integer, Double>();
	}
	
	void setCenter(Map<Integer, Double> center) {
		this.centerPointer = center;
	}
	
	void addTag(Integer tag) {
		tagSet.add(tag);
	}
	
	/**
	 * ����һ��Tag�����Ҹ���CenterPointer
	 * @param tag
	 * @param vector
	 */
	void addTagAndUpdate(Integer tag, Map<Integer, Double> vector) {
		if (vector == null) {
			throw new NullPointerException("vector");
		}
		for (Integer key: centerPointer.keySet()) {
			double sum = centerPointer.get(key) * tagSet.size();
			if (vector.containsKey(key)) {
				sum += vector.get(key);
			}
			centerPointer.put(key, sum / (tagSet.size() + 1));
		}
		
		for (Integer key : vector.keySet()) {
			if (! centerPointer.containsKey(key)) {
				double sum = vector.get(key);
				centerPointer.put(key, sum / (tagSet.size() + 1));
			}
		}
		tagSet.add(tag);
	}
	/**
	 * ���������кϲ��������¼�������ֵ
	 * @param otherTagSet
	 * @param otherCenter
	 */
	void merge(Category category) {
		if (category == null) {
			throw new NullPointerException("category");
		}
		Set<Integer> otherTagSet = category.getTagSet();
		Map<Integer, Double> otherCenter = category.getCenterPointer();
		if (otherTagSet == null) {
			throw new NullPointerException("otherTagSet");
		}
		if (otherCenter == null) {
			throw new NullPointerException("other Center");
		}
		for (Integer key: centerPointer.keySet()) {
			double sum = centerPointer.get(key) * tagSet.size();
			if (otherCenter.containsKey(key)) {
				sum += otherCenter.get(key) * otherTagSet.size();
			}
			centerPointer.put(key, sum / (tagSet.size() + otherCenter.size()));
		}
		
		for (Integer key : otherCenter.keySet()) {
			if (! centerPointer.containsKey(key)) {
				double sum = otherCenter.get(key) * otherTagSet.size();
				centerPointer.put(key, sum / (tagSet.size() + otherCenter.size()));
			}
		}
		this.tagSet.addAll(otherTagSet);
	}
	
	Set<Integer> getTagSet() {
		return this.tagSet;
	}
	
	Map<Integer, Double> getCenterPointer() {
		return this.centerPointer;
	}
	
	/**
	 * ����Ƿ����������tag
	 * @param itemId
	 * @return
	 */
	public boolean contains(int tagId) {
		if (tagSet == null) {
			throw new IllegalStateException("tagSet");
		}
		return tagSet.contains(tagId);
	}
	
	public int size() {
		return tagSet.size();
	}
}
