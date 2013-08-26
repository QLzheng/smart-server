package com.tcl.smart.server.crawler.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.BaikeUrl;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.dao.impl.BaikeBeanDao;
import com.tcl.smart.server.dao.impl.BaikeUrlDao;
import com.tcl.smart.server.dao.impl.MovieDao;
import com.tcl.smart.server.util.Constants;
import com.tcl.smart.server.util.FileOperation;

/**
 * 百度百科词条爬虫，同时解析词条并入库，支持被封后自动延时执行
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaiduBaikeCrawler {
	public static void crawl() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		MovieDao movieDao = (MovieDao) context.getBean("movieDao");
		List<String> ids = movieDao.getAllMovieId();
		List<String> keys = new ArrayList<String>();
		for (String id : ids) {
			MovieModel model = movieDao.getMovieById(id);
			keys.add(model.getTitle());
			List<String> keywords = model.getKeyQueryItems();
			if (keywords != null)
				for (String keyword : keywords)
					keys.add(keyword);
		}
		crawl(keys, "E://baike/");
	}

	public static void crawl(List<String> keywords, String savePath) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		BaikeUrlDao baikeUrlDao = (BaikeUrlDao) context.getBean("baikeUrlDao");
		BaikeBeanDao baikeBeanDao = (BaikeBeanDao) context.getBean("baikeBeanDao");
		int size = keywords.size();
		System.out.println(Constants.getCurrentTime() + " Size: " + size);
		FileDownLoader downLoader = new FileDownLoader();
		for (int i = 0; i < size; i++) {
			String filePath = downLoader.downloadBaiduBaikeFile(keywords.get(i), baikeUrlDao, savePath);
			System.out.println(Constants.getCurrentTime() + "【" + (i + 1) + "/" + size + "】 Fetching " + keywords.get(i) + ": " + filePath);
			if (filePath != null && !filePath.endsWith("htm")) {
				if (filePath.endsWith("none")) {
					System.err.println(Constants.getCurrentTime() + "【" + (i + 1) + "/" + size + "】 No term " + keywords.get(i));
				} else {
					System.err.println(Constants.getCurrentTime() + " Being blocked, waiting for 20 min..");
					try {
						Thread.sleep(1000 * 60 * 20);
					} catch (InterruptedException e) {
					}
				}
			} else {
				List<BaikeBean> baikes = BaiduBaikeParserTool.extractText(filePath);
				if (baikes != null) {
					for (BaikeBean baike : baikes) {
						if (!baikeBeanDao.existBaikeBean(baike)) {
							baikeBeanDao.insertBaikeBean(baike);
							System.out.println(Constants.getCurrentTime() + " Saved parsed baike: " + baike);
						}
					}
				}
			}
			try {
				Thread.sleep(10000);
				if (i != 0 && i % 20 == 0)
					Thread.sleep(1000 * 60 * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String> getPage(int sizePerPage, int page, List<String> keys) {
		int startIndex = page * sizePerPage;
		if (startIndex >= keys.size())
			return new ArrayList<String>();
		int endIndex = (startIndex + sizePerPage) >= keys.size() ? keys.size() : (startIndex + sizePerPage);
		return keys.subList(startIndex, endIndex);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> keys = FileOperation.readFileByLinesForArray("E://baike/keywords.txt");
		// int sizePerPage = 100000;
		// List<String> pageKeys = getPage(sizePerPage, 0, keys);
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		BaikeUrlDao baikeUrlDao = (BaikeUrlDao) context.getBean("baikeUrlDao");
		List<BaikeUrl> crawledBaikeUrls = baikeUrlDao.findBaikeUrlsByResult("ok");
		for (BaikeUrl baikeUrl : crawledBaikeUrls) {
			keys.remove(baikeUrl.getName());
		}
		List<BaikeUrl> noneBaikeUrls = baikeUrlDao.findBaikeUrlsByResult("none");
		for (BaikeUrl baikeUrl : noneBaikeUrls) {
			keys.remove(baikeUrl.getName());
		}
		crawl(keys, "E://baike/");
	}
}
