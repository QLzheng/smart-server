package com.tcl.smart.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.bean.cmd.TvWikInfoRequestAck;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;
import com.tcl.smart.server.dao.IRecommendNewsDao;
import com.tcl.smart.server.dao.impl.MovieDao;
import com.tcl.smart.server.service.IHuanApiService;
import com.tcl.smart.server.update.IUpdate;
import com.tcl.smart.server.update.IUpdateEngine;

public class UpdateRecommendResultTest {

	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		// IUpdate recommendResultUpdate = (IUpdate)
		// context.getBean("recommendResultUpdate");
		// MovieDao movieDao = (MovieDao) context.getBean("movieDao");
		// IEpgModelDao epgDao = (IEpgModelDao) context.getBean("epgModelDao");
		// IHuanApiService huanApiService = (IHuanApiService)
		// context.getBean("huanApiService");
		final IUpdateEngine periodUpdateEngine = (IUpdateEngine) context.getBean("periodicUpdateEngine");
		IRecommendNewsDao recNewsDao = (IRecommendNewsDao) context.getBean("recommendNewsDao");
		INewsSearchRstItemDao programNewsDao = (INewsSearchRstItemDao) context.getBean("newsSearchRstItemDao");

		/* Test wiki data */
		// List<String> ids = movieDao.getAllMovieId();
		// int i = 0;
		// for(String id : ids){
		// System.out.println("Read at : " + i++ );
		// System.out.println(movieDao.getMovieById(id));
		// System.out.println();
		// }

		// /*Test get epg recommender news from database */
		// List<EpgModel> epgModels = epgDao.findAll();
		// i = 0;
		// for(EpgModel epg : epgModels ){
		// ObjectId id = epg.getId();
		// System.out.println("Read at : " + i++ );
		// System.out.println("Recommend for " + id );
		// List<NewsSearchRstItem> news =
		// recNewsDao.getRecommendResults(id.toString(),10);
		// System.out.println("Recommend result : "+ news);
		// System.out.println();
		// }

		/* Test update recommender result */
		System.out.println("Test update recommender result ....................... ");
		// recommendResultUpdate.update();

		periodUpdateEngine.update();
		// try {
		// // waiting for 5 second to stop update.
		// Thread.sleep(1000*2);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// periodUpdateEngine.stop();

		/* Get deatail wiki informatin for special movie and update it */
		// TvWikInfoRequestAck data =
		// huanApiService.getWikiInfo("51524ab4ed454b1a2c000001");
		// String id = "51524ab4ed454b1a2c000001";
		// movieDao.updateMovie(data);
		// System.out.println(movieDao.getMovieById(id));

	}
}
