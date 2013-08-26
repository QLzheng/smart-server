package com.tcl.smart.server.service.impl;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.smart.server.service.IImageSearchService;
import com.tcl.smart.server.util.CustomPostMethod;
import com.tcl.smart.server.util.HttpClientUtil;

/**
 * @author fanjie
 * @date 2013-4-2
 */
public class ImageSearchService implements IImageSearchService {
	private static final Logger logger = LoggerFactory.getLogger(ImageSearchService.class);

	@Override
	public void search(String absPicPath) {
		File image = new File(absPicPath);
		if (!image.exists()) {
			return;
		}
		CustomPostMethod postMethod = new CustomPostMethod("http://stu.baidu.com/i?rt=0&rn=10&ct=0&stt=0&tn=baiduimagepc&fr=flash");
		try {
			FilePart fp = new FilePart("filedata", image);
			Part[] parts = { fp };

			MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mre);
			postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);// 设置连接时间
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				logger.info(postMethod.getResponseBodyAsString());
				String resp = HttpClientUtil.getHttp(postMethod.getResponseBodyAsString());
				logger.info(resp);
			} else {
				logger.error("Fail in searching image from baidu.");
			}
		} catch (Exception e) {
			logger.error("Error when searching image from baidu.", e);
		} finally {
			postMethod.releaseConnection();
		}
	}
}
