package com.tcl.smart.server.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tcl.smart.server.bean.NewsClassification;
import com.tcl.smart.server.dao.INewsClassificationDao;

/**
 * 新闻分类操作
 * 
 * @author fanjie
 * @date 2013-4-1
 */
public class NewsClassificationDao implements INewsClassificationDao {
	public static final String DBNAME = "NewsClassification";
	public static final String COL_ID = "_id";
	public static final String COL_SOURCE = "source";
	public static final String COL_TITLE = "title";
	public static final String COL_LINK = "link";
	public static final String COL_DESCRIPTION = "description";
	public static final String COL_RSS_URL = "rss_url";
	public static final String COL_IMAGE_URL = "image_url";
	public static final String COL_IMAGE_TITLE = "image_title";
	public static final String COL_IMAGE_LINK = "image_link";
	public static final String COL_IMAGE_WIDTH = "image_width";
	public static final String COL_IMAGE_HEIGHT = "image_height";
	public static final String COL_IMAGE_DESCRIPTION = "image_description";
	public static final String COL_LANGUAGE = "language";
	public static final String COL_VERSION = "version";
	public static final String COL_COPYRIGHT = "copyright";

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;

	public void init() {
		col = mongoTemplate.getCollection(DBNAME);
	}

	@Override
	public List<NewsClassification> findAllNewsClassifications() {
		DBCursor cursor = col.find();
		List<NewsClassification> newsClassifications = new ArrayList<NewsClassification>();
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			String _id = (String) dbObj.get(COL_ID);
			String source = (String) dbObj.get(COL_SOURCE);
			String title = (String) dbObj.get(COL_TITLE);
			String link = (String) dbObj.get(COL_LINK);
			String description = (String) dbObj.get(COL_DESCRIPTION);
			String rss_url = (String) dbObj.get(COL_RSS_URL);
			String image_url = (String) dbObj.get(COL_IMAGE_URL);
			String image_title = (String) dbObj.get(COL_IMAGE_TITLE);
			String image_link = (String) dbObj.get(COL_IMAGE_LINK);
			String image_width = (String) dbObj.get(COL_IMAGE_WIDTH);
			String image_height = (String) dbObj.get(COL_IMAGE_HEIGHT);
			String image_description = (String) dbObj.get(COL_IMAGE_DESCRIPTION);
			String language = (String) dbObj.get(COL_LANGUAGE);
			String version = (String) dbObj.get(COL_VERSION);
			String copyright = (String) dbObj.get(COL_COPYRIGHT);
			NewsClassification newsClassification = new NewsClassification();
			newsClassification.set_id(_id);
			newsClassification.setSource(source);
			newsClassification.setTitle(title);
			newsClassification.setLink(link);
			newsClassification.setDescription(description);
			newsClassification.setRss_url(rss_url);
			newsClassification.setImage_url(image_url);
			newsClassification.setImage_title(image_title);
			newsClassification.setImage_link(image_link);
			newsClassification.setImage_width(image_width);
			newsClassification.setImage_height(image_height);
			newsClassification.setImage_description(image_description);
			newsClassification.setLanguage(language);
			newsClassification.setVersion(version);
			newsClassification.setCopyright(copyright);
			newsClassifications.add(newsClassification);
		}
		return newsClassifications;
	}

	@Override
	public NewsClassification findNewsClassificationById(String newsClassificationId) {
		DBObject query = new BasicDBObject();
		query.put(COL_ID, newsClassificationId);
		DBObject obj = col.findOne(query);
		if (obj != null) {
			NewsClassification newsClassification = new NewsClassification();
			newsClassification.set_id(newsClassificationId);
			newsClassification.setSource((String) obj.get(COL_SOURCE));
			newsClassification.setTitle((String) obj.get(COL_TITLE));
			newsClassification.setLink((String) obj.get(COL_LINK));
			newsClassification.setDescription((String) obj.get(COL_DESCRIPTION));
			newsClassification.setRss_url((String) obj.get(COL_RSS_URL));
			newsClassification.setImage_url((String) obj.get(COL_IMAGE_URL));
			newsClassification.setImage_title((String) obj.get(COL_IMAGE_TITLE));
			newsClassification.setImage_link((String) obj.get(COL_IMAGE_LINK));
			newsClassification.setImage_width((String) obj.get(COL_IMAGE_WIDTH));
			newsClassification.setImage_height((String) obj.get(COL_IMAGE_HEIGHT));
			newsClassification.setImage_description((String) obj.get(COL_IMAGE_DESCRIPTION));
			newsClassification.setLanguage((String) obj.get(COL_LANGUAGE));
			newsClassification.setVersion((String) obj.get(COL_VERSION));
			newsClassification.setCopyright((String) obj.get(COL_COPYRIGHT));
			return newsClassification;
		} else
			return null;
	}

	@Override
	public void updateOrInsertNewsClassification(NewsClassification newsClassification) {
		BasicDBObject doc = new BasicDBObject();
		doc.put(COL_ID, newsClassification.get_id());
		doc.put(COL_SOURCE, newsClassification.getSource());
		doc.put(COL_TITLE, newsClassification.getTitle());
		doc.put(COL_LINK, newsClassification.getLink());
		doc.put(COL_DESCRIPTION, newsClassification.getDescription());
		doc.put(COL_RSS_URL, newsClassification.getRss_url());
		doc.put(COL_IMAGE_URL, newsClassification.getImage_url());
		doc.put(COL_IMAGE_TITLE, newsClassification.getImage_title());
		doc.put(COL_IMAGE_LINK, newsClassification.getImage_link());
		doc.put(COL_IMAGE_WIDTH, newsClassification.getImage_width());
		doc.put(COL_IMAGE_HEIGHT, newsClassification.getImage_height());
		doc.put(COL_IMAGE_DESCRIPTION, newsClassification.getImage_description());
		doc.put(COL_LANGUAGE, newsClassification.getLanguage());
		doc.put(COL_VERSION, newsClassification.getVersion());
		doc.put(COL_COPYRIGHT, newsClassification.getCopyright());
		col.update(new BasicDBObject().append(COL_ID, newsClassification.get_id()), doc, true, false);
	}
}
