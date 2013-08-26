package com.tcl.smart.server.dao;

import java.util.Date;

import com.tcl.smart.server.bean.cmd.TvListRequestAckData;

/**
 * @author fanjie
 * @date 2013-4-10
 */
public interface ITvListDao {
	public TvListRequestAckData findTvListByDay(String day);

	public TvListRequestAckData findTvListByDay(Date date);

	public boolean existDayTvList(String day);

	public boolean existDayTvList(Date date);

	public void saveTvlist(TvListRequestAckData tvlist, boolean overwrite);
}
