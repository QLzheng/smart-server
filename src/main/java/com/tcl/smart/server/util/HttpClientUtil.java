package com.tcl.smart.server.util;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 以get/post的方式发送数据到指定的http接口
 * 
 * @author fanjie
 * @date 2012-11-21
 */
public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * get方式
	 * 
	 * @param url
	 * @return
	 */
	public static String getHttp(String url) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				try {
					byte[] responseBody = getMethod.getResponseBody();
					responseMsg = new String(responseBody);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("HTTP: 请求返回状态：" + statusCode);
			}
			return responseMsg;
		} catch (Exception e) {
			logger.error("Error in get method.", e);
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}

	/**
	 * post方式
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static String postHttp(String url, NameValuePair[] parameters) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("utf-8");
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("ContentType", "application/json;utf-8");
		try {
			postMethod.addParameters(parameters);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				try {
					responseMsg = postMethod.getResponseBodyAsString();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("请求返回状态：" + statusCode);
			}
			return responseMsg;
		} catch (Exception e) {
			logger.error("Error in post method.", e);
		} finally {
			postMethod.releaseConnection();
		}
		return null;
	}

}