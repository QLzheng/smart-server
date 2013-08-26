package com.tcl.smart.server.cloud.bean;

import java.util.List;

public class VideoSourceList {
	private int source;
	private List<VideoGenreList> genres;
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public List<VideoGenreList> getGenres() {
		return genres;
	}
	public void setGenres(List<VideoGenreList> genres) {
		this.genres = genres;
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n    source ="+source);
		sb.append("\n    genres ="+genres);
		return sb.toString();
	}
}
