package com.tcl.smart.server.bean;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tcl.smart.server.util.Constants;

public class EpgModel {
	private static final int MAX_TITLE_LEN = 4;

	private ObjectId id;
	@Field("channel_code")
	private String channelCode;
	@Field("created_at")
	private Date createdAt;
	private String date;
	@Field("start_time")
	private Date startTime;
	private String name;
	@Field("end_time")
	private Date endTime;
	private Boolean publish;
	private List<String> tags;
	private String time;
	private String souId;
	@Field("updated_at")
	private Date updatedAt;
	@Field("wiki_id")
	private String wikiId;
	private String channelName;
	private String channelId;
	private String channelType;
	private String channelLogo;
	private String wikiCover;

	@Override
	public String toString() {
		String s = Constants.parseTime2(startTime) + " -- " + Constants.parseTime2(endTime) + ": " + name;
		return s;
	}

	public String getName() {
		return name;
	}

	public String getNameEtc() {
		if (name == null)
			return null;
		if (name.length() > MAX_TITLE_LEN) {
			return name.substring(0, MAX_TITLE_LEN) + "..";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getDateTime() {
		return date;
	}

	public void setDateTime(String dateTime) {
		this.date = dateTime;
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

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getSouId() {
		return souId;
	}

	public void setSouId(String souId) {
		this.souId = souId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getWikiId() {
		return wikiId;
	}

	public void setWikiId(String wikiId) {
		this.wikiId = wikiId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	public EpgModelDistinctId getDistinctId() {
		EpgModelDistinctId distinctId = new EpgModelDistinctId();
		distinctId.setName(name);
		distinctId.setWikiId(wikiId);
		return distinctId;
	}

	public String getParsedName() {
		return getDistinctId().getParsedName();
	}
}
