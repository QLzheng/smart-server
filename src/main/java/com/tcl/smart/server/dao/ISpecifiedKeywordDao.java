package com.tcl.smart.server.dao;

import java.util.List;

import com.tcl.smart.server.bean.SpecifiedKeyword;

/**
 * @author fanjie
 * @date 2013-7-9
 */
public interface ISpecifiedKeywordDao {
	public List<SpecifiedKeyword> findAllSpecifiedKeywords();

	public SpecifiedKeyword findSpecifiedKeywordByEpgId(String epgId);

	public void insertSpecifiedKeyword(SpecifiedKeyword specifiedKeyword);

	public void removeSpecifiedKeyword(String epgId);
}
