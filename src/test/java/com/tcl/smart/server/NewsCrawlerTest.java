package com.tcl.smart.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.service.IDailyCrawlerService;

public class NewsCrawlerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IDailyCrawlerService crawler = (IDailyCrawlerService) context.getBean("newsDailyCrawlerByKeywordsService");
		crawler.start();
	}
}
