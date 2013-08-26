package com.tcl.smart.server.bean;

import java.util.Date;
import java.util.List;

public class YihaodianIndex {
	private String version;
	private Date updatetime;
	private String category_path;
	private String promotion_path;
	private String product_desc_path;
	private List<String> product_descs;
	private String product_path;
	private List<String> products;
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("version").append(":").append(version).append("\n");
		sb.append("updatetime").append(":").append(updatetime).append("\n");
		sb.append("category_path").append(":").append(category_path).append("\n");
		sb.append("promotion_path").append(":").append(promotion_path).append("\n");
		sb.append("product_desc_path").append(":").append(product_desc_path);
		if(product_descs!=null) sb.append(":size=").append(String.valueOf(product_descs.size())).append("\n");
		else sb.append("\n");
		sb.append("product_path").append(":").append(product_path);
		if(products!=null) sb.append(":size=").append(String.valueOf(products.size())).append("\n");
		else sb.append("\n");
		return sb.toString();		
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getCategory_path() {
		return category_path;
	}
	public void setCategory_path(String category_path) {
		this.category_path = category_path;
	}
	public String getPromotion_path() {
		return promotion_path;
	}
	public void setPromotion_path(String promotion_path) {
		this.promotion_path = promotion_path;
	}
	public String getProduct_desc_path() {
		return product_desc_path;
	}
	public void setProduct_desc_path(String product_desc_path) {
		this.product_desc_path = product_desc_path;
	}
	public List<String> getProduct_descs() {
		return product_descs;
	}
	public void setProduct_descs(List<String> product_descs) {
		this.product_descs = product_descs;
	}
	public String getProduct_path() {
		return product_path;
	}
	public void setProduct_path(String product_path) {
		this.product_path = product_path;
	}
	public List<String> getProducts() {
		return products;
	}
	public void setProducts(List<String> products) {
		this.products = products;
	}
	
}
