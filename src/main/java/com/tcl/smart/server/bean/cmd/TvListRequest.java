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
@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.NONE)
public class TvListRequest {
	@XmlAttribute
	private String website = "http://iptv.cedock.com";

	@XmlElement
	private TvListRequestParameter parameter = new TvListRequestParameter();

	public TvListRequest() {
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public TvListRequestParameter getParameter() {
		return parameter;
	}

	public void setParameter(TvListRequestParameter parameter) {
		this.parameter = parameter;
	}
}
