package com.tcl.smart.server.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.tcl.smart.server.bean.BaikeUrl;
import com.tcl.smart.server.dao.IBaikeUrlDao;

/**
 * @author fanjie
 * @date 2013-4-27
 */
public class BaikeUrlDao implements IBaikeUrlDao {
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private String col = "baike_url";

	private DBCollection dbCol;

	public void init() {
		dbCol = mongoTemplate.getCollection(col);
		dbCol.ensureIndex(new BasicDBObject().append("_id", 1));
		dbCol.ensureIndex(new BasicDBObject().append("baiduPath", 1));
	}

	@Override
	public List<BaikeUrl> findAllBaikeUrls() {
		List<BaikeUrl> baikeUrls = mongoTemplate.findAll(BaikeUrl.class, col);
		return baikeUrls;
	}

	@Override
	public BaikeUrl findBaikeUrlByName(String name) {
		Query query = new Query(Criteria.where("_id").is(name));
		BaikeUrl baikeUrl = mongoTemplate.findOne(query, BaikeUrl.class, col);
		return baikeUrl;
	}

	@Override
	public List<BaikeUrl> findBaikeUrlsByResult(String result) {
		Query query = new Query(Criteria.where("result").is(result));
		List<BaikeUrl> baikeUrls = mongoTemplate.find(query, BaikeUrl.class, col);
		return baikeUrls;
	}

	@Override
	public void insertBaikeUrl(BaikeUrl baikeUrl) {
		mongoTemplate.save(baikeUrl, col);
	}

	@Override
	public void updateBaikeUrl(BaikeUrl baikeUrl) {
		if (findBaikeUrlByName(baikeUrl.getName()) != null) {
			Query query = new Query(Criteria.where("_id").is(baikeUrl.getName()));
			mongoTemplate.remove(query, col);
			mongoTemplate.save(baikeUrl, col);
		}
	}
}
