package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * 百度百科的开放分类
 * 
 * @author fanjie
 * @date 2013-5-8
 */
public class BaikeCategory implements Serializable {
	private static final long serialVersionUID = -5539447172515477592L;

	private String className;

	private String href;

	private String content;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCluster() {
		if (className != null && className.contains("open-tag")) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return (isCluster() ? "[category]" : "") + content;
	}
}
