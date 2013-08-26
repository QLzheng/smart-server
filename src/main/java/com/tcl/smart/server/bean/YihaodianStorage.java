package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * <storage> <region region="湖北">1</region> （是否有库存及其省份，1表示有库存，0表示没库存） </storage>
 * 
 * @author Jerryangly
 * 
 */
public class YihaodianStorage implements Serializable {
	private static final long serialVersionUID = 7792039032190172925L;
	private String region;
	private Boolean storage;

	public YihaodianStorage(String region, Boolean storage) {
		this.region = region;
		this.storage = storage;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Boolean getStorage() {
		return storage;
	}

	public void setStorage(Boolean storage) {
		this.storage = storage;
	}

}
