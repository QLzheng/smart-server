package com.tcl.smart.server.service;

import java.util.List;

import com.tcl.smart.server.bean.BaikeBean;

public interface IBaikeRecommendService {
	List<BaikeBean> getRecommenderWiki(String epgId, int size);
}
