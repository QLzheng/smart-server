package com.tcl.smart.server.bean;

import java.util.List;

/**
 * 百度百科中的表格行
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeTableTr {
	private List<BaikeTableTd> tds;

	public List<BaikeTableTd> getTds() {
		return tds;
	}

	public void setTds(List<BaikeTableTd> tds) {
		this.tds = tds;
	}
}
