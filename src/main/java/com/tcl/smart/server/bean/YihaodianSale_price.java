package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * 1号店价格及其对应的省份 <sale_price> <region region="湖北">20.80</region> （1号店价格及其对应的省份）
 * </sale_price>
 * 
 * @author Jerryangly
 * 
 */
public class YihaodianSale_price implements Serializable {
	private static final long serialVersionUID = 1382491589778985714L;
	private String region;
	private double price;

	public YihaodianSale_price(String region, double price) {
		this.price = price;
		this.region = region;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
