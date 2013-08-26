package com.tcl.smart.server.model;

public enum RecName {

	REC_NEWS("news"),REC_BAIKE("baike"), REC_PRODUCT("product"), REC_VIDEO("video");

	private String name;

	private RecName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
