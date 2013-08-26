package com.tcl.smart.server.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcl.smart.server.bean.DistinctChannel;
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.GetFlexigridJson;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.bean.SpecifiedKeyword;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.ISpecifiedKeywordDao;
import com.tcl.smart.server.dao.impl.NewsSearchRstItemDao;
import com.tcl.smart.server.service.IBaikeSearchService;
import com.tcl.smart.server.service.impl.BlockException;
import com.tcl.smart.server.service.impl.NewsRssSearchService;
import com.tcl.smart.server.util.Constants;
import com.tcl.smart.server.util.DateJsonValueProcessor;

/**
 * @author fanjie
 * @date 2013-7-9
 */
@Controller
@RequestMapping("/clive/*")
public class OperationController {

	private JsonConfig cfg = new JsonConfig();

	@Qualifier("epgModelDao")
	@Autowired
	private IEpgModelDao epgModelDao;

	@Qualifier("newsRssSearchService")
	@Autowired
	private NewsRssSearchService newsRssService;

	@Qualifier("newsSearchRstItemDao")
	@Autowired
	private NewsSearchRstItemDao newsSearchRstItemDao;

	@Qualifier("specifiedKeywordDao")
	@Autowired
	private ISpecifiedKeywordDao specifiedKeywordDao;

	@Qualifier("baikeSearchServiceForDailyCrawler")
	@Autowired
	private IBaikeSearchService baikeSearchService;

	@RequestMapping(value = "operation", method = RequestMethod.GET)
	public String operation() {
		return "clive/operation";
	}

	public OperationController() {
		cfg.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor());
	}

	@RequestMapping(value = "channel-list", method = RequestMethod.GET)
	public @ResponseBody
	String channelListHTML() {
		List<DistinctChannel> channels = epgModelDao.group2Channels();
		if (channels == null || channels.size() == 0)
			return "失败：暂无信息！";
		StringBuffer sb = new StringBuffer();
		for (DistinctChannel channel : channels) {
			sb.append("<li class=ui-widget-content\" channelCode=" + channel.getChannel_code() + ">" + channel.getName() + "</li>");
		}
		return sb.toString();
	}

	@RequestMapping(value = "program-list/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	String programListHTML(@PathVariable String channel_code) {
		List<EpgModel> epgs = epgModelDao.findEpgModelsByChannelCode(channel_code);
		if (epgs == null || epgs.size() == 0)
			return "失败：暂无信息！";
		StringBuffer sb = new StringBuffer();
		for (EpgModel epg : epgs) {
			sb.append("<li class=\"ui-widget-content\" epgId=" + epg.getId().toString() + ">" + Constants.parseTime2(epg.getStartTime()) + " -- "
					+ Constants.parseTime2(epg.getEndTime()) + ": " + epg.getName() + "</li>");
		}
		return sb.toString();
	}

	@RequestMapping(value = "list-keyword", method = RequestMethod.POST)
	public @ResponseBody
	String listKeyword() {
		List<SpecifiedKeyword> specifiedKeywords = specifiedKeywordDao.findAllSpecifiedKeywords();
		if (specifiedKeywords == null || specifiedKeywords.size() == 0)
			return GetFlexigridJson.getJson(0, new ArrayList<SpecifiedKeyword>());
		return GetFlexigridJson.getJson(1, specifiedKeywords);
	}

	@RequestMapping(value = "query-keyword/{epgId}", method = RequestMethod.GET)
	public @ResponseBody
	String queryKeyword(@PathVariable String epgId) {
		SpecifiedKeyword specifiedKeyword = specifiedKeywordDao.findSpecifiedKeywordByEpgId(epgId);
		if (specifiedKeyword == null)
			return "";
		return specifiedKeyword.getKeyword();
	}

	@RequestMapping(value = "save-keyword/{epgId}", method = RequestMethod.GET)
	public @ResponseBody
	String saveKeyword(@PathVariable String epgId, @RequestParam(value = "keyword", required = false) String keyword) {
		if (epgId == null || epgId.trim().equals("") || keyword == null || keyword.trim().equals(""))
			return null;
		try {
			String decodeKeyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
			SpecifiedKeyword specifiedKeyword = new SpecifiedKeyword();
			specifiedKeyword.setEpgId(epgId);
			specifiedKeyword.setKeyword(decodeKeyword);
			EpgModel epg = epgModelDao.findEpgModelById(epgId);
			if (epg != null) {
				specifiedKeyword.setChannelCode(epg.getChannelCode());
				specifiedKeyword.setChannelId(epg.getChannelId());
				specifiedKeyword.setChannelLogo(epg.getChannelLogo());
				specifiedKeyword.setChannelName(epg.getChannelName());
				specifiedKeyword.setChannelType(epg.getChannelType());
				specifiedKeyword.setEndTime(epg.getEndTime());
				specifiedKeyword.setStartTime(epg.getStartTime());
				specifiedKeyword.setProgramName(epg.getName());
				specifiedKeyword.setWikiId(epg.getWikiId());
				specifiedKeyword.setWikiCover(epg.getWikiCover());
			}
			specifiedKeywordDao.insertSpecifiedKeyword(specifiedKeyword);

			// 更新新闻
			List<NewsSearchRstItem> news = newsRssService.searchNews(decodeKeyword);
			if (news == null) {
				return "none";
			}
			for (NewsSearchRstItem newsItem : news) {
				newsSearchRstItemDao.saveNewsSearchRstItemReplaceSameNews(newsItem);
			}

			// 更新百科
			try {
				baikeSearchService.search(decodeKeyword);
			} catch (BlockException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return "ok";
	}

	@RequestMapping(value = "remove-keyword/{epgId}", method = RequestMethod.GET)
	public @ResponseBody
	String removeKeyword(@PathVariable String epgId) {
		if (epgId == null || epgId.trim().equals(""))
			return null;
		specifiedKeywordDao.removeSpecifiedKeyword(epgId);
		return "ok";
	}
}
