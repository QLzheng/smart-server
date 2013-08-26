package com.tcl.smart.server.bean;

import java.io.File;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcl.smart.server.util.Constants;

/**
 * @author fanjie
 * @date 2013-3-5
 */
@XmlRootElement(name = "objects")
@XmlAccessorType(XmlAccessType.NONE)
public class RecommendObject implements Serializable {
	private static final long serialVersionUID = 7147655178295906609L;

	private static final int MAX_TITLE_LEN = 4;

	@XmlElement
	private String id;
	@XmlElement
	private String title;
	@XmlElement
	private String reason;
	@XmlElement
	private String cover;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleEtc() {
		if (title.length() > MAX_TITLE_LEN) {
			return title.substring(0, MAX_TITLE_LEN) + "..";
		}
		return title;
	}

	public String getReason() {
		String reasonStr = "暂无信息";
		if ((reason != null && reason.trim().equals("Update")) || reason == null) {
			reasonStr = "热门电影推荐";
		} else {
			String[] reasons = reason.split("->");
			String[] hisFilms = new String[reasons.length - 1];
			for (int i = 0; i < reasons.length - 1; i++) {
				hisFilms[i] = reasons[i];
			}
			StringBuffer reasonSB = new StringBuffer();
			reasonSB.append("您看过");
			for (int i = 0; i < hisFilms.length; i++) {
				reasonSB.append("<b>").append(hisFilms[i]).append("</b>");
				if (i != hisFilms.length - 1) {
					reasonSB.append("、");
				}
			}
			reasonStr = reasonSB.toString();
		}
		return reasonStr;
	}

	public String getLoveTag() {
		String reasonStr = "暂无信息";
		if ((reason != null && reason.trim().equals("Update")) || reason == null) {
			reasonStr = "暂无信息";
		} else {
			String[] reasons = reason.split("->");
			String[] loveTags = reasons[reasons.length - 1].substring(1, reasons[reasons.length - 1].length() - 1).split(" ");
			StringBuffer reasonSB = new StringBuffer();
			for (int i = 0; i < loveTags.length; i++) {
				reasonSB.append("<b>").append(loveTags[i]).append("</b>");
				if (i != loveTags.length - 1) {
					reasonSB.append(" ");
				}
			}
			reasonStr = reasonSB.toString();
		}
		return reasonStr;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
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
}
