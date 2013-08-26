package com.tcl.smart.server.service;

import java.util.List;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.service.impl.BlockException;

/**
 * @author fanjie
 * @date 2013-5-2
 */
public interface IBaikeSearchService {
	public List<BaikeBean> search(String key) throws BlockException;

	public List<BaikeBean> search(EpgModelDistinctId distinct) throws BlockException;

	public String hotlinkingPictureForHashImgName(String url);
}
