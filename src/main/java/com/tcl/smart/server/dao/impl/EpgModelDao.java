package com.tcl.smart.server.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.tcl.smart.server.bean.DistinctChannel;
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.cmd.Channel;
import com.tcl.smart.server.bean.cmd.Program;
import com.tcl.smart.server.bean.cmd.TvListRequestAckData;
import com.tcl.smart.server.dao.IEpgModelDao;

/**
 * @author fanjie
 * @date 2013-4-11
 */
public class EpgModelDao implements IEpgModelDao {
	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private String col = "huan_epg";

	private DBCollection dbCol;

	public void init() {
		dbCol = mongoTemplate.getCollection(col);
		dbCol.ensureIndex(new BasicDBObject().append("_id", 1));
		dbCol.ensureIndex(new BasicDBObject().append("start_time", 1));
		dbCol.ensureIndex(new BasicDBObject().append("end_time", 1));
		dbCol.ensureIndex(new BasicDBObject().append("name", 1).append("wiki_id", 1));
	}

	@Override
	public List<EpgModel> findAll() {
		List<EpgModel> epgModels = mongoTemplate.findAll(EpgModel.class, col);
		return epgModels;
	}

	@Override
	public List<DistinctChannel> group2Channels() {
		GroupBy groupBy = GroupBy.key("channel_code", "channelName", "channelId", "channelType", "channelLogo", "date").initialDocument("{count:0}")
				.reduceFunction("function(doc, prev){prev.count+=1}");
		GroupByResults<EpgModel> results = mongoTemplate.group(col, groupBy, EpgModel.class);
		List<DistinctChannel> distincts = new ArrayList<DistinctChannel>();
		if (results == null)
			return distincts;
		BasicDBList list = (BasicDBList) results.getRawResults().get("retval");
		for (int i = 0; i < list.size(); i++) {
			BasicDBObject obj = (BasicDBObject) list.get(i);
			DistinctChannel distinctId = new DistinctChannel();
			distinctId.setName((String) obj.get("channelName"));
			distinctId.setChannel_code((String) obj.get("channel_code"));
			distinctId.setChannelId((String) obj.get("channelId"));
			distinctId.setChannelType((String) obj.get("channelType"));
			distinctId.setChannelLogo((String) obj.get("channelLogo"));
			distinctId.setDate((String) obj.get("date"));
			distincts.add(distinctId);
		}
		return distincts;
	}

	@Override
	public List<EpgModelDistinctId> groupByNameAndWikiId() {
		GroupBy groupBy = GroupBy.key("name", "wiki_id").initialDocument("{count:0}").reduceFunction("function(doc, prev){prev.count+=1}");
		GroupByResults<EpgModel> results = mongoTemplate.group(col, groupBy, EpgModel.class);
		List<EpgModelDistinctId> distincts = new ArrayList<EpgModelDistinctId>();
		if (results == null)
			return distincts;
		BasicDBList list = (BasicDBList) results.getRawResults().get("retval");
		for (int i = 0; i < list.size(); i++) {
			BasicDBObject obj = (BasicDBObject) list.get(i);
			EpgModelDistinctId distinctId = new EpgModelDistinctId();
			distinctId.setName((String) obj.get("name"));
			distinctId.setWikiId((String) obj.get("wiki_id"));
			distinctId.setCount((Double) obj.get("count"));
			distincts.add(distinctId);
		}
		return distincts;
	}

	@Override
	public List<EpgModel> findEpgModelsByName(String name) {
		Query query = new Query(Criteria.where("name").is(name));
		List<EpgModel> epgModels = mongoTemplate.find(query, EpgModel.class, col);
		return epgModels;
	}

	public List<EpgModel> findEpgModelsByChannelCode(String channel_code) {
		Query query = new Query(Criteria.where("channel_code").is(channel_code));
		List<EpgModel> epgModels = mongoTemplate.find(query, EpgModel.class, col);
		return epgModels;
	}

	public EpgModel findCurrentEpgModelByChannelCode(String channel_code) {
		Date now = new Date();
		return findEpgModelByChannelCodeAndTime(channel_code, now);
	}

	public EpgModel findEpgModelByChannelCodeAndTime(String channel_code, Date time) {
		if (time == null)
			return null;
		Query query = new Query(Criteria.where("channel_code").is(channel_code).and("start_time").lte(time).and("end_time").gte(time));
		EpgModel model = mongoTemplate.findOne(query, EpgModel.class, col);
		return model;
	}

	@Override
	public EpgModel findEpgModelById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		EpgModel model = mongoTemplate.findOne(query, EpgModel.class, col);
		return model;
	}

	@Override
	public void saveEpgModel(EpgModel model) {
		mongoTemplate.save(model, col);
	}

	@Override
	public void saveEpgModelsByTvList(TvListRequestAckData tvList) {
		if (tvList == null)
			return;
		Date date = new Date();
		List<Channel> channels = tvList.getChannel();
		for (Channel channel : channels) {
			List<Program> programs = channel.getProgram();
			if (programs == null)
				continue;
			for (Program program : programs) {
				try {
					Date startTime = formatter.parse(program.getDate() + " " + program.getStart_time());
					Date endTime = formatter.parse(program.getDate() + " " + program.getEnd_time());
					EpgModel epgModel = new EpgModel();
					epgModel.setChannelCode(channel.getCode());
					epgModel.setCreatedAt(date);
					epgModel.setDateTime(tvList.getDay());
					epgModel.setName(program.getName());
					epgModel.setPublish(true);
					epgModel.setStartTime(startTime);
					epgModel.setEndTime(endTime);
					epgModel.setTime(program.getStart_time());
					epgModel.setUpdatedAt(date);
					epgModel.setWikiId(program.getWiki_id());
					epgModel.setChannelId(channel.getId());
					epgModel.setChannelLogo(channel.getLogo());
					epgModel.setChannelType(channel.getType());
					epgModel.setChannelName(channel.getName());
					epgModel.setWikiCover(program.getWiki_cover());
					saveEpgModel(epgModel);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void removeHistory() {
		Query query = new Query();
		mongoTemplate.remove(query, col);
	}
}
