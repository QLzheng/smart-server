package com.tcl.smart.server.dao.impl;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tcl.smart.server.bean.FavorateProduct;
import com.tcl.smart.server.dao.IFavorateProductDao;

/**
 * @author fanjie
 * @date 2013-5-20
 */
public class FavorateProductDao implements IFavorateProductDao {
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private String col = "favorate_products";

	@Override
	public FavorateProduct findFavorateProductsByDefaultUser() {
		return findFavorateProductsByUser(FavorateProduct.DEFAULT_USER);
	}

	public void insertFavorateProductByDefaultUser(String productId) {
		insertFavorateProduct(FavorateProduct.DEFAULT_USER, productId);
	}

	public void removeFavorateProductByDefaultUser(String productId) {
		removeFavorateProductByUser(FavorateProduct.DEFAULT_USER, productId);
	}

	public long countFavorateProductsByDefaultUser() {
		return countFavorateProductsByUser(FavorateProduct.DEFAULT_USER);
	}

	public long countFavorateProductsByUser(String user) {
		Query query = new Query(Criteria.where("user").is(user));
		FavorateProduct favorate = mongoTemplate.findOne(query, FavorateProduct.class, col);
		if (favorate == null || favorate.getProductIds() == null || favorate.getProductIds().size() == 0)
			return 0;
		return favorate.getProductIds().size();
	}

	@Override
	public FavorateProduct findFavorateProductsByUser(String user) {
		Query query = new Query(Criteria.where("user").is(user));
		FavorateProduct favorate = mongoTemplate.findOne(query, FavorateProduct.class, col);
		return favorate;
	}

	public void insertFavorateProduct(String user, String productId) {
		Query query = new Query(Criteria.where("user").is(user));
		FavorateProduct existFavorate = mongoTemplate.findOne(query, FavorateProduct.class, col);
		if (existFavorate != null) {
			if (!existFavorate.getProductIds().contains(productId)) {
				existFavorate.getProductIds().add(productId);
			}
			mongoTemplate.remove(query, col);
			mongoTemplate.save(existFavorate, col);
		} else {
			existFavorate = new FavorateProduct();
			existFavorate.setUser(user);
			existFavorate.setProductIds(Arrays.asList(productId));
			mongoTemplate.save(existFavorate, col);
		}
	}

	public void removeFavorateProductByUser(String user, String productId) {
		Query query = new Query(Criteria.where("user").is(user));
		FavorateProduct favorate = mongoTemplate.findOne(query, FavorateProduct.class, col);
		if (favorate != null) {
			favorate.getProductIds().remove(productId);
			mongoTemplate.remove(query, col);
			mongoTemplate.save(favorate, col);
		}
	}
}
