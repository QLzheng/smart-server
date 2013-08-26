package com.tcl.smart.server.bean;

/**
 * 百度百科中的表格
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeTable {
	private BaikeTableHeader header;

	private BaikeTableBody body;

	public BaikeTableHeader getHeader() {
		return header;
	}

	public void setHeader(BaikeTableHeader header) {
		this.header = header;
	}

	public BaikeTableBody getBody() {
		return body;
	}

	public void setBody(BaikeTableBody body) {
		this.body = body;
	}
}
