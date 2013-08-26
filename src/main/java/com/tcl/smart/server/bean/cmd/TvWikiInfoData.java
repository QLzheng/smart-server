package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-9
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.NONE)
public class TvWikiInfoData {
	@XmlAttribute(name = "wiki_id")
	private String wiki_id;

	public String getWiki_id() {
		return wiki_id;
	}

	public void setWiki_id(String wiki_id) {
		this.wiki_id = wiki_id;
	}
}
