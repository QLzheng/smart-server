package com.tcl.smart.server.bean;

import java.util.List;

/**
 * 百度百科中的表格表体
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeTableBody {
	private List<BaikeTableTr> trs;

	public List<BaikeTableTr> getTrs() {
		return trs;
	}

	public void setTrs(List<BaikeTableTr> trs) {
		this.trs = trs;
	}
}
