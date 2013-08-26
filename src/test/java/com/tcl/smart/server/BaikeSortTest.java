package com.tcl.smart.server;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.dao.IBaikeBeanDao;

public class BaikeSortTest {
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IBaikeBeanDao baikeDao = (IBaikeBeanDao) context.getBean("baikeBeanDao");
//		IFeatureNewsDao featureDao = (IFeatureNewsDao) context.getBean("featureNewsDao");
		
		List<BaikeBean> a =baikeDao.findBaikeBeansByName("·¶±ù±ù");
		for(BaikeBean b:a){
			System.out.println(b.allToText());
		}
		
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
	   
//	    long t1 = System.nanoTime();
//	    hybridRecommenderUpdate.update();
//	    System.out.println("Elapse time :"+(System.nanoTime()-t1)*1e-9);
		
	}
}
