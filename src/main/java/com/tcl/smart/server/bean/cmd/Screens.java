package com.tcl.smart.server.bean.cmd;

import java.util.ArrayList;
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
@XmlRootElement(name = "screens")
@XmlAccessorType(XmlAccessType.NONE)
public class Screens {
	@XmlAttribute
	private String num;
	@XmlElement
	private List<Screen> screen;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public void setScreen(List<Screen> screen) {
		this.screen = screen;
	}

	public List<String> getScreenCovers() {
		if (screen == null || screen.size() == 0)
			return null;
		List<String> covers = new ArrayList<String>();
		for (Screen s : screen) {
			covers.add(s.getCover());
		}
		return covers;
	}

}