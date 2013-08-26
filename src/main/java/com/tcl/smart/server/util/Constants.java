package com.tcl.smart.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
	private static PropertiesBean properties;

	public static String constructRecommendUrl(int userId, int size) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(properties.getRecommendIp()).append(":").append(properties.getRecommendPort()).append("/").append(properties.getRecommendProj()).append("/");
		sb.append(properties.getRecommendRecommend()).append("?userId=").append(userId).append("&size=").append(size);
		return sb.toString();
	}

	public static String constructBehaviourUrl(int userId, String itemId) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(properties.getRecommendIp()).append(":").append(properties.getRecommendPort()).append("/").append(properties.getRecommendProj()).append("/");
		sb.append(properties.getRecommendBehavior()).append("?userId=").append(userId).append("&itemId=").append(itemId);
		return sb.toString();
	}

	public static String constructQueryItemUrl(String itemId) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(properties.getRecommendIp()).append(":").append(properties.getRecommendPort()).append("/").append(properties.getRecommendProj()).append("/");
		sb.append(properties.getRecommendQueryItem()).append("?itemId=").append(itemId);
		return sb.toString();
	}

	public static String constructResetAllUrl() {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(properties.getRecommendIp()).append(":").append(properties.getRecommendPort()).append("/").append(properties.getRecommendProj()).append("/");
		sb.append(properties.getRecommendResetall());
		return sb.toString();
	}

	public static String constructResetUrl(int userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(properties.getRecommendIp()).append(":").append(properties.getRecommendPort()).append("/").append(properties.getRecommendProj()).append("/");
		sb.append(properties.getRecommendReset()).append("?userId=").append(userId);
		return sb.toString();
	}

	public static String constructCliveRecommendUrl(String cid, int count) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(properties.getRecommendCliveIp()).append(":").append(properties.getRecommendClivePort()).append("/").append(properties.getRecommendCliveProj()).append("/");
		sb.append(properties.getRecommendCliveRecommend()).append("?count=").append(count).append("&cid=").append(cid);
		return sb.toString();
	}

	public static String constructCliveTrainOnceUrl() {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(properties.getRecommendCliveIp()).append(":").append(properties.getRecommendClivePort()).append("/").append(properties.getRecommendCliveProj()).append("/");
		sb.append(properties.getRecommendCliveTrainOnce());
		return sb.toString();
	}

	public static PropertiesBean getProperties() {
		return properties;
	}

	public static void setProperties(PropertiesBean properties) {
		Constants.properties = properties;
	}

	public static String getCurrentTime() {
		Date now = new Date(System.currentTimeMillis());
		return parseTime(now);
	}

	public static String parseTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public static String parseTime2(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public static String parseTime3(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public static String parseTimeDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		return dateStr;
	}
}
