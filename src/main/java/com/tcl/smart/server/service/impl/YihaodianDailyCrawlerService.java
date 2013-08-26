package com.tcl.smart.server.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tcl.smart.server.service.IDailyCrawlerService;
import com.tcl.smart.server.service.IYihaodianApiService;
import com.tcl.smart.server.service.IYihaodianApiService.STATE;
import com.tcl.smart.server.util.Constants;

public class YihaodianDailyCrawlerService implements IDailyCrawlerService {
	private static final Logger logger = LoggerFactory.getLogger(YihaodianDailyCrawlerService.class);
	
	@Qualifier("yihaodianApiService")
	@Autowired
	private IYihaodianApiService YihaodianApiService;
	
	private Timer dailyScheduler;

	private DailyTask dailyTask;

	private Date dailyTime;

	private boolean runNow;

	public YihaodianDailyCrawlerService() {
		this(2, false);
	}

	public YihaodianDailyCrawlerService(int dailyRunHour, boolean runNow) {
		this.runNow = runNow;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, dailyRunHour);
		this.dailyTime = calendar.getTime();
		logger.info("If starting yihaodian product daily task will start at " + Constants.parseTime(calendar.getTime()));
	}

	@Override
	public void start() {
		dailyScheduler = new Timer("Yihaodian product daily task");
		YihaodianApiService.changeState(STATE.RUN);
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

		public void run() {
			try {
				logger.info("yihaodian product daily task begin..");
				YihaodianApiService.getAndStroeProducts();
				logger.info("Finish yihaodian product daily task.");
			} catch (Throwable t) {
				logger.error("Some error happened in baike daily task.", t);
			}
		}

		public void stop() {
			YihaodianApiService.changeState(STATE.STOP);
		}
	}
}

