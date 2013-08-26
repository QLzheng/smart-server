package com.tcl.smart.server.dao;

import java.util.List;
import com.tcl.smart.server.bean.NewsItem;

public interface INewsItemDao {
	public List<NewsItem> findAllNewsItems();

	public void removeOldestNewsByClassify(String classify_id);

	public List<NewsItem> findNewestNewsByClassify(String classify_id, int size);

	public NewsItem findNewsItemById(String newsId);

	public boolean isExistNewsItemByClassifyAndTitle(String classify_id, String title);

	public long countNewsItemByClassify(String classify_id);

	public boolean insertNewsItem(NewsItem newsItem);
}
