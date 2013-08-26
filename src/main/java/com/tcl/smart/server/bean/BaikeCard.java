package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * 百度百科的“百科名片”
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeCard implements Serializable {
	private static final long serialVersionUID = -5381569246318679705L;

	// <div class="mod-top"> 下
	// <h4 class="card-title"> 中的文本内容则为卡片标题
	private String title = "百科名片";

	// <div class="pic">
	private BaikeImage img;

	// <div class="para">
	private BaikePara summary;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BaikeImage getImg() {
		return img;
	}

	public void setImg(BaikeImage img) {
		this.img = img;
	}

	public BaikePara getSummary() {
		return summary;
	}

	public void setSummary(BaikePara summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return title + ": " + img + " | " + summary;
	}
}
