package com.tcl.smart.server.crawler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.VideoUrlInfo;
import com.tcl.smart.server.dao.IVideoDao;
import com.tcl.smart.server.util.FileOperation;
import com.tcl.smart.server.util.HttpClientUtil;

public class VideoUrlCrawler {

	private static final Logger logger = LoggerFactory.getLogger(VideoUrlCrawler.class);

	public static String strToURL(String str) {
		String result = null;
		try {
			result = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error in encoding.", e);
		}
		return result;
	}

	public void crawIncremental(List<VideoUrlInfo> videoInfos, IVideoDao dao) {
		List<VideoUrlInfo> newVideoInfos = new ArrayList<VideoUrlInfo>();
		for (VideoUrlInfo info : videoInfos) {
			if (dao.findVideoUrlInfoById(info.get_id()) == null) {
				newVideoInfos.add(info);
			}
		}
		logger.info(getCurrentTime() + " Start to craw, incremental size is " + newVideoInfos.size());
		crawVideoUrls(newVideoInfos, dao);
	}

	public void crawVideoUrls(List<VideoUrlInfo> videoInfos, IVideoDao dao) {
		long t1 = System.currentTimeMillis();
		int size = videoInfos.size();
		int current = 0;
		int index = 0;
		for (VideoUrlInfo videoInfo : videoInfos) {
			current++;
			index++;
			// VideoUrlInfo parsedVideoInfo = crawVideoUrlInFm(videoInfo);
			VideoUrlInfo parsedVideoInfo = crawVideoUrlInYouku(videoInfo);
			if (parsedVideoInfo != null && parsedVideoInfo.getUrl() != null) {
				dao.updateOrAddVideoUrlInfo(videoInfo);
				logger.info(getCurrentTime() + " 【" + current + " / " + size + "】 " + parsedVideoInfo + " crawlling finished!");
			} else {
				dao.updateOrAddVideoUrlInfo(videoInfo);
				logger.info(getCurrentTime() + " 【" + current + " / " + size + "】 " + videoInfo.getTitle() + " no result!");
			}
			if (index % 100 == 0) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					logger.error("Error in sleeping.", e);
				}
			}
		}
		logger.info("Elapse time :" + (System.currentTimeMillis() - t1) / 1000 / 60 + "m");
	}

	public String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		return date;
	}

	public VideoUrlInfo crawVideoUrlInFm(VideoUrlInfo videoInfo) {
		// String[] strs = new
		// String[]{"2012","盗梦空间","泰囧","失恋33天","让子弹飞","非诚勿扰","叶问","一代宗师"};
		// String[] strs = new String[]{"2012","失恋33天","让子弹飞"};
		String constantStr = "http://dianying.fm/search?key=";
		String urlPrefix = "http://dianying.fm";
		String valueStr = "";
		String fileModel = "film";

		String str = videoInfo.getTitle();
		String model = videoInfo.getModel();
		String keyStr;
		try {
			keyStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error in encoding.", e);
			return null;
		}
		valueStr = HttpClientUtil.getHttp(constantStr + keyStr);

		/* step 1: search */
		String infoUrl = "";
		String regEx = "<a target=\"_blank\" href=\"([^\"]+)\">([^<]+)</a>\\s+<span class=\"muted\">";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(valueStr);
		logger.info("Step 1:");
		if (mat.find()) {
			String infoHref = mat.group(1);
			infoUrl = urlPrefix + infoHref;
			logger.info(infoHref);
			logger.info(infoUrl);
			String name = mat.group(2);
			logger.info(name);
		}

		/* step 2: get the url of the movie */
		valueStr = HttpClientUtil.getHttp(infoUrl);

		String videoUrl = null;

		/* Parse movie's url address */
		if (model.compareToIgnoreCase(fileModel) == 0) {
			String regEx2 = "<a href=\"([^\"]+)\" target=\"_blank\" [title=\"([^\"]+)\"]|[class=\"btn btn-primary\"]><i class=\"icon-white icon-play\"></i> 立即播放</a>";
			Pattern pat2 = Pattern.compile(regEx2);
			Matcher mat2 = pat2.matcher(valueStr);
			logger.info("Step 2:");
			if (mat2.find()) {
				String videoHref = mat2.group(1);
				videoUrl = urlPrefix + videoHref;
				logger.info(videoHref);
				logger.info(videoUrl);
			}
		}
		/* parse teleplay's url address */
		else {
			String regEx3 = "<a target=\"_blank\"href=\"([^\"]+)\" class=\"btn btn-mini span1\" title=\"([^\"]+)\">第 1 集</a>";
			Pattern pat3 = Pattern.compile(regEx3);
			Matcher mat3 = pat3.matcher(valueStr);

			logger.info("Step 2-2:");
			if (mat3.find()) {
				String videoHref = mat3.group(1);
				videoUrl = urlPrefix + videoHref;
				logger.info(videoUrl);
			}
		}

		if (videoUrl == null) {
			return null;
		}
		videoInfo.setUrl(videoUrl);
		return videoInfo;
	}

	public VideoUrlInfo crawVideoUrlInYouku(VideoUrlInfo videoInfo) {
		String constantStr = "http://www.soku.com/search_video/q_";
		String valueStr = "";

		String str = videoInfo.getTitle();
		String keyStr;
		try {
			keyStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error in encoding.", e);
			return null;
		}
		try {
			valueStr = new String(HttpClientUtil.getHttp(constantStr + keyStr).getBytes(), "UTF-8");
		} catch (Exception e) {
			logger.error("Error in getting.", e);
			return null;
		}
		String videoUrl = getUrlOfFilm(valueStr);

		if (videoUrl == null) {
			videoUrl = getUrlOfTeleplay(valueStr);
			if (videoUrl == null) {
				return null;
			}
		}
		videoInfo.setUrl(videoUrl);
		return videoInfo;
	}

	public VideoUrlInfo crawVideoUrlInYoukuForTelplay(VideoUrlInfo videoInfo) {
		String constantStr = "http://www.soku.com/search_video/q_";
		String valueStr = "";

		String str = videoInfo.getTitle();
		String keyStr;
		try {
			keyStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error in encoding.", e);
			return null;
		}
		try {
			valueStr = new String(HttpClientUtil.getHttp(constantStr + keyStr).getBytes(), "UTF-8");
		} catch (Exception e) {
			logger.error("Error in getting.", e);
			return null;
		}
		String videoUrl = getUrlOfTeleplay(valueStr);
		if (videoUrl == null) {
			return null;
		}
		videoInfo.setUrl(videoUrl);
		videoInfo.setSource(null);
		return videoInfo;
	}

	public String getUrlOfFilm(String resultHtml) {
		String videoUrl = null;
		String regEx = "<div class=\"btnplay_s\"><a target=\"_blank\"  href=\"([^\"]+)\"";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(resultHtml);
		if (mat.find()) {
			videoUrl = mat.group(1);
		}
		return videoUrl;
	}

	public String getUrlOfTeleplay(String resultHtml) {
		String videoUrl = null;
		String regEx = "<li><a href='([^\"]+)' site='youku' _log_cate=\"\\d+\" _log_type='\\d+' _log_ct='\\d+' _log_pos=\\d+  _log_directpos='\\d+'  _log_sid='\\d+' target='_blank'>1</a></li>";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(resultHtml);
		if (mat.find()) {
			videoUrl = mat.group(1);
		}
		return videoUrl;
	}

	public VideoUrlInfo getNearestUrl(VideoUrlInfo videoInfo) {
		String constantStr = "http://www.soku.com/search_video/q_";
		String valueStr = "";

		String str = videoInfo.getTitle();
		String keyStr;
		try {
			keyStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error in encoding.", e);
			return null;
		}
		try {
			valueStr = new String(HttpClientUtil.getHttp(constantStr + keyStr).getBytes(), "UTF-8");
		} catch (Exception e) {
			logger.error("Error in getting.", e);
			return null;
		}

		String videoUrl = null;
		String regEx = "<li class=\"v_link\">\\s+<a title=\"[^\"]+\".+href=\"([^\"]+)\"";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(valueStr);
		if (mat.find()) {
			videoUrl = mat.group(1);
			videoInfo.setUrl(videoUrl);
			videoInfo.setSource("nearest");
		} else {
			videoInfo.setSource("notfound");
			return videoInfo;
		}
		return videoInfo;
	}

	public void handleYoukuIllegalUrl(VideoUrlInfo videoUrlInfo) {
		if (videoUrlInfo.getUrl() != null && videoUrlInfo.getUrl().contains("?playSite=")) {
			String regEx = "/search/redirect.html\\?playSite=([^&]+)&.+&url=([^&]+)&speed.+";
			Pattern pat = Pattern.compile(regEx);
			Matcher mat = pat.matcher(videoUrlInfo.getUrl());
			String source = null;
			String videoUrl = null;
			if (mat.find()) {
				source = mat.group(1);
				videoUrl = mat.group(2);
				if (source.equals("乐视")) {
					videoUrlInfo.setSource("letv");
					videoUrlInfo.setUrl(videoUrl);
				} else if (source.contains("电影")) {
					videoUrlInfo.setSource("m1905");
					videoUrlInfo.setUrl(videoUrl);
				} else {
					videoUrlInfo.setSource("notsupport");
					videoUrlInfo.setUrl(videoUrl);
				}
			}
		}
	}

	public void crawOriData() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IVideoDao dao = (IVideoDao) context.getBean("videoDao");
		List<VideoUrlInfo> videoInfos = dao.findAllVideoInfosByVideos();
		logger.info("Crawlling...");
		crawIncremental(videoInfos, dao);
		logger.info("All crawlling finished!");
	}

	public void fixIllegalUrls() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IVideoDao dao = (IVideoDao) context.getBean("videoDao");
		List<VideoUrlInfo> videoInfos = dao.findAllVideoInfos();
		List<VideoUrlInfo> illegalVideoInfos = new ArrayList<VideoUrlInfo>();
		for (VideoUrlInfo videoUrlInfo : videoInfos) {
			if (videoUrlInfo.getUrl() != null && videoUrlInfo.getUrl().contains("?playSite=") && videoUrlInfo.getSource() == null) {
				illegalVideoInfos.add(videoUrlInfo);
			}
		}
		logger.info("Fix illegal url...");
		int size = illegalVideoInfos.size();
		int current = 0;
		for (VideoUrlInfo videoInfo : illegalVideoInfos) {
			current++;
			handleYoukuIllegalUrl(videoInfo);
			logger.info(getCurrentTime() + " 【" + current + " / " + size + "】 " + videoInfo.getTitle() + " (" + videoInfo.getSource() + ") :" + videoInfo.getUrl());
			dao.updateOrAddVideoUrlInfo(videoInfo);
		}
		logger.info("All illegal url fixed!");
	}

	public List<String> getNotSupportAndNotTeleplayTitles() {
		File file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator
				+ "notsupportAndNotteleplay.txt");
		List<String> titles = FileOperation.readFileByLinesForArray(file.getAbsolutePath());
		return titles;
	}

	public void tryUnsupportUrlsOfTeleplay() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IVideoDao dao = (IVideoDao) context.getBean("videoDao");
		List<VideoUrlInfo> videoInfos = dao.findAllVideoInfos();
		List<VideoUrlInfo> notSupportVideoInfos = new ArrayList<VideoUrlInfo>();
		File file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator
				+ "notsupportAndNotteleplay.txt");
		List<String> titles = getNotSupportAndNotTeleplayTitles();
		for (VideoUrlInfo videoUrlInfo : videoInfos) {
			if (videoUrlInfo.getSource() != null && videoUrlInfo.getSource().equals("notsupport")) {
				if (!titles.contains(videoUrlInfo.getTitle())) {
					notSupportVideoInfos.add(videoUrlInfo);
				}
			}
		}
		logger.info("Try not supported url...");
		int size = notSupportVideoInfos.size();
		int current = 0;
		for (VideoUrlInfo videoInfo : notSupportVideoInfos) {
			current++;
			if (crawVideoUrlInYoukuForTelplay(videoInfo) != null) {
				if (videoInfo.getSource() != null)
					logger.info(getCurrentTime() + " 【" + current + " / " + size + "】 " + videoInfo.getTitle() + " not support :" + videoInfo.getUrl());
				else
					logger.info(getCurrentTime() + " 【" + current + " / " + size + "】 " + videoInfo.getTitle() + " :" + videoInfo.getUrl());
				dao.updateOrAddVideoUrlInfo(videoInfo);
			} else {
				dao.updateOrAddVideoUrlInfo(videoInfo);
				logger.info(getCurrentTime() + " 【" + current + " / " + size + "】 " + videoInfo.getTitle() + " : cannot craw for teleplay.");
				FileOperation.appendStrInFile(file.getAbsolutePath(), videoInfo.getTitle());
			}
		}
		logger.info("All not supported url tried!");
	}

	public void crawNearestUrlForNull() {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IVideoDao dao = (IVideoDao) context.getBean("videoDao");
		List<VideoUrlInfo> videoInfos = dao.findAllVideoInfos();
		List<VideoUrlInfo> nullVideoInfos = new ArrayList<VideoUrlInfo>();
		for (VideoUrlInfo videoUrlInfo : videoInfos) {
			if ((videoUrlInfo.getUrl() == null && videoUrlInfo.getSource() == null) || (videoUrlInfo.getSource() != null && videoUrlInfo.getSource().equals("notsupport"))) {
				nullVideoInfos.add(videoUrlInfo);
			}
		}
		logger.info("Craw nearest url for null...");
		int size = nullVideoInfos.size();
		int current = 0;
		for (VideoUrlInfo videoInfo : nullVideoInfos) {
			current++;
			VideoUrlInfo nearestVideoInfo = getNearestUrl(videoInfo);
			if (nearestVideoInfo != null && nearestVideoInfo.getUrl() != null)
				logger.info(getCurrentTime() + " 【" + current + " / " + size + "】 【nearest】" + videoInfo.getTitle() + " :" + videoInfo.getUrl());
			else
				logger.info(getCurrentTime() + " 【" + current + " / " + size + "】  【nearest】" + videoInfo.getTitle() + " not found nearest.");
			dao.updateOrAddVideoUrlInfo(videoInfo);
		}
		logger.info("All nearest url crawed!");
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// ApplicationContext context = new
		// FileSystemXmlApplicationContext("classpath:spring-config.xml");
		// IVideoDao dao = (IVideoDao) context.getBean("videoDao");
		// List<VideoUrlInfo> videoInfos = dao.findAllVideoInfos();
		// List<VideoUrlInfo> youkuVideoInfos = new ArrayList<VideoUrlInfo>();
		// for (VideoUrlInfo urlInfo : videoInfos) {
		// if (urlInfo.getSource() == null) {
		// youkuVideoInfos.add(urlInfo);
		// }
		// }
		// for (VideoUrlInfo urlInfo : youkuVideoInfos) {
		// urlInfo.setSource("youku");
		// dao.updateOrAddVideoUrlInfo(urlInfo);
		// }

		// VideoUrlCrawler crawler = new VideoUrlCrawler();
		// crawler.crawOriData();
		// crawler.fixIllegalUrls();
		// crawler.tryUnsupportUrlsOfTeleplay();
		// crawler.crawNearestUrlForNull();
	}
}
