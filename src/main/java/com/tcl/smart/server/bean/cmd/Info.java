package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-10
 */
@XmlRootElement(name = "info")
@XmlAccessorType(XmlAccessType.NONE)
public class Info {
	@XmlAttribute
	private String director;
	@XmlAttribute
	private String actors;
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String area;
	@XmlAttribute
	private String language;
	@XmlAttribute
	private String score;
	@XmlAttribute
	private String playdate;
	@XmlAttribute
	private String praise;
	@XmlAttribute
	private String dispraise;
	@XmlAttribute
	private String source;
	@XmlAttribute
	private String prefer;

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getPlaydate() {
		return playdate;
	}

	public void setPlaydate(String playdate) {
		this.playdate = playdate;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public String getDispraise() {
		return dispraise;
	}

	public void setDispraise(String dispraise) {
		this.dispraise = dispraise;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPrefer() {
		return prefer;
	}

	public void setPrefer(String prefer) {
		this.prefer = prefer;
	}
}