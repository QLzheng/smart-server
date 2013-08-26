package com.tcl.smart.server.crawler.custom;

import java.util.Set;

/**
 * @author fanjie
 * @date 2013-4-26
 */
public class Crawler {
	private void initCrawlerWithSeeds(String[] seeds) {
		for (int i = 0; i < seeds.length; i++)
			LinkDB.addUnvisitedUrl(seeds[i]);
	}

	public void crawling(String[] seeds) {
		LinkFilter filter = new LinkFilter() {
			public boolean accept(String url) {
				if (url.startsWith("http://baike.baidu.com"))
					return true;
				else
					return false;
			}
		};
		initCrawlerWithSeeds(seeds);
		while (!LinkDB.unVisitedUrlsEmpty() && LinkDB.getVisitedUrlNum() <= 1000) {
			String visitUrl = LinkDB.unVisitedUrlDeQueue();
			if (visitUrl == null)
				continue;
			FileDownLoader downLoader = new FileDownLoader();
			downLoader.downloadFile(visitUrl);
			LinkDB.addVisitedUrl(visitUrl);

			Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
			for (String link : links) {
				LinkDB.addUnvisitedUrl(link);
			}
		}
	}

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.crawling(new String[] { "http://baike.baidu.com" });
	}
}