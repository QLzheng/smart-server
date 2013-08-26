package com.tcl.smart.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;
import com.tcl.smart.server.dao.IRecommendNewsDao;
import com.tcl.smart.server.dao.impl.MovieDao;
import com.tcl.smart.server.update.IUpdate;
import com.tcl.smart.server.update.IUpdateEngine;

public class UpdateRecommendResultOpt {

	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		 IUpdate recommendResultUpdate = (IUpdate)context.getBean("recommendResultUpdate");
		 MovieDao movieDao = (MovieDao) context.getBean("movieDao");
		// IHuanApiService huanApiService = (IHuanApiService)
		// context.getBean("huanApiService");
		IEpgModelDao epgDao = (IEpgModelDao) context.getBean("epgModelDao");
		IRecommendNewsDao recNewsDao = (IRecommendNewsDao) context.getBean("recommendNewsDao");
		INewsSearchRstItemDao programNewsDao = (INewsSearchRstItemDao) context.getBean("newsSearchRstItemDao");
		

		
		/*Test update recommender result */
		System.out.println("Test update recommender result ....................... ");
		long t1 = System.nanoTime();
		recommendResultUpdate.update();
		System.out.println("Elapse time :"+(System.nanoTime()-t1)*1e-9);
		
//		String name = "ДэМо(9)";
//		String wiki_id = "4ff4f753cf63579748000364";
//		
//		List<NewsSearchRstItem> news = programNewsDao.findNewsSearchRstItemsByProgram(name, wiki_id);
//	    System.out.println("Rec news : " + news);
	    
	    
	    
		
		/* Test wiki data */
//		 List<String> ids = movieDao.getAllMovieId();
//		 int i = 0;
//		 for(String id : ids){
//			 
//			 if(i>10)
//				 break;
//			 
//		 System.out.println("Read at : " + i++ );
//		 System.out.println(movieDao.getMovieById(id));
//		 System.out.println();
//		 
//		 MovieModel movie = movieDao.getMovieById(id);
//		 
//		 Map<String, Object> messages = new HashMap<String, Object>();
//			
//			messages.put("_id", movie.get_id());
//			messages.put("content", movie.getContent());
//			messages.put("country", movie.getCountry());
//			messages.put("cover", movie.getCover());
//			messages.put("director", movie.getDirector());
//			messages.put("language", movie.getLanguage());
//			messages.put("released", movie.getReleased());
//			messages.put("starring", movie.getStarring());
//			messages.put("tags", movie.getTags());
//			messages.put("title", movie.getTitle());
//			
//		 }

		
		
		List<EpgModel> epgModels = epgDao.findAll();
		int i = 0;
		for(EpgModel epg : epgModels ){
			ObjectId id = epg.getId();
			System.out.println("Read at : " + i++ );
			System.out.println("Recommend for " + id.toString()  + "  progam_nam :" +epg.getName() );
			List<NewsSearchRstItem> news = recNewsDao.getRecommendResults(id.toString(),10);
			System.out.println("Recommend result : ");
			for(NewsSearchRstItem e : news){
				System.out.println("New title : " + e.getTitle() +"   key: "+ e.getProgram_wiki_keys() +  "   key type : " + e.getKeyType());
			}
			System.out.println();
		}

	}
}
