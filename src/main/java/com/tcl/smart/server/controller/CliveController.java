package com.tcl.smart.server.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.BaseNewsItem;
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.FavorateProduct;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.NewsItem;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.bean.RecommendObject;
import com.tcl.smart.server.bean.RecommendResults;
import com.tcl.smart.server.bean.SpecifiedKeyword;
import com.tcl.smart.server.bean.VideoUrlInfo;
import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.bean.YihaodianSale_price;
import com.tcl.smart.server.bean.cmd.TvListRequestAck;
import com.tcl.smart.server.bean.cmd.TvWikInfoRequestAck;
import com.tcl.smart.server.dao.IBaikeBeanDao;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.IFavorateProductDao;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.INewsClassificationDao;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;
import com.tcl.smart.server.dao.IRecommendNewsDao;
import com.tcl.smart.server.dao.IRecommendProductDao;
import com.tcl.smart.server.dao.ISpecifiedKeywordDao;
import com.tcl.smart.server.dao.IVideoDao;
import com.tcl.smart.server.dao.IYihaodianProductDao;
import com.tcl.smart.server.dao.impl.NewsItemDao;
import com.tcl.smart.server.service.IBaikeRecommendService;
import com.tcl.smart.server.service.IBaikeSearchService;
import com.tcl.smart.server.service.ICodeImgService;
import com.tcl.smart.server.service.ICrawlerService;
import com.tcl.smart.server.service.IDailyCrawlerService;
import com.tcl.smart.server.service.IDemoDataService;
import com.tcl.smart.server.service.IHuanApiService;
import com.tcl.smart.server.service.impl.NewsDailyCrawlerByKeywordsService;
import com.tcl.smart.server.update.IUpdateEngine;
import com.tcl.smart.server.util.Constants;
import com.tcl.smart.server.util.HttpClientUtil;
import com.tcl.smart.server.util.KeyType;

/**
 * @author fanjie
 * @date 2013-4-9
 */
@Controller
@RequestMapping("/clive/*")
public class CliveController {

	private static final Logger logger = LoggerFactory.getLogger(CliveController.class);

	private static final int MAX_TITLE_LEN = 35;

	@Qualifier("restTemplate")
	@Autowired
	private RestTemplate restTemplate;

	@Qualifier("huanApiService")
	@Autowired
	private IHuanApiService huanApiService;

	@Qualifier("periodicUpdateEngine")
	@Autowired
	private IUpdateEngine periodUpdateEngine;

	@Qualifier("baikeSearchService")
	@Autowired
	private IBaikeSearchService baikeSearchService;

	@Qualifier("baikeDailyCrawlerService")
	@Autowired
	private IDailyCrawlerService baikeDailyCrawlerService;

	@Qualifier("newsDailyCrawlerByKeywordsService")
	@Autowired
	private NewsDailyCrawlerByKeywordsService newsDailyCrawlerByKeywordsService;

	@Qualifier("newsTimingCrawlerService")
	@Autowired
	private ICrawlerService crawlerService;

	@Qualifier("recommendNewsDao")
	@Autowired
	private IRecommendNewsDao recommendNewsDao;

	@Qualifier("baikeRecommendService")
	@Autowired
	private IBaikeRecommendService baikeRecommendService;

	@Qualifier("epgModelDao")
	@Autowired
	private IEpgModelDao epgModelDao;

	@Qualifier("baikeBeanDao")
	@Autowired
	private IBaikeBeanDao baikeDao;

	@Qualifier("movieDao")
	@Autowired
	private IMovieDao movieDao;

	@Qualifier("videoDao")
	@Autowired
	private IVideoDao videoDao;

	@Qualifier("newsItemDao")
	@Autowired
	private NewsItemDao newsItemDao;

	@Qualifier("newsSearchRstItemDao")
	@Autowired
	private INewsSearchRstItemDao newsSearchRstItemDao;

	@Qualifier("newsClassificationDao")
	@Autowired
	private INewsClassificationDao newsClassificationDao;

	@Qualifier("YihaodianDailyCrawlerService")
	@Autowired
	private IDailyCrawlerService yihaodianDailyCrawlerService;

	@Qualifier("codeImgService")
	@Autowired
	private ICodeImgService codeImgService;

	@Qualifier("demoDataService")
	@Autowired
	private IDemoDataService demoDataService;

	@Qualifier("rcommendProductDao")
	@Autowired
	private IRecommendProductDao productsRecommendDao;

