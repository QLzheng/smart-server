package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * ∞Ÿ∂»Õº∆¨¡¥Ω”
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeImage implements Serializable {
	private static final long serialVersionUID = 303645392477839575L;

	private String width;

	private String height;

	private String url;

	private String title;

	private String alt;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return title + "(" + alt + ")" + "(" + width + "|" + height + "): " + url;
	}
}
