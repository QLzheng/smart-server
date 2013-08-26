package com.tcl.smart.server.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-3-6
 */
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
public class BehaviourResult {
	@XmlElement
	private String success;

	@XmlElement
	private ErrorCode error;

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
}
