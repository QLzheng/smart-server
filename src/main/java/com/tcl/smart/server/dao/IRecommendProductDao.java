package com.tcl.smart.server.dao;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.List;

import com.tcl.smart.server.bean.YihaodianProductBean;

public interface IRecommendProductDao {

	void putRecProductById(String epgId, List<String> products, List<String> keys);
	List<YihaodianProductBean> getRecProductById(String epgId, int size);
	void putAllRecResults(Object2ObjectOpenHashMap<String, List<String>> maps);
	Object2ObjectOpenHashMap<String, List<YihaodianProductBean>> getAllRecResults();
    void removeRecResultByEpgId(String epgId);
    void dropAll();
    List<String> getAllEpgId();
}
