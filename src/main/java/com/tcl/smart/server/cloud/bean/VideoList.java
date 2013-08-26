package com.tcl.smart.server.cloud.bean;

import java.util.List;

public class VideoList {
	private int source_number;
	private List<VideoSourceList> sources;
	public int getSource_number() {
		return source_number;
	}
	public void setSource_number(int source_number) {
		this.source_number = source_number;
	}
	public List<VideoSourceList> getSources() {
		return sources;
	}
	public void setSources(List<VideoSourceList> sources) {
		this.sources = sources;
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n source_number ="+source_number);
		sb.append("\n sources ="+sources);
		return sb.toString();
	}
}
