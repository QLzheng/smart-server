package com.tcl.smart.server.util;

import java.io.Serializable;

public enum KeyType implements Serializable {
	DIRECTOR("导演"), ACTOR("演员"), WRITER("作者"), HOST("主持人"), GUSET("嘉宾"), WIKI_TITLE("节目名"), PROGRAM_NAME("节目名"), TEAM("球队"), TAG("标签"), CONTENT("节目内容");

	private String name;

	private KeyType(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