	@Qualifier("yihaodianProductDao")
	@Autowired
	private IYihaodianProductDao productDao;

	@Qualifier("favorateProductDao")
	@Autowired
	private IFavorateProductDao favorateProductDao;

	@Qualifier("specifiedKeywordDao")
	@Autowired
	private ISpecifiedKeywordDao specifiedKeywordDao;

	@RequestMapping(value = "wiki/{wiki_id}", method = RequestMethod.GET)
	public @ResponseBody
	TvWikInfoRequestAck getWikiInfo(@PathVariable String wiki_id) {
		return huanApiService.getWikiInfo(wiki_id);
	}

	@RequestMapping(value = "tvlist", method = RequestMethod.GET)
	public @ResponseBody
	TvListRequestAck getTvList() {
		return huanApiService.getTodayChannelPrograms();
	}

	@RequestMapping(value = "today-update", method = RequestMethod.GET)
	public @ResponseBody
	String todayUpdate() {
		try {
			newsDailyCrawlerByKeywordsService.doOnce();
			return "Update successful.";
		} catch (Throwable t) {
			return "Update error, " + t.getMessage();
		}
	}

	@RequestMapping(value = "crawl-start", method = RequestMethod.GET)
	public @ResponseBody
	String crawlStart() {
		try {
			newsDailyCrawlerByKeywordsService.start();
			// crawlerService.start();
			yihaodianDailyCrawlerService.start();

			periodUpdateEngine.update();
			// baikeDailyCrawlerService.start();
			return "Start crawling task.";
		} catch (Throwable t) {
			return "Start crawling task: " + t.getMessage();
		}
	}

	@RequestMapping(value = "crawl-stop", method = RequestMethod.GET)
	public @ResponseBody
	String crawlStop() {
		try {
			newsDailyCrawlerByKeywordsService.stop();
			// crawlerService.stop();
			yihaodianDailyCrawlerService.stop();
			periodUpdateEngine.stop();
			// baikeDailyCrawlerService.stop();
			return "Stop crawling task.";
		} catch (Throwable t) {
			return "Stop crawling task: " + t.getMessage();
		}
	}

	@RequestMapping(value = "recommend-news/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	List<? extends BaseNewsItem> recommendNews(@PathVariable String channel_code, @RequestParam(value = "size", required = false) int size) {
		EpgModel model = epgModelDao.findCurrentEpgModelByChannelCode(channel_code);
		if (model == null) {
			logger.error("Cannot find any program playing now.");
			return null;
		}

		// 已定制关键字
		SpecifiedKeyword specifiedKeyword = specifiedKeywordDao.findSpecifiedKeywordByEpgId(model.getId().toString());
		if (specifiedKeyword != null) {
			List<NewsSearchRstItem> items = newsSearchRstItemDao.findNewsSearchRstItemsByKeyword(specifiedKeyword.getKeyword(), size);
			return items;
		}

		List<NewsSearchRstItem> news = recommendNewsDao.getRecommendResults(model.getId().toString(), size);
		List<BaseNewsItem> rtnNews = new ArrayList<BaseNewsItem>();
		if (news != null && news.size() > 0) {
			for (NewsSearchRstItem item : news)
				rtnNews.add(item);
		}
		return rtnNews;
	}

	@RequestMapping(value = "current-program-title/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	String currentProgramTitle(@PathVariable String channel_code) {
		// For local DEMO
		if (channel_code.startsWith("tcl_demo_")) {
			try {
				long timestamp = Long.parseLong(channel_code.substring(9));
				return demoDataService.currentProgramTitle(timestamp);
			} catch (Exception e) {
				return "错误的时间戳：" + channel_code;
			}
		}

		String programTitle = "未知节目(" + channel_code + ")";
		EpgModel epg = epgModelDao.findCurrentEpgModelByChannelCode(channel_code);
		if (epg != null) {
			programTitle = epg.getChannelName() + "：" + epg.getName();
		}
		if (programTitle.length() > MAX_TITLE_LEN) {
			programTitle = programTitle.substring(0, MAX_TITLE_LEN) + "..";
		}
		return programTitle;
	}

