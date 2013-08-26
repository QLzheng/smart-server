package com.tcl.smart.server.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.dao.IYihaodianProductDao;

public class YihaodianProductDao implements IYihaodianProductDao {
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;
	private DBCollection col;
	private DBCollection col_key_products;
	private String dbColName;
	private String key_products_db_colName;

	public YihaodianProductDao(String dbColName, String key_products_db_colName) {
		this.dbColName = dbColName;
		this.key_products_db_colName = key_products_db_colName;
	}

	public void init() {
		col = mongoTemplate.getCollection(dbColName);
		col.ensureIndex(new BasicDBObject().append("_id", 1));
		col.ensureIndex(new BasicDBObject().append("name", 1));
		col.ensureIndex(new BasicDBObject().append("title", 1).append("brand_name", 1).append("subtitle", 1).append("product_desc", 1).append("character.value", 1));
		col_key_products = mongoTemplate.getCollection(key_products_db_colName);
	}

	@Override
	public List<YihaodianProductBean> findAllYihaodianProducts() {
		List<YihaodianProductBean> products = mongoTemplate.findAll(YihaodianProductBean.class, dbColName);
		return products;
	}

	public void removeYihaodianProductById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		mongoTemplate.remove(query, dbColName);
	}

	@Override
	public YihaodianProductBean findYihaodianProductById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		YihaodianProductBean product = mongoTemplate.findOne(query, YihaodianProductBean.class, dbColName);
		return product;
	}

	@Override
	public List<YihaodianProductBean> findYihaodianProductByIds(List<String> ids) {
		Query query = new Query(Criteria.where("_id").in(ids));
		List<YihaodianProductBean> products = mongoTemplate.find(query, YihaodianProductBean.class, dbColName);
		return products;
	}

	@Override
	public List<YihaodianProductBean> findYihaodianProductByTitle(String title) {
		List<YihaodianProductBean> products = mongoTemplate.find(new Query(new Criteria("title").regex(".*?" + title + ".*")), YihaodianProductBean.class, dbColName);
		return products;
	}

	@Override
	public void updateProduct_desc(String id, String desc) {
		Query query = new Query(Criteria.where("_id").is(id));
		Update update = new Update().set("product_desc", desc);
		mongoTemplate.updateFirst(query, update, dbColName);
	}

	@Override
	public void insertYihaodianProductBean(YihaodianProductBean product) {
		mongoTemplate.save(product, dbColName);
	}

	/**
	 * check some values of keys to find the product be related to feature.
	 */
	@Override
	public List<YihaodianProductBean> findYihaodianProductByFeature(String feature) {
		Query query = new Query(new Criteria().orOperator(new Criteria("title").regex(".*?" + feature + ".*"), new Criteria("brand_name").regex(".*?" + feature + ".*"),
				new Criteria("subtitle").regex(".*?" + feature + ".*"), new Criteria("product_desc").regex(".*?" + feature + ".*"),
				new Criteria("character.value").regex(".*?" + feature + ".*")));
		List<YihaodianProductBean> products = mongoTemplate.find(query, YihaodianProductBean.class, dbColName);
		return products;
	}

	@Override
	public List<YihaodianProductBean> findYihaodianProductByFeature(String feature, int size) {
		Query query = new Query(new Criteria().orOperator(new Criteria("title").regex(".*?" + feature + ".*"), new Criteria("brand_name").regex(".*?" + feature + ".*"),
				new Criteria("subtitle").regex(".*?" + feature + ".*"), new Criteria("product_desc").regex(".*?" + feature + ".*"),
				new Criteria("character.value").regex(".*?" + feature + ".*"))).limit(size);
		List<YihaodianProductBean> products = mongoTemplate.find(query, YihaodianProductBean.class, dbColName);
		return products;
	}

	@Override
	public List<YihaodianProductBean> getDefaultRecProducts(int number) {
		// random
		int i = (int) (Math.random() * 20) * number;
		List<YihaodianProductBean> products = mongoTemplate.find(new Query().limit(number).skip(i), YihaodianProductBean.class, dbColName);
		return products;
	}

	@Override
	public List<String> findYihaodianProductIdsByFeature(String feature) {
		List<String> idList = new ArrayList<String>();
		DBObject query = new BasicDBObject();
		query.put("_id", feature);
		DBObject temp = col_key_products.findOne(query);

		/* if got key->productList,return */
		if (temp != null) {
			idList = (List<String>) temp.get("list");
			return idList;
		} else {
			/* if not got key->productList,find and store */
			List<YihaodianProductBean> products = findYihaodianProductByFeature(feature);
			if (products != null && !products.isEmpty()) {
				for (YihaodianProductBean pro : products) {
					idList.add(pro.getId());
				}
				DBObject putdb = new BasicDBObject();
				putdb.put("_id", feature);
				putdb.put("list", idList);
				col_key_products.save(putdb);
				return idList;
			}
			/* save the null result also in case of check again */
			DBObject putdb = new BasicDBObject();
			putdb.put("_id", feature);
			putdb.put("list", null);
			col_key_products.save(putdb);
			return null;
		}
	}

	@Override
	public void resetDB() {
		if (col != null) {
			col.drop();
		}
		col = mongoTemplate.getCollection(dbColName);
		col.ensureIndex(new BasicDBObject().append("_id", 1));
		col.ensureIndex(new BasicDBObject().append("name", 1));
		if (col_key_products != null) {
			col_key_products.drop();
		}
		col_key_products = mongoTemplate.getCollection(key_products_db_colName);
	}

}
