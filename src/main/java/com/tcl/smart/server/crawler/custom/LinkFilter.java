package com.tcl.smart.server.crawler.custom;

public interface LinkFilter {
	public boolean accept(String url);
}