package com.tcl.smart.server.update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.ILogInformation;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.IRecommendProductDao;
import com.tcl.smart.server.dao.IYihaodianProductDao;
import com.tcl.smart.server.model.LogModel;
import com.tcl.smart.server.util.LogConstants;

public class ProductRecommendUpdate implements IUpdate {

	private IEpgModelDao epgDao;
	private IRecommendProductDao recProductDao;
	private IYihaodianProductDao yiHaoDianProDao;
	private IMovieDao movieDao;
	private ILogInformation logInfo;
	private static final int recNum = 20;
	private int numberThreads = 10;
	private static final Logger logger = LoggerFactory.getLogger(ProductRecommendUpdate.class);

	public ProductRecommendUpdate(IEpgModelDao epgDao, IRecommendProductDao recProductDao,
			IYihaodianProductDao yiHaoDianProDao, IMovieDao movieDao, int numberThreads,
			ILogInformation logInfo) {
		this.epgDao = epgDao;
		this.recProductDao = recProductDao;
		this.yiHaoDianProDao = yiHaoDianProDao;
		this.movieDao = movieDao;
		this.numberThreads = numberThreads;
		this.logInfo = logInfo;
	}


	@Override
	public void update() {
		
		LogModel log1 = null, log2 = null;
		long t0 = System.currentTimeMillis();
		Date startTime = new Date(t0);
		String infor = "start training products recommender";
		log1 = new LogModel(startTime, LogConstants.TRAINING, LogConstants.START_PRODUCTS_TRAINING_OPER,
				LogConstants.SUCCESS, infor, LogConstants.GENERAL_LEVEL);
		/**put the start training log to database**/
		logInfo.putLogInfo(log1);
		
		/* Read all epg programs */
		List<EpgModel> epgModels = epgDao.findAll();
		int size = epgModels.size();
		
		/* Drop recommendation result of off line epg */
		recProductDao.dropAll();

//		updateEpgList(epgModels);

		int numberPerThread = size / numberThreads;
		final CountDownLatch threadSignal = new CountDownLatch(numberThreads);

		for (int i = 0; i < numberThreads; i++) {
			int start, end;
			if (i == numberThreads - 1) {
				start = i * numberPerThread;
				end = epgModels.size();
			} else {
				start = i * numberPerThread;
				end = i * numberPerThread + numberPerThread;
			}
			final List<EpgModel> epgListPer = epgModels.subList(start, end);
			final String threadName="Product_update_thread-"+i;
			new Thread(new Runnable() {
				public void run() {
					logger.debug("Thread = " + Thread.currentThread().getName()
							+ ", number epg =" + epgListPer.size());
					long t1 = System.currentTimeMillis();
					
					updateEpgList(epgListPer);
					
					logger.debug("Thread = " + Thread.currentThread().getName()
							+ " done in " + (System.currentTimeMillis() - t1)
							/ 1000.0 + " seconds.");
					threadSignal.countDown();
				}
			},threadName).start();
		}
		try {
			threadSignal.await();
			logger.debug("all thread ends");
		} catch (InterruptedException e) {
			log2 = new LogModel(new Date(System.currentTimeMillis()), LogConstants.TRAINING, LogConstants.END_PRODUCTS_TRAINING_OPER, 
					LogConstants.FAILURE, "training failure", LogConstants.GENERAL_LEVEL);
			logInfo.putLogInfo(log2);
			e.printStackTrace();
		}

		long t1 = System.currentTimeMillis();
		Date endTime = new Date(t1);
		String infor2 = "Start traing at " + startTime + " and end at " + endTime + ", epg size " + size +
				 ", spend time "+(t1 - t0)/1000.0 + " seconds.";
		log2 = new LogModel(endTime, LogConstants.TRAINING, LogConstants.END_PRODUCTS_TRAINING_OPER, 
				LogConstants.SUCCESS, infor2, LogConstants.GENERAL_LEVEL);
		logInfo.putLogInfo(log2);
		
		logger.debug("ProductRecommendUpdate done in time :" + (System.nanoTime() - t0) * 1e-9);
	}

	
	/**
	 * input epgModel List,for each epg
	 * get all the features and search for product
	 * store the product list into database
	 * @param epgModels
	 */
	private void updateEpgList(List<EpgModel> epgModels) {
		/* For every epg program, update it's product recommendation list */
		int i=0;
		int total=epgModels.size();
		for (EpgModel epg : epgModels) {
			i++;
			logger.debug("Thread = " + Thread.currentThread().getName()+" Doing at: " + i +"/"+total );
			updateOneEpg(epg);
//			String epgId = epg.getId().toString();
//			String wikiId = epg.getWikiId();
//			/* get all the keys for query */
//			List<String> keys = new ArrayList<String>();
//			if (wikiId != null && wikiId != "") {
//				MovieModel movie = movieDao.getMovieById(wikiId);
//				if (movie != null) {
//					keys = movie.getKeyQueryItems();
//					List<String> tags = movie.getTags();
//					if (tags != null && tags.size() > 0) {
//						keys.addAll(tags);
//					}
//				}
//			}
//			if (keys.isEmpty()) {
//				keys.add(epg.getParsedName());
//			}
//
//			/* Search products according to epg information */
//			List<String> proIds = new ArrayList<String>();
//			List<String> keywords = new ArrayList<String>();
//			if (!keys.isEmpty()) {
//				for (String s : keys) {
//					List<String> list = yiHaoDianProDao.findYihaodianProductIdsByFeature(s);
//					if (list != null && !list.isEmpty()){
//						proIds.addAll(list);
//						
//						int len = list.size();
//						for(int j = 0; j<len; j++)
//							keywords.add(s);
//					}
//				}
//			}
//
//			if (!proIds.isEmpty()) {
//				proIds = proIds.subList(0,Math.min(recNum, proIds.size()));
//				keywords = keywords.subList(0, Math.min(recNum, keywords.size()));
//				/* Update the recommender result */
//				recProductDao.putRecProductById(epgId, proIds, keywords);
//			}
		}
	}

	
	private void updateOneEpg(EpgModel epg ){
		if(null == epg)
			return;
		
		String epgId = epg.getId().toString();
		String wikiId = epg.getWikiId();
		/* get all the keys for query */
		List<String> keys = new ArrayList<String>();
		if (wikiId != null && wikiId != "") {
			MovieModel movie = movieDao.getMovieById(wikiId);
			if (movie != null) {
				keys = movie.getKeyQueryItems();
				List<String> tags = movie.getTags();
				if (tags != null && tags.size() > 0) {
					keys.addAll(tags);
				}
			}
		}
		if (keys.isEmpty()) {
			keys.add(epg.getParsedName());
		}

		/* Search products according to epg information */
		List<String> proIds = new ArrayList<String>();
		List<String> keywords = new ArrayList<String>();
		if (!keys.isEmpty()) {
			for (String s : keys) {
				List<String> list = yiHaoDianProDao.findYihaodianProductIdsByFeature(s);
				if (list != null && !list.isEmpty()){
					proIds.addAll(list);
					
					int len = list.size();
					for(int j = 0; j<len; j++)
						keywords.add(s);
				}
			}
		}

		if (!proIds.isEmpty()) {
			proIds = proIds.subList(0,Math.min(recNum, proIds.size()));
			keywords = keywords.subList(0, Math.min(recNum, keywords.size()));
			/* Update the recommender result */
			recProductDao.putRecProductById(epgId, proIds, keywords);
		}
	}

	
	@Override
	public void updateByEpgId(String id) {
		
		EpgModel epg = epgDao.findEpgModelById(id);
		if(null == epg)
			return;
		
		updateOneEpg(epg);
		
		/** Log information */
		String infor2 = "Training one epg product's recommendation epg id = " + id ;
		LogModel log = new LogModel(new Date(System.currentTimeMillis()), LogConstants.TRAINING, LogConstants.END_PRODUCTS_TRAINING_OPER, 
				LogConstants.SUCCESS, infor2, LogConstants.GENERAL_LEVEL);
		logInfo.putLogInfo(log);
		
	}

}
