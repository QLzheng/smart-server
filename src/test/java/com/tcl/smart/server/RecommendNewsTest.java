package com.tcl.smart.server;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.IRecommendNewsDao;

public class RecommendNewsTest {
	
	public static void main(String[] args) {
	
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IEpgModelDao epgDao = (IEpgModelDao) context.getBean("epgModelDao");
		IRecommendNewsDao recNewsDao = (IRecommendNewsDao) context.getBean("recommendNewsDao");
		
		/*Test epg program data */
		List<EpgModel> epgModels = epgDao.findAll();
		int i = 0;
		for(EpgModel epg : epgModels ){
			
//			if(i>10)
//				break;
			
			ObjectId id = epg.getId();
			
			System.out.println("Read at : " + i++ );
//			System.out.println(movieDao.getMovieById(id));
			System.out.println("Recommend for " + id );
			System.out.println("Recommend result : "+ recNewsDao.getRecommendResults(id.toString(),10));
			System.out.println();
		}
	}

}
