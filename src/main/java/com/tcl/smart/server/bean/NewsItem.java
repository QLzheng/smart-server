package com.tcl.smart.server.bean;

import java.util.Date;
import com.tcl.smart.server.util.Constants;

/**
 * 新闻条目数据模型
 * 
 * @author fanjie
 * @date 2013-4-1
 */
public class NewsItem extends BaseNewsItem {
	private static final long serialVersionUID = 3322518765798958317L;
	private String classify_id;
	private String content;
	private Date pubDate;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.parseTime(pubDate)).append(" ").append(title).append("(").append(link).append("): ").append(description);
		return sb.toString();
	}

	public String getClassify_id() {
		return classify_id;
	}

	public void setClassify_id(String classify_id) {
		this.classify_id = classify_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
}
