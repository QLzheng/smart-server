package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-10
 */
@XmlRootElement(name = "program")
@XmlAccessorType(XmlAccessType.NONE)
public class Program {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String date;
	@XmlAttribute(name = "start_time")
	private String start_time;
	@XmlAttribute(name = "end_time")
	private String end_time;
	@XmlAttribute(name = "wiki_id")
	private String wiki_id;
	@XmlAttribute(name = "wiki_cover")
	private String wiki_cover;
	@XmlAttribute
	private String hasvideo;
	@XmlAttribute
	private String source;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getWiki_id() {
		return wiki_id;
	}

	public void setWiki_id(String wiki_id) {
		this.wiki_id = wiki_id;
	}

	public String getWiki_cover() {
		return wiki_cover;
	}

	public void setWiki_cover(String wiki_cover) {
		this.wiki_cover = wiki_cover;
	}

	public String getHasvideo() {
		return hasvideo;
	}

	public void setHasvideo(String hasvideo) {
		this.hasvideo = hasvideo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}