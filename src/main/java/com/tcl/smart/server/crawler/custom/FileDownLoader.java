package com.tcl.smart.server.crawler.custom;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.tcl.smart.server.bean.BaikeUrl;
import com.tcl.smart.server.dao.IBaikeUrlDao;

/**
 * @author fanjie
 * @date 2013-4-26
 */
public class FileDownLoader {
	private static final String URL_PREFIX = "baike.baidu.com";

	/**
	 * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
	 * 
	 * @param url
	 * @param contentType
	 * @return
	 */
	public String getFileNameByUrl(String url, String contentType) {
		if (contentType.indexOf("html") != -1 && url.endsWith(".htm")) {
			url = url.substring(url.lastIndexOf("/") + 1, url.length());
			return url;
		}
		return null;
	}

	/**
	 * 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
	 * 
	 * @param data
	 * @param filePath
	 */
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

	/**
	 * 下载 url 指向的网页
	 * 
	 * @param url
	 * @return
	 */
	public String downloadFile(String url) {
		String filePath = null;
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
				filePath = null;
				return filePath;
			}
			byte[] responseBody = getMethod.getResponseBody();
			filePath = "E://baike/" + getFileNameByUrl(url, getMethod.getResponseHeader("Content-Type").getValue());
			saveToLocal(responseBody, filePath);
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return filePath;
	}

	/**
	 * 下载百度域名下的图片
	 * 
	 * @param url
	 * @return
	 */
	public String downloadBaiduImg(String url, String filePath) {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
				filePath = null;
				return filePath;
			}
			byte[] responseBody = getMethod.getResponseBody();
			saveToLocal(responseBody, filePath);
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return filePath;
	}

	public String downloadBaiduBaikeFile(String key, IBaikeUrlDao baikeUrlDao, String savePath) {
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
					System.err.println("Method failed: " + getMethod.getStatusLine());
					filePath = null;
					return filePath;
				}
				byte[] responseBody = getMethod.getResponseBody();
				String fileName = getMethod.getPath().substring(getMethod.getPath().lastIndexOf("/") + 1, getMethod.getPath().length());
				filePath = savePath + fileName;
				saveToLocal(responseBody, filePath);
				if (baikeUrlDao != null) {
					BaikeUrl baikeUrl = new BaikeUrl();
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
			} catch (HttpException e) {
				System.out.println("Please check your provided http address!");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				getMethod.releaseConnection();
			}
			return filePath;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			filePath = null;
			return filePath;
		}
	}

	public static void main(String[] args) {
		FileDownLoader downLoader = new FileDownLoader();
		// downLoader.downloadBaiduBaikeFile("非诚勿扰", null, "E://baike/");
		downLoader.downloadBaiduImg("http://g.hiphotos.baidu.com/baike/s%3D220/sign=ae30282d31adcbef053479049cae2e0e/ca1349540923dd5428dcb681d109b3de9d8248af.jpg",
				"E://baidu-img/temp.jpg");
	}
}