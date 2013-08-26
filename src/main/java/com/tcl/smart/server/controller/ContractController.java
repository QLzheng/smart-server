package com.tcl.smart.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.tcl.smart.server.bean.BehaviourResult;
import com.tcl.smart.server.bean.ItemInfo;
import com.tcl.smart.server.bean.RecommendResults;
import com.tcl.smart.server.bean.VideoUrlInfo;
import com.tcl.smart.server.dao.IVideoDao;
import com.tcl.smart.server.util.Constants;

/**
 * @author fanjie
 * @date 2013-3-5
 */
@Controller
@RequestMapping("/contract/*")
public class ContractController {

	private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

	@Qualifier("restTemplate")
	@Autowired
	private RestTemplate restTemplate;

	@Qualifier("videoDao")
	@Autowired
	private IVideoDao videoDao;

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam(value = "userId", required = false) int userId, @RequestParam(value = "size", required = false) int size,
			@RequestParam(value = "web", required = false) boolean web, Model model) {
		model.addAttribute("userId", userId);
		model.addAttribute("size", size);
		model.addAttribute("web", web);
		RecommendResults recommendRst = recommend(userId, size);
		model.addAttribute("recommend", recommendRst);
		return Constants.getProperties().getViewPage();
	}

	@RequestMapping(value = "behaviour", method = RequestMethod.GET)
	public @ResponseBody
	VideoUrlInfo behaviour(@RequestParam(value = "userId", required = false) int userId, @RequestParam(value = "itemId", required = false) String itemId) {
		String url = Constants.constructBehaviourUrl(userId, itemId);
		try {
			restTemplate.getForObject(url, BehaviourResult.class);
		} catch (Exception e) {
			logger.error("Error to add behaviour: " + e.getMessage());
		}
		VideoUrlInfo videoUrlInfo = videoDao.findVideoUrlInfoById(itemId);
		if (videoUrlInfo == null) {
			videoUrlInfo = new VideoUrlInfo();
			videoUrlInfo.set_id(itemId);
			videoUrlInfo.setUrl(Constants.getProperties().getDefaultVideoUrl());
		} else if (videoUrlInfo.getUrl() == null || (videoUrlInfo.getSource() != null && videoUrlInfo.getSource().equals("notsupport")))
			videoUrlInfo.setUrl(Constants.getProperties().getDefaultVideoUrl());
		return videoUrlInfo;
	}

	@RequestMapping(value = "recommend", method = RequestMethod.GET)
	public @ResponseBody
	RecommendResults recommend(@RequestParam(value = "userId", required = false) int userId, @RequestParam(value = "size", required = false) int size) {
		String url = Constants.constructRecommendUrl(userId, size);
		try {
			RecommendResults result = restTemplate.getForObject(url, RecommendResults.class);
			return result;
		} catch (Exception e) {
			logger.error("Error to get recommend results: " + e.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "queryItem", method = RequestMethod.GET)
	public @ResponseBody
	ItemInfo queryItem(@RequestParam(value = "itemId", required = false) String itemId) {
		String url = Constants.constructQueryItemUrl(itemId);
		try {
			ItemInfo result = restTemplate.getForObject(url, ItemInfo.class);
			if (result != null)
				result.setSource(videoDao.findVideoUrlInfoById(itemId).getSource());
			return result;
		} catch (Exception e) {
			logger.error("Error to get recommend results: " + e.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "reset-all", method = RequestMethod.GET)
	public @ResponseBody
	boolean resetAll() {
		String url = Constants.constructResetAllUrl();
		try {
			String result = restTemplate.getForObject(url, String.class);
			logger.info("Reset all user: " + result);
			return true;
		} catch (Exception e) {
			logger.error("Error to reset all: " + e.getMessage());
			return false;
		}
	}

	@RequestMapping(value = "reset", method = RequestMethod.GET)
	public @ResponseBody
	boolean reset(@RequestParam(value = "userId", required = false) int userId) {
		String url = Constants.constructResetUrl(userId);
		try {
			String result = restTemplate.getForObject(url, String.class);
			logger.info("Reset user " + userId + ": " + result);
			return true;
		} catch (Exception e) {
			logger.error("Error to reset " + userId + ": " + e.getMessage());
			return false;
		}
	}
}