package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * 百度百科中的一个段落
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikePara implements Serializable {
	private static final long serialVersionUID = 4221438053033334003L;

	private String html;

	private String text;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
