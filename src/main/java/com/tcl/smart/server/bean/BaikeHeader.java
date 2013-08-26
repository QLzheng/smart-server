package com.tcl.smart.server.bean;

import java.io.Serializable;

/**
 * 百度百科词条有多个解释时标志每个词条的词头
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeHeader implements Serializable {
	private static final long serialVersionUID = -7743676270000559077L;
	private String index;
	private String content;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return index + content;
	}
}
