package com.tcl.smart.server.service;

import java.util.List;

import com.tcl.smart.server.bean.NewsItem;

/**
 * @author fanjie
 * @date 2013-4-2
 */
public interface ICrawlerService {
	public List<NewsItem> getNewestNews(int size);

	public void craw();

	public void start();

	public void stop();
}
