package com.tcl.smart.server.service.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.BaikeUrl;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.crawler.custom.BaiduBaikeParserTool;
import com.tcl.smart.server.crawler.custom.FileDownLoader;
import com.tcl.smart.server.dao.IBaikeBeanDao;
import com.tcl.smart.server.dao.IBaikeUrlDao;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.service.IBaikeSearchService;
import com.tcl.smart.server.util.Constants;

/**
 * 根据节目搜索百科服务
 * 
 * @author fanjie
 * @date 2013-5-2
 */
public class BaikeSearchService implements IBaikeSearchService {
	private static final String URL_PREFIX = "baike.baidu.com";
	private static final Logger logger = LoggerFactory.getLogger(BaikeSearchService.class);

	@Qualifier("movieDao")
	@Autowired
	private IMovieDao movieDao;

	@Qualifier("baikeBeanDao")
	@Autowired
	private IBaikeBeanDao baikeDao;

	@Qualifier("baikeUrlDao")
	@Autowired
	private IBaikeUrlDao baikeUrlDao;

	private FileDownLoader downLoader = new FileDownLoader();

	private void saveToLocal(byte[] data, String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
			for (int i = 0; i < data.length; i++)
				out.write(data[i]);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String downloadBaikeHTM(String key) {
		String filePath = null;
		String constantStr = "http://baike.baidu.com/searchword/?pic=1&sug=1&enc=gbk&word=";
		String keyStr;
		try {
			keyStr = URLEncoder.encode(key, "gbk");
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			GetMethod getMethod = new GetMethod(constantStr + keyStr);
			getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			try {
				int statusCode = httpClient.executeMethod(getMethod);
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("Method failed: " + getMethod.getStatusLine());
					return null;
				}
				byte[] responseBody = getMethod.getResponseBody();
				String fileName = getMethod.getPath().substring(getMethod.getPath().lastIndexOf("/") + 1, getMethod.getPath().length());
				try {
					filePath = System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator + Constants.getProperties().getBaikeFolder()
							+ fileName;
				} catch (Exception e) {
					filePath = System.getProperty("user.dir") + File.separator + "fileName";
				}
				saveToLocal(responseBody, filePath);

				BaikeUrl baikeUrl = baikeUrlDao.findBaikeUrlByName(key);
				if (baikeUrl == null) {
					baikeUrl = new BaikeUrl();
					baikeUrl.setName(key);
					baikeUrl.setBaiduPath(URL_PREFIX + getMethod.getPath());
					if (getMethod.getPath().endsWith("none")) {
						baikeUrl.setResult("none");
					} else if (getMethod.getPath().endsWith("vcode")) {
						baikeUrl.setResult("blocked");
					} else {
						baikeUrl.setResult("ok");
					}
					baikeUrlDao.insertBaikeUrl(baikeUrl);
				}
				return filePath;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				getMethod.releaseConnection();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public synchronized List<BaikeBean> search(String key) throws BlockException {
		if (key == null || key.trim().equals(""))
			return null;

		// 本地库已有该百科数据则直接返回
		List<BaikeBean> baikes = baikeDao.findBaikeBeansByName(key.trim());
		if (baikes != null && baikes.size() > 0)
			return baikes;

		// 本地库已记录该关键字无词条
		BaikeUrl baikeUrl = baikeUrlDao.findBaikeUrlByName(key.trim());
		if (baikeUrl != null && "none".equals(baikeUrl.getResult())) {
			return null;
		}

		// 本地库没有则下载该词条HTM，然后解析并入库
		String htmPath = downloadBaikeHTM(key.trim());
		if (htmPath == null)
			return null;
		if (htmPath.endsWith("vcode")) {
			logger.error("Being blocked by baidu.");
			throw new BlockException();
		}
		File htm = new File(htmPath);
		baikes = BaiduBaikeParserTool.extractText(htm.getAbsolutePath());
		if (baikes != null) {
			for (BaikeBean baike : baikes) {
				baikeDao.insertBaikeBean(baike);
			}
		}
		return baikeDao.findBaikeBeansByName(key.trim());
	}

	@Override
	public synchronized List<BaikeBean> search(EpgModelDistinctId distinct) throws BlockException {
		List<BaikeBean> baikes = new ArrayList<BaikeBean>();
		List<String> keyWords = distinct.getKeys(movieDao);
		for (String keyword : keyWords) {
			List<BaikeBean> thisBaikes = search(keyword);
			if (thisBaikes != null && thisBaikes.size() > 0) {
				baikes.addAll(thisBaikes);
			}
		}
		return baikes;
	}

	@Override
	public String hotlinkingPictureForHashImgName(String url) {
		if (url == null || url.trim().equals(""))
			return null;
		String fileName = url.hashCode() + url.substring(url.lastIndexOf('.'));
		String filePath = System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator + Constants.getProperties().getHotlinkingImgFolder()
				+ fileName;
		downLoader.downloadBaiduImg(url, filePath);
		return fileName;
	}
}
