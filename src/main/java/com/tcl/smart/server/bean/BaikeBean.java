package com.tcl.smart.server.bean;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.tcl.smart.server.util.KeyType;

/**
 * 百度百科词条Bean，同一个词条有多个解释时则存在多个Bean
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeBean implements Serializable {
	private static final long serialVersionUID = 3092053025245741985L;

	private static final int MAX_TITLE_LEN = 4;

	@Id
	private String id;

	/** 词条名称 */
	private String name;

	/** 当该词条只有一个解释时为空，否则不为空 */
	private BaikeHeader header;

	/** 百科名片，可能为空 */
	private BaikeCard card;

	/** 一系列段落簇，当只有一个段落簇时该簇无标题，否则有标题，且可能有多级标题 */
	private List<BaikeSection> sections;

	/** 一系列开放分类，可能为空 */
	private List<BaikeCategory> categories;

	/** 词条类型 */
	private List<KeyType> keyType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEtc() {
		if (name == null)
			return null;
		if (name.length() > MAX_TITLE_LEN) {
			return name.substring(0, MAX_TITLE_LEN) + "..";
		}
		return name;
	}

	public List<BaikeSection> getSections() {
		return sections;
	}

	public void setSections(List<BaikeSection> sections) {
		this.sections = sections;
	}

	public BaikeCard getCard() {
		return card;
	}

	public void setCard(BaikeCard card) {
		this.card = card;
	}

	public BaikeHeader getHeader() {
		return header;
	}

	public void setHeader(BaikeHeader header) {
		this.header = header;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<BaikeCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<BaikeCategory> categories) {
		this.categories = categories;
	}

	public String getShowTitle() {
		String title = name;
		if (header != null) {
			title = title + " " + header.getContent();
		}
		return title;
	}

	public String getCardImgUrl() {
		if (card != null) {
			BaikeImage img = card.getImg();
			if (img != null) {
				return img.getUrl();
			}
		}
		return null;
	}

	public String getShowContent() {
		StringBuffer content = new StringBuffer();
		try {
			if (card != null) {
				content.append("<h2><b>" + card.getTitle() + "：</b></h2>");
				content.append("<h2>" + card.getSummary() + "</h2>");
			}
			if (sections != null) {
				for (int i = 0; i < sections.size(); i++) {
					BaikeSection section = sections.get(i);
					List<String> heads = section.getHeads();
					if (heads != null && heads.size() > 0) {
						content.append("<h2><b>" + heads.get(heads.size() - 1) + "：</b></h2>");
					}
					for (int j = 0; j < section.getParas().size(); j++) {
						BaikePara para = section.getParas().get(j);
						content.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;" + para.getText() + "</h2>");
					}
				}
			}
		} catch (Exception e) {
			content.append("暂无信息");
		}
		return content.toString();
	}

	/**
	 * get all the text to a string, no composing
	 * 
	 * @return
	 */
	public String allToText() {
		StringBuffer content = new StringBuffer();
		content.append(id);
		content.append(name);
		if (header != null) {
			content.append(header.getContent());
		}
		try {
			if (card != null) {
				content.append(card.getSummary());
			}
			if (sections != null) {
				for (int i = 0; i < sections.size(); i++) {
					BaikeSection section = sections.get(i);
					List<String> heads = section.getHeads();
					if (heads != null) {
						content.append(heads.toString());
					}
					for (int j = 0; j < section.getParas().size(); j++) {
						BaikePara para = section.getParas().get(j);
						content.append(para.getText());
					}
				}
			}
		} catch (Exception e) {
			content.append("暂无信息");
		}
		return content.toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name).append(" ");
		if (header != null)
			sb.append(header);
		sb.append(" ");
		if (card != null)
			sb.append(card);
		sb.append(" ").append(sections);
		return sb.toString();
	}

	public List<KeyType> getKeyType() {
		return keyType;
	}

	public void setKeyType(List<KeyType> keyType) {
		this.keyType = keyType;
	}

	public String getSource() {
		StringBuffer des = new StringBuffer();
		if (keyType != null) {
			for (KeyType type : keyType) {
				des.append(type.toString());
				des.append(" ");
			}
		}
		return des.toString();
	}
}
