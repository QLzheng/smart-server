package com.tcl.smart.server.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author fanjie
 * @date 2013-3-5
 */
@JsonIgnoreProperties(value = { "status", "message" })
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
public class RecommendResults {
	@XmlElement
	private String success = "false";

	@XmlElement
	private ErrorCode error;

	@XmlElement
	private List<RecommendObject> objects = new ArrayList<RecommendObject>();

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public ErrorCode getError() {
		return error;
	}

	public void setError(ErrorCode error) {
		this.error = error;
	}

	public List<RecommendObject> getObjects() {
		return objects;
	}

	public void setObjects(List<RecommendObject> objects) {
		this.objects = objects;
	}
}
