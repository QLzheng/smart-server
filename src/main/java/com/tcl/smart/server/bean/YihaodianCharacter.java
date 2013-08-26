package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * 一号店 商品属性 <character>（商品属性） <name>类目</name> <value>纸尿裤</value> </character>
 * 
 * @author Jerryangly
 * 
 */
public class YihaodianCharacter implements Serializable {
	private static final long serialVersionUID = -1941945541255789202L;
	private String name;
	private String value;

	public YihaodianCharacter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
