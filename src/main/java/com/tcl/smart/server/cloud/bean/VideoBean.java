package com.tcl.smart.server.cloud.bean;

import java.util.List;

public class VideoBean {
	private int id;
	private String name;
	private int genre;
	private int hits;
	private String trend;
	private String source;
	private List<String> actor;
	private List<String> director;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGenre() {
		return genre;
	}
	public void setGenre(int genre) {
		this.genre = genre;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public String getTrend() {
		return trend;
	}
	public void setTrend(String trend) {
		this.trend = trend;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public List<String> getActor() {
		return actor;
	}
	public void setActor(List<String> actor) {
		this.actor = actor;
	}
	public List<String> getDirector() {
		return director;
	}
	public void setDirector(List<String> director) {
		this.director = director;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n          name ="+name);
		sb.append(" id ="+id);
		sb.append(" genre ="+genre);
		sb.append(" trend ="+trend);
		sb.append(" hits ="+hits);
		sb.append(" source ="+source);
		sb.append(" actor ="+actor);
		sb.append(" director ="+director);
		return sb.toString();
	}

}
