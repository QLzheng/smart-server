package com.tcl.smart.server.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.BaikeUrl;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.cmd.Channel;
import com.tcl.smart.server.bean.cmd.Program;
import com.tcl.smart.server.bean.cmd.TvListRequestAck;
import com.tcl.smart.server.dao.IBaikeBeanDao;
import com.tcl.smart.server.dao.IBaikeUrlDao;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.ITvListDao;
import com.tcl.smart.server.service.IBaikeSearchService;
import com.tcl.smart.server.service.IDailyCrawlerService;
import com.tcl.smart.server.service.IHuanApiService;
import com.tcl.smart.server.util.Constants;

/**
 * 每日固定时间获取明日的欢网节目单，针对节目单上的所有节目爬取百科内容
 * 
 * @author fanjie
 * @date 2013-5-2
 */
public class BaikeDailyCrawlerService implements IDailyCrawlerService {
	private static final Logger logger = LoggerFactory.getLogger(BaikeDailyCrawlerService.class);

	@Qualifier("huanApiService")
	@Autowired
	private IHuanApiService huanApiService;

	@Qualifier("baikeSearchServiceForDailyCrawler")
	@Autowired
	private IBaikeSearchService baikeSearchService;

	@Qualifier("movieDao")
	@Autowired
	private IMovieDao movieDao;

	@Qualifier("tvListDao")
	@Autowired
	private ITvListDao tvListDao;

	@Qualifier("epgModelDao")
	@Autowired
	private IEpgModelDao epgModelDao;

	@Qualifier("baikeBeanDao")
	@Autowired
	private IBaikeBeanDao baikeDao;

	@Qualifier("baikeUrlDao")
	@Autowired
	private IBaikeUrlDao baikeUrlDao;

	private Timer dailyScheduler;

	private DailyTask dailyTask;

	private Date dailyTime;

	private boolean runNow;

	public BaikeDailyCrawlerService() {
		this(2, false);
	}

	public BaikeDailyCrawlerService(int dailyRunHour, boolean runNow) {
		this.runNow = runNow;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, dailyRunHour);
		this.dailyTime = calendar.getTime();
		logger.info("If starting baike daily task will start at " + Constants.parseTime(calendar.getTime()));
	}

	@Override
	public void start() {
		dailyScheduler = new Timer("Baike daily task");
		dailyTask = new DailyTask();
		if (runNow) {
			dailyScheduler.schedule(dailyTask, 0, 1000 * 60 * 60 * 24);
		} else {
			dailyScheduler.schedule(dailyTask, dailyTime, 1000 * 60 * 60 * 24);
		}
	}

	@Override
	public void stop() {
		if (dailyScheduler != null)
			dailyScheduler.cancel();
		if (dailyTask != null)
			dailyTask.stop();
	}

	class DailyTask extends TimerTask {
		private volatile boolean stop = false;
		private volatile boolean isRunning = false;

		public void run() {
			try {
				if (isRunning)
					return;
				isRunning = true;
				logger.info("Baike daily task begin..");
				fetchTomorrowTvListAndSearchKeys();
				logger.info("Finish baike daily task.");
			} catch (Throwable t) {
				logger.error("Some error happened in baike daily task.", t);
			} finally {
				isRunning = false;
			}
		}

		private void fetchTomorrowTvListAndSearchKeys() {
			logger.info("Fetching tomorrow's tv list..");
			TvListRequestAck tvList = huanApiService.getTomorrowChannelPrograms();
			int times = 100;
			while (tvList == null && times-- > 0) {
				try {
					logger.info("Fetching fail, refetching after 10s..");
					Thread.sleep(10000);
					tvList = huanApiService.getTomorrowChannelPrograms();
				} catch (InterruptedException e) {
				}
			}
			logger.info("Finish Fetching tomorrow's tv list.");

			int size = tvList.getData().getProgramNum();
			int index = 1;
			int crawledKeys = 0;
			logger.info("Start searching tomorrow's baike, size: " + size);
			for (Channel channel : tvList.getData().getChannel()) {
				for (Program program : channel.getProgram()) {
					if (stop)
						break;
					EpgModelDistinctId distinct = new EpgModelDistinctId();
					distinct.setName(program.getName());
					distinct.setWikiId(program.getWiki_id());
					List<BaikeBean> baikes = new ArrayList<BaikeBean>();
					List<String> keyWords = distinct.getKeys(movieDao);
					try {
						int existSize = 0;
						for (String keyword : keyWords) {
							if (keyword == null || keyword.trim().equals(""))
								continue;
							List<BaikeBean> queryRst = baikeDao.findBaikeBeansByName(keyword.trim());
							if (queryRst != null && queryRst.size() > 0) {
								existSize += queryRst.size();
								continue;
							} else {
								BaikeUrl baikeUrl = baikeUrlDao.findBaikeUrlByName(keyword.trim());
								if (baikeUrl != null && "none".equals(baikeUrl.getResult())) {
									continue;
								}
							}
							List<BaikeBean> thisBaikes = baikeSearchService.search(keyword);
							if (thisBaikes != null && thisBaikes.size() > 0) {
								baikes.addAll(thisBaikes);
							}
							try {
								crawledKeys++;
								Thread.sleep(10000);
								if (crawledKeys != 0 && crawledKeys % 20 == 0)
									Thread.sleep(1000 * 60 * 2);
							} catch (InterruptedException e) {
							}
						}
						logger.info("【" + index + " / " + size + "】 Finish crawling baike about epg model: " + distinct + ", crawled: " + baikes.size() + ", existed: " + existSize);
						index++;
					} catch (BlockException e1) {
						System.err.println(Constants.getCurrentTime() + " Being blocked, waiting for 20 min..");
						try {
							Thread.sleep(1000 * 60 * 20);
						} catch (InterruptedException e) {
						}
					}
				}
			}
			logger.info("Finish searching tomorrow's baike.");
		}

		public void stop() {
			stop = true;
		}
	}
}
