package com.tcl.smart.server.bean;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;

/**
 * @author fanjie
 * @date 2013-8-19
 */
public class HotBaiduNewsBean implements Serializable {
	private static final long serialVersionUID = -6932429307259678860L;

	/** 排行榜区域 */
	private long index;
	@Id
	private String keyword;
	private String keywordUrl;
	private long heat;
	private boolean isHeatUp;
	private boolean isNew;

	/** 相关新闻区域 */
	private HotBaiduNewsRelatedNewsArea relatedNewsArea;

	private Date fetchTime;

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public long getHeat() {
		return heat;
	}

	public void setHeat(long heat) {
		this.heat = heat;
	}

	public boolean isHeatUp() {
		return isHeatUp;
	}

	public void setHeatUp(boolean isHeatUp) {
		this.isHeatUp = isHeatUp;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public Date getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(Date fetchTime) {
		this.fetchTime = fetchTime;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeywordUrl() {
		return keywordUrl;
	}

	public void setKeywordUrl(String keywordUrl) {
		this.keywordUrl = keywordUrl;
	}

	public HotBaiduNewsRelatedNewsArea getRelatedNewsArea() {
		return relatedNewsArea;
	}

	public void setRelatedNewsArea(HotBaiduNewsRelatedNewsArea relatedNewsArea) {
		this.relatedNewsArea = relatedNewsArea;
	}
}
