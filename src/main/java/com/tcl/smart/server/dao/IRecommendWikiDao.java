package com.tcl.smart.server.dao;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.List;

import com.tcl.smart.server.bean.BaikeBean;


public interface IRecommendWikiDao {

	void putRecommenderWiki(String epgId, List<String> wik);
	List<BaikeBean> getRecommenderWiki(String epgId, int size);
	void putAllRecommenderWiki(Object2ObjectOpenHashMap<String, List<String>> maps);
	Object2ObjectOpenHashMap<String, List<BaikeBean>> getAllRecommenderWiki();
	void removeWikiByEpgId(String epgId);
    void dropAll();
    List<String> getAllEpgId();
	
}
