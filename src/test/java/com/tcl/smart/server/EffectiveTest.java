package com.tcl.smart.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.dao.IRecommendProductDao;
import com.tcl.smart.server.dao.IYihaodianProductDao;

public class EffectiveTest {
	
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IRecommendProductDao recProDao = (IRecommendProductDao) context.getBean("rcommendProductDao");
		IYihaodianProductDao yiHaoDianDao = (IYihaodianProductDao) context.getBean("yihaodianProductDao");
		
//		System.out.println("Test productRecommendUpdate ............. ");
//		long t1 = System.nanoTime();
//		recProDao.getAllEpgId();
//		System.out.println("Elapse time :"+(System.nanoTime()-t1)*1e-9);
		
		int n = 50, recNum = 20;;
		long t1 = System.nanoTime();
		
		List<YihaodianProductBean> defaultRecList = yiHaoDianDao.getDefaultRecProducts(n);
		List<YihaodianProductBean> newDefaultList = new ArrayList<YihaodianProductBean>();
		Random r = new Random();
		/*Random select some items from the default list */
		for(int i=0; i<recNum; i++){
			int index = r.nextInt(n);
			newDefaultList.add(defaultRecList.get(index));
		}
			
		System.out.println("Elapse time :"+(System.nanoTime()-t1)*1e-9);
	}

}
