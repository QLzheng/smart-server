package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * �ٶȰٿƵġ��ٿ���Ƭ��
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeCard implements Serializable {
	private static final long serialVersionUID = -5381569246318679705L;

	// <div class="mod-top"> ��
	// <h4 class="card-title"> �е��ı�������Ϊ��Ƭ����
	private String title = "�ٿ���Ƭ";

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
