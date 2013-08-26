package com.tcl.smart.server.dao;

import java.util.List;

import com.tcl.smart.server.bean.BaikeUrl;

/**
 * @author fanjie
 * @date 2013-4-27
 */
public interface IBaikeUrlDao {
	public List<BaikeUrl> findAllBaikeUrls();

	public List<BaikeUrl> findBaikeUrlsByResult(String result);

	public BaikeUrl findBaikeUrlByName(String name);

	public void insertBaikeUrl(BaikeUrl baikeUrl);

	public void updateBaikeUrl(BaikeUrl baikeUrl);
}
