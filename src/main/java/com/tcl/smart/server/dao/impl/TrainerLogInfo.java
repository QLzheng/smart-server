package com.tcl.smart.server.dao.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tcl.smart.server.dao.ITrainerLogInfo;
import com.tcl.smart.server.model.RecName;
import com.tcl.smart.server.model.TrainerLog;

public class TrainerLogInfo implements ITrainerLogInfo{

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;
	private String dbName;
	private static final String REC_NAME ="rec_name";
	private static final String STARTIME = "star_time";
	private static final String ENDTIME = "end_time";
	private static final String TOTALEPGSIZE = "total_epg_size";
	private static final String TOTALTRAININGSIZE = "total_training_size";
	
	
	public void init(){
		col = mongoTemplate.getCollection(dbName);
	}
	
	public TrainerLogInfo(String dbName) {
		super();
		this.dbName = dbName;
	}
	
	@Override
	public void putTrainerLogInfo(TrainerLog logInfo) {
		if(null == logInfo)
			return;
		
		BasicDBObject obj = new BasicDBObject();
		obj.put(STARTIME, logInfo.getStartTime());
		obj.put(ENDTIME, logInfo.getEndTime());
		obj.put(REC_NAME, logInfo.getName());
		obj.put(TOTALEPGSIZE, logInfo.getTotal_epg_size());
		obj.put(TOTALTRAININGSIZE, logInfo.getTotal_training_size());
		
		col.save(obj);
	}
	
	
	@Override
	public List<TrainerLog> getTrainerLogInfoByName(RecName name) {
		
		List<TrainerLog> list = null;
		Query query = new Query(Criteria.where(REC_NAME).is(name));
		query.sort().on(STARTIME,Order.DESCENDING);
		list = mongoTemplate.find(query, TrainerLog.class, dbName);
		
		return list;
	}
	
	
	@Override
	public Object2ObjectOpenHashMap<String, List<TrainerLog>> getAllTrainerLogInfo() {
		
		Object2ObjectOpenHashMap<String, List<TrainerLog>> maps = new Object2ObjectOpenHashMap<String, List<TrainerLog>>();
		DBCursor cur = col.find();
		
		while(cur.hasNext()){
			DBObject obj = cur.next();
			String name = (String) obj.get(REC_NAME);
			TrainerLog log = DBObjectToModel(obj);
			
			List<TrainerLog> logs = null;
			logs = maps.get(name);
			if(null == logs){
				logs = new ArrayList<TrainerLog>();
				logs.add(log);
			}else{
				logs.add(log);
			}
		}
		
		/** sort the log information by star time*/
		Iterator<Object2ObjectOpenHashMap.Entry<String, List<TrainerLog>>> iter = maps.object2ObjectEntrySet().fastIterator();
		while(iter.hasNext()){
			Object2ObjectOpenHashMap.Entry<String, List<TrainerLog>> entry = iter.next();
			List<TrainerLog> list = entry.getValue();
			Collections.sort(list,COMPARATOR_LOG_BYTIME_DESC);
		}
		
		return maps;
	}
	
	private TrainerLog DBObjectToModel(DBObject obj){
		TrainerLog log = null;
		if(null == obj)
			return log;
		
		String name = (String) obj.get(REC_NAME);
		Date starT = (Date) obj.get(STARTIME);
		Date endT = (Date) obj.get(ENDTIME);
		int num1 = (int) obj.get(TOTALEPGSIZE);
		int num2 = (int) obj.get(TOTALTRAININGSIZE);
		log = new TrainerLog(starT, endT, num1, num2, name);
		
		return log;
	}
	
	private Comparator<TrainerLog> COMPARATOR_LOG_BYTIME_DESC = new Comparator<TrainerLog>(){
		@Override
		public int compare(TrainerLog item1, TrainerLog item2){
			long t1 = item1.getStartTime().getTime();
			long t2 = item2.getStartTime().getTime();
			return t1<t2 ? 1 : t1>t2 ? -1 : 0;
		}
	};
}
