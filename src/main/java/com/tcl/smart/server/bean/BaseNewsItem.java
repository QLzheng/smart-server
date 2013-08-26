package com.tcl.smart.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcl.smart.server.util.KeyType;

/**
 * 新闻条目基础类型
 * 
 * @author fanjie
 * @date 2013-4-17
 */
public class BaseNewsItem implements Serializable {
	private static final long serialVersionUID = -260585714908516502L;

	private static final int MAX_TITLE_LEN = 4;

	protected String _id;
	protected String title;
	protected String description;
	protected String link;
	protected Date updateTime;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTitleEtc() {
		if (title.length() > MAX_TITLE_LEN) {
			return title.substring(0, MAX_TITLE_LEN) + "..";
		}
		return title;
	}

	public String getSource() {
		if (this instanceof NewsSearchRstItem) {
			NewsSearchRstItem news = (NewsSearchRstItem) this;
			List<KeyType> types = news.getKeyType();
			StringBuffer des = new StringBuffer();
			if (types != null) {
				for (KeyType type : types) {
					des.append(type.toString());
					des.append(" ");
				}
			}
			return des.toString();
		} else {
			return "热门新闻";
		}
	}

	public String getImageUrl() {
		if (description == null || description.trim().equals(""))
			return null;
		String regEx = "<img border=\"0\" src=\"(.+)\">";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(description);
		if (mat.find()) {
			String imgUrl = mat.group(1).replace("&amp;", "&");
			return imgUrl;
		}
		return null;
	}

	public String getIgnoredImgDescription() {
		if (description == null || description.trim().equals(""))
			return null;
		String regEx = "<img[^>]*>";
		return description.replaceAll(regEx, "");
	}
}
