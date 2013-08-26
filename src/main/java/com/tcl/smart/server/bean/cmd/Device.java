package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-9
 */
@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.NONE)
public class Device {
	@XmlAttribute
	private String devmodel = "hs16";
	@XmlAttribute
	private String dnum = "1234";
	@XmlAttribute
	private String didtoken = "x";
	@XmlAttribute
	private String ver = "12.3.4";

	public String getDevmodel() {
		return devmodel;
	}

	public void setDevmodel(String devmodel) {
		this.devmodel = devmodel;
	}

	public String getDnum() {
		return dnum;
	}

	public void setDnum(String dnum) {
		this.dnum = dnum;
	}

	public String getDidtoken() {
		return didtoken;
	}

	public void setDidtoken(String didtoken) {
		this.didtoken = didtoken;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}
}
