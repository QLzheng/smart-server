package com.tcl.smart.server.service;

import java.util.List;

import com.tcl.smart.server.bean.NewsSearchRstItem;

/**
 * Sort news by program information
 * @author YangJie
 * @date 2013-4-19
 */
public interface INewsSortService {
	public List<NewsSearchRstItem> sort(List<NewsSearchRstItem> newsRstItem);
}
