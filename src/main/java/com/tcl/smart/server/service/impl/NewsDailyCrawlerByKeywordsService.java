package com.tcl.smart.server.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.NewsClassification;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.bean.cmd.Channel;
import com.tcl.smart.server.bean.cmd.Program;
import com.tcl.smart.server.bean.cmd.TvListRequestAck;
import com.tcl.smart.server.bean.cmd.TvListRequestAckData;
import com.tcl.smart.server.bean.cmd.TvWikInfoRequestAck;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.INewsClassificationDao;
import com.tcl.smart.server.dao.INewsItemDao;
import com.tcl.smart.server.dao.ITvListDao;
import com.tcl.smart.server.dao.impl.NewsSearchRstItemDao;
import com.tcl.smart.server.service.IDailyCrawlerService;
import com.tcl.smart.server.service.IHuanApiService;
import com.tcl.smart.server.update.IUpdate;
import com.tcl.smart.server.util.Constants;

/**
 * 每日固定时间获取欢网节目单，针对节目单上的所有节目爬取新闻内容
 * 
 * @author fanjie
 * @date 2013-4-11
 */
public class NewsDailyCrawlerByKeywordsService implements IDailyCrawlerService {
	private static final Logger logger = LoggerFactory.getLogger(NewsDailyCrawlerByKeywordsService.class);

	@Qualifier("restTemplate")
	@Autowired
	private RestTemplate restTemplate;

	@Qualifier("newsSearchRstItemDao")
	@Autowired
	private NewsSearchRstItemDao newsSearchRstItemDao;

	@Qualifier("newsRssSearchService")
	@Autowired
	private NewsRssSearchService newsRssService;

	@Qualifier("huanApiService")
	@Autowired
	private IHuanApiService huanApiService;

	@Qualifier("movieDao")
	@Autowired
	private IMovieDao movieDao;

	@Qualifier("tvListDao")
	@Autowired
	private ITvListDao tvListDao;

	@Qualifier("epgModelDao")
	@Autowired
	private IEpgModelDao epgModelDao;

	@Qualifier("newsItemDao")
	@Autowired
	private INewsItemDao newsItemDao;

	@Qualifier("newsClassificationDao")
	@Autowired
	private INewsClassificationDao newsClassificationDao;

	@Qualifier("dailyUpdateProdectRec")
	@Autowired
	private IUpdate update;

	private Timer dailyScheduler;

	private ScheduledExecutorService timingScheduler;

	private DailyTask dailyTask;

	private TimingTask timingTask;

	private Date dailyTime;

	private long timingPeriod = 10;

	private boolean runNow;

	private volatile boolean isDailyRunning = false;

	public NewsDailyCrawlerByKeywordsService() {
		this(2, 10, false);
	}