	@RequestMapping(value = "current-epg-html/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	String currentEpgHTML(@PathVariable String channel_code) {
		// For local DEMO
		if (channel_code.startsWith("tcl_demo_")) {
			try {
				long timestamp = Long.parseLong(channel_code.substring(9));
				return demoDataService.currentEpgHTML(timestamp);
			} catch (Exception e) {
				return "错误的时间戳：" + channel_code;
			}
		}

		EpgModel model = epgModelDao.findCurrentEpgModelByChannelCode(channel_code);
		if (model == null) {
			logger.error("Cannot find any program playing now.");
			return null;
		}
		MovieModel movie = movieDao.getMovieById(model.getWikiId());
		if (movie != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img id=\"" + model.getWikiId() + "\" src=\"" + model.getWikiCover() + "\" />");
			sb.append("<div class=\"ps_desc\"><h3>" + model.getNameEtc() + "</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img src=\"" + model.getChannelLogo() + "\" />");
			sb.append("<div class=\"ps_desc\"><h3>" + model.getNameEtc() + "</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
			return sb.toString();
		}
	}

	@RequestMapping(value = "current-epg-info-html/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	String currentEpgInformationHTML(@PathVariable String channel_code) {
		// For local DEMO
		if (channel_code.startsWith("tcl_demo_")) {
			try {
				long timestamp = Long.parseLong(channel_code.substring(9));
				return demoDataService.currentEpgInformationHTML(timestamp);
			} catch (Exception e) {
				return "错误的时间戳：" + channel_code;
			}
		}

		EpgModel model = epgModelDao.findCurrentEpgModelByChannelCode(channel_code);
		if (model == null) {
			logger.error("Cannot find any program playing now.");
			return "失败：无法获取当前节目！";
		}
		MovieModel movie = movieDao.getMovieById(model.getWikiId());
		if (movie != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>" + model.getName() + "</font></b></h2>");
			if (movie.getUpdated_at() != null)
				sb.append("<h2>更新时间：" + Constants.parseTime(movie.getUpdated_at()) + "</h2>");
			if (movie.getHost() != null && movie.getHost().size() > 0)
				sb.append("<h2>主持人：" + movie.getHost() + "</h2>");
			if (movie.getGuest() != null && movie.getGuest().size() > 0)
				sb.append("<h2>嘉宾：" + movie.getGuest() + "</h2>");
			if (movie.getDirector() != null && movie.getDirector().size() > 0)
				sb.append("<h2>导演：" + movie.getDirector() + "</h2>");
			if (movie.getStarring() != null && movie.getStarring().size() > 0)
				sb.append("<h2>演员：" + movie.getStarring() + "</h2>");
			sb.append("<h2>播出频道：" + model.getChannelName() + "</h2>");
			sb.append("<h2>播出时间：" + Constants.parseTime2(model.getStartTime()) + " -- " + Constants.parseTime2(model.getEndTime()) + "</h2>");
			if (movie.getTags() != null && movie.getTags().size() > 0)
				sb.append("<h2>节目类型：" + movie.getTags() + "</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;" + movie.getContent() + "</h2>");
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>" + model.getName() + "</font></b></h2>");
			sb.append("<h2>播出频道：" + model.getChannelName() + "</h2>");
			sb.append("<h2>播出时间：" + Constants.parseTime2(model.getStartTime()) + " -- " + Constants.parseTime2(model.getEndTime()) + "</h2>");
			return sb.toString();
		}
	}

