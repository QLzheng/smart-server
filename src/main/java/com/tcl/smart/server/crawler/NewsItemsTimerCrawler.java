package com.tcl.smart.server.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.tcl.smart.server.bean.NewsClassification;
import com.tcl.smart.server.bean.NewsItem;
import com.tcl.smart.server.dao.impl.NewsClassificationDao;
import com.tcl.smart.server.dao.impl.NewsItemDao;

/**
 * @author fanjie
 * @date 2013-4-1
 */
public class NewsItemsTimerCrawler {

	private static final Logger logger = LoggerFactory.getLogger(NewsItemsTimerCrawler.class);

	private ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
	private NewsClassificationDao classDao;
	private NewsItemDao newsdao;

	public NewsItemsTimerCrawler() {
		classDao = (NewsClassificationDao) context.getBean("newsClassificationDao");
		classDao.init();
		newsdao = (NewsItemDao) context.getBean("newsItemDao");
		newsdao.init();
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
			if (newsdao.insertNewsItem(item))
				insertNum++;
		}
		logger.info("Finished in Crawling classification " + newsClassification.get_id() + " for " + insertNum + " items.");
		return insertNum;
	}

	public void crawNews() {
		List<NewsClassification> newsClasss = classDao.findAllNewsClassifications();
		int crawledNum = 0;
		logger.info("Start Crawling...");
		for (NewsClassification newsClass : newsClasss) {
			try {
				crawledNum += craw(newsClass);
			} catch (Exception e) {
				logger.error("Error in crawling.", e);
			}
		}
		logger.info("Finish crawling, total of " + crawledNum + " items.");
	}

	public static void main(String[] args) {
		final NewsItemsTimerCrawler crawler = new NewsItemsTimerCrawler();
		Timer timer = new Timer(false);
		timer.schedule(new TimerTask() {
			public void run() {
				crawler.crawNews();
			}
		}, 0, 1000 * 60 * 5);
	}
}
