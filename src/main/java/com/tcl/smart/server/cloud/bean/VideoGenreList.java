package com.tcl.smart.server.cloud.bean;

import java.util.List;

public class VideoGenreList {
	private int genre;
	private List<VideoBean> list;
	public int getGenre() {
		return genre;
	}
	public void setGenre(int genre) {
		this.genre = genre;
	}
	public List<VideoBean> getList() {
		return list;
	}
	public void setList(List<VideoBean> list) {
		this.list = list;
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n       genre ="+genre);
		sb.append("\n       list ="+list);
		return sb.toString();

	}
}
