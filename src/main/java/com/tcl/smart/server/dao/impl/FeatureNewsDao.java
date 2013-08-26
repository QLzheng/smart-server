package com.tcl.smart.server.dao.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap.Entry;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.tcl.smart.server.dao.IFeatureNewsDao;

public class FeatureNewsDao implements IFeatureNewsDao{


	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;
	private String dbName;
	
	private static final String FEATURE_ID="_id";
	private static final String NEWS_LIST="feature_news";
	private static final DBObject FEATURE_NEWS_LIST = BasicDBObjectBuilder.start().add(NEWS_LIST,1).get();
	
	public void init(){
		col = mongoTemplate.getCollection(dbName);
	}
	
	
	public FeatureNewsDao(String dbName) {
		this.dbName = dbName;
	}
	
	
	@Override
	public void putFeatureNews(String feature, List<String> newsList) {
		
		BasicDBObject obj = new BasicDBObject();
		obj.put(FEATURE_ID, feature);
		
		if(newsList != null && !newsList.isEmpty()){
			obj.put(NEWS_LIST, newsList);
			col.update(new BasicDBObject().append(FEATURE_ID, feature), obj,true,false);
		}
	}

	@Override
	public void putAllFeaturesNews(Object2ObjectOpenHashMap<String, List<String>> featureNewsMap) {
		
		Iterator<Entry<String, List<String>>> iter = featureNewsMap.object2ObjectEntrySet().fastIterator();
		while(iter.hasNext()){
			Entry<String, List<String>> entry = iter.next();
			String featureId = entry.getKey();
			List<String> newsList = entry.getValue();
			putFeatureNews(featureId,newsList);
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNewsByFeature(String feature) {
		
		BasicDBObject query = new BasicDBObject();
		query.put(FEATURE_ID, feature);
		DBObject obj = col.findOne(query, FEATURE_NEWS_LIST);
		if(obj != null)
			return (List<String>) obj.get(NEWS_LIST);
		
		return Collections.emptyList();
	}

}
