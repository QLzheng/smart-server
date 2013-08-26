package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-10
 */
@XmlRootElement(name = "media")
@XmlAccessorType(XmlAccessType.NONE)
public class Media {
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String title;
	@XmlAttribute
	private String model;
	@XmlElement
	private Info info;
	@XmlElement
	private String description;
	@XmlElement
	private Posters posters;
	@XmlElement
	private Screens screens;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Posters getPosters() {
		return posters;
	}

	public void setPosters(Posters posters) {
		this.posters = posters;
	}

	public Screens getScreens() {
		return screens;
	}

	public void setScreens(Screens screens) {
		this.screens = screens;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}