package com.tcl.smart.server.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.tcl.smart.server.dao.ILogInformation;
import com.tcl.smart.server.model.LogModel;

public class LogInformation implements ILogInformation{

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;
	private String dbName;
	private static final String TIME = "time";
	private static final String TYPE = "type";
	private static final String OPERATION = "operation";
	private static final String LEVEL = "level";
	private static final String STATUS = "status";
	private static final String INFORMATION = "information";
	
	public void init(){
		col = mongoTemplate.getCollection(dbName);
	}
	
	public LogInformation(String dbName) {
		this.dbName = dbName;
	}
	
	@Override
	public void putLogInfo(LogModel log) {
		if(null == log)
			return ;
		
		BasicDBObject obj = new BasicDBObject();
		obj.put(TIME, log.getTime());
		obj.put(TYPE, log.getType());
		obj.put(OPERATION, log.getOperation());
		obj.put(LEVEL, log.getLevel());
		obj.put(STATUS, log.getStatus());
		obj.put(INFORMATION, log.getInformation());
		
		col.save(obj);
	}

	@Override
	public List<LogModel> getLogByParam(Date time, String type, String operation,
			String level) {
		
		List<LogModel> logs = null;
		
		Query query = new Query();
		if(null != time)
			query.addCriteria(Criteria.where(TIME).gte(time));
		if(null != type)
			query.addCriteria(Criteria.where(TYPE).is(type));
		if(null != operation)
			query.addCriteria(Criteria.where(OPERATION).is(operation));
		if(null != level)
			query.addCriteria(Criteria.where(LEVEL).is(level));
		
		logs = mongoTemplate.find(query, LogModel.class, dbName);
		return logs;
		
	}

}
