package com.tcl.smart.server.bean.cmd;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;

/**
 * @author fanjie
 * @date 2013-4-10
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.NONE)
public class TvListRequestAckData {
	@XmlAttribute
	private String language;
	@XmlAttribute
	private String total;
	@XmlElement
	private List<Channel> channel;

	@Id
	/** 节目单日期：xxxx-xx-xx */
	private String day;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<Channel> getChannel() {
		return channel;
	}

	public void setChannel(List<Channel> channel) {
		this.channel = channel;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getProgramNum() {
		if (channel == null || channel.size() == 0)
			return 0;
		int size = 0;
		for (Channel c : channel) {
			size += (c.getProgram() == null ? 0 : c.getProgram().size());
		}
		return size;
	}
}