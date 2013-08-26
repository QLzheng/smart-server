package com.tcl.smart.server.dao.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;
import com.tcl.smart.server.dao.IRecommendNewsDao;
import com.tcl.smart.server.util.KeyType;
import com.tcl.smart.server.util.KeywordTypeInMovie;

public class RecommendNewsDao implements IRecommendNewsDao{

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;
	private String dbName;
	private INewsSearchRstItemDao newsDao;
	private IMovieDao movieDao;
	
	private static final String EPG_ID="_id";
	private static final String NEWS_LIST="rec_news";
	private static final DBObject RECOMMENDAITON_LIST = BasicDBObjectBuilder.start().add(NEWS_LIST,1).get();
	
	
	public void init(){
		col = mongoTemplate.getCollection(dbName);
	}
	
	public RecommendNewsDao(String dbName, INewsSearchRstItemDao newsDao,IMovieDao movieDao) {
		this.dbName = dbName;
		this.newsDao = newsDao;
		this.movieDao = movieDao;
	}
	
	@Override
	public void putRecommendNewsResult(String epgId, List<String> news) {
		
		BasicDBObject obj = new BasicDBObject();
		obj.put(EPG_ID, epgId);
		
		if(news != null && !news.isEmpty()){
//			obj.put(NEWS_LIST, (DBObject)mongoTemplate.getConverter().convertToMongoType(news));
//			col.save(obj);
			obj.put(NEWS_LIST, news);
			col.update(new BasicDBObject().append(EPG_ID, epgId), obj,true,false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NewsSearchRstItem> getRecommendResults(String epgId, int size) {
		
		List<NewsSearchRstItem> newsList = new ArrayList<NewsSearchRstItem>();
		BasicDBObject query = new BasicDBObject();
		query.put(EPG_ID, epgId);
		
		DBObject obj = col.findOne(query, RECOMMENDAITON_LIST);
		if(obj != null){
		    List<String> newsIds = (List<String>) obj.get(NEWS_LIST);
		    newsIds = newsIds.subList(0, Math.min(size,newsIds.size()));
		    List<NewsSearchRstItem> temp = newsDao.findNewsSearchRstItemsByIds(newsIds);
		    
		    if(temp != null && !temp.isEmpty()){
		    	Object2ObjectOpenHashMap<String,NewsSearchRstItem> map = new Object2ObjectOpenHashMap<String,NewsSearchRstItem>();
			    for(NewsSearchRstItem item : temp){
			    	map.put(item.get_id(), item);
			    }
			    for(String id : newsIds){
			    	NewsSearchRstItem item = map.get(id);
			    	if(item != null)
			    	    newsList.add(item);
			    }
			    /*Add explanation for recommender item */
		    	newsList = addExplanation(newsList);
		        return newsList;
		    }
		}
		return Collections.emptyList();
	}

	@Override
	public void putRecommendNewsResults(Object2ObjectOpenHashMap<String, List<String>> maps) {
		
		Iterator<Object2ObjectOpenHashMap.Entry<String, List<String>>> iter = maps.object2ObjectEntrySet().fastIterator();
		while(iter.hasNext()){
			Object2ObjectOpenHashMap.Entry<String, List<String>> entry = iter.next();
			String epgId = entry.getKey();
			List<String> newsList = entry.getValue();
			putRecommendNewsResult(epgId, newsList);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object2ObjectOpenHashMap<String, List<NewsSearchRstItem>> getAllRecResults() {
		
		Object2ObjectOpenHashMap<String, List<NewsSearchRstItem>> maps = new Object2ObjectOpenHashMap<String, List<NewsSearchRstItem>>();
		DBCursor cur = col.find();
		while(cur.hasNext()){
			DBObject obj = cur.next();
			String epgId = (String) obj.get(EPG_ID);
			List<String> newsIds = (List<String>) obj.get(NEWS_LIST);
			 List<NewsSearchRstItem> news = newsDao.findNewsSearchRstItemsByIds(newsIds);
			maps.put(epgId, news);
		}
		return maps;
	}

	@Override
	public void removeRecResultByEpgId(String epgId) {
		col.remove(BasicDBObjectBuilder.start().add(EPG_ID, epgId).get());
	}

	@Override
	public void dropAll() {
		if(col != null){
			col.drop();
		}
		col = mongoTemplate.getCollection(dbName);  
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllEpgId() {
		return col.distinct(EPG_ID);
	} 

	
	private List<NewsSearchRstItem> addExplanation(List<NewsSearchRstItem> newsList){
		
		for(NewsSearchRstItem item : newsList){
			List<KeyType> keyType = new ArrayList<KeyType>();
			String wikiId = item.getProgram_wiki_id();
			List<String> key = item.getProgram_wiki_keys();
			if(wikiId != null && wikiId!=""){
				MovieModel movie = movieDao.getMovieById(wikiId);
				if(movie != null){
					List<KeyType> type = KeywordTypeInMovie.getKeyType(key.get(0), movie);
					if(type != null && !type.isEmpty())
					   keyType.addAll(type);
				}
			}else{
				keyType.add(KeyType.PROGRAM_NAME);
			}
			item.setKeyType(keyType);
		}
		return newsList;
	}
}
