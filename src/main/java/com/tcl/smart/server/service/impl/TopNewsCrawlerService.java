package com.tcl.smart.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;

import com.tcl.smart.server.bean.HotBaiduNewsBean;
import com.tcl.smart.server.bean.HotBaiduNewsRelatedNewsArea;
import com.tcl.smart.server.crawler.custom.BaikeTagFilter;
import com.tcl.smart.server.service.ITopNewsCrawlerService;

/**
 * @author fanjie
 * @date 2013-8-19
 */
public class TopNewsCrawlerService implements ITopNewsCrawlerService {

	@Override
	public List<HotBaiduNewsBean> fetchRealtimeHotBaiduNews() {
		Parser parser;
		try {
			parser = new Parser("http://top.baidu.com/buzz?b=1");
			parser.setEncoding("GBK");
			TagNameFilter filter = new TagNameFilter("tr");
			NodeList trs = parser.parse(filter);
			List<TableRow> rows = new ArrayList<TableRow>();
			for (int i = 0; i < trs.size(); i++) {
				TableRow tr = (TableRow) trs.elementAt(i);
				NodeList tds = tr.getChildren();
				NodeList validTds = tds.extractAllNodesThatMatch(new BaikeTagFilter("td", null, "first"));
				if (validTds != null && validTds.size() > 0) {
					rows.add(tr);
					parseHotNewsMoreInfo(parseHotNewsBasicInfo(tr));
					Thread.sleep(1000);
				}
			}
			// parseHotNewsMoreInfo(parseHotNewsBasicInfo(rows.get(2)));
			return null;
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}

	private HotBaiduNewsBean parseHotNewsBasicInfo(TableRow row) {
		try {
			HotBaiduNewsBean bean = new HotBaiduNewsBean();

			NodeList tds = row.getChildren();
			NodeList node_index = tds.extractAllNodesThatMatch(new BaikeTagFilter("td", null, "first"));
			TableColumn td_index = (TableColumn) node_index.elements().nextNode();
			NodeList td_index_cld = td_index.getChildren();
			NodeList node_index_span = td_index_cld.extractAllNodesThatMatch(new BaikeTagFilter("span", null, null));
			Span span_index = (Span) node_index_span.elements().nextNode();

			bean.setIndex(Long.parseLong(span_index.getStringText()));

			NodeList node_keyword = tds.extractAllNodesThatMatch(new BaikeTagFilter("td", null, "keyword"));
			TableColumn td_keyword = (TableColumn) node_keyword.elements().nextNode();
			NodeList td_keyword_cld = td_keyword.getChildren();
			NodeList node_keyword_link = td_keyword_cld.extractAllNodesThatMatch(new BaikeTagFilter("a", null, "list-title"));
			LinkTag link_keyword = (LinkTag) node_keyword_link.elements().nextNode();
			NodeList node_keyword_newicon = td_keyword_cld.extractAllNodesThatMatch(new BaikeTagFilter("span", null, "icon icon-new"));
			if (node_keyword_newicon != null && node_keyword_newicon.size() > 0) {
				bean.setNew(true);
			} else {
				bean.setNew(false);
			}
			bean.setKeyword(link_keyword.getStringText());
			bean.setKeywordUrl("http://top.baidu.com/" + link_keyword.getAttribute("href").substring(2));

			NodeList node_heat = tds.extractAllNodesThatMatch(new BaikeTagFilter("td", null, "last"));
			TableColumn td_heat = (TableColumn) node_heat.elements().nextNode();
			NodeList td_heat_cld = td_heat.getChildren();
			NodeList node_heat_span = td_heat_cld.extractAllNodesThatMatch(new BaikeTagFilter("span", null, null));
			Span span_heat = (Span) node_heat_span.elements().nextNode();

			bean.setHeat(Long.parseLong(span_heat.getStringText()));
			if ("icon-rise".equals(span_heat.getAttribute("class"))) {
				bean.setHeatUp(true);
			} else {
				bean.setHeatUp(false);
			}

			bean.setFetchTime(new Date());

			System.out.println();
			System.out.println("*****Base Information*****");
			System.out.println("Index: " + bean.getIndex());
			System.out.println("Key: " + bean.getKeyword());
			System.out.println("Key url: " + bean.getKeywordUrl());
			System.out.println("Heat: " + bean.getHeat());
			System.out.println("Is heat up: " + bean.isHeatUp());
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void parseHotNewsMoreInfo(HotBaiduNewsBean bean) {
		
		try {
			Parser parser = new Parser(bean.getKeywordUrl());
			parser.setEncoding("GBK");
			NodeList allnode = parser.parse(null);
			NodeList node_newsbox = allnode.extractAllNodesThatMatch(new BaikeTagFilter("div", null, null), true);

			/** 相关新闻区域的匹配结果 */
			NodeList node_relatednews = node_newsbox.extractAllNodesThatMatch(new BaikeTagFilter("div", null, "related-news-box"));
			if (node_relatednews != null && node_relatednews.size() > 0) {
				HotBaiduNewsRelatedNewsArea relatedNewsArea = new HotBaiduNewsRelatedNewsArea();
				NodeList node_img = node_relatednews.extractAllNodesThatMatch(new BaikeTagFilter("img", null, null), true);
				String imgUrl = null;
				if (node_img != null && node_img.size() > 0) {
					ImageTag image = (ImageTag) node_img.elements().nextNode();
					imgUrl = image.getAttribute("src");
					relatedNewsArea.setNewsImgUrl(imgUrl);
				}

				NodeList node_title = node_relatednews.extractAllNodesThatMatch(new BaikeTagFilter(null, null, "title"), true);
				NodeList node_title_link = node_title.extractAllNodesThatMatch(new BaikeTagFilter("a", null, null), true);
				LinkTag link_title = (LinkTag) node_title_link.elements().nextNode();
				String href = link_title.getAttribute("href");
				String title = link_title.getStringText();
				relatedNewsArea.setHref(href);
				relatedNewsArea.setTitle(title);

				NodeList node_source = node_relatednews.extractAllNodesThatMatch(new BaikeTagFilter("span", null, "web"), true);
				Span span_source = (Span) node_source.elements().nextNode();
				String source = span_source.getStringText();
				relatedNewsArea.setSource(source);

				NodeList node_date = node_relatednews.extractAllNodesThatMatch(new BaikeTagFilter("span", null, "date"), true);
				Span span_date = (Span) node_date.elements().nextNode();
				String date = span_date.getStringText();
				relatedNewsArea.setDate(date);

				NodeList node_summary = node_relatednews.extractAllNodesThatMatch(new BaikeTagFilter("p", null, "text"), true);
				ParagraphTag p_summary = (ParagraphTag) node_summary.elements().nextNode();
				String summary = p_summary.getStringText();
				relatedNewsArea.setSummary(summary);

				bean.setRelatedNewsArea(relatedNewsArea);

				System.out.println("*****More Information*****");
				System.out.println("Image url: " + bean.getRelatedNewsArea().getNewsImgUrl());
				System.out.println("News href: " + bean.getRelatedNewsArea().getHref());
				System.out.println("Title: " + bean.getRelatedNewsArea().getTitle());
				System.out.println("Source: " + bean.getRelatedNewsArea().getSource());
				System.out.println("Date: " + bean.getRelatedNewsArea().getDate());
				System.out.println("Summary: " + bean.getRelatedNewsArea().getSummary());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TopNewsCrawlerService t = new TopNewsCrawlerService();
		t.fetchRealtimeHotBaiduNews();
	}
}
