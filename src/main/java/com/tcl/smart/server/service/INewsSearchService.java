package com.tcl.smart.server.service;

import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.NewsSearchRstItem;

/**
 * @author fanjie
 * @date 2013-4-7
 */
public interface INewsSearchService {
	public List<SyndEntry> search(String keyword);

	public List<NewsSearchRstItem> searchNews(String keyword);

	public List<NewsSearchRstItem> search(EpgModelDistinctId distinct);
}
