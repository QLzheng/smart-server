package com.tcl.smart.server.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.dao.IVideoDao;

/**
 * @author fanjie
 * @date 2013-3-7
 */
public class Crawler {
	private static final Logger logger = LoggerFactory.getLogger(Crawler.class);

	private String urlPrefix1 = "http://image.5i.tv/thumb/100/150/";
	private String urlPrefix2 = "http://image.5i.tv/thumb/150/200/";
	private String downloadFolder1 = "E:\\thumbs\\100_150\\";
	private String downloadFolder2 = "E:\\thumbs\\150_200\\";

	public Crawler() {
		File downloadPathFile = new File(downloadFolder1);
		if (!downloadPathFile.exists() || !downloadPathFile.isDirectory()) {
			downloadPathFile.mkdirs();
		}
		File downloadPathFile2 = new File(downloadFolder2);
		if (!downloadPathFile2.exists() || !downloadPathFile2.isDirectory()) {
			downloadPathFile2.mkdirs();
		}
	}

	public void download(String folder, String sourceUrlPrefix, String fileName) {
		URL url = null;
		try {
			url = new URL(sourceUrlPrefix + fileName);
			URLConnection conn = url.openConnection();
			InputStream inputStream = conn.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(new File(folder + fileName));
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (Exception e) {
			logger.error("Error in downloading file.", e);
		}
	}

	public void craw(List<String> fileNames) {
		List<String> fileNames1 = new ArrayList<String>();
		for (String fileName : fileNames) {
			File downloadPathFile = new File(downloadFolder1 + fileName);
			if (!downloadPathFile.exists()) {
				fileNames1.add(fileName);
			}
		}
		int size = fileNames1.size();
		int current = 1;
		for (String fileName : fileNames1) {
			if (fileName != null && !fileName.trim().equals("")) {
				craw1(fileName);
				logger.info("¡¾Type1 " + current++ + " / " + size + "¡¿ " + fileName + " download finished!");
			}
		}

		List<String> fileNames2 = new ArrayList<String>();
		for (String fileName : fileNames) {
			File downloadPathFile = new File(downloadFolder2 + fileName);
			if (!downloadPathFile.exists()) {
				fileNames2.add(fileName);
			}
		}
		size = fileNames2.size();
		current = 1;
		for (String fileName : fileNames2) {
			if (fileName != null && !fileName.trim().equals("")) {
				craw2(fileName);
				logger.info("¡¾Type2 " + current++ + " / " + size + "¡¿ " + fileName + " download finished!");
			}
		}
	}

	public void craw1(String fileName) {
		download(downloadFolder1, urlPrefix1, fileName);
	}

	public void craw2(String fileName) {
		download(downloadFolder2, urlPrefix2, fileName);
	}

	public static void main(String[] args) {
		Crawler crowler = new Crawler();
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IVideoDao dao = (IVideoDao) context.getBean("videoDao");
		List<String> fileNames = dao.findAllPics();
		logger.info("Downloading...");
		crowler.craw(fileNames);
		logger.info("All download finished!");
	}
}
