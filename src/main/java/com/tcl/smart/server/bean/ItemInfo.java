package com.tcl.smart.server.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcl.smart.server.util.Constants;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.NONE)
public class ItemInfo {
	private String _id;
	private String content;
	private String country;
	private String cover;
	private List<String> director = new ArrayList<String>();
	private String language;
	private String released;
	private List<String> starring = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();
	private String title;
	private String source;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getContent() {
		return content;
	}

	public String getContentEtc() {
		int showLength = 60;
		if (content.length() > showLength) {
			return content.substring(0, showLength) + "...";
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public List<String> getDirector() {
		return director;
	}

	public List<String> getDirectorEtc() {
		return director;
	}

	public void setDirector(List<String> director) {
		this.director = director;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}

	public List<String> getStarring() {
		return starring;
	}

	public List<String> getStarringEtc() {
		return starring;
	}

	public void setStarring(List<String> starring) {
		this.starring = starring;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl1() {
		if (cover == null || cover.trim().equals("")) {
			return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicDefault();
		}
		File pic = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator
				+ Constants.getProperties().getRecommendPicFolder1() + cover);
		if (!pic.exists() || !pic.isFile()) {
			return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicDefault();
		}
		return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicFolder1() + cover;
	}

	public String getPicUrl2() {
		if (cover == null || cover.trim().equals("")) {
			return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicDefault();
		}
		File pic = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator
				+ Constants.getProperties().getRecommendPicFolder2() + cover);
		if (!pic.exists() || !pic.isFile()) {
			return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicDefault();
		}
		return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicFolder2() + cover;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