	@RequestMapping(value = "recommend-baikes-html/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	String recommendBaikesHTML(@PathVariable String channel_code, @RequestParam(value = "size", required = false) int size) {
		// For local DEMO
		if (channel_code.startsWith("tcl_demo_")) {
			try {
				long timestamp = Long.parseLong(channel_code.substring(9));
				return demoDataService.generateDemoBaikesHTML(timestamp);
			} catch (Exception e) {
				return "错误的时间戳：" + channel_code;
			}
		}

		EpgModel model = epgModelDao.findCurrentEpgModelByChannelCode(channel_code);
		if (model == null) {
			logger.error("Cannot find any program playing now.");
			return "失败：无法获取当前节目！";
		}
		try {
			List<BaikeBean> baikes = null;
			// 已定制关键字
			SpecifiedKeyword specifiedKeyword = specifiedKeywordDao.findSpecifiedKeywordByEpgId(model.getId().toString());
			if (specifiedKeyword != null) {
				baikes = baikeSearchService.search(specifiedKeyword.getKeyword());
			} else {
				baikes = baikeRecommendService.getRecommenderWiki(model.getId().toString(), size);
			}
			if (baikes == null)
				return "失败：无法获取百科推荐！";
			else if (baikes.size() == 0)
				return "<span style='position:absolute;text-align:center;line-height:177px;width:325px;'><h2>无百科推荐！</h2></span>";
			StringBuffer sb = new StringBuffer();
			for (BaikeBean baike : baikes) {
				String title = baike.getNameEtc();
				String imgUrl = baike.getCardImgUrl();
				if (imgUrl != null && !imgUrl.trim().equals("")) {
					String fileName = imgUrl.hashCode() + imgUrl.substring(imgUrl.lastIndexOf('.'));
					File pic = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator
							+ Constants.getProperties().getHotlinkingImgFolder() + fileName);
					if (!pic.exists() || !pic.isFile()) {
						baikeSearchService.hotlinkingPictureForHashImgName(imgUrl);
					}
					imgUrl = Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getHotlinkingImgFolder() + fileName;
				} else {
					imgUrl = "../../clive/images/wiki.jpg";
				}
				sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
				String source = baike.getSource();
				if (!source.trim().equals(""))
					sb.append("<div class=\"source-tag\"><h3>" + source + "</h3></div>");
				sb.append("<img id=\"" + baike.getId() + "\" src=" + imgUrl + " />");
				sb.append("<div class=\"ps_desc\"><h3>" + title + "</h3></div>");
				sb.append("<div class=\"z-movie-playmask\"></div>");
				sb.append("</div>");
			}
			return sb.toString();
		} catch (Throwable e) {
			logger.error("Error when get baike recommend result.", e);
			return "失败：推荐系统异常！";
		}
	}

	@RequestMapping(value = "recommend-products-html/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	String recommendProductsHTML(@PathVariable String channel_code, @RequestParam(value = "size", required = false) int size) {
		// For local DEMO
		if (channel_code.startsWith("tcl_demo_")) {
			try {
				long timestamp = Long.parseLong(channel_code.substring(9));
				return demoDataService.generateDemoProductsHTML(timestamp);
			} catch (Exception e) {
				return "错误的时间戳：" + channel_code;
			}
		}

		EpgModel model = epgModelDao.findCurrentEpgModelByChannelCode(channel_code);
		if (model == null) {
			logger.error("Cannot find any program playing now.");
			return "失败：无法获取当前节目！";
		}
		try {
			List<YihaodianProductBean> products = null;
			// 已定制关键字
			SpecifiedKeyword specifiedKeyword = specifiedKeywordDao.findSpecifiedKeywordByEpgId(model.getId().toString());
			if (specifiedKeyword != null) {
				products = productDao.findYihaodianProductByFeature(specifiedKeyword.getKeyword(), size);
				for (YihaodianProductBean product : products) {
					product.setKeyType(new ArrayList<KeyType>());
					product.setSearchKey(specifiedKeyword.getKeyword());
				}
			} else {
				products = productsRecommendDao.getRecProductById(model.getId().toString(), size);
			}
			if (products == null || products.size() == 0)
				return "<span style='position:absolute;text-align:center;line-height:177px;width:325px;'><h2>无产品推荐！</h2></span>";
			StringBuffer sb = new StringBuffer();
			for (YihaodianProductBean product : products) {
				sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
				String source = product.getSource();
				if (product.getKeyType() != null) {
					if (!source.trim().equals(""))
						sb.append("<div class=\"source-tag\"><h3>" + source + "</h3></div>");
					else
						sb.append("<div class=\"source-tag\"><h3>" + product.getSearchKey() + "</h3></div>");
				}
				sb.append("<img id=\"" + product.getId() + "\" src=" + product.getPic_url() + " />");
				sb.append("<div class=\"ps_desc\"><h3>" + product.getTitleEtc() + "</h3></div>");
				sb.append("<div class=\"z-movie-playmask\"></div>");
				sb.append("</div>");
			}
			return sb.toString();
		} catch (Throwable e) {
			logger.error("Error when get product recommend result.", e);
			return "失败：推荐系统异常！";
		}
	}

	@RequestMapping(value = "baike-info-html/{baikeId}", method = RequestMethod.GET)
	public @ResponseBody
	String baikeInformationHTML(@PathVariable String baikeId) {
		BaikeBean baike = baikeDao.findBaikeBeanById(baikeId);
		StringBuffer sb = new StringBuffer();
		sb.append("<h2><b><font id='key_title'>" + baike.getShowTitle() + "</font></b></h2>");
		sb.append(baike.getShowContent());
		return sb.toString();
	}

	@RequestMapping(value = "product-info-html/{productId}", method = RequestMethod.GET)
	public @ResponseBody
	String productInformationHTML(@PathVariable String productId) {
		YihaodianProductBean product = productDao.findYihaodianProductById(productId);
		StringBuffer sb = new StringBuffer();
		String subTitle = (product.getSubtitle() == null || product.getSubtitle().trim().equals("")) ? "" : (" (" + product.getSubtitle() + ")");
		sb.append("<h2><b><font id='key_title'>" + product.getTitle() + subTitle + "</font></b></h2>");
		sb.append("<h2>品牌：" + product.getBrand_name() + "</h2>");
		sb.append("<h2>更新时间：" + Constants.parseTime(product.getUpdatetime()) + "</h2>");
		String parsedDesc = product.getIgnoredImgDescription();
		if (parsedDesc != null && !parsedDesc.trim().equals(""))
			sb.append("<h2 id='productDes' style='display:none;'>描述：" + parsedDesc + "</h2>");
		List<YihaodianSale_price> prices = product.getSale_price();
		String codeData = product.getProduct_url_m();
		String imgSrc = Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getCodeImgFolder() + codeData.hashCode() + ".png";
		File img = new File(imgSrc);
		if (!img.exists() || !img.isFile()) {
			codeImgService.generateCodeImgByDefaultSetting(codeData);
		}
		if (prices != null) {
			YihaodianSale_price guangdongPrice = null;
			YihaodianSale_price lowestPrice = prices.get(0);
			double lowest = Double.MAX_VALUE;
			for (YihaodianSale_price price : prices) {
				if (price.getPrice() < lowest) {
					lowestPrice = price;
					lowest = price.getPrice();
				}
				if (price.getRegion().trim().equals("广东")) {
					guangdongPrice = price;
				}
			}
			if (guangdongPrice == null) {
				sb.append("<div style=\"position:relative;\">");
				sb.append("<br/><h2>广东&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;暂无货源</h2>");
				sb.append("</div>");
				sb.append("<div style=\"position:relative;\">");
				sb.append("<br/><h2>￥" + lowestPrice.getPrice() + "</h2>");
				sb.append("<h2>" + lowestPrice.getRegion() + "</h2>");
				sb.append("<h2>电话：400-007-1111</h2>");
				sb.append("<img id=\"codeImg\" src=" + imgSrc + " />");
				sb.append("</div>");
			} else {
				sb.append("<div style=\"position:relative;\">");
				sb.append("<br/><h2>￥" + guangdongPrice.getPrice() + "</h2>");
				sb.append("<h2>" + guangdongPrice.getRegion() + "</h2>");
				sb.append("<h2>电话：400-007-1111</h2>");
				sb.append("<img id=\"codeImg\" src=" + imgSrc + " />");
				sb.append("</div>");
			}
		} else {
			sb.append("<h2>暂无现货</h2>");
		}
		sb.append("<br/><img id=\"mapImg\" src=\"../../clive/images/map.png\" />");
		return sb.toString();
	}

	@RequestMapping(value = "recommend-news-html/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	String recommendNewsHTML(@PathVariable String channel_code, @RequestParam(value = "size", required = false) int size) {
		// For local DEMO
		if (channel_code.startsWith("tcl_demo_")) {
			try {
				long timestamp = Long.parseLong(channel_code.substring(9));
				return demoDataService.generateDemoNewsHTML(timestamp);
			} catch (Exception e) {
				return "错误的时间戳：" + channel_code;
			}
		}

		List<? extends BaseNewsItem> items = recommendNews(channel_code, size);
		if (items == null)
			return "失败：无法获取导读推荐！";
		else if (items.size() == 0)
			return "<span style='position:absolute;text-align:center;line-height:177px;width:325px;'><h2>无新闻推荐！</h2></span>";
		StringBuffer sb = new StringBuffer();
		for (BaseNewsItem item : items) {
			String imgUrl = "../../clive/images/feed.png";
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			String source = item.getSource();
			if (!source.trim().equals(""))
				sb.append("<div class=\"source-tag\"><h3>" + source + "</h3></div>");
			sb.append("<img class_type=\"" + item.getClass().getName() + "\" id=\"" + item.get_id() + "\" src=\"" + imgUrl + "\" />");
			sb.append("<div class=\"ps_desc\"><h3>" + item.getTitleEtc() + "</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
		}
		return sb.toString();
	}

	@RequestMapping(value = "news-info-html/{itemId}", method = RequestMethod.GET)
	public @ResponseBody
	String newsInformationHTML(@PathVariable String itemId, @RequestParam(value = "class_type", required = false) String class_type) {
		if (NewsSearchRstItem.class.getName().equals(class_type)) {
			NewsSearchRstItem item = newsSearchRstItemDao.findNewsSearchRstItemsById(itemId);
			StringBuffer sb = new StringBuffer();
			sb.append("<h2><b><font id='key_title'>" + item.getTitle() + "</font></b></h2>");
			sb.append("<h2>发布时间：" + Constants.parseTime(item.getUpdateTime()) + "</h2>");
			sb.append("<h2>标签：<b><font color='red'>" + item.getProgram_wiki_keys() + "</font></b></h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;" + item.getIgnoredImgDescription() + "</h2>");

			String codeData = item.getLink();
			String imgSrc = Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getCodeImgFolder() + codeData.hashCode() + ".png";
			File img = new File(imgSrc);
			if (!img.exists() || !img.isFile()) {
				codeImgService.generateCodeImgByDefaultSetting(codeData);
			}
			sb.append("<div style=\"position:relative;\">");
			sb.append("<br/><h2>查看详细信息请扫描二维码：</h2>");
			sb.append("<img id=\"newsCodeImg\" src=" + imgSrc + " />");
			sb.append("</div><br/>");
			return sb.toString();
		} else {
			NewsItem item = newsItemDao.findNewsItemById(itemId);
			if (item == null)
				return "失败：暂无信息！";
			StringBuffer sb = new StringBuffer();
			sb.append("<h2><b>" + item.getTitle() + "</b></h2>");
			sb.append("<h2><b>发布时间：" + Constants.parseTime(item.getPubDate()) + "</b></h2>");
			sb.append("<h2><b>标签：" + newsClassificationDao.findNewsClassificationById(item.getClassify_id()).getTitle() + "最新新闻</b></h2>");
			sb.append("<h2><b>&nbsp;&nbsp;&nbsp;&nbsp;" + item.getIgnoredImgDescription() + "</b></h2>");
			return sb.toString();
		}
	}

	@RequestMapping(value = "recommend-vods/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	RecommendResults recommendVods(@PathVariable String channel_code, @RequestParam(value = "size", required = false) int size) {
		EpgModel model = epgModelDao.findCurrentEpgModelByChannelCode(channel_code);
		if (model == null) {
			logger.error("Cannot find any program playing now.");
			return null;
		}
		String url = Constants.constructCliveRecommendUrl(model.getId().toString(), size);
		try {
			RecommendResults result = restTemplate.getForObject(url, RecommendResults.class);
			return result;
		} catch (Exception e) {
			logger.error("Error to get recommend results: " + e.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "recommend-vods-html/{channel_code}", method = RequestMethod.GET)
	public @ResponseBody
	String recommendVodsHTML(@PathVariable String channel_code, @RequestParam(value = "size", required = false) int size) {
		// For local DEMO
		if (channel_code.startsWith("tcl_demo_")) {
			try {
				long timestamp = Long.parseLong(channel_code.substring(9));
				return demoDataService.generateDemoVodsHTML(timestamp);
			} catch (Exception e) {
				return "错误的时间戳：" + channel_code;
			}
		}

		RecommendResults recommendRst = recommendVods(channel_code, size);
		StringBuffer sb = new StringBuffer();
		if (recommendRst == null)
			return "失败：无法获取导视推荐！";
		if (recommendRst.getSuccess().equals(false))
			return "失败：" + recommendRst.getError().getMessage();
		if (recommendRst.getObjects() == null)
			return "<span style='position:absolute;text-align:center;line-height:177px;width:325px;'><h2>无导视推荐！</h2></span>";
		for (RecommendObject obj : recommendRst.getObjects()) {
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img id=\"" + obj.getId() + "\" src=\"" + obj.getPicUrl1() + "\" reason=\"" + obj.getReason() + "\" loveTag=\"" + obj.getLoveTag() + "\"/>");
			sb.append("<div class=\"ps_desc\"><h3>" + obj.getTitleEtc() + "</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
		}
		return sb.toString();
	}

	@RequestMapping(value = "vod-video-source/{itemId}", method = RequestMethod.GET)
	public @ResponseBody
	String vodVideoSourceText(@PathVariable String itemId) {
		VideoUrlInfo urlInfo = videoDao.findVideoUrlInfoById(itemId);
		String source = "暂无播放源";
		if (urlInfo != null && urlInfo.getSource() != null) {
			if (urlInfo.getSource().equals("youku")) {
				source = "优酷网-最佳片源";
			} else if (urlInfo.getSource().equals("letv")) {
				source = "乐视网";
			} else if (urlInfo.getSource().equals("m1905")) {
				source = "M1905电影网";
			} else if (urlInfo.getSource().equals("nearest")) {
				source = "优酷网-热门匹配";
			} else if (urlInfo.getSource().equals("notfound")) {
				source = "暂无播放源";
			}
		}
		return source;
	}

	@RequestMapping(value = "vod-behaviour/{itemId}", method = RequestMethod.GET)
	public @ResponseBody
	String behaviour(@PathVariable String itemId, @RequestParam(value = "channel", required = false) String channel) {
		// For local DEMO
		if (channel.startsWith("tcl_demo_")) {
			return demoDataService.getDemoVodUrl(itemId);
		}

		VideoUrlInfo videoUrlInfo = videoDao.findVideoUrlInfoById(itemId);
		if (videoUrlInfo == null) {
			return Constants.getProperties().getDefaultVideoUrl();
		} else if (videoUrlInfo.getUrl() == null || (videoUrlInfo.getSource() != null && videoUrlInfo.getSource().equals("notsupport")))
			return Constants.getProperties().getDefaultVideoUrl();
		return videoUrlInfo.getUrl();
	}

	@RequestMapping(value = "count-favorate-size", method = RequestMethod.GET)
	public @ResponseBody
	long countFavorateSize(@RequestParam(required = false) String user, @RequestParam(value = "channel", required = false) String channel) {
		// For local DEMO
		if (channel.startsWith("tcl_demo_")) {
			return favorateProductDao.countFavorateProductsByUser("demo");
		}

		if (user != null) {
			return favorateProductDao.countFavorateProductsByUser(user);
		} else {
			return favorateProductDao.countFavorateProductsByDefaultUser();
		}
	}

	@RequestMapping(value = "product-add-favorate/{productId}", method = RequestMethod.GET)
	public @ResponseBody
	String addFavorate(@RequestParam(required = false) String user, @PathVariable String productId, @RequestParam(value = "channel", required = false) String channel) {
		// For local DEMO
		if (channel.startsWith("tcl_demo_")) {
			user = "demo";
		}

		if (productId == null || productId.trim().equals("")) {
			return "false";
		}
		if (user != null) {
			favorateProductDao.insertFavorateProduct(user, productId);
		} else {
			favorateProductDao.insertFavorateProductByDefaultUser(productId);
		}
		return "true";
	}

	@RequestMapping(value = "product-remove-favorate/{productId}", method = RequestMethod.GET)
	public @ResponseBody
	String removeFavorate(@RequestParam(required = false) String user, @PathVariable String productId, @RequestParam(value = "channel", required = false) String channel) {
		// For local DEMO
		if (channel.startsWith("tcl_demo_")) {
			user = "demo";
		}

		if (productId == null || productId.trim().equals("")) {
			return "false";
		}
		if (user != null) {
			favorateProductDao.removeFavorateProductByUser(user, productId);
		} else {
			favorateProductDao.removeFavorateProductByDefaultUser(productId);
		}
		return "true";
	}

	@RequestMapping(value = "product-show-favorates", method = RequestMethod.GET)
	public @ResponseBody
	String getFavorateProductsHTML(@RequestParam(required = false) String user, @RequestParam(value = "channel", required = false) String channel) {
		// For local DEMO
		if (channel.startsWith("tcl_demo_")) {
			user = "demo";
		}

		try {
			List<YihaodianProductBean> products = null;
			FavorateProduct favorate = null;
			if (user != null) {
				favorate = favorateProductDao.findFavorateProductsByUser(user);
			} else {
				favorate = favorateProductDao.findFavorateProductsByDefaultUser();
			}
			if (favorate == null) {
				return "<span style='position:absolute;text-align:center;line-height:177px;width:325px;'><h2>无物品收藏！</h2></span>";
			} else {
				products = productDao.findYihaodianProductByIds(favorate.getProductIds());
				if (products == null || products.size() == 0)
					return "<span style='position:absolute;text-align:center;line-height:177px;width:325px;'><h2>无物品收藏！</h2></span>";
				else {
					StringBuffer sb = new StringBuffer();
					String imgSrc = Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getImageFolder() + "favorate_nohover.png";
					for (YihaodianProductBean product : products) {
						sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
						sb.append("<img id=\"" + product.getId() + "\" src=" + product.getPic_url() + " />");
						sb.append("<div class=\"ps_desc\"><h3>" + product.getTitleEtc() + "</h3></div>");
						sb.append("<img id='favorate-mask' src=" + imgSrc + " />");
						sb.append("</div>");
					}
					return sb.toString();
				}
			}
		} catch (Throwable e) {
			logger.error("Error when get favorate products result.", e);
			return null;
		}
	}

	@RequestMapping(value = "vod-info-html/{itemId}", method = RequestMethod.GET)
	public @ResponseBody
	String vodInformationHTML(@PathVariable String itemId) {
		// For local DEMO
		if (itemId.startsWith("tcl_demo_vod_")) {
			return demoDataService.getDemoVodInformationHTML(itemId);
		}

		MovieModel movie = movieDao.getMovieById(itemId);
		VideoUrlInfo urlInfo = videoDao.findVideoUrlInfoById(itemId);
		String source = "暂无播放源";
		if (urlInfo != null && urlInfo.getSource() != null) {
			if (urlInfo.getSource().equals("youku")) {
				source = "优酷网-最佳片源";
			} else if (urlInfo.getSource().equals("letv")) {
				source = "乐视网";
			} else if (urlInfo.getSource().equals("m1905")) {
				source = "M1905电影网";
			} else if (urlInfo.getSource().equals("nearest")) {
				source = "优酷网-热门匹配";
			} else if (urlInfo.getSource().equals("notfound")) {
				source = "暂无播放源";
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<h2>节目名称：<b><font id='key_title'>" + movie.getTitle() + "</font></b></h2>");
		if (movie.getUpdated_at() != null)
			sb.append("<h2>更新时间：" + Constants.parseTime(movie.getUpdated_at()) + "</h2>");
		sb.append("<h2>片源：" + source + "</h2>");
		if (movie.getHost() != null && movie.getHost().size() > 0)
			sb.append("<h2>主持人：" + movie.getHost() + "</h2>");
		if (movie.getGuest() != null && movie.getGuest().size() > 0)
			sb.append("<h2>嘉宾：" + movie.getGuest() + "</h2>");
		if (movie.getDirector() != null && movie.getDirector().size() > 0)
			sb.append("<h2>导演：" + movie.getDirector() + "</h2>");
		if (movie.getStarring() != null && movie.getStarring().size() > 0)
			sb.append("<h2>演员：" + movie.getStarring() + "</h2>");
		if (movie.getTags() != null && movie.getTags().size() > 0)
			sb.append("<h2>节目类型：" + movie.getTags() + "</h2>");
		sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;" + movie.getContentEtc() + "</h2>");
		return sb.toString();
	}

	@RequestMapping(value = "update", method = RequestMethod.GET)
	public @ResponseBody
	String update() {
		periodUpdateEngine.update();
		return "Period upate recommender result.";
	}

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam(value = "web", required = false) boolean web, @RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "size", required = false) String size, Model model) {
		String channel_code = "c8bf387b1824053bdb0423ef806a2227";
		if (channel != null)
			channel_code = channel;
		model.addAttribute("web", web);
		if (size == null || size.trim().equals(""))
			model.addAttribute("size", 20);
		else
			model.addAttribute("size", Integer.parseInt(size));
		model.addAttribute("channel", channel_code);
		return "clive/index";
	}

	@RequestMapping(value = "movieInformation", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getMovieInfor(@RequestParam(value = "itemId", required = true) String itemId) {
		MovieModel movie = movieDao.getMovieById(itemId);
		Map<String, Object> messages = new HashMap<String, Object>();
		messages.put("_id", movie.get_id());
		messages.put("content", movie.getContent());
		messages.put("country", movie.getCountry());
		messages.put("cover", movie.getCover());
		messages.put("director", movie.getDirector());
		messages.put("language", movie.getLanguage());
		messages.put("released", movie.getReleased());
		messages.put("starring", movie.getStarring());
		messages.put("tags", movie.getTags());
		messages.put("title", movie.getTitle());
		return messages;
	}

	@RequestMapping(value = "proxy", method = RequestMethod.GET)
	public @ResponseBody
	String ajaxProxy(@RequestParam(value = "url", required = true) String url) {
		try {
			String response = new String(HttpClientUtil.getHttp(url).getBytes(), "UTF-8");
			return response;
		} catch (Exception e) {
			logger.error("Error in getting url: " + url, e);
			return null;
		}
	}
}
