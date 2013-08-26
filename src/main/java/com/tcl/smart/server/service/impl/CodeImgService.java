package com.tcl.smart.server.service.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.smart.server.service.ICodeImgService;
import com.tcl.smart.server.util.Constants;

/**
 * 二维码自动生成服务
 * 
 * @author fanjie
 * @date 2013-5-26
 */
public class CodeImgService implements ICodeImgService {
	private static final Logger logger = LoggerFactory.getLogger(CodeImgService.class);

	@Override
	public String generateCodeImgByDefaultSetting(String data) {
		return generateCodeImg(120, 120, data, null, "H");
	}

	@Override
	public String generateCodeImg(int width, int height, String data, String encoding, String error_level) {
		String filePath = null;
		String dataStr;
		try {
			dataStr = URLEncoder.encode(data, "gbk");
			String url = "https://chart.googleapis.com/chart?cht=qr&chld=" + error_level + "&chs=" + width + "x" + height + "&chl=" + dataStr;
			if (encoding != null)
				url += "&choe=" + encoding;
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			GetMethod getMethod = new GetMethod(url);
			getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			try {
				int statusCode = httpClient.executeMethod(getMethod);
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("Method failed: " + getMethod.getStatusLine());
					return null;
				}
				byte[] responseBody = getMethod.getResponseBody();
				String fileName = data.hashCode() + ".png";
				try {
					filePath = System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator + Constants.getProperties().getCodeImgFolder()
							+ fileName;
				} catch (Exception e) {
					filePath = System.getProperty("user.dir") + File.separator + fileName;
				}
				saveToLocal(responseBody, filePath);
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

	public static void main(String[] args) {
		CodeImgService s = new CodeImgService();
		s.generateCodeImgByDefaultSetting("http://news.ifeng.com/mil/video/detail_2013_04/01/23737681_0.shtml");
	}
}
