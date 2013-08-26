package com.tcl.smart.server.update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.dao.IBaikeBeanDao;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.ILogInformation;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.IRecommendWikiDao;
import com.tcl.smart.server.model.LogModel;
import com.tcl.smart.server.service.IBaikeRecommendService;
import com.tcl.smart.server.service.IBaikeSearchService;
import com.tcl.smart.server.service.IBaikeSortService;
import com.tcl.smart.server.service.impl.BlockException;
import com.tcl.smart.server.util.LogConstants;

public class WikiUpdate implements IUpdate,IBaikeRecommendService {
	private static final Logger logger = LoggerFactory.getLogger(WikiUpdate.class);
	private IRecommendWikiDao wikiDao;
	private IEpgModelDao epgDao;
	private IMovieDao movieDao;
	private IBaikeBeanDao baikeDao;
	private IBaikeSortService baikeSort;
	private IBaikeSearchService baikeSearch;
	private ILogInformation logInfo;

	public WikiUpdate(IRecommendWikiDao wikiDao, IEpgModelDao epgDao,
			IMovieDao movieDao, IBaikeBeanDao baikeDao,
			IBaikeSortService baikeSort,IBaikeSearchService baikeSearch,
			ILogInformation logInfo) {
		this.wikiDao = wikiDao;
		this.epgDao = epgDao;
		this.movieDao = movieDao;
		this.baikeDao = baikeDao;
		this.baikeSort = baikeSort;
		this.baikeSearch = baikeSearch;
		this.logInfo = logInfo;
	}

	@Override
	public void update() {
		
		LogModel log1 = null, log2 = null;
		int trainingSize = 0;
		long t0 = System.currentTimeMillis();
		
		/** Log information */
		Date startTime = new Date(t0);
		String infor = "start training baike recommender";
		log1 = new LogModel(startTime, LogConstants.TRAINING, LogConstants.START_BAIKE_TRAINING_OPER,
				LogConstants.SUCCESS, infor, LogConstants.GENERAL_LEVEL);
		/**put the start training log to database**/
		logInfo.putLogInfo(log1);
		

		/* Drop recommendation result of off line epg */
		wikiDao.dropAll();
		
		/* Get all epg programs */
		List<EpgModel> epgModels = epgDao.findAll();
		int i= 0, size=epgModels.size();
		
		for (EpgModel epg : epgModels) {
			logger.info("Doing at: " + i++ +"/"+size );
			ObjectId id = epg.getId();
			String wikiId = epg.getWikiId();
			List<String> wikiIds = new ArrayList<String>();
			
			if (wikiId != null && wikiId != "") {
				/* Get the wiki information of the special epg */
				MovieModel movie = movieDao.getMovieById(wikiId);
				if (movie != null) {
					String title = movie.getTitle();
					/* Get the baike's terms information of program_name,directors and actors */
					List<String> actorDirectors = movie.getKeyQueryItems();
					/*delete the first one of actorDirectors------the title*/
					actorDirectors.remove(title);
					List<BaikeBean> baikeList = baikeDao.findBaikeBeansByName(title);
					/* Sort the baike's terms according to the relation. Add program name baike first, may be not limit to 1.  */
					if (baikeList != null && !baikeList.isEmpty()){
						baikeList = baikeSort.sort(baikeList, title, actorDirectors);
						wikiIds.add(baikeList.get(0).getId());
					}
					
					for (String name : actorDirectors) {
						List<BaikeBean> baikeList2 = baikeDao.findBaikeBeansByName(name);
						/* Sort the baike's terms according to the relation. Add program name baike first, may be not limit to 1.  */
						if (baikeList2 != null && !baikeList2.isEmpty()){
							baikeList2 = baikeSort.sort(baikeList2, title, actorDirectors);
							wikiIds.add(baikeList2.get(0).getId());
						}
					}
				}
			} else{
				String epg_name = epg.getParsedName();
				List<BaikeBean> baikeList = baikeDao.findBaikeBeansByName(epg_name);
				/* Sort the baike's terms according to the relation. Add program name baike first, may be not limit to 1.  */
				if (baikeList != null && !baikeList.isEmpty()){
					for(BaikeBean b:baikeList){
						wikiIds.add(b.getId());
					}
				}
			}
			if (!wikiIds.isEmpty()){
				wikiDao.putRecommenderWiki(id.toString(), wikiIds);
				trainingSize++;
			}
		}
		
		/** Log information */
		long t1 = System.currentTimeMillis();
		Date endTime = new Date(t1);
		String infor2 = "Start traing at " + startTime + ", end at " + endTime + ", " + 
				trainingSize + "/" + size + " has recommender result." + ", spend time "+(t1 - t0)
					/ 1000.0 + " seconds.";
		log2 = new LogModel(endTime, LogConstants.TRAINING, LogConstants.END_BAIKE_TRAINING_OPER, 
				LogConstants.SUCCESS, infor2, LogConstants.GENERAL_LEVEL);
		logInfo.putLogInfo(log2);
	}
	
