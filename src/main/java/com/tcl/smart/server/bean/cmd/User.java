package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-9
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class User {
	@XmlAttribute
	private String huanid = "1234";
	@XmlAttribute
	private String token = "x";
	@XmlAttribute
	private String ver = "2";

	public String getHuanid() {
		return huanid;
	}

	public void setHuanid(String huanid) {
		this.huanid = huanid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

}
