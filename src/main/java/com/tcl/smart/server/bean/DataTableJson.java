package com.tcl.smart.server.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanjie
 * @date 2013-7-11
 */
@XmlAccessorType(XmlAccessType.NONE)
public class DataTableJson {
	@XmlElement
	private List<String[]> aaData = new ArrayList<String[]>();

	public List<String[]> getAaData() {
		return aaData;
	}

	public void setAaData(List<String[]> aaData) {
		this.aaData = aaData;
	}

	public void addRecord(String[] record) {
		this.aaData.add(record);
	}
}