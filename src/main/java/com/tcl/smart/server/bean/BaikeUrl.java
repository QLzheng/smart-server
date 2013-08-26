package com.tcl.smart.server.bean;

import org.springframework.data.annotation.Id;

/**
 * 百度百科爬虫对词条、链接及状态的记录
 * 
 * @author fanjie
 * @date 2013-4-27
 */
public class BaikeUrl {
	@Id
	private String name;
	private String baiduPath;
	private String result;

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BaikeUrl) {
			BaikeUrl other = (BaikeUrl) obj;
			return name.equals(other.getName());
		}
		return name.equals(obj);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaiduPath() {
		return baiduPath;
	}

	public void setBaiduPath(String baiduPath) {
		this.baiduPath = baiduPath;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
