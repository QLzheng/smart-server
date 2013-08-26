package com.tcl.smart.server.dao.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
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
import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.dao.IBaikeBeanDao;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.IRecommendWikiDao;
import com.tcl.smart.server.util.KeyType;
import com.tcl.smart.server.util.KeywordTypeInMovie;

public class RecommendWikiDao implements IRecommendWikiDao{
	
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;
	private String dbName;
	private IBaikeBeanDao baikeDao;
	private IMovieDao movieDao;
	private IEpgModelDao epgDao;
	
	private static final String EPG_ID="_id";
	private static final String WIKI_LIST="wiki";
	private static final DBObject RECOMMENDAITON_WIKI = BasicDBObjectBuilder.start().add(WIKI_LIST,1).get();
	
	
	public void init(){
		col = mongoTemplate.getCollection(dbName);
	}
	
	
	public RecommendWikiDao(String dbName,IBaikeBeanDao baikeDao, IMovieDao movieDao, IEpgModelDao epgDao) {
		this.dbName = dbName;
		this.baikeDao = baikeDao;
		this.movieDao = movieDao;
		this.epgDao = epgDao;
	}


	@Override
	public void putRecommenderWiki(String epgId, List<String> wiki) {
		
		BasicDBObject obj = new BasicDBObject();
		obj.put(EPG_ID, epgId);
		
		if(wiki != null && !wiki.isEmpty()){
			obj.put(WIKI_LIST, wiki);
			col.update(new BasicDBObject().append(EPG_ID, epgId), obj,true,false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaikeBean> getRecommenderWiki(String epgId, int size) {
		
		List<BaikeBean> wikis = new ArrayList<BaikeBean>();
		BasicDBObject query = new BasicDBObject();
		query.put(EPG_ID, epgId);
		DBObject obj = col.findOne(query, RECOMMENDAITON_WIKI);
		if(obj != null){
			 List<String> wikiIds = (List<String>) obj.get(WIKI_LIST);
			 for(String id : wikiIds){
				 BaikeBean bk = baikeDao.findBaikeBeanById(id);
				 if(bk != null){
					 bk = addExplanation(epgId, bk);
					 wikis.add(bk);
				 }
			 }
			 return wikis;
		}
		return null;
	}

	
	private BaikeBean addExplanation(String epgId, BaikeBean bk) {
		
		EpgModel epg = epgDao.findEpgModelById(epgId);
		String wikiId = epg.getWikiId();
		String name = bk.getName();
		List<KeyType> keyType = new ArrayList<KeyType>();
		if(wikiId != null && wikiId!=""){
			MovieModel movie = movieDao.getMovieById(wikiId);
			if(movie != null){
				List<KeyType> type = KeywordTypeInMovie.getKeyType(name, movie);
				if(type != null && !type.isEmpty())
				   keyType.addAll(type);
			}
		}
		else{
			keyType.add(KeyType.PROGRAM_NAME);
		}
		bk.setKeyType(keyType);
		return bk;
	}
	


	@Override
	public void putAllRecommenderWiki(Object2ObjectOpenHashMap<String, List<String>> maps) {
		
		Iterator<Object2ObjectOpenHashMap.Entry<String, List<String>>> iter = maps.object2ObjectEntrySet().fastIterator();
		while(iter.hasNext()){
			Object2ObjectOpenHashMap.Entry<String, List<String>> entry = iter.next();
			String epgId = entry.getKey();
			List<String> newsList = entry.getValue();
			putRecommenderWiki(epgId, newsList);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object2ObjectOpenHashMap<String, List<BaikeBean>> getAllRecommenderWiki() {
		
		Object2ObjectOpenHashMap<String, List<BaikeBean>> maps = new Object2ObjectOpenHashMap<String, List<BaikeBean>>();
		DBCursor cur = col.find();
		while(cur.hasNext()){
			DBObject obj = cur.next();
			String epgId = (String) obj.get(EPG_ID);
			List<String> wikiIds = (List<String>) obj.get(WIKI_LIST);
			List<BaikeBean> wikis = new ArrayList<BaikeBean>();
			for(String id : wikiIds){
				 BaikeBean bk = baikeDao.findBaikeBeanById(id);
				 if(bk != null)
					 wikis.add(bk);
			 }
			maps.put(epgId, wikis);
		}
		return maps;
	}


	@Override
	public void removeWikiByEpgId(String epgId) {
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

}
