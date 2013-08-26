package com.tcl.smart.server.bean;

/**
 * 新闻分类数据模型
 * 
 * @author fanjie
 * @date 2013-4-1
 */
public class NewsClassification {
	private String _id;
	private String source;
	private String title;
	private String link;
	private String description;
	private String rss_url;
	private String image_url;
	private String image_title;
	private String image_link;
	private String image_width;
	private String image_height;
	private String image_description;
	private String language;
	private String version;
	private String copyright;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getRss_url() {
		return rss_url;
	}

	public void setRss_url(String rss_url) {
		this.rss_url = rss_url;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getImage_title() {
		return image_title;
	}

	public void setImage_title(String image_title) {
		this.image_title = image_title;
	}

	public String getImage_link() {
		return image_link;
	}

	public void setImage_link(String image_link) {
		this.image_link = image_link;
	}

	public String getImage_width() {
		return image_width;
	}

	public void setImage_width(String image_width) {
		this.image_width = image_width;
	}

	public String getImage_height() {
		return image_height;
	}

	public void setImage_height(String image_height) {
		this.image_height = image_height;
	}

	public String getImage_description() {
		return image_description;
	}

	public void setImage_description(String image_description) {
		this.image_description = image_description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

}
