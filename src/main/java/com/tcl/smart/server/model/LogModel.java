package com.tcl.smart.server.model;

import java.util.Date;

public class LogModel {
	
	private Date time;
	private String type;
	private String operation;
	private String status;
	private String information;
	private String level;
	
	
	public LogModel(Date time, String type, String operation, String status,
			String information, String level) {
		this.time = time;
		this.type = type;
		this.operation = operation;
		this.status = status;
		this.information = information;
		this.level = level;
	}


	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getOperation() {
		return operation;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getInformation() {
		return information;
	}


	public void setInformation(String information) {
		this.information = information;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}
}
