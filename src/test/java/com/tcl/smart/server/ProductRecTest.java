package com.tcl.smart.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.dao.IRecommendProductDao;
import com.tcl.smart.server.update.IUpdate;

public class ProductRecTest {
	
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IUpdate proRecUpdate = (IUpdate)context.getBean("productRecommendUpdate");
		IUpdate dailyRecUpdate = (IUpdate)context.getBean("dailyUpdateProdectRec");
		IRecommendProductDao recProDao = (IRecommendProductDao) context.getBean("rcommendProductDao");
		
		System.out.println("Test productRecommendUpdate ............. ");
		long t1 = System.nanoTime();
		proRecUpdate.update();
		System.out.println("Elapse time :"+(System.nanoTime()-t1)*1e-9);
		
		
//		System.out.println("Test dailyUpdateProdectRec ............. ");
//		long t2 = System.nanoTime();
//		dailyRecUpdate.update();
//		System.out.println("Elapse time :"+(System.nanoTime()-t2)*1e-9);
		
		
//		String epgId = "519a569754ea11cf3905520f";
//		int size = 10;
//		List<YihaodianProductBean> list = recProDao.getRecProductById(epgId, size);
//		if(list !=null && !list.isEmpty()){
//			for(YihaodianProductBean e : list){
//				System.out.println( "productId : "+ e.getId() + "   name: " + e.getTitle());
//				System.out.println();
//			}
//		}
		
	}

}
