package com.tcl.smart.server.util;

import java.io.Serializable;

public enum KeyType implements Serializable {
	DIRECTOR("����"), ACTOR("��Ա"), WRITER("����"), HOST("������"), GUSET("�α�"), WIKI_TITLE("��Ŀ��"), PROGRAM_NAME("��Ŀ��"), TEAM("���"), TAG("��ǩ"), CONTENT("��Ŀ����");

	private String name;

	private KeyType(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
