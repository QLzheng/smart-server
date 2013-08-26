package com.tcl.smart.server.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.dao.IBaikeBeanDao;

/**
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeBeanDao implements IBaikeBeanDao {
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private String col = "baike";

	private DBCollection dbCol;

	public void init() {
		dbCol = mongoTemplate.getCollection(col);
		dbCol.ensureIndex(new BasicDBObject().append("_id", 1));
		dbCol.ensureIndex(new BasicDBObject().append("name", 1));
	}

	@Override
	public List<BaikeBean> findAllBaikeBeans() {
		List<BaikeBean> baikes = mongoTemplate.findAll(BaikeBean.class, col);
		return baikes;
	}

	public BaikeBean findBaikeBeanById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		BaikeBean baike = mongoTemplate.findOne(query, BaikeBean.class, col);
		return baike;
	}

	@Override
	public List<BaikeBean> findBaikeBeansByName(String name) {
		Query query = new Query(Criteria.where("name").is(name));
		List<BaikeBean> baikes = mongoTemplate.find(query, BaikeBean.class, col);
		return baikes;
	}

	@Override
	public boolean existBaikeBean(BaikeBean baike) {
		Query query = new Query(Criteria.where("name").is(baike.getName()));
		long count = mongoTemplate.count(query, col);
		return count > 0;
	}

	@Override
	public boolean existStrictBaikeBean(BaikeBean baike) {
		Query query = null;
		if (baike.getHeader() != null) {
			query = new Query(Criteria.where("name").is(baike.getName()).and("header.index").is(baike.getHeader().getIndex()));
		} else {
			query = new Query(Criteria.where("name").is(baike.getName()));
		}
		long count = mongoTemplate.count(query, col);
		return count > 0;
	}

	@Override
	public void insertBaikeBean(BaikeBean baike) {
		mongoTemplate.save(baike, col);
	}

	@Override
	public void updateBaikeBean(BaikeBean baike) {
		Query query = new Query(Criteria.where("_id").is(baike.getId()));
		mongoTemplate.remove(query, col);
		mongoTemplate.save(baike, col);
	}
}
