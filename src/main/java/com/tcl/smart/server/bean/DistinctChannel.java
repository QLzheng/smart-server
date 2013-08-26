package com.tcl.smart.server.bean;

/**
 * @author fanjie
 * @date 2013-7-9
 */
public class DistinctChannel {
	private String name;
	private String channel_code;
	private String date;
	private String channelId;
	private String channelType;
	private String channelLogo;

	@Override
	public String toString() {
		return name + "(code:" + channel_code + ", id:" + channelId + ", date:" + date + ", type:" + channelType + ", logo:" + channelLogo + ")";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getChannelLogo() {
		return channelLogo;
	}

	public void setChannelLogo(String channelLogo) {
		this.channelLogo = channelLogo;
	}
}
