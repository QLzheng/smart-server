package com.tcl.smart.server.initialization;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.NewsClassification;
import com.tcl.smart.server.dao.impl.NewsClassificationDao;

/**
 * @author fanjie
 * @date 2013-4-1
 */
public class NewsClassificationInitialization implements IInitialization {

	public void initialization() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		NewsClassificationDao dao = (NewsClassificationDao) context.getBean("newsClassificationDao");
		dao.init();
		NewsClassification newsClassification = new NewsClassification();
		newsClassification.setSource("baidu");
		newsClassification.setLink("http://news.baidu.com");
		newsClassification.setLanguage("zh-cn");
		newsClassification.setImage_link("news.baidu.com");
		newsClassification.setImage_title("news.baidu.com");
		newsClassification.setImage_url("http://img.baidu.com/img/logo-news.gif");
		newsClassification.setImage_height("31");
		newsClassification.setImage_width("88");
		newsClassification.setVersion("2.0");
		newsClassification.setTitle("");
		newsClassification.setCopyright("");
		newsClassification.setRss_url("");
		newsClassification.setDescription("");
		newsClassification.setImage_description("");
		for (int i = 1; i <= 14; i++) {
			newsClassification.set_id(i + "");
			dao.updateOrInsertNewsClassification(newsClassification);
		}
	}

	public static void main(String[] args) {
//		NewsClassificationInitialization init = new NewsClassificationInitialization();
//		init.initialization();
	}

}
