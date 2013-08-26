package com.tcl.smart.server.service;

import com.tcl.smart.server.bean.cmd.TvListRequestAck;
import com.tcl.smart.server.bean.cmd.TvWikInfoRequestAck;

/**
 * @author fanjie
 * @date 2013-4-10
 */
public interface IHuanApiService {
	public TvWikInfoRequestAck getWikiInfo(String wiki_id);

	public TvListRequestAck getTodayChannelPrograms();

	public TvListRequestAck getTomorrowChannelPrograms();
}
