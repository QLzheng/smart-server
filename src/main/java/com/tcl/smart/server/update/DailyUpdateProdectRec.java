package com.tcl.smart.server.update;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DailyUpdateProdectRec implements IUpdate{
	
	private static final Logger logger = LoggerFactory.getLogger(PeriodicUpdateEngine.class);

	private List<IUpdate> updates;
	
	public DailyUpdateProdectRec(List<IUpdate> updates) {
		this.updates = updates;
	}
	
	public void updateTask() {
		logger.debug("Start update products recommendation.");
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
				logger.info("*-*-*-*-*-*-*-*-*-*-*   " + update.getClass().getName() + " End! Using " + t2 + " seconds!");
			}
		}
		double t3 = (System.currentTimeMillis() - t0) / 1000.0;
		Date dateend = new Date();
		logger.debug("End updating...  total use " + t3 + " seconds!    at =  " + dateend);
	}

	@Override
	public void update() {
		updateTask();
	}

	@Override
	public void updateByEpgId(String id) {
		// TODO Auto-generated method stub
		
	}

}
