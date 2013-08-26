package com.tcl.smart.server.bean;

import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * 物品收藏记录
 * 
 * @author fanjie
 * @date 2013-5-20
 */
public class FavorateProduct {
	public final static String DEFAULT_USER = "test";
	@Id
	private String id;
	private String user = DEFAULT_USER;
	private List<String> productIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<String> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}
}
