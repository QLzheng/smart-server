package com.tcl.smart.server.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.tcl.smart.server.bean.NewsItem;
import com.tcl.smart.server.dao.INewsItemDao;

/**
 * 新闻条目操作
 * 
 * @author fanjie
 * @date 2013-4-1
 */
public class NewsItemDao implements INewsItemDao {
	public static final int MAX_NEWS_SIZE_EVERY_CLASS = 300;

	public static final String DBNAME = "NewsItem";
	public static final String COL_ID = "_id";
	public static final String COL_CLASSIFY_ID = "classify_id";
	public static final String COL_TITLE = "title";
	public static final String COL_LINK = "link";
	public static final String COL_DESCRIPTION = "description";
	public static final String COL_CONTENT = "content";
	public static final String COL_PUBDATE = "pubDate";

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private DBCollection col;

	public void init() {
		col = mongoTemplate.getCollection(DBNAME);
		col.ensureIndex(new BasicDBObject().append(COL_CLASSIFY_ID, 1).append(COL_TITLE, 1));
	}

	public long getNewsItemCount() {
		return col.count();
	}

	@Override
	public void removeOldestNewsByClassify(String classify_id) {
		DBObject query = new BasicDBObject();
		query.put(COL_CLASSIFY_ID, classify_id);
		DBObject order = new BasicDBObject();
		order.put(COL_PUBDATE, -1);
		DBCursor cursor = col.find(query).sort(order).skip(MAX_NEWS_SIZE_EVERY_CLASS);
		List<NewsItem> newsItems = new ArrayList<NewsItem>();
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			String _id = (String) dbObj.get(COL_ID);
			String title = (String) dbObj.get(COL_TITLE);
			String link = (String) dbObj.get(COL_LINK);
			String description = (String) dbObj.get(COL_DESCRIPTION);
			String content = (String) dbObj.get(COL_CONTENT);
			Date pubDate = (Date) dbObj.get(COL_PUBDATE);
			NewsItem newsItem = new NewsItem();
			newsItem.set_id(_id);
			newsItem.setClassify_id(classify_id);
			newsItem.setTitle(title);
			newsItem.setLink(link);
			newsItem.setDescription(description);
			newsItem.setContent(content);
			newsItem.setPubDate(pubDate);
			newsItems.add(newsItem);
			col.remove(dbObj);
		}
	}

	@Override
	public List<NewsItem> findAllNewsItems() {
		DBCursor cursor = col.find();
		List<NewsItem> newsItems = new ArrayList<NewsItem>();
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			String _id = (String) dbObj.get(COL_ID);
			String classify_id = (String) dbObj.get(COL_CLASSIFY_ID);
			String title = (String) dbObj.get(COL_TITLE);
			String link = (String) dbObj.get(COL_LINK);
			String description = (String) dbObj.get(COL_DESCRIPTION);
			String content = (String) dbObj.get(COL_CONTENT);
			Date pubDate = (Date) dbObj.get(COL_PUBDATE);
			NewsItem newsItem = new NewsItem();
			newsItem.set_id(_id);
			newsItem.setClassify_id(classify_id);
			newsItem.setTitle(title);
			newsItem.setLink(link);
			newsItem.setDescription(description);
			newsItem.setContent(content);
			newsItem.setPubDate(pubDate);
			newsItems.add(newsItem);
		}
		return newsItems;
	}

	public List<NewsItem> findNewestNewsByClassify(String classify_id, int size) {
		DBObject query = new BasicDBObject();
		query.put(COL_CLASSIFY_ID, classify_id);
		DBObject order = new BasicDBObject();
		order.put(COL_PUBDATE, -1);
		DBCursor cursor = col.find(query).sort(order);
		List<NewsItem> newsItems = new ArrayList<NewsItem>();
		while (cursor.hasNext() && size > 0) {
			DBObject dbObj = cursor.next();
			String _id = (String) dbObj.get(COL_ID);
			String title = (String) dbObj.get(COL_TITLE);
			String link = (String) dbObj.get(COL_LINK);
			String description = (String) dbObj.get(COL_DESCRIPTION);
			String content = (String) dbObj.get(COL_CONTENT);
			Date pubDate = (Date) dbObj.get(COL_PUBDATE);
			NewsItem newsItem = new NewsItem();
			newsItem.set_id(_id);
			newsItem.setClassify_id(classify_id);
			newsItem.setTitle(title);
			newsItem.setLink(link);
			newsItem.setDescription(description);
			newsItem.setContent(content);
			newsItem.setPubDate(pubDate);
			newsItems.add(newsItem);
			size--;
		}
		return newsItems;
	}

	@Override
	public NewsItem findNewsItemById(String newsId) {
		DBObject query = new BasicDBObject();
		query.put(COL_ID, newsId);
		DBObject obj = col.findOne(query);
		if (obj != null) {
			NewsItem newsItem = new NewsItem();
			newsItem.set_id(newsId);
			newsItem.setClassify_id((String) obj.get(COL_CLASSIFY_ID));
			newsItem.setTitle((String) obj.get(COL_TITLE));
			newsItem.setLink((String) obj.get(COL_LINK));
			newsItem.setDescription((String) obj.get(COL_DESCRIPTION));
			newsItem.setContent((String) obj.get(COL_CONTENT));
			newsItem.setPubDate((Date) obj.get(COL_PUBDATE));
			return newsItem;
		} else
			return null;
	}

	public boolean isExistNewsItemByClassifyAndTitle(String classify_id, String title) {
		DBObject query = new BasicDBObject();
		query.put(COL_CLASSIFY_ID, classify_id);
		query.put(COL_TITLE, title);
		DBObject obj = col.findOne(query);
		return obj != null;
	}

	public long countNewsItemByClassify(String classify_id) {
		DBObject query = new BasicDBObject();
		query.put(COL_CLASSIFY_ID, classify_id);
		long count = col.count(query);
		return count;
	}

	@Override
	public boolean insertNewsItem(NewsItem newsItem) {
		String _id = newsItem.get_id();
		if (_id == null) {
			_id = getNewsItemCount() + 1 + "";
		}
		if (!isExistNewsItemByClassifyAndTitle(newsItem.getClassify_id(), newsItem.getTitle())) {
			BasicDBObject doc = new BasicDBObject();
			doc.put(COL_CLASSIFY_ID, newsItem.getClassify_id());
			doc.put(COL_ID, _id);
			doc.put(COL_TITLE, newsItem.getTitle());
			doc.put(COL_LINK, newsItem.getLink());
			doc.put(COL_DESCRIPTION, newsItem.getDescription());
			doc.put(COL_CONTENT, newsItem.getContent());
			doc.put(COL_PUBDATE, newsItem.getPubDate());
			WriteResult rst = col.update(new BasicDBObject().append(COL_ID, _id), doc, true, false);
			if (rst.getError() != null)
				System.out.println(rst.getLastError());
			return true;
		}
		return false;
	}
}
