package com.tcl.smart.server.service;

import java.util.List;

import com.tcl.smart.server.bean.HotBaiduNewsBean;

/**
 * @author fanjie
 * @date 2013-8-19
 */
public interface ITopNewsCrawlerService {
	public List<HotBaiduNewsBean> fetchRealtimeHotBaiduNews();
}
