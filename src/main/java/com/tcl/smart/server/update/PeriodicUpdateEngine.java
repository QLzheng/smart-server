package com.tcl.smart.server.update;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeriodicUpdateEngine implements IUpdateEngine {

	private static final Logger logger = LoggerFactory.getLogger(PeriodicUpdateEngine.class);

	private List<IUpdate> updates;
	private long interval;
	private ScheduledExecutorService scheduler;

	public PeriodicUpdateEngine() {
	}

	public PeriodicUpdateEngine(List<IUpdate> updates, long interval) {
		this.updates = updates;
		this.interval = TimeUnit.MILLISECONDS.convert(interval, TimeUnit.MINUTES);
	}

	class UpdateTask extends TimerTask {
		public void run() {
			Date date = new Date();
			logger.debug("Start update recommender list.....    at  " + date);
			long t0 = System.currentTimeMillis();
			if (updates == null) {
				logger.error("No update configured.");
			} else {
				long t1 = System.currentTimeMillis();
				for (IUpdate update : updates) {
					logger.debug("*-*-*-*-*-*-*-*-*-*-*   " + update.getClass().getName() + " Start!");
					update.update();
					double t2 = (System.currentTimeMillis() - t1) / 1000.0;
					t1 = System.currentTimeMillis();
					logger.info("*-*-*-*-*-*-*-*-*-*-*   " + update.getClass().getName() + " End! Use " + t2 + " seconds!");
				}
			}
			double t3 = (System.currentTimeMillis() - t0) / 1000.0;
			Date dateend = new Date();
			logger.debug("End updating...  total use " + t3 + " seconds!    at =  " + dateend);

			date.setTime(date.getTime() + interval);
			Calendar ca = Calendar.getInstance();
			ca.setTime(date);
			logger.info("Next update will be performed at " + ca.getTime());
		};
	};

	@Override
	public void update() {
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new UpdateTask(), 0, interval / 1000, TimeUnit.SECONDS);
	}

	@Override
	public void stop() {
		logger.info("Stop update.................");
		if (scheduler != null) {
			scheduler.shutdown();
			logger.info("Is it stopping : " + scheduler.isShutdown());
		}
	}
}
