package com.tcl.smart.server.bean.cmd;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-10
 */
@XmlRootElement(name = "posters")
@XmlAccessorType(XmlAccessType.NONE)
public class Posters {
	@XmlAttribute
	private String num;
	@XmlElement
	private List<Poster> poster;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public List<Poster> getPoster() {
		return poster;
	}

	public void setPoster(List<Poster> poster) {
		this.poster = poster;
	}

	public String getCover() {
		if (poster == null || poster.size() == 0) {
			return null;
		}
		String url = poster.get(0).getUrl();
		if (url == null || url.trim().equals("") || url.indexOf('/') == -1) {
			return null;
		}
		return url.substring(url.lastIndexOf('/') + 1, url.length());
	}
}