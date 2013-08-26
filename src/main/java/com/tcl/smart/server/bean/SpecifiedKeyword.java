package com.tcl.smart.server.bean;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.tcl.smart.server.util.Constants;

/**
 * 运维系统针对节目的关键字定制
 * 
 * @author fanjie
 * @date 2013-7-15
 */
public class SpecifiedKeyword {
	@Id
	private String epgId;
	private String keyword;
	private String startDateStr;
	private String startTimeStr;
	private String endTimeStr;
	private String programName;
	private String wikiId;
	private String channelCode;
	private String channelName;
	private String channelId;
	private String channelType;
	private String channelLogo;
	private String wikiCover;

	@Override
	public String toString() {
		return "keyword:" + keyword + ", channelName:" + channelName + ", programName:" + programName + ", time:" + startDateStr + " " + startTimeStr + " -- " + endTimeStr;
	}

	public String getEpgId() {
		return epgId;
	}

	public void setEpgId(String epgId) {
		this.epgId = epgId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public void setStartTime(Date startTime) {
		this.startDateStr = Constants.parseTimeDay(startTime);
		this.startTimeStr = Constants.parseTime2(startTime);
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public void setEndTime(Date endTime) {
		this.endTimeStr = Constants.parseTime2(endTime);
	}

	public String getWikiId() {
		return wikiId;
	}

	public void setWikiId(String wikiId) {
		this.wikiId = wikiId;
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

	public String getWikiCover() {
		return wikiCover;
	}

	public void setWikiCover(String wikiCover) {
		this.wikiCover = wikiCover;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
}
