package com.tcl.smart.server.service.impl;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.service.INewsSearchService;

/**
 * 根据节目搜索新闻服务
 * 
 * @author fanjie
 * @date 2013-4-7
 */
public class NewsRssSearchService implements INewsSearchService {
	private static final Logger logger = LoggerFactory.getLogger(NewsRssSearchService.class);

	@Qualifier("movieDao")
	@Autowired
	private IMovieDao movieDao;

	@Override
	public List<SyndEntry> search(String keyword) {
		if (keyword == null || keyword.trim().equals(""))
			return null;
		SyndFeed feed = null;
		try {
			String encodeKeyWord = URLEncoder.encode(keyword, "UTF-8");
			URL feedUrl = new URL("http://news.baidu.com/ns?tn=newsrss&sr=0&cl=2&rn=20&ct=0&word=" + encodeKeyWord);
			SyndFeedInput input = new SyndFeedInput();
			feed = input.build(new XmlReader(feedUrl));
			if (feed == null)
				return null;
			@SuppressWarnings("unchecked")
			List<SyndEntry> list = feed.getEntries();
			return list;
		} catch (Exception e) {
			logger.error("Error in crawling keyword: " + keyword, e);
			return null;
		}
	}

	@Override
	public List<NewsSearchRstItem> searchNews(String keyword) {
		List<NewsSearchRstItem> rst = new ArrayList<NewsSearchRstItem>();
		List<SyndEntry> entries = search(keyword);
		if (entries != null) {
			for (int i = 0; i < entries.size(); i++) {
				SyndEntry entry = entries.get(i);
				NewsSearchRstItem item = new NewsSearchRstItem();
				item.setDescription(entry.getDescription().getValue());
				item.setLink(entry.getLink());
				item.setTitle(entry.getTitle());
				item.setUpdateTime(new Date());
				item.setProgram_wiki_keys(Arrays.asList(keyword));
				rst.add(item);
			}
		}
		return rst;
	}

	@Override
	public List<NewsSearchRstItem> search(EpgModelDistinctId distinct) {
		List<String> keywords = distinct.getKeys(movieDao);
		List<NewsSearchRstItem> rst = new ArrayList<NewsSearchRstItem>();
		try {
			for (String keyword : keywords) {
				List<SyndEntry> entries = search(keyword);
				if (entries != null) {
					for (int i = 0; i < entries.size(); i++) {
						SyndEntry entry = entries.get(i);
						NewsSearchRstItem item = new NewsSearchRstItem();
						item.setDescription(entry.getDescription().getValue());
						item.setLink(entry.getLink());
						item.setTitle(entry.getTitle());
						item.setUpdateTime(new Date());
						item.setProgram_name(distinct.getName());
						item.setProgram_wiki_keys(Arrays.asList(keyword));
						String wiki_id = distinct.getWikiId();
						if (wiki_id != null) {
							item.setProgram_wiki_id(wiki_id);
							MovieModel model = movieDao.getMovieById(wiki_id);
							if (model != null) {
								item.setProgram_wiki_name(model.getTitle());
							}
						}
						rst.add(item);
					}
				}
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
				}
			}
		} catch (Throwable e) {
			logger.error("Error in crawling epg model: " + distinct, e);
			return rst;
		}
		return rst;
	}
}
