package com.tcl.smart.server.update;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.ILogInformation;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;
import com.tcl.smart.server.dao.IRecommendNewsDao;
import com.tcl.smart.server.model.LogModel;
import com.tcl.smart.server.service.INewsSortService;
import com.tcl.smart.server.util.LogConstants;

public class RecommendResultUpdate implements IUpdate {

	private IEpgModelDao epgDao;
	private INewsSearchRstItemDao programNewsDao;
	private IRecommendNewsDao recNewsDao;
	private INewsSortService newsSortService;
	private ILogInformation logInfo;

	private static final int recNum = 20;

	public RecommendResultUpdate(IRecommendNewsDao recNewsDao,
			IEpgModelDao epgDao, INewsSearchRstItemDao programNewsDao,
			INewsSortService newsSortService, ILogInformation logInfo) {
		this.recNewsDao = recNewsDao;
		this.epgDao = epgDao;
		this.programNewsDao = programNewsDao;
		this.newsSortService = newsSortService;
		this.logInfo = logInfo;
	}

	@Override
	public void update() {

		LogModel log1 = null, log2 = null;
		int total = 0;
		long t0 = System.currentTimeMillis();
		
		/** Log information */
		Date startTime = new Date(t0);
		String infor = "start training news recommender";
		log1 = new LogModel(startTime, LogConstants.TRAINING, LogConstants.START_NEWS_TRAINING_OPER,
				LogConstants.SUCCESS, infor, LogConstants.GENERAL_LEVEL);
		/**put the start training log to database**/
		logInfo.putLogInfo(log1);
		
		/** Read all epg programs */
		List<EpgModel> epgModels = epgDao.findAll();
		int size = epgModels.size();
		ObjectOpenHashSet<String> epgIdSet = new ObjectOpenHashSet<String>();
		
        /** For every epg program, update it's recommender list */
		for (EpgModel epg : epgModels) {
			String id = epg.getId().toString();
			epgIdSet.add(id);
			
			/** Only update the programs those have not been played */
			if (epg.getEndTime() != null&& epg.getEndTime().getTime() > t0) {
				
				boolean flag = updateOneEpg(epg);
				if(flag)
					total++;
//				String program_wiki_id = epg.getWikiId();
//				String program_name = epg.getName();
//
//				/** Get the correlated news of this epg */
//				if (program_wiki_id != null && program_name != null) {
//					List<NewsSearchRstItem> news = programNewsDao.findNewsSearchRstItemsByProgram(program_name,program_wiki_id);
//					if (news != null && !news.isEmpty()) {
//						/** Call ranking function for sorting the news list */
//						news = newsSortService.sort(news);
//						news = news.subList(0, Math.min(recNum, news.size()));
//						
//						/* Update the recommender news */
//						List<String> newsIds = new ArrayList<String>();
//						for (NewsSearchRstItem item : news) {
//							newsIds.add(item.get_id());
//						}
//						recNewsDao.putRecommendNewsResult(id, newsIds);
//						total++;
//					}
//				}
			} else
				continue;
		}

		/** Delete recommendation result of off line epg */
		List<String> ids = recNewsDao.getAllEpgId();
		for (String id : ids) {
			if (!epgIdSet.contains(id))
				recNewsDao.removeRecResultByEpgId(id);
		}
		
		/** Log information */
		long t1 = System.currentTimeMillis();
		Date endTime = new Date(t1);
		String infor2 = "Start traing at " + startTime + ", end at " + endTime + ", " + 
		           total + "/" + size + " has recommender result." + ", spend time "+(t1 - t0)
					/ 1000.0 + " seconds.";
		log2 = new LogModel(endTime, LogConstants.TRAINING, LogConstants.END_NEWS_TRAINING_OPER, 
				LogConstants.SUCCESS, infor2, LogConstants.GENERAL_LEVEL);
		logInfo.putLogInfo(log2);
	}

	
	private boolean updateOneEpg(EpgModel epg){
		
		boolean flag = false;
		String program_wiki_id = epg.getWikiId();
		String program_name = epg.getName();

		/** Get the correlated news of this epg */
		if (program_wiki_id != null && program_name != null) {
			List<NewsSearchRstItem> news = programNewsDao.findNewsSearchRstItemsByProgram(program_name,program_wiki_id);
			if (news != null && !news.isEmpty()) {
				/** Call ranking function for sorting the news list */
				news = newsSortService.sort(news);
				news = news.subList(0, Math.min(recNum, news.size()));
				
				/** Update the recommender news */
				List<String> newsIds = new ArrayList<String>();
				for (NewsSearchRstItem item : news) {
					newsIds.add(item.get_id());
				}
				recNewsDao.putRecommendNewsResult(epg.getId().toString(), newsIds);
				flag = true;
			}
		}
		return flag;
	}
	
	
	@Override
	public void updateByEpgId(String id) {
		EpgModel epg = epgDao.findEpgModelById(id);
		if(null == epg)
			return;
		
		updateOneEpg(epg);
		
		/** Log information */
		String infor2 = "Training one epg news's recommendation epg id = " + id ;
		LogModel log = new LogModel(new Date(System.currentTimeMillis()), LogConstants.TRAINING, LogConstants.END_NEWS_TRAINING_OPER, 
				LogConstants.SUCCESS, infor2, LogConstants.GENERAL_LEVEL);
		logInfo.putLogInfo(log);
	}
}
