package com.tcl.smart.server.update;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.IFeatureNewsDao;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;

public class FeatureNewsUpdate implements IUpdate{

	private INewsSearchRstItemDao programNewsDao;
	private IFeatureNewsDao featureNewsDao;
	private static final int hours = 1;
	
	
	public FeatureNewsUpdate(INewsSearchRstItemDao programNewsDao,IFeatureNewsDao featureNewsDao) {
		this.programNewsDao = programNewsDao;
		this.featureNewsDao = featureNewsDao;
	}


	@Override
	public void update() {
		
		incrementUpdate();
		
//		UpdateAll();
	}

	
	
	/*Increment update feature-news related table*/
	public void incrementUpdate(){
		
		/*1.Get the latest update news */
		List<NewsSearchRstItem> news = programNewsDao.findNewsSearchRstItemsInHours(hours);
		
		/*2.Transfer the news to feature-news relation*/
		Object2ObjectOpenHashMap<String, List<String>> featureNewsMap = new Object2ObjectOpenHashMap<String, List<String>>();
		
		for(NewsSearchRstItem item : news){
			List<String> keys = item.getProgram_wiki_keys();
			
			/*Get the description of news */
			String description = item.getDescription();
			
			if(keys != null && !keys.isEmpty()){
				for(String s : keys){
					
					/*Make sure the news contain the key */
					if(description.indexOf(s) >=0){
						if(featureNewsMap.containsKey(s)){
							List<String> newsList = featureNewsMap.get(s);
							newsList.add(item.get_id());
						}else{
							List<String> newsList = new ArrayList<String>();
							newsList.add(item.get_id());
							featureNewsMap.put(s, newsList);
						}
					}
				}
			}
		}
		
		/*Save feature-news related table to database*/
		Iterator<Entry<String, List<String>>> iter = featureNewsMap.object2ObjectEntrySet().fastIterator();
		while(iter.hasNext()){
			Entry<String, List<String>> entry = iter.next();
			String featureId = entry.getKey();
			List<String> newsList = entry.getValue();
			
			/*Get the news of the feature that have exist int database*/
			List<String> tem = featureNewsDao.getNewsByFeature(featureId);
			/*If the feature has exist in database */
			if(tem!=null && !tem.isEmpty()){
				ObjectOpenHashSet<String> set = new ObjectOpenHashSet<String>();
				for(String id : tem){
					set.add(id);
				}
				
				/*Add the feature update news*/
				for(String id : newsList){
					if(!set.contains(id))
						tem.add(id);
				}
				/*Save to database*/
				featureNewsDao.putFeatureNews(featureId, tem);
			}else{
				featureNewsDao.putFeatureNews(featureId, newsList);
			}
		}
	}
	
	
	/*Update all feature-new related table*/
	public void UpdateAll(){
		
		/*Get all news information*/
		List<NewsSearchRstItem> news = programNewsDao.findAll();
		Object2ObjectOpenHashMap<String, List<String>> featureNewsMap = new Object2ObjectOpenHashMap<String, List<String>>();
	
		int i =0;
		/*Split the news to feature-news related table*/
		for(NewsSearchRstItem item : news){
			List<String> keys = item.getProgram_wiki_keys();
			
			System.out.println("Doing news : " + i++);
			
			/*Get the description of news */
			String description = item.getDescription();
			
			if(keys != null && !keys.isEmpty()){
				for(String s : keys){
					/*Make sure the news contain the key */
					if(description.indexOf(s) >=0){
						if(featureNewsMap.containsKey(s)){
							List<String> newsList = featureNewsMap.get(s);
							newsList.add(item.get_id());
						}else{
							List<String> newsList = new ArrayList<String>();
							newsList.add(item.get_id());
							featureNewsMap.put(s, newsList);
						}
					}
				}
			}
		}
		
		/*Save feature-news related table to database*/
		featureNewsDao.putAllFeaturesNews(featureNewsMap);
	}


	@Override
	public void updateByEpgId(String id) {
		// TODO Auto-generated method stub
		
	}
}
