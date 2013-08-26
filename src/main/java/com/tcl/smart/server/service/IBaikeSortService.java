package com.tcl.smart.server.service;

import java.util.List;

import com.tcl.smart.server.bean.BaikeBean;


/**
 * Sort Baike list by program information
 * @author Jerryangly
 *
 */
public interface IBaikeSortService {
	public List<BaikeBean> sort(List<BaikeBean> baikeBeans,String program_name,List<String> actorDirectors);
}
