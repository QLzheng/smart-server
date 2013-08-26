package com.tcl.smart.server.dao;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.List;

import com.tcl.smart.server.bean.NewsSearchRstItem;

public interface IRecommendNewsDao {

	void putRecommendNewsResult(String epgId, List<String> news);
	List<NewsSearchRstItem> getRecommendResults(String epgId, int size);
	void putRecommendNewsResults(Object2ObjectOpenHashMap<String, List<String>> maps);
	Object2ObjectOpenHashMap<String, List<NewsSearchRstItem>> getAllRecResults();
    void removeRecResultByEpgId(String epgId);
    void dropAll();
    List<String> getAllEpgId();
}
