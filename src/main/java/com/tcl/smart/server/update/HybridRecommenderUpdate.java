package com.tcl.smart.server.update;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.IFeatureNewsDao;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;
import com.tcl.smart.server.dao.IRecommendNewsDao;
import com.tcl.smart.server.service.INewsSortService;

public class HybridRecommenderUpdate implements IUpdate {

	private IFeatureNewsDao featureNewsDao;
	private INewsSortService sortNews;
	private IEpgModelDao epgDao;
	private IMovieDao movieDao;
	private INewsSearchRstItemDao newsDao;
	private IRecommendNewsDao recNewsDao;

	private static final int recNum = 20;

	public HybridRecommenderUpdate(IFeatureNewsDao featureNewsDao,INewsSortService sortNews,
			IEpgModelDao epgDao, IMovieDao movieDao,INewsSearchRstItemDao newsDao, 
			IRecommendNewsDao recNewsDao) {
		this.featureNewsDao = featureNewsDao;
		this.sortNews = sortNews;
		this.epgDao = epgDao;
		this.movieDao = movieDao;
		this.newsDao = newsDao;
		this.recNewsDao = recNewsDao;
	}

	@Override
	public void update() {

		/* Read all epg programs */
		List<EpgModel> epgModels = epgDao.findAll();
		ObjectOpenHashSet<String> epgIdsSet = new ObjectOpenHashSet<String>();

		int featureNum = (int) (recNum * 0.2);

		int j = 0;
		/* For every epg program, update it's recommender list */
		for (EpgModel epg : epgModels) {
			
			epgIdsSet.add(epg.getId().toString());
			System.out.println("Doing at " + j++ +"   ------------------  ");

			List<String> recNews = new ArrayList<String>();
			List<NewsSearchRstItem> epgNews= null;
			List<NewsSearchRstItem> featureNews =  new ArrayList<NewsSearchRstItem>();
			
			ObjectId id = epg.getId();
			String program_wiki_id = epg.getWikiId();
			String program_name = epg.getName();
			String wikiId = epg.getWikiId();
			
			long currentTime = System.currentTimeMillis();
			if( epg.getEndTime()!=null && epg.getEndTime().getTime() > currentTime){
				
				if (program_wiki_id != null && program_name != null) {

					epgNews = newsDao.findNewsSearchRstItemsByProgram(program_name, program_wiki_id);
					
					if (wikiId != null && wikiId != "") {
						MovieModel movie = movieDao.getMovieById(wikiId);
						if(movie != null){
							List<String> actorDirectors = new ArrayList<String>();

							List<String> actor = movie.getStarring();
							if (actor != null && !actor.isEmpty())
								actorDirectors.addAll(actor);

							List<String> director = movie.getDirector();
							if (director != null && !director.isEmpty())
								actorDirectors.addAll(director);

							List<String> newsIds = new ArrayList<String>();
							if (actorDirectors != null && !actorDirectors.isEmpty()) {
								for (String s : actorDirectors)
									newsIds.addAll(featureNewsDao.getNewsByFeature(s));
							}
							
							List<NewsSearchRstItem> temp = null;
		                    if(newsIds !=null && !newsIds.isEmpty()){
//		                    	System.out.println("Features:  " +  actorDirectors + "   Related news item number : " + newsIds.size());
		                    	temp = newsDao.findNewsSearchRstItemsByIds(newsIds);
		    					temp = sortNews.sort(temp);
		    					
		    					/* Filter duplicate news */
		    					ObjectOpenHashSet<String> titleSet = new ObjectOpenHashSet<String>();
		    					for(NewsSearchRstItem item :temp){
		    						String title = item.getTitle();
		    						if(!titleSet.contains(title)){
		    							titleSet.add(title);
		    							featureNews.add(item);
		    						}
		    						else
		    							continue;
		    					}
		    					
		    					/*Filter item than have exist in epgNews list*/
		    					ObjectOpenHashSet<String> epgIdSet = new ObjectOpenHashSet<String>();
		    					for(NewsSearchRstItem item : epgNews)
		    						epgIdSet.add(item.get_id());
		    					
		    					for(int i=0; i<featureNews.size() ; i++){
		    						if(epgIdSet.contains(featureNews.get(i).get_id()))
		    							featureNews.remove(i);
		    					}
		                    }
		                    
		                    /*Get feature related news */
		    				if(featureNews !=null && !featureNews.isEmpty()){
		    					featureNews = featureNews.subList(0, Math.min(featureNum, featureNews.size()));
		    				}
						}
					}
					
					/* Get epg recommender news */
					if (epgNews != null && !epgNews.isEmpty()) {
						epgNews = sortNews.sort(epgNews);
						if(featureNews !=null && !featureNews.isEmpty()){
							int size = featureNews.size(); 
						   epgNews = epgNews.subList(0, Math.min(recNum-size, epgNews.size()));
						}
						else
						   epgNews = epgNews.subList(0, Math.min(recNum, epgNews.size()));
					}
					
					
					for(NewsSearchRstItem item : epgNews){
						recNews.add(item.get_id());
					}
					for(NewsSearchRstItem item : featureNews)
						recNews.add(item.get_id());
					
					
					recNewsDao.putRecommendNewsResult(id.toString(), recNews);
					
					/*Test epg-news recommender*/
//					List<NewsSearchRstItem> tt = recNewsDao.getRecommendResults(id.toString(), recNum);
//					System.out.println("Get reommend from epg-news related table ");
//					System.out.println("Recommend for : " + epg.getName() + "  ------------------- " );
//					for(NewsSearchRstItem item : tt)
//						System.out.println(item);
					
//					System.out.println();
//					System.out.println("Get recommend from hybrid method ");
//					System.out.println("Recommend for : " + epg.getName() + " -------------------  " );
//					for(NewsSearchRstItem item : epgNews)
//						System.out.println(item);
//					for(NewsSearchRstItem item : featureNews)
//						System.out.println(item);
//					System.out.println();
					
				}else
					continue;
			}
		}
		
		/*Delete the recommendation result of off line epg*/
		List<String> ids = recNewsDao.getAllEpgId();
		for(String id : ids){
			if(!epgIdsSet.contains(id))
				recNewsDao.removeRecResultByEpgId(id);
		}
	}

	@Override
	public void updateByEpgId(String id) {
		// TODO Auto-generated method stub
		
	}
}
		

