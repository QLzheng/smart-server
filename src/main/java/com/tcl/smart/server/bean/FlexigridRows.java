package com.tcl.smart.server.bean;

public class FlexigridRows<T> {
	private int id;
	private T cell;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public T getCell() {
		return cell;
	}

	public void setCell(T cell) {
		this.cell = cell;
	}

	public FlexigridRows(int id, T cell) {
		super();
		this.id = id;
		this.cell = cell;
	}
}