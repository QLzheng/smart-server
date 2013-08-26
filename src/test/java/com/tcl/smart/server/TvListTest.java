package com.tcl.smart.server;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.cmd.TvListRequestAck;
import com.tcl.smart.server.bean.cmd.TvListRequestAckData;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.ITvListDao;
import com.tcl.smart.server.service.IBaikeSearchService;
import com.tcl.smart.server.service.IDailyCrawlerService;
import com.tcl.smart.server.service.IHuanApiService;
import com.tcl.smart.server.service.impl.BlockException;

public class TvListTest {
	public static void testSaveTvList() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IHuanApiService huanApiService = (IHuanApiService) context.getBean("huanApiService");
		TvListRequestAck ack = huanApiService.getTodayChannelPrograms();
		ITvListDao tvListDao = (ITvListDao) context.getBean("tvListDao");
		tvListDao.saveTvlist(ack.getData(), false);
		System.out.println(tvListDao.findTvListByDay(new Date()));
	}

	public static void testSaveEpgModel() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IEpgModelDao epgModelDao = (IEpgModelDao) context.getBean("epgModelDao");
		ITvListDao tvListDao = (ITvListDao) context.getBean("tvListDao");
		TvListRequestAckData tvList = tvListDao.findTvListByDay("2013-04-10");
		epgModelDao.saveEpgModelsByTvList(tvList);
	}

	public static void testFindEpgModel() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IEpgModelDao epgModelDao = (IEpgModelDao) context.getBean("epgModelDao");
		System.out.println(epgModelDao.findEpgModelById("5166512454eab35400499bcc"));
	}

	public static void testGroupEpgModel() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IEpgModelDao epgModelDao = (IEpgModelDao) context.getBean("epgModelDao");
		System.out.println(epgModelDao.groupByNameAndWikiId());
	}

	public static void testCrawl() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IDailyCrawlerService service = (IDailyCrawlerService) context.getBean("newsDailyCrawlerByKeywordsService");
		service.start();
	}

	public static void testBaikeSearch() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IBaikeSearchService baikeService = (IBaikeSearchService) context.getBean("baikeSearchService");
		List<BaikeBean> baikes;
		try {
			baikes = baikeService.search("·Ç³ÏÎðÈÅ");
			System.out.println(baikes.size());
		} catch (BlockException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IHuanApiService huanApiService = (IHuanApiService) context.getBean("huanApiService");
		TvListRequestAck ack = huanApiService.getTodayChannelPrograms();
		System.out.println(ack.getData());
	}
}
