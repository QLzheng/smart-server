package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-9
 */
@XmlRootElement(name = "parameter")
@XmlAccessorType(XmlAccessType.NONE)
public class TvWikiInfoRequestParameter {
	@XmlAttribute
	private String type = "GetWikiInfo";
	@XmlAttribute
	private String language = "zh-CN";
	@XmlElement
	private Device device = new Device();
	@XmlElement
	private User user = new User();
	@XmlElement
	private TvWikiInfoData data = new TvWikiInfoData();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TvWikiInfoData getData() {
		return data;
	}

	public void setData(TvWikiInfoData data) {
		this.data = data;
	}
}
