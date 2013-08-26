package com.tcl.smart.server.dao;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.List;

public interface IFeatureNewsDao {

	void putFeatureNews(String feature, List<String> newsList);
	void putAllFeaturesNews(Object2ObjectOpenHashMap<String,List<String>> featureNewsMap);
	List<String> getNewsByFeature(String feature);
	
}
