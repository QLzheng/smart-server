package com.tcl.smart.server;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.IRecommendNewsDao;
import com.tcl.smart.server.service.INewsSortService;

public class NewSortByKeyTest {
	
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IRecommendNewsDao recNewsDao = (IRecommendNewsDao) context.getBean("recommendNewsDao");
		INewsSortService newsSort = (INewsSortService) context.getBean("newSortByKeys");
		
		System.out.println("Test NewSortByKeys .............. ");
		String epgId = "519510a454ea69bbf929b442";
		List<NewsSearchRstItem> newsList = recNewsDao.getRecommendResults(epgId, 20);
		System.out.println(newsList.size());
		newsList = newsSort.sort(newsList);
		for(NewsSearchRstItem item : newsList){
			System.out.println(item.toString());
			System.out.println();
		}
		
//		Object2ObjectOpenHashMap<String, List<NewsSearchRstItem>> maps = recNewsDao.getAllRecResults();
//		Iterator<Object2ObjectOpenHashMap.Entry<String, List<NewsSearchRstItem>>> iter = maps.object2ObjectEntrySet().fastIterator();
//		int i = 0;
//		while(iter.hasNext()){
//			if(i>1)
//				break;
//			Object2ObjectOpenHashMap.Entry<String, List<NewsSearchRstItem>> entry = iter.next();
//			String id = entry.getKey();
//			List<NewsSearchRstItem> list = entry.getValue();
//			list = newsSort.sort(list);
//			for(NewsSearchRstItem item : list){
//				System.out.println("Id : " + id);
//				System.out.println(item.toString());
//				System.out.println();
//			}
//			i++;
//		}
	}

}
