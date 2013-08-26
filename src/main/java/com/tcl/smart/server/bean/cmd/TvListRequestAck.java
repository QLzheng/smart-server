package com.tcl.smart.server.bean.cmd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanjie
 * @date 2013-4-10
 */
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
public class TvListRequestAck {
	@XmlElement
	private RequestAckError error;
	@XmlElement
	private TvListRequestAckData data;

	public RequestAckError getError() {
		return error;
	}

	public void setError(RequestAckError error) {
		this.error = error;
	}

	public TvListRequestAckData getData() {
		return data;
	}

	public void setData(TvListRequestAckData data) {
		this.data = data;
	}
}