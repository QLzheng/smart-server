package com.tcl.smart.server;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.IFeatureNewsDao;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;
import com.tcl.smart.server.update.IUpdate;

public class FeatureNewsTest {
	
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IFeatureNewsDao featureDao = (IFeatureNewsDao) context.getBean("featureNewsDao");
		IUpdate featureNewUpdate = (IUpdate) context.getBean("featureNewsUpdate");
		INewsSearchRstItemDao newsDao = (INewsSearchRstItemDao) context.getBean("newsSearchRstItemDao");
		IUpdate hybridRecommenderUpdate = (IUpdate) context.getBean("hybridRecommenderUpdate");
		
		
		/*Store feature-news information to database*/
//		System.out.println("Test update feature-news information ....................... ");
//		featureNewUpdate.update();
		
		/*Test read feature-news information from database*/
//		String feature ="ÁõÊ«Ê«";
//		System.out.println("Read the news of feature : " + feature);
//	    List<String> newsIds = featureDao.getNewsByFeature(feature);
//	    System.out.println("NewsId list : " + newsIds);
//		List<NewsSearchRstItem> temp = newsDao.findNewsSearchRstItemsByIds(newsIds);
//		int j = 0;
//	    if(temp != null && !temp.isEmpty()){
//	    	for(NewsSearchRstItem n : temp)
//	    		System.out.println("The " + j++ + "th " + " news: " + n);
//		}
//	    
	    /*Test hybrid recommend */
	    System.out.println("Test hybridRecommenderUpdate .............. ");
	    long t1 = System.nanoTime();
	    hybridRecommenderUpdate.update();
	    System.out.println("Elapse time :"+(System.nanoTime()-t1)*1e-9);
		
	}

}
