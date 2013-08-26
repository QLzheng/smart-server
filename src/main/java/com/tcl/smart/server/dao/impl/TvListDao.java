package com.tcl.smart.server.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tcl.smart.server.bean.cmd.TvListRequestAckData;
import com.tcl.smart.server.dao.ITvListDao;

/**
 * @author fanjie
 * @date 2013-4-10
 */
public class TvListDao implements ITvListDao {
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private String col = "TvList";

	@Override
	public TvListRequestAckData findTvListByDay(String day) {
		Query query = new Query(Criteria.where("_id").is(day));
		TvListRequestAckData tvList = mongoTemplate.findOne(query, TvListRequestAckData.class, col);
		return tvList;
	}

	@Override
	public TvListRequestAckData findTvListByDay(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String day = format.format(new Date());
		return findTvListByDay(day);
	}

	@Override
	public boolean existDayTvList(String day) {
		Query query = new Query(Criteria.where("_id").is(day));
		long count = mongoTemplate.count(query, col);
		return count > 0;
	}

	@Override
	public boolean existDayTvList(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String day = format.format(new Date());
		return existDayTvList(day);
	}

	@Override
	public void saveTvlist(TvListRequestAckData tvlist, boolean overwrite) {
		if (!existDayTvList(tvlist.getDay()))
			mongoTemplate.save(tvlist, col);
		else {
			if (overwrite) {
				mongoTemplate.save(tvlist, col);
			}
		}
	}
}
