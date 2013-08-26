package com.tcl.smart.server.model;

import java.util.Date;

public class TrainerLog {
	
	private Date startTime;
	private Date endTime;
	private int total_epg_size;
	private int total_training_size;
	private String name;
	
	public TrainerLog(Date startTime, Date endTime, int total_epg_size,
			int total_training_size, String name) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.total_epg_size = total_epg_size;
		this.total_training_size = total_training_size;
		this.setName(name);
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getTotal_epg_size() {
		return total_epg_size;
	}

	public void setTotal_epg_size(int total_epg_size) {
		this.total_epg_size = total_epg_size;
	}

	public int getTotal_training_size() {
		return total_training_size;
	}

	public void setTotal_training_size(int total_training_size) {
		this.total_training_size = total_training_size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
