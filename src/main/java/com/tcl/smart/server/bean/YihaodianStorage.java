package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * <storage> <region region="����">1</region> ���Ƿ��п�漰��ʡ�ݣ�1��ʾ�п�棬0��ʾû��棩 </storage>
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
