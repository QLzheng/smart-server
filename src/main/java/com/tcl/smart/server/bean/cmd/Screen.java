package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-10
 */
@XmlRootElement(name = "screen")
@XmlAccessorType(XmlAccessType.NONE)
public class Screen {
	@XmlAttribute
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCover() {
		if (url == null || url.trim().equals("") || url.indexOf('/') == -1) {
			return null;
		}
		return url.substring(url.lastIndexOf('/') + 1, url.length());
	}
}