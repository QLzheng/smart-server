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
public class TvWikiInfoRequest {
	@XmlAttribute
	private String website = "http://iptv.cedock.com";

	@XmlElement
	private TvWikiInfoRequestParameter parameter = new TvWikiInfoRequestParameter();

	public TvWikiInfoRequest() {
	}

	public TvWikiInfoRequest(String wiki_id) {
		parameter.getData().setWiki_id(wiki_id);
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public TvWikiInfoRequestParameter getParameter() {
		return parameter;
	}

	public void setParameter(TvWikiInfoRequestParameter parameter) {
		this.parameter = parameter;
	}
}
