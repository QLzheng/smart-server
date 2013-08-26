package com.tcl.smart.server;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.IRecommendWikiDao;
import com.tcl.smart.server.update.IUpdate;

public class WikiRecTest {
	
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/spring-config.xml");
//		IUpdate wikiUpdate = (IUpdate) context.getBean("wikiUpdate");
//		IRecommendWikiDao wikiDao = (IRecommendWikiDao)context.getBean("recommendWikiDao");
//		IEpgModelDao epgDao = (IEpgModelDao) context.getBean("epgModelDao");
//		
//		System.out.println("Test WikiUpdate .............. ");
//		long t1 = System.nanoTime();
//		wikiUpdate.update();
//		System.out.println("Elapse time :"+(System.nanoTime()-t1)*1e-9);
		
		 MongoTemplate mongoTemplate = (MongoTemplate)context.getBean("mongoTemplate");
		 System.out.println(mongoTemplate);
//		System.out.println("Test RecommendWikiDao .............. ");
//		int size = 10;
//		String[] epgIds = new String[]{"51830d2fcdceff15cfef1d42","51830d2fcdceff15cfef1d31"};
//		for(String id : epgIds){
//			List<BaikeBean> baike = wikiDao.getRecommenderWiki(id, size);
//			if(baike !=null && !baike.isEmpty()){
//				for(BaikeBean bk : baike)
//					System.out.println("Baike id:" +bk.getId() + "     baike name:" + bk.getName() + "     baike head:" + bk.getHeader());
//			}
//			System.out.println();
//		}
		
//		List<EpgModel> epgModels = epgDao.findAll();
//		int i = 0, size = epgModels.size();
//		for(EpgModel epg : epgModels ){
//			String id = epg.getId().toString();
//			System.out.println("Read at : " + i++ +"/" + size);
//			System.out.println("Recommend for " + id.toString()  + "  progam_nam :" +epg.getName() );
//			System.out.println("Recommend result : ");
//			List<BaikeBean> baike = wikiDao.getRecommenderWiki(id, 10);
//			if(baike !=null && !baike.isEmpty()){
//				for(BaikeBean bk : baike)
//					System.out.println("Baike id:" +bk.getId() + "     baike name:" + bk.getName() + "     keyType:" + bk.getKeyType());
//			}
//			System.out.println();
//		}
		
//		String epgId = "519f9c9b54ead1ee8cf32dae";
//		List<BaikeBean> baike = wikiDao.getRecommenderWiki(epgId, 10);
//		if(baike !=null && !baike.isEmpty()){
//			for(BaikeBean bk : baike)
//				System.out.println("Baike id:" +bk.getId() + "     baike name:" + bk.getName() + "     keyType:" + bk.getKeyType());
//		}
		
	}

}
