package com.tcl.smart.server.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.tcl.smart.server.bean.NewsClassification;
import com.tcl.smart.server.bean.NewsItem;
import com.tcl.smart.server.dao.INewsClassificationDao;
import com.tcl.smart.server.dao.INewsItemDao;
import com.tcl.smart.server.service.ICrawlerService;

/**
 * @author fanjie
 * @date 2013-4-2
 */
public class NewsTimingCrawlerService implements ICrawlerService {

	private static final Logger logger = LoggerFactory.getLogger(NewsTimingCrawlerService.class);

	@Qualifier("newsClassificationDao")
	@Autowired
	private INewsClassificationDao newsClassificationDao;

	@Qualifier("newsItemDao")
	@Autowired
	private INewsItemDao newsItemDao;

	public static long delay = 1000;

	public static long period = 1000 * 60 * 5;

	private Timer timer;

	private List<NewsItem> items = new ArrayList<NewsItem>();

	public NewsTimingCrawlerService(int periodMinute) {
		period = 1000 * 60 * periodMinute;
	}

	public List<NewsItem> getNewestNews(int size) {
		if (items == null || items.size() == 0) {
			cacheNewestNewsList();
		}
		return items.subList(0, size);
	}

	public void cacheNewestNewsList() {
		List<NewsItem> newestItems = new ArrayList<NewsItem>();
		List<NewsClassification> classifies = newsClassificationDao.findAllNewsClassifications();
		for (NewsClassification classify : classifies) {
			List<NewsItem> newitems = newsItemDao.findNewestNewsByClassify(classify.get_id(), 2);
			for (NewsItem item : newitems) {
				newestItems.add(item);
			}
		}
		items = newestItems;
	}

	public int craw(NewsClassification newsClassification) throws IllegalArgumentException, FeedException, IOException {
		logger.info("Crawling classification " + newsClassification.get_id());
		URL feedUrl = new URL(newsClassification.getRss_url());
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedUrl));

		@SuppressWarnings("unchecked")
		List<SyndEntry> list = feed.getEntries();
		int insertNum = 0;
		for (int i = 0; i < list.size(); i++) {
			SyndEntry entry = list.get(i);
			NewsItem item = new NewsItem();
			item.setClassify_id(newsClassification.get_id());
			item.setDescription(entry.getDescription().getValue());
			item.setLink(entry.getLink());
			item.setPubDate(entry.getPublishedDate());
			item.setTitle(entry.getTitle());
			if (newsItemDao.insertNewsItem(item))
				insertNum++;
		}
		logger.info("Finished in Crawling classification " + newsClassification.get_id() + " for " + insertNum + " items.");
		return insertNum;
	}

	@Override
	public void craw() {
		List<NewsClassification> newsClasss = newsClassificationDao.findAllNewsClassifications();
		int crawledNum = 0;
		logger.info("Start Crawling...");
		for (NewsClassification newsClass : newsClasss) {
			try {
				crawledNum += craw(newsClass);
				Thread.sleep(5000);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (FeedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("Finish crawling, total of " + crawledNum + " items.");
	}

	@Override
	public void start() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				craw();
				cacheNewestNewsList();
			}
		}, delay, period);
	}

	@Override
	public void stop() {
		logger.info("Stop crawling.");
		timer.cancel();
	}
}
