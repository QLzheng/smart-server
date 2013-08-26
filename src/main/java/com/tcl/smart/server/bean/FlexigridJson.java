package com.tcl.smart.server.bean;

import java.util.List;

public class FlexigridJson<T> {
	private int total;
	private int page;
	private List<FlexigridRows<T>> rows;

	public FlexigridJson(int total, int page, List<FlexigridRows<T>> rows) {
		super();
		this.total = total;
		this.page = page;
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<FlexigridRows<T>> getRows() {
		return rows;
	}

	public void setRows(List<FlexigridRows<T>> rows) {
		this.rows = rows;
	}
}