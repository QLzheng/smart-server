package com.tcl.smart.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.bean.cmd.Channel;
import com.tcl.smart.server.bean.cmd.Program;
import com.tcl.smart.server.bean.cmd.TvListRequestAckData;
import com.tcl.smart.server.dao.ITvListDao;
import com.tcl.smart.server.dao.impl.NewsSearchRstItemDao;

public class NewsSearchRstItemTest {

	public static void testRssSearch() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		ITvListDao tvListDao = (ITvListDao) context.getBean("tvListDao");
		// NewsSearchRstItemDao newsDao = (NewsSearchRstItemDao)
		// context.getBean("newsSearchRstItemDao");
		// NewsRssSearchService newsRssService = (NewsRssSearchService)
		// context.getBean("newsRssSearchService");
		long t1 = System.currentTimeMillis();
		TvListRequestAckData tvList = tvListDao.findTvListByDay(new Date());
		List<Program> programs = new ArrayList<Program>();
		for (Channel channel : tvList.getChannel()) {
			for (Program program : channel.getProgram()) {
				programs.add(program);
			}
		}
		System.out.println(programs.size());
		// for (Program program : programs) {
		// List<NewsSearchRstItem> news =
		// newsRssService.search(program.getName());
		// for (NewsSearchRstItem newsItem : news) {
		// newsDao.saveNewsSearchRstItem(newsItem);
		// }
		// System.out.println("Finish program: " + program.getName());
		// }
		long t2 = System.currentTimeMillis();
		System.out.println("Done: " + (t2 - t1) + "ms.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		NewsSearchRstItemDao newsDao = (NewsSearchRstItemDao) context.getBean("newsSearchRstItemDao");
		List<NewsSearchRstItem> items = newsDao.findNewsSearchRstItemsByProgram(null, null);
		for (NewsSearchRstItem item : items) {
			newsDao.removeNewsSearchRstItem(item);
		}
	}
}
