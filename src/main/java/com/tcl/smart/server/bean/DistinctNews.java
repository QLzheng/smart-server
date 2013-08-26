package com.tcl.smart.server.bean;


/**
 * @author fanjie
 * @date 2013-5-16
 */
public class DistinctNews {
	private String title;
	private String description;
	private Double count = new Double(1);

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
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

	@Override
	public String toString() {
		return "¡¾" + count + "¡¿" + title + "(" + description + "): ";
	}
}