	public NewsDailyCrawlerByKeywordsService(int dailyRunHour, int timingPeriod, boolean runNow) {
		this.runNow = runNow;
		this.timingPeriod = timingPeriod;
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (runNow) {
			calendar.set(year, month, day, dailyRunHour, 0, 0);
		} else {
			calendar.set(year, month, day, dailyRunHour, 0, 0);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		this.dailyTime = calendar.getTime();
		logger.info("If starting news daily task will start at " + Constants.parseTime(calendar.getTime()));
	}

	@Override
	public void start() {
		if (runNow)
			isDailyRunning = true;
		startDailyTask();
		startTimingTask();
	}

	@Override
	public void stop() {
		stopDailyTask();
		stopTimingTask();
	}

	public void startDailyTask() {
		dailyScheduler = new Timer("News daily task");
		dailyTask = new DailyTask();
		dailyScheduler.schedule(dailyTask, dailyTime, 1000 * 60 * 60 * 24);
	}

	public void stopDailyTask() {
		if (dailyScheduler != null)
			dailyScheduler.cancel();
	}

	public void startTimingTask() {
		while (isDailyRunning) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
		timingScheduler = Executors.newScheduledThreadPool(1);
		timingTask = new TimingTask();
		timingScheduler.scheduleWithFixedDelay(timingTask, 0, timingPeriod, TimeUnit.MINUTES);
	}

	public void stopTimingTask() {
		if (timingScheduler != null)
			timingScheduler.shutdown();
		if (timingTask != null)
			timingTask.awaitTerminal();
	}

	public void doOnce() {
		new DailyTask().run();
	}

	class DailyTask extends TimerTask {
		public void run() {
			try {
				isDailyRunning = true;
				logger.info("News daily task begin..");
				preSolveHistory();
				fetchTodayTvListAndSaveEpgModels();
				train();
				logger.info("Finish news daily task.");
				isDailyRunning = false;
			} catch (Throwable t) {
				logger.error("Some error happened in news daily task.", t);
				isDailyRunning = false;
			}
		}

		private void fetchTodayTvListAndSaveEpgModels() {
			// 获取今日节目单
			TvListRequestAckData tvListData = tvListDao.findTvListByDay(new Date());
			if (tvListData == null) {
				logger.info("Fetching today's tv list..");
				TvListRequestAck tvList = huanApiService.getTodayChannelPrograms();
				int times = 100;
				while (tvList == null && times-- > 0) {
					try {
						logger.info("Fetching fail, refetching after 10s..");
						Thread.sleep(10000);
						tvList = huanApiService.getTodayChannelPrograms();
					} catch (InterruptedException e) {
					}
				}
				tvListData = tvList.getData();
				logger.info("Finish fetching today's tv list.");
			}

			logger.info("Saving today's tv list and epg model..");
			// 保存节目单
			tvListDao.saveTvlist(tvListData, false);
			// 保存EpgModel
			epgModelDao.saveEpgModelsByTvList(tvListData);
			logger.info("Finish saving today's tv list and epg model.");

			// 更新本地WIKI库
			logger.info("Upgrading today's wiki list..");
			for (Channel channel : tvListData.getChannel()) {
				if (channel.getProgram() == null)
					continue;
				for (Program program : channel.getProgram()) {
					String wiki_id = program.getWiki_id();
					if (wiki_id != null) {
						MovieModel movieModel = movieDao.getMovieById(wiki_id);
						if (movieModel == null) {
							TvWikInfoRequestAck wikiInfo = huanApiService.getWikiInfo(wiki_id);
							movieDao.updateMovie(wikiInfo);
						}
					}
				}
			}
			logger.info("Finish upgrading today's wiki list.");
		}

		private void train() {
			// 删除过旧的新闻，每个关键字保存新闻不超过100
			try {
				removeOldestNews();
			} catch (Throwable e) {
				logger.error("Some error happened when removing old news.", e);
			}

			// 删除过旧的分类新闻，每个类别保存新闻不超过300
			try {
				removeOldestClassifyNews();
			} catch (Throwable e) {
				logger.error("Some error happened when removing old classify news.", e);
			}

			// 通知UREC重新训练VOD推荐
			try {
				requestVodTrain();
			} catch (Throwable e) {
				logger.error("Some error happened when requestingvod train.", e);
			}

			// 训练物品推荐
			try {
				requestProductUpdate();
			} catch (Throwable e) {
				logger.error("Some error happened when requesting product update.", e);
			}
		}

		private void preSolveHistory() {
			epgModelDao.removeHistory();
		}

		private void requestVodTrain() {
			String url = Constants.constructCliveTrainOnceUrl();
			try {
				logger.info("Training vod.");
				String result = restTemplate.getForObject(url, String.class);
				logger.info("Finish training vod, result: " + result);
			} catch (Throwable e) {
				logger.error("Error to train vod: " + e.getMessage());
			}
		}

		private void requestProductUpdate() {
			logger.info("Updating product.");
			update.update();
			logger.info("Finish updating product.");
		}

		private void removeOldestNews() {
			logger.info("Removing oldest news if count too much");
			List<EpgModelDistinctId> distincts = newsSearchRstItemDao.groupByEpgsIfCountTooMuch();
			for (EpgModelDistinctId distinct : distincts) {
				newsSearchRstItemDao.removeOldestNewsSearchRstItemsIfCountTooMuch(distinct.getName(), distinct.getWikiId(), distinct.getCount().intValue());
			}
			logger.info("Finish removing oldest news if count too much");
		}

		private void removeOldestClassifyNews() {
			logger.info("Removing oldest classify news if count too much");
			List<NewsClassification> classifies = newsClassificationDao.findAllNewsClassifications();
			for (NewsClassification classify : classifies) {
				newsItemDao.removeOldestNewsByClassify(classify.get_id());
			}
			logger.info("Finish removing oldest classify news if count too much");
		}
	}

	class TimingTask extends TimerTask {
		private ExecutorService crawlerPool;
		private ArrayList<CrawlerCallable> crawlerCallers;

		public void run() {
			try {
				logger.info("Timing task begin..");
				crawNewsByEpgs();
				logger.info("Finish timing task.");
			} catch (Throwable t) {
				logger.error("Some error happened in timing task.", t);
			}
		}

		private void crawNewsByEpgs() {
			final List<EpgModelDistinctId> distincts = Collections.synchronizedList(epgModelDao.groupByNameAndWikiId());
			final int size = distincts.size();
			int poolSize = 5;
			crawlerCallers = new ArrayList<CrawlerCallable>();
			int sizePerPool = size / (poolSize - 1);
			for (int i = 0; i < poolSize && i * sizePerPool < size; i++) {
				int startIndex = i * sizePerPool;
				int endIndex = (startIndex + sizePerPool) >= size ? size : (startIndex + sizePerPool);
				CrawlerCallable crawlerCallable = new CrawlerCallable(i, distincts.subList(startIndex, endIndex));
				crawlerCallers.add(crawlerCallable);
			}
			long t1 = System.currentTimeMillis();
			logger.info("Start crawling, " + size + " programs need to be crawled..");
			crawlerPool = Executors.newCachedThreadPool();
			int newsSize = 0;
			try {
				List<Future<Integer>> newsSizeList = crawlerPool.invokeAll(crawlerCallers, 20, TimeUnit.MINUTES);
				for (Future<Integer> future : newsSizeList) {
					try {
						if (future.isDone() && !future.isCancelled()) {
							int thisSize = future.get();
							newsSize += thisSize;
						}
					} catch (Exception e) {
						logger.error("Error when waiting the return of crawling thread.", e);
						future.cancel(true);
					}
				}
			} catch (Exception e) {
				logger.error("Error when invokng the thread pool.", e);
			}
			long t2 = System.currentTimeMillis();
			logger.info("Finish crawling all crawling, programs: " + size + ", time: " + (t2 - t1) / 1000 / 60 + "min, news items: " + newsSize);
			crawlerPool.shutdown();
		}

		public void awaitTerminal() {
			if (crawlerPool != null) {
				crawlerPool.shutdown();
				for (CrawlerCallable crawler : crawlerCallers) {
					crawler.stop();
				}
				try {
					crawlerPool.awaitTermination(1, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					crawlerPool.shutdownNow();
					logger.error("Error when terminating the thread pool.", e);
				}
			}
		}

		class CrawlerCallable implements Callable<Integer> {
			private int id;
			private List<EpgModelDistinctId> epgModelDistinctIds;
			private volatile boolean stop = false;

			public CrawlerCallable(int id, List<EpgModelDistinctId> epgModelDistinctIds) {
				this.id = id;
				this.epgModelDistinctIds = epgModelDistinctIds;
			}

			public void stop() {
				stop = true;
			}

			@Override
			public Integer call() throws Exception {
				if (epgModelDistinctIds == null || epgModelDistinctIds.size() == 0)
					return 0;
				logger.info("Start crawling thread " + id + ", size: " + epgModelDistinctIds.size() + ", thread: " + id);
				int newsSize = 0;
				try {
					for (EpgModelDistinctId distinct : epgModelDistinctIds) {
						if (stop || isDailyRunning) {
							logger.info("Canceling crawling thread " + id);
							break;
						}
						logger.info("Searching news about epg model: " + distinct + ", thread: " + id + "...");
						List<NewsSearchRstItem> news = newsRssService.search(distinct);
						logger.info("Saving news about epg model: " + distinct + ", thread: " + id + "...");
						if (news == null) {
							logger.error("Error in crawling epg model: " + distinct + ", thread: " + id);
							continue;
						}
						int newItems = 0;
						for (NewsSearchRstItem newsItem : news) {
							if (newsSearchRstItemDao.saveNewsSearchRstItemReplaceSameNews(newsItem)) {
								newItems++;
							}
						}
						newsSize += newItems;
						logger.info("Finish crawling news about epg model: " + distinct + ", items: " + news.size() + ", new items: " + newItems + ", thread: " + id);
						try {
							Thread.sleep(5000);
						} catch (Exception e) {
						}
					}
				} catch (Throwable t) {
					logger.error("Error when crawling thread " + id, t);
				}
				logger.info("Finish crawling thread " + id);
				return newsSize;
			}
		}
	}
}
