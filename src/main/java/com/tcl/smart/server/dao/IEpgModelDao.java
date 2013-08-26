package com.tcl.smart.server.dao;

import java.util.Date;
import java.util.List;

import com.tcl.smart.server.bean.DistinctChannel;
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.cmd.TvListRequestAckData;

/**
 * @author fanjie
 * @date 2013-4-11
 */
public interface IEpgModelDao {
	public List<EpgModel> findAll();

	public List<DistinctChannel> group2Channels();

	public List<EpgModelDistinctId> groupByNameAndWikiId();

	public List<EpgModel> findEpgModelsByName(String name);

	public List<EpgModel> findEpgModelsByChannelCode(String channel_code);

	public EpgModel findCurrentEpgModelByChannelCode(String channel_code);

	public EpgModel findEpgModelByChannelCodeAndTime(String channel_code, Date time);

	public EpgModel findEpgModelById(String id);

	public void saveEpgModel(EpgModel model);

	public void saveEpgModelsByTvList(TvListRequestAckData tvList);

	public void removeHistory();
}
