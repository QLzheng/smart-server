package com.tcl.smart.server.bean;

/**
 * 百度百科中的表格列
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeTableTd {
	private String className;

	private String html;

	private String text;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
}
