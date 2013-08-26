package com.tcl.smart.server.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tcl.smart.server.bean.VideoUrlInfo;
import com.tcl.smart.server.dao.IVideoDao;

public class VideoDao implements IVideoDao {
	@Autowired
	@Value("${db.video}")
	private String videoDb;

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;

	public void init() {
		col = mongoTemplate.getCollection("VideoUrlInfo");
		col.ensureIndex(new BasicDBObject().append("_id", 1));
	}

	public List<String> findAllPics() {
		DBCollection col = mongoTemplate.getCollection(videoDb);
		DBCursor cursor = col.find();
		List<String> fileNames = new ArrayList<String>();
		while (cursor.hasNext()) {
			String cover = (String) cursor.next().get("cover");
			fileNames.add(cover);
		}
		return fileNames;
	}

	public List<VideoUrlInfo> findAllVideoInfosByVideos() {
		DBCollection col = mongoTemplate.getCollection(videoDb);
		DBCursor cursor = col.find();
		List<VideoUrlInfo> videoUrlInfos = new ArrayList<VideoUrlInfo>();
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			String id = ((ObjectId) dbObj.get("_id")).toString();
			String title = (String) dbObj.get("title");
			String model = (String) dbObj.get("model");
			VideoUrlInfo videoUrlInfo = new VideoUrlInfo();
			videoUrlInfo.set_id(id);
			videoUrlInfo.setTitle(title);
			videoUrlInfo.setModel(model);
			videoUrlInfos.add(videoUrlInfo);
		}
		return videoUrlInfos;
	}

	public List<VideoUrlInfo> findAllVideoInfos() {
		DBCollection col = mongoTemplate.getCollection("VideoUrlInfo");
		DBCursor cursor = col.find();
		List<VideoUrlInfo> videoUrlInfos = new ArrayList<VideoUrlInfo>();
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			String id = (String) dbObj.get("_id");
			String title = (String) dbObj.get("title");
			String url = (String) dbObj.get("url");
			String source = (String) dbObj.get("source");
			VideoUrlInfo videoUrlInfo = new VideoUrlInfo();
			videoUrlInfo.set_id(id);
			videoUrlInfo.setTitle(title);
			videoUrlInfo.setUrl(url);
			videoUrlInfo.setSource(source);
			videoUrlInfos.add(videoUrlInfo);
		}
		return videoUrlInfos;
	}

	public void updateOrAddVideoUrlInfo(VideoUrlInfo videoInfo) {
		BasicDBObject doc = new BasicDBObject();
		doc.put("_id", videoInfo.get_id());
		doc.put("title", videoInfo.getTitle());
		if (videoInfo.getUrl() != null)
			doc.put("url", videoInfo.getUrl());
		if (videoInfo.getSource() != null)
			doc.put("source", videoInfo.getSource());
		col.update(new BasicDBObject().append("_id", videoInfo.get_id()), doc, true, false);
	}

	public VideoUrlInfo findVideoUrlInfoById(String id) {
		DBObject query = new BasicDBObject();
		query.put("_id", id);
		DBObject obj = col.findOne(query);
		if (obj != null) {
			VideoUrlInfo videoInfo = new VideoUrlInfo();
			videoInfo.set_id(id);
			videoInfo.setTitle((String) obj.get("title"));
			if (obj.get("url") != null)
				videoInfo.setUrl((String) obj.get("url"));
			if (obj.get("source") != null)
				videoInfo.setSource((String) obj.get("source"));
			return videoInfo;
		} else
			return null;
	}
}