	/**
	 * 生成一个epg的对应baike推荐，同时存入数据库。
	 * @param epgId
	 * @return
	 */
	private List<BaikeBean> generateRecommenderForEpg(String epgId){
		EpgModel epg = epgDao.findEpgModelById(epgId);
		if(epg==null){
			logger.error("valid epg id");
			return null;
		}
		ObjectId id = epg.getId();
		String wikiId = epg.getWikiId();
		List<String> wikiIds = new ArrayList<String>();
		List<BaikeBean> baikeReturn = new ArrayList<BaikeBean>();
		
		if (wikiId != null && wikiId != "") {
			/* Get the wiki information of the special epg */
			MovieModel movie = movieDao.getMovieById(wikiId);
			if (movie != null) {
				String title = movie.getTitle();
				/* Get the baike's terms information of program_name,directors and actors */
				List<String> actorDirectors = movie.getKeyQueryItems();
				actorDirectors.remove(title);
				/* Get the baike's terms information of program_name,directors and actors */
				List<BaikeBean> baikeList = null;
				try {
					baikeList = baikeSearch.search(title);
				} catch (BlockException e) {
					e.printStackTrace();
				}
				/* Sort the baike's terms according to the relation. Add program name baike first, may be not limit to 1.  */
				if (baikeList != null && !baikeList.isEmpty()){
					baikeList = baikeSort.sort(baikeList, title, actorDirectors);
					baikeReturn.add(baikeList.get(0));
					wikiIds.add(baikeList.get(0).getId());
				}
				
				for (String name : actorDirectors) {
					List<BaikeBean> baikeList2 = null;
					try {
						baikeList2 = baikeSearch.search(name);
					} catch (BlockException e) {
						e.printStackTrace();
					}
//					List<BaikeBean> baikeList2 = baikeDao.findBaikeBeansByName(name);
					/* Sort the baike's terms according to the relation. Add program name baike first, may be not limit to 1.  */
					if (baikeList2 != null && !baikeList2.isEmpty()){
						baikeList2 = baikeSort.sort(baikeList2, title, actorDirectors);
						baikeReturn.add(baikeList2.get(0));
						wikiIds.add(baikeList2.get(0).getId());
					}
				}
			}
		} else{
			String epg_name = epg.getParsedName();
//			List<BaikeBean> baikeList = baikeDao.findBaikeBeansByName(epg_name);
			List<BaikeBean> baikeList = null;
			try {
				baikeList = baikeSearch.search(epg_name);
			} catch (BlockException e) {
				e.printStackTrace();
			}
			/* Sort the baike's terms according to the relation. Add program name baike first, may be not limit to 1.  */
			if (baikeList != null && !baikeList.isEmpty()){
				List<String> filterList = new ArrayList<String>(){{
					add("电视");
					add("电影");
					add("电视剧");
				}};
				baikeList = baikeSort.sort(baikeList, epg_name, filterList);
				baikeReturn.addAll(baikeList);
				wikiIds.add(baikeList.get(0).getId());
			}
		}
		if (!wikiIds.isEmpty())
			wikiDao.putRecommenderWiki(id.toString(), wikiIds);

		return baikeReturn;
	}
	
	
	

	@Override
	public List<BaikeBean> getRecommenderWiki(String epgId, int size) {
		List<BaikeBean> returnlist = wikiDao.getRecommenderWiki(epgId, size);
		if(null==returnlist||returnlist.isEmpty()){
			returnlist = generateRecommenderForEpg(epgId);
		}
		int sizeR = size > returnlist.size() ? returnlist.size() : size;
		return returnlist.subList(0, sizeR);
	}

	@Override
	public void updateByEpgId(String id) {
		
		List<BaikeBean> baikeList = generateRecommenderForEpg(id);
		if(null == baikeList || baikeList.isEmpty())
			return;
		
		List<String> wikiIds = new ArrayList<String>();
		for(BaikeBean b : baikeList){
			wikiIds.add(b.getId());
		}
		wikiDao.putRecommenderWiki(id, wikiIds);
		
		/** Log information */
		String infor2 = "Training one epg baike's recommendation epg id = " + id ;
		LogModel log = new LogModel(new Date(System.currentTimeMillis()), LogConstants.TRAINING, LogConstants.END_BAIKE_TRAINING_OPER, 
				LogConstants.SUCCESS, infor2, LogConstants.GENERAL_LEVEL);
		logInfo.putLogInfo(log);
	}

}
