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
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.IRecommendProductDao;
import com.tcl.smart.server.dao.IYihaodianProductDao;
import com.tcl.smart.server.util.KeyType;
import com.tcl.smart.server.util.KeywordTypeInMovie;

public class RcommendProductDao implements IRecommendProductDao{
	
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;
	private String dbName;
	private IYihaodianProductDao productDao;
	private IMovieDao movieDao;
	private IEpgModelDao epgDao;
	
	private static final String EPG_ID="_id";
	private static final String PRODECT_LIST="wiki";
	private static final String RELATED_KEY="key";
	private static final DBObject RECOMMENDAITON_PRODUCT = BasicDBObjectBuilder.start().add(PRODECT_LIST,1).add(RELATED_KEY,1).get();

	public void init(){
		col = mongoTemplate.getCollection(dbName);
	}
	
	public RcommendProductDao(String dbName, IYihaodianProductDao productDao,IMovieDao movieDao, IEpgModelDao epgDao) {
		this.dbName = dbName;
		this.productDao = productDao;
		this.movieDao = movieDao;
		this.epgDao = epgDao;
	}

	@Override
	public void putRecProductById(String epgId, List<String> products, List<String> keys) {
		
		BasicDBObject obj = new BasicDBObject();
		obj.put(EPG_ID, epgId);
		
		if(products != null && !products.isEmpty()){
			obj.put(PRODECT_LIST, products);
			obj.put(RELATED_KEY, keys);
			col.update(new BasicDBObject().append(EPG_ID, epgId), obj,true,false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YihaodianProductBean> getRecProductById(String epgId, int size) {
		
		List<YihaodianProductBean> productsList = new ArrayList<YihaodianProductBean>();
		BasicDBObject query = new BasicDBObject();
		query.put(EPG_ID, epgId);
		DBObject obj = col.findOne(query, RECOMMENDAITON_PRODUCT);
		if(obj != null){
			 List<String> productIds = (List<String>) obj.get(PRODECT_LIST);
			 List<String> keywords = (List<String>) obj.get(RELATED_KEY);
			 for(int i=0;i < Math.min(size, productIds.size()); i++){
				 String key = keywords.get(i);
				 YihaodianProductBean pro = productDao.findYihaodianProductById(productIds.get(i));
				 if(pro != null){
					 pro = addExplanation(epgId, pro , key);
					 productsList.add(pro);
				 }
			 }
		}
		/* If the number less than the recNum then add extra default product */
//		if (productsList.size() < size) {
//			int n = size - productsList.size();
//			List<YihaodianProductBean> list = productDao.getDefaultRecProducts(n);
//			productsList.addAll(list);
//		}
		return productsList;
	}

	
	private YihaodianProductBean addExplanation(String epgId, YihaodianProductBean pro, String key) {
		
		EpgModel epg = epgDao.findEpgModelById(epgId);
		String wikiId = epg.getWikiId();
		List<KeyType> keyType = new ArrayList<KeyType>();
		if(wikiId != null && wikiId!=""){
			MovieModel movie = movieDao.getMovieById(wikiId);
			if(movie != null){
				List<KeyType> type = KeywordTypeInMovie.getKeyType(key, movie);
				if(type != null && !type.isEmpty())
				   keyType.addAll(type);
			}
		}
		else{
			keyType.add(KeyType.PROGRAM_NAME);
		}
		pro.setKeyType(keyType);
		pro.setSearchKey(key);
		return pro;
	}

	
	@Override
	public void putAllRecResults(Object2ObjectOpenHashMap<String, List<String>> maps) {
		Iterator<Object2ObjectOpenHashMap.Entry<String, List<String>>> iter = maps.object2ObjectEntrySet().fastIterator();
		while(iter.hasNext()){
			Object2ObjectOpenHashMap.Entry<String, List<String>> entry = iter.next();
			String epgId = entry.getKey();
			List<String> list = entry.getValue();
			putRecProductById(epgId, list, null);
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Object2ObjectOpenHashMap<String, List<YihaodianProductBean>> getAllRecResults() {
		Object2ObjectOpenHashMap<String, List<YihaodianProductBean>> maps = new Object2ObjectOpenHashMap<String, List<YihaodianProductBean>>();
		DBCursor cur = col.find();
		while(cur.hasNext()){
			DBObject obj = cur.next();
			String epgId = (String) obj.get(EPG_ID);
			List<String> productIds = (List<String>) obj.get(PRODECT_LIST);
			List<YihaodianProductBean> products = new ArrayList<YihaodianProductBean>();
			for(String id : productIds){
				YihaodianProductBean pro = productDao.findYihaodianProductById(id);
				 if(pro != null)
					 products.add(pro);
			 }
			maps.put(epgId, products);
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
}
