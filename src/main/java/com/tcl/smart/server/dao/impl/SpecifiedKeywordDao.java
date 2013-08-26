package com.tcl.smart.server.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tcl.smart.server.bean.SpecifiedKeyword;
import com.tcl.smart.server.dao.ISpecifiedKeywordDao;

/**
 * @author fanjie
 * @date 2013-7-9
 */
public class SpecifiedKeywordDao implements ISpecifiedKeywordDao {
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private String col = "specified_keyword";

	@Override
	public List<SpecifiedKeyword> findAllSpecifiedKeywords() {
		List<SpecifiedKeyword> specifiedKeywords = mongoTemplate.findAll(SpecifiedKeyword.class, col);
		return specifiedKeywords;
	}

	@Override
	public SpecifiedKeyword findSpecifiedKeywordByEpgId(String epgId) {
		Query query = new Query(Criteria.where("_id").is(epgId));
		SpecifiedKeyword specifiedKeyword = mongoTemplate.findOne(query, SpecifiedKeyword.class, col);
		return specifiedKeyword;
	}

	@Override
	public void insertSpecifiedKeyword(SpecifiedKeyword specifiedKeyword) {
		mongoTemplate.save(specifiedKeyword, col);
	}

	@Override
	public void removeSpecifiedKeyword(String epgId) {
		Query query = new Query(Criteria.where("_id").is(epgId));
		mongoTemplate.remove(query, col);
	}
}
