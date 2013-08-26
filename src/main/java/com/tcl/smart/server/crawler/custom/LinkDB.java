package com.tcl.smart.server.crawler.custom;

import java.util.HashSet;
import java.util.Set;

/**
 * ���������Ѿ����ʹ� Url �ʹ����ʵ� Url ����
 * 
 * @author fanjie
 * @date 2013-4-26
 */
public class LinkDB {

	private static Set<String> visitedUrl = new HashSet<String>();
	private static Queue<String> unVisitedUrl = new Queue<String>();

	public static Queue<String> getUnVisitedUrl() {
		return unVisitedUrl;
	}

	public static void addVisitedUrl(String url) {
		visitedUrl.add(url);
	}

	public static void removeVisitedUrl(String url) {
		visitedUrl.remove(url);
	}

	public static String unVisitedUrlDeQueue() {
		return unVisitedUrl.deQueue();
	}

	/**
	 * ��֤ÿ�� url ֻ������һ��
	 * 
	 * @param url
	 */
	public static void addUnvisitedUrl(String url) {
		if (url != null && !url.trim().equals("") && !visitedUrl.contains(url) && !unVisitedUrl.contians(url))
			unVisitedUrl.enQueue(url);
	}

	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}

	public static boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.empty();
	}
}