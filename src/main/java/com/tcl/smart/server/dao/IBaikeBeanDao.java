package com.tcl.smart.server.dao;

import java.util.List;

import com.tcl.smart.server.bean.BaikeBean;

/**
 * @author fanjie
 * @date 2013-4-28
 */
public interface IBaikeBeanDao {
	public List<BaikeBean> findAllBaikeBeans();

	public BaikeBean findBaikeBeanById(String id);

	public List<BaikeBean> findBaikeBeansByName(String name);

	public boolean existBaikeBean(BaikeBean baike);

	public boolean existStrictBaikeBean(BaikeBean baike);

	public void insertBaikeBean(BaikeBean baike);

	public void updateBaikeBean(BaikeBean baike);
}
