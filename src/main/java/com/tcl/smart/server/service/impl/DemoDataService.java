package com.tcl.smart.server.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.bean.RecommendObject;
import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.bean.YihaodianSale_price;
import com.tcl.smart.server.dao.IMovieDao;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;
import com.tcl.smart.server.dao.IYihaodianProductDao;
import com.tcl.smart.server.service.IBaikeSearchService;
import com.tcl.smart.server.service.IDemoDataService;
import com.tcl.smart.server.util.Constants;
import com.tcl.smart.server.util.KeyType;
import com.tcl.smart.server.util.Serializer;

/**
 * @author fanjie
 * @date 2013-5-30
 */
public class DemoDataService implements IDemoDataService {
	private static final Logger logger = LoggerFactory.getLogger(DemoDataService.class);

	private static long TIMESTAMP_XINWEN = 100000;
	private static long TIMESTAMP_AD = 158000;

	private static String PATH_DEMO_SERIALIZE = null;

	private static String FILE_DEMO_BAIKE_XINWEN = "baike_xinwen.tmp";
	private static String FILE_DEMO_BAIKE_AD = "baike_ad.tmp";
	private static String FILE_DEMO_BAIKE_FCWR = "baike_fcwr.tmp";

	private static String FILE_DEMO_NEWS_XINWEN = "news_xinwen.tmp";
	private static String FILE_DEMO_NEWS_AD = "news_ad.tmp";
	private static String FILE_DEMO_NEWS_FCWR = "news_fcwr.tmp";

	private static String FILE_DEMO_PRODUCT_XINWEN = "product_xinwen.tmp";
	private static String FILE_DEMO_PRODUCT_AD = "product_ad.tmp";
	private static String FILE_DEMO_PRODUCT_FCWR = "product_fcwr.tmp";

	private static String FILE_DEMO_VOD_XINWEN = "vod_xinwen.tmp";
	private static String FILE_DEMO_VOD_AD = "vod_ad.tmp";
	private static String FILE_DEMO_VOD_FCWR = "vod_fcwr.tmp";

	@Qualifier("movieDao")
	@Autowired
	private IMovieDao movieDao;

	@Qualifier("newsSearchRstItemDao")
	@Autowired
	private INewsSearchRstItemDao newsItemDao;

	@Qualifier("baikeSearchService")
	@Autowired
	private IBaikeSearchService baikeSearchService;

	@Qualifier("yihaodianProductDao")
	@Autowired
	private IYihaodianProductDao yihaodianProductDao;

	private void ensurePath() {
		if (PATH_DEMO_SERIALIZE == null) {
			PATH_DEMO_SERIALIZE = System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator
					+ Constants.getProperties().getSerializeFolder();
		}
	}

	public String currentProgramTitle(long timestamp) {
		if (timestamp <= TIMESTAMP_XINWEN) {
			String programTitle = "CCTV-13新闻：新闻联播";
			return programTitle;
		} else if (timestamp <= TIMESTAMP_AD) {
			String programTitle = "广告：美宝莲";
			return programTitle;
		} else {
			String programTitle = "江苏卫视：非诚勿扰";
			return programTitle;
		}
	}

	public String currentEpgHTML(long timestamp) {
		if (timestamp <= TIMESTAMP_XINWEN) {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img src='../../clive/images/xinwen.jpg' />");
			sb.append("<div class=\"ps_desc\"><h3>新闻联播</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
			return sb.toString();
		} else if (timestamp <= TIMESTAMP_AD) {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img src='../../clive/images/ad.jpg' />");
			sb.append("<div class=\"ps_desc\"><h3>广告</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img src='../../clive/images/fcwr.jpg' />");
			sb.append("<div class=\"ps_desc\"><h3>非诚勿扰</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
			return sb.toString();
		}
	}

	public String currentEpgInformationHTML(long timestamp) {
		MovieModel movie = null;
		if (timestamp <= TIMESTAMP_XINWEN) {
			movie = movieDao.getMovieById("4eaa5a58edcd88db17001495");
		} else if (timestamp <= TIMESTAMP_AD) {
			movie = movieDao.getMovieById("5170e50eed454b5c72000000");
		} else {
			movie = movieDao.getMovieById("4d0088482f2a241bd700ce8a");
		}
		if (movie != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font color='red'>" + movie.getTitle() + "</font></b></h2>");
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
			if (movie.getTags() != null && movie.getTags().size() > 0)
				sb.append("<h2>节目类型：" + movie.getTags() + "</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;" + movie.getContent() + "</h2>");
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font color='red'>DEMO测试视频</font></b></h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;一个国家经济的崛起有赖于一批民族企业的壮大。\"诞生\"于改革开放之初的TCL，现已成长为有抱负的\"青年\"。作为率先国际化的中国企业，TCL没有任何可以借鉴的蓝本，在全球范围内探索着一条中国品牌从未走过的道路。经过国际化的严峻考验和不懈探索，我们在国际化道路上的步伐更加坚定。通过国际化并购，TCL实现了由扎根本土的中国企业向放眼全球的国际化企业的历史性转变，进入了全新发展阶段。如果把近几年TCL的国际化探索看作是被动适应国际化带来的冲击和挑战,那么，从现在开始，我们就已经进入到一个主动谋划国际化未来发展目标和任务的新阶段。我们将站在新的历史起点上，力争将TCL建成一个富有竞争力的国际化企业！在未来两到三年TCL发展的总体战略思路中，我们要固本强基、持续创新，实现新跨越。\"固本强基\"，就是要巩固基础管理之根本,强化核心能力之根基；\"持续创新\"，就是要以技术、流程和文化的变革突破，将企业的经营管理水平提升到国际化要求的新高度。我们坚信，\"敢为天下先\"的TCL人，定能凭借变革创新的智慧、百折不挠的毅力,谱写TCL历史的新篇章，实现TCL发展的新跨越！</h2>");
			return sb.toString();
		}
	}

	public String getDemoVodUrl(String itemId) {
		if ("tcl_demo_vod_xinwen_1".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTU5MTU2NTgw.html";
		} else if ("tcl_demo_vod_xinwen_2".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTYxNzM1NzI4.html";
		} else if ("tcl_demo_vod_xinwen_3".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XMTQ4MzAzMzcy.html";
		} else if ("tcl_demo_vod_xinwen_4".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTYyMTM4OTQw.html";
		} else if ("tcl_demo_vod_xinwen_5".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTYxODY2MTMy.html";
		} else if ("tcl_demo_vod_xinwen_6".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTYyMjc2MjI4.html";
		}

		else if ("tcl_demo_vod_ad_1".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XMzUwMjYyOTEy.html";
		} else if ("tcl_demo_vod_ad_2".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XMzY4NTM5MzI0.html";
		} else if ("tcl_demo_vod_ad_3".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XMzY4ODQyMDk2.html";
		} else if ("tcl_demo_vod_ad_4".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XMzA0MTU3ODU2.html";
		} else if ("tcl_demo_vod_ad_5".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XMzUwMjYzODEy.html";
		} else if ("tcl_demo_vod_ad_6".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XMzUwMjY0Nzg4.html";
		}

		else if ("tcl_demo_vod_fcwr_1".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTYzODM0ODQ0.html";
		} else if ("tcl_demo_vod_fcwr_2".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTQ3NDYwMDg0.html";
		} else if ("tcl_demo_vod_fcwr_3".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XMTU5NzM5NzEy.html";
		} else if ("tcl_demo_vod_fcwr_4".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTE3NDgwODg0.html";
		} else if ("tcl_demo_vod_fcwr_5".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNTY0Nzc0MDY4.html";
		} else if ("tcl_demo_vod_fcwr_6".equals(itemId)) {
			return "http://v.youku.com/v_show/id_XNDM3MDg1MzYw.html";
		}

		else {
			return "";
		}
	}

	public String getDemoVodInformationHTML(String itemId) {
		if ("tcl_demo_vod_xinwen_1".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>李克强离京对印度、巴基斯坦、瑞士、德国进行正式访问</font></b></h2>");
			sb.append("<h2>更新时间：2013-5-16 19:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[张宏民]</h2>");
			sb.append("<h2>节目类型：[新闻联播]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;视频: 李克强离京对印度、巴基斯坦、瑞士、德国进行正式访问 130519</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_2".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>国务院总理李克强访问瑞士</font></b></h2>");
			sb.append("<h2>更新时间：2013-5-25 19:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[张宏民]</h2>");
			sb.append("<h2>节目类型：[新闻联播]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;视频: 国务院总理李克强访问瑞士 130525</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_3".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>李克强抵达苏黎世开始对瑞士进行正式访问</font></b></h2>");
			sb.append("<h2>更新时间：2012-1-25 19:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[张宏民]</h2>");
			sb.append("<h2>节目类型：[新闻联播]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;视频: 李克强抵达苏黎世开始对瑞士进行正式访问</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_4".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>李克强参观瑞士爱因斯坦博物馆时强调 创新是人类活力的源泉</font></b></h2>");
			sb.append("<h2>更新时间：2013-5-26 19:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[张宏民]</h2>");
			sb.append("<h2>节目类型：[新闻联播]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;视频: 李克强参观瑞士爱因斯坦博物馆时强调 创新是人类活力的源泉</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_5".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>李克强参观瑞士家庭农庄强调 加强中瑞合作 促农业现代化</font></b></h2>");
			sb.append("<h2>更新时间：2013-5-25 19:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[张宏民]</h2>");
			sb.append("<h2>节目类型：[新闻联播]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;视频: 李克强参观瑞士家庭农庄强调 加强中瑞合作 促农业现代化 130525</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_6".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>李克强圆满结束对瑞士访问</font></b></h2>");
			sb.append("<h2>更新时间：2013-5-26 19:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[张宏民]</h2>");
			sb.append("<h2>节目类型：[新闻联播]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;视频: 李克强圆满结束对瑞士访问 130526</h2>");
			return sb.toString();
		}

		else if ("tcl_demo_vod_ad_1".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>美宝莲潮妆学院-清新约会妆</font></b></h2>");
			sb.append("<h2>更新时间：2012-5-6 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>节目类型：[时尚]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;美宝莲纽约创立于1920年，是全球最大化妆品公司欧莱雅集团在中国发展最为成功的品牌之一，在全球超过129个国家和地区均有销售。2012年第六次成为梅赛德斯-奔驰纽约时装周的官方化妆品赞助商。致力于帮助中国女性更好地展示属于自己的“美”，内在的美，形象的美，自信的美！2011年，推出潮妆学院，让广大女生懂化妆，学化妆，爱化妆，全民一起“无妆不潮”。 </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_2".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>中性帅气的春日妆容（无妆不潮） 美宝莲中国</font></b></h2>");
			sb.append("<h2>更新时间：2012-5-6 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>节目类型：[时尚]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;美宝莲纽约创立于1920年，是全球最大化妆品公司欧莱雅集团在中国发展最为成功的品牌之一，在全球超过129个国家和地区均有销售。2012年第六次成为梅赛德斯-奔驰纽约时装周的官方化妆品赞助商。致力于帮助中国女性更好地展示属于自己的“美”，内在的美，形象的美，自信的美！2011年，推出潮妆学院，让广大女生懂化妆，学化妆，爱化妆，全民一起“无妆不潮”。 </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_3".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>美寶蓮-早春明亮動人渡假風妝容</font></b></h2>");
			sb.append("<h2>更新时间：2012-5-7 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>节目类型：[时尚]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;美宝莲系列产品 ：<br/>&nbsp;&nbsp;&nbsp;&nbsp;倍潤型BB霜： 搽上去之後皮膚非常有光澤 視覺上看起來膚質很細緻飽滿 適合乾性皮膚或者面型消瘦的女生使用<br/>&nbsp;&nbsp;&nbsp;&nbsp;星鑚炫目五色眼影盘：五種顏色都是基本妝容必備的顏色很實用 粉末極細 也很顯色<br/>&nbsp;&nbsp;&nbsp;&nbsp;密扇睫毛膏：裏面含有豐富的纖維 睫毛稀少的人也可以變得很長很濃密 而且溫水可卸 用卸妝液卸也很好卸 不會融化 不會碎成末狀 會一條條的脫下來~<br/>&nbsp;&nbsp;&nbsp;&nbsp;礦物水漾親膚曬紅： 散粉狀腮紅 粉末幼嫩 又方便定妝<br/>&nbsp;&nbsp;&nbsp;&nbsp;絕色持久唇膏： 顏色飽滿 很顯色 稍嫌搽厚了以後有點黏黏的感覺</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_4".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>美宝莲潮妆学院呈献: 职场裸妆</font></b></h2>");
			sb.append("<h2>更新时间：2012-6-21 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>节目类型：[时尚]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;美宝莲纽约创立于1920年，是全球最大化妆品公司欧莱雅集团在中国发展最为成功的品牌之一，在全球超过129个国家和地区均有销售。2012年第六次成为梅赛德斯-奔驰纽约时装周的官方化妆品赞助商。致力于帮助中国女性更好地展示属于自己的“美”，内在的美，形象的美，自信的美！2011年，推出潮妆学院，让广大女生懂化妆，学化妆，爱化妆，全民一起“无妆不潮”。 </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_5".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>美宝莲潮妆学院-迷人大眼妆</font></b></h2>");
			sb.append("<h2>更新时间：2012-4-2 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>节目类型：[时尚]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;美宝莲纽约创立于1920年，是全球最大化妆品公司欧莱雅集团在中国发展最为成功的品牌之一，在全球超过129个国家和地区均有销售。2012年第六次成为梅赛德斯-奔驰纽约时装周的官方化妆品赞助商。致力于帮助中国女性更好地展示属于自己的“美”，内在的美，形象的美，自信的美！2011年，推出潮妆学院，让广大女生懂化妆，学化妆，爱化妆，全民一起“无妆不潮”。 </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_6".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>美宝莲潮妆学院-风格职场妆</font></b></h2>");
			sb.append("<h2>更新时间：2012-8-16 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>节目类型：[时尚]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;美宝莲纽约创立于1920年，是全球最大化妆品公司欧莱雅集团在中国发展最为成功的品牌之一，在全球超过129个国家和地区均有销售。2012年第六次成为梅赛德斯-奔驰纽约时装周的官方化妆品赞助商。致力于帮助中国女性更好地展示属于自己的“美”，内在的美，形象的美，自信的美！2011年，推出潮妆学院，让广大女生懂化妆，学化妆，爱化妆，全民一起“无妆不潮”。 </h2>");
			return sb.toString();
		}

		else if ("tcl_demo_vod_fcwr_1".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>非诚勿扰51岁女嘉宾成功牵手小17岁男！不可思议的牵手！</font></b></h2>");
			sb.append("<h2>更新时间：2013-3-16 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[孟飞]</h2>");
			sb.append("<h2>嘉宾：[乐嘉，黄菡]</h2>");
			sb.append("<h2>节目类型：[大型生活服务类节目]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;视频: 非诚勿扰51岁女嘉宾成功牵手小17岁男！不可思议的牵手！</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_2".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>《非诚勿扰》官晶晶朱峰 发现王国浪漫童话婚礼秀</font></b></h2>");
			sb.append("<h2>更新时间：2013-1-16 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[孟飞]</h2>");
			sb.append("<h2>嘉宾：[乐嘉，黄菡]</h2>");
			sb.append("<h2>节目类型：[大型生活服务类节目]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;大连发现王国浪漫无极限，《非诚勿扰》史上最感人的情侣，官晶晶和朱峰，步入爱的殿堂，演绎“最童话”的浪漫婚礼！</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_3".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>《非诚勿扰》“富二代”澄清事实</font></b></h2>");
			sb.append("<h2>更新时间：2013-4-12 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[孟飞]</h2>");
			sb.append("<h2>嘉宾：[乐嘉，黄菡]</h2>");
			sb.append("<h2>节目类型：[大型生活服务类节目]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;《非诚勿扰》是江苏卫视一档适应现代生活节奏的大型婚恋交友节目，为广大单身男女提供公开的婚恋交友平台，精良的节目制作和全新的婚恋交友模式得到观众和网友广泛关注。 </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_4".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>乐嘉请辞江苏卫视 《非诚勿扰》暂不受影响</font></b></h2>");
			sb.append("<h2>更新时间：2013-3-16 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[孟飞]</h2>");
			sb.append("<h2>嘉宾：[乐嘉，黄菡]</h2>");
			sb.append("<h2>节目类型：[大型生活服务类节目]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;视频: 乐嘉请辞江苏卫视 《非诚勿扰》暂不受影响（流畅）</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_5".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>《非诚勿扰》新西兰专场，江一燕助场</font></b></h2>");
			sb.append("<h2>更新时间：2013-4-7 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[孟飞]</h2>");
			sb.append("<h2>嘉宾：[乐嘉，黄菡]</h2>");
			sb.append("<h2>节目类型：[大型生活服务类节目]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;《非诚勿扰》是2010年1月15日中国大陆江苏卫视制作的一档婚恋交友真人秀节目，由江苏电视台新闻节目主持人孟非主持，前期由乐嘉和黄菡两个人分析点评，2013年4月8-7日起由于正代班乐嘉[1]，2013年4月13日起由曾子航代班于正。节目首播时间为每周六和周日的21:10，后改为21:20，同日23:00左右重播，周六周日12:30左右第二次重播（后改为11:30，分别重播上周六、周日）。2011年7月推出澳洲专场；2011年10月推出美国专场；2012年1月推出英国专场；2012年6月推出法国专场；201</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_6".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>节目名称：<b><font id='key_title'>《非诚勿扰》韩国专场</font></b></h2>");
			sb.append("<h2>更新时间：2013-5-8 12:00:00</h2>");
			sb.append("<h2>片源：优酷网-最佳片源</h2>");
			sb.append("<h2>主持人：[孟飞]</h2>");
			sb.append("<h2>嘉宾：[乐嘉，黄菡]</h2>");
			sb.append("<h2>节目类型：[大型生活服务类节目]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;中国内地最火爆的综艺节目《非常勿扰》韩国专场报名启动仪式于8月8日上午在韩国旅游发展局隆重拉开帷幕。 8月1日-9月15日：在首尔、釜山等地接受报名 9月16日-9月30日：对报名者进行面试 10月上旬：在江苏卫视进行录制 10月末：正式播出</h2>");
			return sb.toString();
		}

		else {
			return "暂无信息";
		}
	}

	@Override
	public String generateDemoBaikesHTML(long timestamp) {
		ensurePath();
		List<BaikeBean> baikes = new ArrayList<BaikeBean>();
		if (timestamp <= TIMESTAMP_XINWEN) {
			baikes = getDemoBaikes_xinwen();
		} else if (timestamp <= TIMESTAMP_AD) {
			baikes = getDemoBaikes_ad();
		} else {
			baikes = getDemoBaikes_fcwr();
		}
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
	}

	@Override
	public String generateDemoNewsHTML(long timestamp) {
		ensurePath();
		List<NewsSearchRstItem> items = new ArrayList<NewsSearchRstItem>();
		if (timestamp <= TIMESTAMP_XINWEN) {
			items = getDemoNews_xinwen();
		} else if (timestamp <= TIMESTAMP_AD) {
			items = getDemoNews_ad();
		} else {
			items = getDemoNews_fcwr();
		}
		StringBuffer sb = new StringBuffer();
		for (NewsSearchRstItem item : items) {
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

	@Override
	public String generateDemoProductsHTML(long timestamp) {
		ensurePath();
		List<YihaodianProductBean> products = new ArrayList<YihaodianProductBean>();
		if (timestamp <= TIMESTAMP_XINWEN) {
			products = getDemoProducts_xinwen();
		} else if (timestamp <= TIMESTAMP_AD) {
			products = getDemoProducts_ad();
		} else {
			products = getDemoProducts_fcwr();
		}
		try {
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
			logger.error("", e);
			return null;
		}
	}

	public String generateDemoVodsHTML(long timestamp) {
		ensurePath();
		List<RecommendObject> vods = new ArrayList<RecommendObject>();
		if (timestamp <= TIMESTAMP_XINWEN) {
			vods = getDemoVods_xinwen();
		} else if (timestamp <= TIMESTAMP_AD) {
			vods = getDemoVods_ad();
		} else {
			vods = getDemoVods_fcwr();
		}
		StringBuffer sb = new StringBuffer();
		for (RecommendObject obj : vods) {
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img id=\"" + obj.getId() + "\" src=\"" + obj.getCover() + "\"/>");
			sb.append("<div class=\"ps_desc\"><h3>" + obj.getTitleEtc() + "</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private List<BaikeBean> getDemoBaikes_xinwen() {
		List<BaikeBean> baikes = null;
		File baikeFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_BAIKE_XINWEN);
		if (baikeFile.exists() && baikeFile.isFile()) {
			try {
				baikes = (List<BaikeBean>) Serializer.readObject(baikeFile);
			} catch (Exception e) {
				baikes = new ArrayList<BaikeBean>();
				logger.error("", e);
			}
		} else {
			baikes = new ArrayList<BaikeBean>();
			try {
				baikes.addAll(baikeSearchService.search("新闻联播"));
				baikes.addAll(baikeSearchService.search("李克强"));
				baikes.addAll(baikeSearchService.search("阿尔伯特·爱因斯坦"));
				baikes.addAll(baikeSearchService.search("瑞士"));
				baikes.addAll(baikeSearchService.search("伯尔尼"));
				baikes.addAll(baikeSearchService.search("爱因斯坦博物馆"));
				baikes.add(baikeSearchService.search("兵马俑").get(0));

				try {
					Serializer.writeObject(baikes, baikeFile);
				} catch (Exception e) {
					logger.error("", e);
				}
			} catch (BlockException e) {
				logger.error("", e);
			}
		}
		return baikes;
	}

	@SuppressWarnings("unchecked")
	private List<BaikeBean> getDemoBaikes_ad() {
		List<BaikeBean> baikes = null;
		File baikeFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_BAIKE_AD);
		if (baikeFile.exists() && baikeFile.isFile()) {
			try {
				baikes = (List<BaikeBean>) Serializer.readObject(baikeFile);
			} catch (Exception e) {
				baikes = new ArrayList<BaikeBean>();
				logger.error("", e);
			}
		} else {
			baikes = new ArrayList<BaikeBean>();
			try {
				baikes.addAll(baikeSearchService.search("美宝莲"));
				baikes.add(baikeSearchService.search("樱桃").get(0));
				baikes.add(baikeSearchService.search("珍珠").get(0));
				baikes.addAll(baikeSearchService.search("皮肤护理"));
				baikes.addAll(baikeSearchService.search("睫毛膏"));
				baikes.add(baikeSearchService.search("威廉姆斯").get(1));
				try {
					Serializer.writeObject(baikes, baikeFile);
				} catch (Exception e) {
					logger.error("", e);
				}
			} catch (BlockException e) {
				logger.error("", e);
			}
		}
		return baikes;
	}

	@SuppressWarnings("unchecked")
	private List<BaikeBean> getDemoBaikes_fcwr() {
		List<BaikeBean> baikes = null;
		File baikeFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_BAIKE_FCWR);
		if (baikeFile.exists() && baikeFile.isFile()) {
			try {
				baikes = (List<BaikeBean>) Serializer.readObject(baikeFile);
			} catch (Exception e) {
				baikes = new ArrayList<BaikeBean>();
				logger.error("", e);
			}
		} else {
			baikes = new ArrayList<BaikeBean>();
			try {
				baikes.add(baikeSearchService.search("非诚勿扰").get(0));
				baikes.addAll(baikeSearchService.search("孟非"));
				baikes.addAll(baikeSearchService.search("乐嘉"));
				baikes.add(baikeSearchService.search("黄菡").get(0));
				baikes.addAll(baikeSearchService.search("百合网"));
				baikes.addAll(baikeSearchService.search("湖南"));

				try {
					Serializer.writeObject(baikes, baikeFile);
				} catch (Exception e) {
					logger.error("", e);
				}
			} catch (BlockException e) {
				logger.error("", e);
			}
		}
		return baikes;
	}

	@SuppressWarnings("unchecked")
	private List<NewsSearchRstItem> getDemoNews_xinwen() {
		List<NewsSearchRstItem> news = new ArrayList<NewsSearchRstItem>();
		File newsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_NEWS_XINWEN);
		if (newsFile.exists() && newsFile.isFile()) {
			try {
				news = (List<NewsSearchRstItem>) Serializer.readObject(newsFile);
			} catch (Exception e) {
				news = new ArrayList<NewsSearchRstItem>();
				logger.error("", e);
			}
		} else {
			NewsSearchRstItem news1 = new NewsSearchRstItem();
			news1.set_id("000000000000000000000001");
			news1.setProgram_wiki_keys(Arrays.asList("李克强"));
			news1.setTitle("李克强参观瑞士爱因斯坦博物馆时强调创新是人类活力的源泉");
			news1.setLink("http://www.ccdy.cn/wenhuabao/yb/201305/t20130527_653395.htm");
			news1.setUpdateTime(new Date());
			StringBuffer des = new StringBuffer();
			des.append("新华社伯尔尼5月25日电 (记者明金维 王昭)当地时间25日上午，正在瑞士访问的国务院总理李克强参观了位于伯尔尼的爱因斯坦博物馆。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;周末的伯尔尼街道宁静、安详。当李克强抵达时，瑞士科技与创新部国务秘书安布罗基奥、伯尔尼市长柴佩特和伯尔尼历史博物馆馆长梅塞利在博物馆入口处迎接。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;爱因斯坦博物馆是伯尔尼历史博物馆的一部分，展品内容丰富。李克强仔细听取了关于爱因斯坦在伯尔尼生活、研究特别是提出狭义相对论的有关介绍。李克强说，爱因斯坦是伯尔尼的骄傲，也是全人类的光荣。爱因斯坦的经历表明，没有想象力，就没有创造力。只有勤奋、好学，才有可能创造伟大的成就。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;当得知伯尔尼历史博物馆正在举办中国秦始皇兵马俑展览时，李克强表示赞许。他说，秦始皇兵马俑是中国历史文化精华的一部分。两个展览同时展出，让东西方文化握手，让中瑞两国文化精华实现了跨越时空的碰撞。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;随后，李克强与在馆内参观的当地民众互动交流，询问他们的学习、工作情况。当得知他们中的一些人有过在中国留学、工作的经历时，李克强勉励他们做中瑞友谊的使者。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;一位瑞士大学生问李克强，你作为中国总理是否还有时间读书，李克强笑着回答，无论工作多忙，都要抽出时间读书。如果不读书，就难以有思想火花闪烁，也难以了解人类文明进程。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;当有民众提出中国如何推动科技创新的问题时，李克强说，创新所创造的财富不可估量。要加强知识产权保护，这样创新者才有激情，才能得到应有回报。企业之间交往要注重技术合作，使产品既有科技含量，又能适合当地市场的需求。中国政府出台了一系列推动科技创新的政策，欢迎瑞士和欧洲企业到中国投资。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;时间在欢声笑语中飞逝。临别前，梅塞利馆长邀请李克强在留言册上留言。李克强提笔写下，“创新是人类活力的源泉”，赢得在场人士的热烈掌声。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;当天，李克强还会见了中国驻瑞士使馆工作人员、中资机构、华侨华人和留学生代表。<br/>");
			news1.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news1);
			newsItemDao.saveNewsSearchRstItem(news1);
			news.add(news1);

			NewsSearchRstItem news2 = new NewsSearchRstItem();
			news2.set_id("000000000000000000000002");
			news2.setProgram_wiki_keys(Arrays.asList("中国", "瑞士", "合作"));
			news2.setTitle("中国与瑞士金融合作空间巨大");
			news2.setLink("http://intl.ce.cn/specials/zxgjzh/201305/29/t20130529_24429684.shtml");
			news2.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("5月24日，中瑞签署结束自贸协定谈判的谅解备忘录。这项重大成果不仅有助于中国和瑞士两国经贸持续深化健康发展，而且有助于促进中国同欧洲之间的关系。中国将继续坚定不移地改革开放，世界各国也将在互利合作中分享中国机遇。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;瑞士国土面积不大，人口也较少，却是世界金融强国，银行业、保险业、证券市场、黄金市场是瑞士金融业的四大支柱，是瑞士经济的重要引擎，近20年金融业对瑞士经济增长贡献近三分之一。瑞士金融业历史悠久、保密制度严格、服务专业，国际化程度高，在全球金融业中独树一帜。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;瑞士金融业发端于16世纪中期，18世纪形成规模。由于瑞士自1815年起信守中立政策，被认为是世界上最安全的资本存放地，加上地理区位优势，以及税制温和及瑞士法郎币值稳定，成为全球金融资产趋之若鹜的避风港。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;瑞士的银行尤其是私人银行一直被认为是世界上最安全的银行，有着悠久的财富管理历史和丰富的金融管理经验。瑞士境内312家银行2011年产值达350亿瑞士法郎；瑞士银行业以资产管理为核心业务，管理资产总额达5.3万亿瑞士法郎。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;瑞士有228家保险公司，资产投资总额近5100亿瑞士法郎，保费收入约545亿瑞士法郎，占全球市场份额的5.5%，人均保费欧洲第一。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1995年，瑞士证券交易所在全世界率先启用在线电子交易系统，2012年交易额近9000亿瑞士法郎。瑞士是世界最大炼金国，人均黄金储备全球第一，全球40%的黄金交易在瑞士进行，苏黎世是全世界第二大黄金交易市场。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;2008年国际金融危机给瑞士金融业带来较大冲击。由于瑞士金融法律和监管体系相对完备，大多数银行储备雄厚、经营谨慎，多数银行和其他金融企业在国际金融危机中运营稳健。为应对国际金融危机，近年来瑞士健全监管体系，完善法律框架，努力维护瑞士国际金融中心地位。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;瑞士是最早同新中国建交的西方国家之一，也是中国在欧洲的重要金融合作伙伴之一。瑞士长于金融，管理经验丰富，被称为银行密度最高的国家。与瑞方加强金融监管、宏观政策和完善资本市场体系等方面的合作，是中国开放型经济发展的客观需要。中国正在深化金融业改革开放，包括稳步推进利率市场化改革以及人民币资本项目可兑换、建立个人投资者境外投资制度、完善金融监管机制等，这将为两国金融企业互动发展提供新的机遇。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;长期以来，瑞士金融业一直十分看重中国，瑞银、瑞信等瑞士跨国银行谋求进一步进入快速发展的中国金融市场。近年来，中国金融业发展势头强劲，本土金融服务需求庞大，境外投资理财需求日益提升。中国深化金融体制改革，面临着对内推进市场化改革、对外加快走出去步伐等挑战。瑞士金融系统监管严谨值得中国学习借鉴，有利于提升中国金融业抗风险能力和竞争力。中瑞在金融方面存在着巨大互补空间，中瑞加强金融领域交流与合作，在国际金融机构中加强协调配合，对促进中国金融业发展，推动中国金融企业进一步“走出去”具有重要意义。<br/>");
			news2.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news2);
			newsItemDao.saveNewsSearchRstItem(news2);
			news.add(news2);

			NewsSearchRstItem news3 = new NewsSearchRstItem();
			news3.set_id("000000000000000000000003");
			news3.setProgram_wiki_keys(Arrays.asList("爱因斯坦博物馆"));
			news3.setTitle("爱因斯坦博物馆 ");
			news3.setLink("http://news.xinhuanet.com/2013-05/25/c_115907608.htm");
			news3.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("新华网北京５月２５日电 位于瑞士伯尔尼历史博物馆二层的爱因斯坦博物馆，２００７年２月１日正式开馆。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;爱因斯坦博物馆是为纪念爱因斯坦相对论提出１００周年而创设。２００５年是世界物理年。为纪念１９０５年爱因斯坦在伯尔尼取得的决定性突破，２００５年伯尔尼举办了《相对论１００年》临时展览。由于当年参观者众多，历史博物馆决定把临时的展览精华办成常设展馆。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;爱因斯坦博物馆共展出２００多件实物、书写和影印文书，回顾爱因斯坦的一生、再现爱因斯坦时代的历史。展览依爱因斯坦成长过程展开，从父亲的作坊、学生时代、结婚生子，最后到他对世界的影响。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;展品中既有爱因斯坦的手表、使用过的物品，又有当时的打字机、电话、水房甚至小商店等实物。此外，展览运用声光电等现代技术，单人床、晾晒的衣物作为投影屏幕，所有的说明都用德、法、英三种文字，另外还设有七种语言的自助解说录音机。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;徜徉在爱因斯坦当年用过的这些物品之间，让人感觉仿佛穿越了历史的时空，与这位科学巨匠进行了一次无声的对话。<br/>");
			news3.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news3);
			newsItemDao.saveNewsSearchRstItem(news3);
			news.add(news3);

			NewsSearchRstItem news4 = new NewsSearchRstItem();
			news4.set_id("000000000000000000000004");
			news4.setProgram_wiki_keys(Arrays.asList("爱因斯坦博物馆", "瑞士"));
			news4.setTitle("我国99.7%出口瑞士产品享零关税 促开拓欧盟市场");
			news4.setLink("http://www.chinadaily.com.cn/micro-reading/dzh/2013-05-26/content_9136386_2.html");
			news4.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("[提要]  24日，李克强在伯尔尼出席瑞士联邦主席毛雷尔举行的欢迎仪式。高虎城指出，中瑞之间的产业结构和贸易结构互补性强，双方合作潜力大，自贸协定的达成，将进一步深化双方经贸合作，推动双方经贸关系迈上新台阶。中瑞自贸协定将为中欧贸易开启新的机会，并为中国与欧盟建立更加紧密的经贸关系起到示范作用。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;国务院总理李克强24日在伯尔尼与瑞士联邦主席毛雷尔举行会谈，双方签署了结束中瑞自贸协定谈判的谅解备忘录，这标志着双方自2010年启动的自贸区谈判基本尘埃落定，离协定签署和批准实施仅一步之遥。双方还宣布建立金融对话机制。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;李克强说，三年前我访问瑞士期间，中瑞宣布启动自贸谈判。经过双方坚持不懈的努力，最终达成了一个高水平、高质量、互惠互利的协定。这是中国与欧洲大陆国家的第一个自贸协定，对拓展和深化中瑞乃至中欧经贸关系具有重要的现实意义和长远影响。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;李克强说，中瑞双方应抓住机遇，扩大投资贸易规模，优化结构，加强在高端机械制造、精密仪器、生物制药、节能环保、现代农业等领域的互利合作，推动两国经贸合作快速全面升级。用好金融对话的新平台，深化在金融监管、宏观政策、完善资本市场体系等方面的合作，加强在国际货币基金组织、世界银行等国际金融机构中的协调配合，共同推动建立公平、公正、包容、有序的国际金融新秩序。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;毛雷尔说，李克强总理此访成果丰硕，两国达成自贸协定并开展金融对话，是两国关系又一里程碑。瑞士有幸成为第一个与中国建立自贸区和金融对话机制的欧洲大陆国家，相信将有力促进瑞士的发展，并为瑞中关系长远发展奠定更加坚实的基础，瑞方愿与中方深化全方位合作，推动双边关系迈上新台阶。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>解读</b><br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>99.7%出口瑞士产品零关税</b><br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;中国商务部长高虎城24日在伯尔尼表示，中瑞自贸协定是一个全面的、高水平的和互利互惠的协定。希望中瑞双方抓紧工作，尽快完成各自国内程序，使协议尽早实施。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;高虎城表示，中瑞自贸协定可以说是近些年中国对外达成的水平最高、最为全面的自贸协定之一。协定的零关税比例很高，瑞方将对中方99.7%的出口立即实施零关税，中方将对瑞方84.2%的出口最终实施零关税；如果加上部分降税的产品，瑞士参与降税的产品比例是99.99%，中方是96.5%。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;高虎城指出，中瑞之间的产业结构和贸易结构互补性强，双方合作潜力大，自贸协定的达成，将进一步深化双方经贸合作，推动双方经贸关系迈上新台阶。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;分析称，中瑞经贸互补性大于竞争性：中国拥有巨大的市场需求和发展潜力，中国的一些制造业也极具竞争力；瑞士是欧洲发达国家，拥有雄厚的科技实力和产业优势，在精细化工、钟表制造和精密仪表等领域拥有较大优势。较强的经济互补性使中瑞经贸关系长期保持健康平稳发展，双边贸易额从建交之初的680万美元发展到2011年创纪录的308亿美元。2012年，面对欧债危机持续发酵和世界经济动荡的不利因素，中瑞双边贸易额依然达到263亿美元。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>有助于我国开拓欧盟市场</b><br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;正如李克强总理24日在瑞士金融界人士午餐会上演讲时所说，中瑞自贸区建设的意义不仅在于中瑞两国，这是中国同欧洲大陆国家的第一个自贸区，是中国同世界经济20强国家的第一个自贸区，对深化中瑞合作是重大利好，对发展中欧关系是重大利好，对世界各国也会产生重要的示范和引领作用。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;中瑞自贸协定将为中欧贸易开启新的机会，并为中国与欧盟建立更加紧密的经贸关系起到示范作用。瑞士是非欧盟国家，但与欧盟同属欧洲经济区，相互之间签署了大约120个合作协议，实行自由贸易。虽然基于原产地限制，中瑞建立自贸区并不意味着中国产品可以用瑞士作跳板自由进入欧盟市场，但这为从中国进口半成品到瑞士加工后再销往欧盟提供了可能，从而在一定程度上拓展了欧盟市场。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;此外，瑞士在吸引外来投资方面还有政治稳定、商业氛围友好、劳动力市场灵活、基础设施良好、科研机构发达等优势。瑞士一直自我定位为中国企业进入欧洲的门户，希望凭借这一优势吸引越来越多的中国企业落户瑞士。据了解，目前已经有65家左右的中国企业在瑞士设立了分支机构。两国缔结自贸协定无疑会使瑞士的这一优势更加明显。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>爱因斯坦博物馆</b><br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;位于瑞士伯尔尼历史博物馆二层的爱因斯坦博物馆，2007年2月1日正式开馆。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;爱因斯坦博物馆是为纪念爱因斯坦相对论提出100周年而创设。2005年是世界物理年。为纪念1905年爱因斯坦在伯尔尼取得的决定性突破，2005年伯尔尼举办了《相对论100年》临时展览。由于当年参观者众多，历史博物馆决定把临时的展览精华办成常设展馆。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;爱因斯坦博物馆共展出200多件实物、书写和影印文书，回顾爱因斯坦的一生、再现爱因斯坦时代的历史。展览依爱因斯坦成长过程展开，从父亲的作坊、学生时代、结婚生子，最后到他对世界的影响。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;展品中既有爱因斯坦的手表、使用过的物品，又有当时的打字机、电话、水房甚至小商店等实物。此外，展览运用声光电等现代技术，单人床、晾晒的衣物作为投影屏幕，所有的说明都用德、法、英三种文字，另外还设有七种语言的自助解说录音机。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;徜徉在爱因斯坦当年用过的这些物品之间，让人感觉仿佛穿越了历史的时空，与这位科学巨匠进行了一次无声的对话。<br/>");
			news4.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news4);
			newsItemDao.saveNewsSearchRstItem(news4);
			news.add(news4);

			NewsSearchRstItem news5 = new NewsSearchRstItem();
			news5.set_id("000000000000000000000005");
			news5.setProgram_wiki_keys(Arrays.asList("中国", "瑞士", "贸易"));
			news5.setTitle("中瑞自贸协定意味着什么？");
			news5.setLink("http://bjyouth.ynet.com/3.1/1305/26/8033983.html");
			news5.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("中瑞签署结束自贸协定谈判谅解备忘录 离协定签署和批准实施仅一步之遥。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;5月24日，李克强总理在瑞士伯尔尼与瑞士联邦主席毛雷尔举行会谈，双方签署了结束中瑞自贸协定谈判的谅解备忘录，并宣布建立金融对话机制，这标志着双方自2010年启动的自贸区谈判基本尘埃落定，离协定签署和批准实施仅一步之遥。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;李克强当天在瑞士金融界人士午餐会上演讲时指出，中瑞自贸协定成果丰富，是一个高水平、内容广泛的协定。中瑞自贸区建设的意义不仅在于中瑞两国，这是中国同欧洲大陆国家的第一个自贸区，是中国同世界经济20强国家的第一个自贸区，对深化中瑞合作是重大利好，对发展中欧关系是重大利好，对世界各国也会产生重要的示范和引领作用。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;三年谈判：从风雪弥漫到开花结果<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;三年多前的2010年1月26日，瑞士伯尔尼。时任国务院副总理李克强与瑞士联邦主席洛伊特哈德举行会谈。双方一致表示，将于当年2月举行第一次有关启动中瑞自贸区联合可行性研究的会议，争取尽快取得成果，并在年内正式启动谈判。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;新华社当时的报道描述：“当天的伯尔尼，风雪弥漫，寒风凛冽。但中瑞双方加快推动自贸区可行性研究及谈判进程的消息，受到广泛关注，犹如为寒冬带来阵阵暖意。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;三年多后的2013年5月24日，在历经九轮的艰苦谈判后，中瑞自贸谈判最终开花结果。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;自贸协定：将促进双方经济增长<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;中国现代国际关系研究院欧洲所所长张健认为，中瑞签署自贸协定，可以促进双方经济增长，也可以为双方消费者带来更便宜的产品。中国当下处于产业转型的关键时刻，通过跟瑞士高新技术、高端产业的合作，有助于中国的产业转型。而对于瑞士这样的小型经济体来说，中国庞大的市场将提升其产品的竞争力。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;此外，在中国深化金融改革、稳步推进利率市场化改革的大背景下，金融业高度发达的瑞士将为探索中的中国金融改革带来借鉴。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;中瑞样板 ：释放自贸区谈判路线图信号<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;张健等专家认为，中瑞自贸区的即将建立，对于正在谈判的其他国家，包括正在研究签订自贸区的国家来说，推动作用明显。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;据悉，由于不承认中国的完全市场经济地位，欧盟、美国等多数发达国家目前尚未和中国签订自贸协定。就在一个多月前，中国和冰岛签订自贸协定，这是中国首次和欧洲国家签订自贸协议。如果能和经济地位更加重要的瑞士签订自贸协定，无疑又将前进一步。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;舆论据此认为，可以通过这种“由小到大”、“由易到难”的路线图，树立成功样板，逐步建立更多的自贸区。虽然瑞士不是欧盟国家，但瑞士与欧盟已签署了双边自贸协定。中国与瑞士达成自贸协定，为中国与欧盟展开双边自贸谈判提供了经验和可能性。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;文/本报记者　孙昌銮<br/>");
			news5.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news5);
			newsItemDao.saveNewsSearchRstItem(news5);
			news.add(news5);

			NewsSearchRstItem news6 = new NewsSearchRstItem();
			news6.set_id("000000000000000000000006");
			news6.setProgram_wiki_keys(Arrays.asList("爱因斯坦", "瑞士"));
			news6.setTitle("瑞士人怎么看爱因斯坦");
			news6.setLink("http://www.stdaily.com/kjrb/content/2010-06/09/content_196470.htm");
			news6.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("“爱因斯坦是瑞士人？”不少人一听到这句话的第一反应就是吃惊：只知道爱因斯坦是出生在德国，后来加入了美国国籍。但是瑞士伯尔尼历史博物馆馆长麦索力博士告诉中国记者：爱因斯坦终身保留瑞士国籍（瑞士允许国民具有双重国籍）。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;许多中国人都知道，瑞士的手表享誉世界，也知道瑞士的军刀全球闻名。其实瑞士人引以自豪的还有他们曾经的公民——伟大的物理学家阿尔伯特·爱因斯坦。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;2005年，是爱因斯坦发表相对论100周年。为了纪念这一人类科学发展史的重大历史事件，伯尔尼历史博物馆特地举办了“爱因斯坦展”。因为100年前，爱因斯坦发表相对论时正是居住在伯尔尼，是专利局的一名小职员。未曾想到，爱因斯坦展吸引了大量的瑞士国民前来参观。仅有770万人口的瑞士国民中，就有35万人走进伯尔尼历史博物馆，走近爱因斯坦。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;在瑞士人眼中，爱因斯坦就是一个普通人，是一个在物理学上做出过伟大贡献的普通人。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;鲁道夫·奥特是研究爱因斯坦的专家。这位已经退休的苏黎世理工大学物理学教授现在的职务是“爱因斯坦研究会会长”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;“1896—1900年，爱因斯坦在苏黎世理工大学就读。”69岁的奥特向中国记者讲述了爱因斯坦的故事。爱因斯坦在上大学时十分喜欢理论物理，但觉得实验物理没有什么意思。“他自学了许多现代物理的课程，这影响了其他课程的学习，成绩不够好，他的老师一度想把他‘踢出去’。老师并没有注意到这个学生已经知道了那么多的现代物理知识。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;像许多大学生一样，爱因斯坦在上大学时也经常逃课，比如数学课，这对他以后的物理学研究不利。爱因斯坦于1905年提出了相对论理论（狭义相对论）。随着进一步的研究，他又有了广义相对论的想法。据奥特教授介绍，爱因斯坦因为数学有点不够用，因而只能与数学特别好的朋友一起研究。“当然，爱因斯坦很后悔他在苏黎世理工大学期间没有好好学数学。”他只得“恶补”数学。后来，爱因斯坦终于正式发表了广义相对论的论文。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;爱因斯坦也逃课？瑞士人介绍他们的杰出科学家时并不避讳这一点。他们不想神化爱因斯坦。他们不像中国宣传自己科学家的成长时，总喜欢塑造“高大全”式的人物。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;爱因斯坦的确有过逃课的历史，但他并非是个不爱学习和研究的人。他逃课不是为了休闲娱乐，而主要是钻研课本上没有或还未讲到的物理学前沿知识。实际上，他的逃课，仅仅是他偏科的一种表现。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;5月30日，爱因斯坦展在中国北京开幕。京城一家媒体对此作报道时用了“爱因斯坦大学物理得过1分”作题目。殊不知，爱因斯坦考试的确得过1分，但有两点该文作者未说明：一是这1分为6分制的1分；二是爱因斯坦并非大学物理得1分，而是实验物理得过1分。我们中国人喜欢从“考试得1分到诺贝尔奖得主”的角度把爱因斯坦理解为“天才”，而瑞士人只是把爱因斯坦这个“天才”看成普通人。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;走进伯尔尼历史博物馆，来到二楼爱因斯坦展的大厅。首先映入眼帘的是一幅巨大的照片：爱因斯坦与几位科学家的合影。除了爱因斯坦以外，其他几位均西装革履，系着领带，只有爱因斯坦独树一帜：穿的大衣不系扣，里面是一件便服，也不打领带，面带微笑地看着每一位前来参观的人。照片旁边的文字说明上写着：爱因斯坦并不注重外表。博物馆的讲解员告诉中国记者，这是爱因斯坦的典型风格——与别人不一样。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;笔者在参观过程中，一直在寻找爱因斯坦究竟在哪些地方与别人不一样。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1885年—1894年，爱因斯坦在慕尼黑上学，全班52名同学一起照了一张合影。讲解员让中国记者找一找哪一位是小爱因斯坦。记者们似乎都看不出哪张小脸与后来的大物理学家相像。讲解员提醒：就是里面那个与众不同的孩子。51个孩子都严肃地注视着相机，只有一个孩子冲着镜头微笑；别的孩子都衣冠楚楚，只有那个微笑的孩子大衣只系了上边两粒纽扣，下边空敞着。这个孩子就是小爱因斯坦。这张合影与前述那张爱因斯坦巨照相似。或许，自然就是这个相对论提出者一生的一贯风格。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;上学、工作、教书、恋爱、结婚、育子、再婚、科研、获奖……爱因斯坦在哪里与别人不一样呢？是指他提出相对论理论？抑或是指他获得诺贝尔奖？<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;没有找到爱因斯坦“与别人的不一样”，相反笔者在瑞士却了解了不少爱因斯坦和许多人大致一样的地方。在爱因斯坦故居，爱因斯坦博物馆馆长奥特拿着一张1901年的报纸复印件告诉中国记者，当年爱因斯坦没有工作。他在伯尔尼当地的报纸上刊登广告找工作，希望当家庭教师挣点钱——爱因斯坦大学毕业后也曾找不到工作。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1900年，爱因斯坦大学毕业时的理想是留在学校里教物理，但没有成功。8年后，他终于获准在伯尔尼大学教物理了。但很可惜，没有几个学生喜欢听他的课。伯尔尼历史档案馆馆长皮特博士特别向中国记者展示了当年爱因斯坦的“档案”。1908年7月6日的一份爱因斯坦上课的记录表明，在他讲述“热量分子理论”课程时，只有3个学生听他的课。另一天的记录是4个学生听了他的讲课。后来校方不得不取消了他的课程——爱因斯坦的教学生涯也曾“走麦城”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;参观爱因斯坦展览，走进爱因斯坦故居，探访爱因斯坦曾求学及任教的苏黎世理工大学，一件件小事，一个个故事，笔者慢慢理解了在瑞士人心中爱因斯坦“与别人不一样”的真实含义：爱因斯坦与那些或不苟言笑或迂腐呆板的科学家们不一样，但他却与最普通的百姓有着太多的相同的人生经历。他是个普通人。正像伯尔尼历史博物馆馆长麦索力博士所说：爱因斯坦就像你的邻居一样亲切。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;爱因斯坦是世界级的大物理学家。瑞士人以他为自豪，但是瑞士人并没有把他“神化”。在向中国记者介绍爱因斯坦时，他的找不到工作、他的听课人数太少，甚至他的某些狡黠都没有被刻意隐瞒。我们看到了一个真实的爱因斯坦。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;在瑞士参观探访之际，笔者心中总有一个挥之不去的疑问：为什么在中国的媒介上所见到的中国科学家们多是“神”或“完人”？要么是科学家本人成了科学的代言人、代名词，要么是科学家在从事科学研究的过程中似乎也完成了道德及心灵的“羽化”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;那么，透过瑞士人看待科学家的态度，我们是否有些思考呢？<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;今年是中华人民共和国和瑞士联邦建交60周年。瑞士驻华使馆和瑞士伯尔尼历史博物馆在北京特别举办了“阿尔伯特·爱因斯坦（1879—1955）”展览，以庆祝两国建交60周年。展览共展出了200多件展品，其中包括爱因斯坦生前使用过或珍藏的物品，介绍爱因斯坦工作、生活和历史背景的影视资料，以及一些理解爱因斯坦科学观念的互动展品。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;为了让中国人更全面地了解爱因斯坦，了解瑞士，日前，瑞士国家形象委员会邀请中国记者到瑞士参观访问，其中的一项重要内容就是了解爱因斯坦在瑞士的历史。<br/>");
			news6.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news6);
			newsItemDao.saveNewsSearchRstItem(news6);
			news.add(news6);

			try {
				Serializer.writeObject(news, newsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return news;
	}

	@SuppressWarnings("unchecked")
	private List<NewsSearchRstItem> getDemoNews_ad() {
		List<NewsSearchRstItem> news = new ArrayList<NewsSearchRstItem>();
		File newsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_NEWS_AD);
		if (newsFile.exists() && newsFile.isFile()) {
			try {
				news = (List<NewsSearchRstItem>) Serializer.readObject(newsFile);
			} catch (Exception e) {
				news = new ArrayList<NewsSearchRstItem>();
				logger.error("", e);
			}
		} else {
			NewsSearchRstItem news1 = new NewsSearchRstItem();
			news1.set_id("000000000000000000000011");
			news1.setProgram_wiki_keys(Arrays.asList("美宝莲"));
			news1.setTitle("美宝莲手机版官网全新上线，潮妆资讯一手掌控!");
			news1.setLink("http://beauty.pclady.com.cn/fashion/1305/971161.html");
			news1.setUpdateTime(new Date());
			StringBuffer des = new StringBuffer();
			des.append("想要了解你喜爱的潮妆品牌需要几个步骤?打开电脑、打开浏览器、查找官网、点击进入,可能当中还需要开机密码……这实在麻烦了点，不是吗?美宝莲4月上线全新手机版官网，现在开始，只要用手机搜索“美宝莲”或在手机浏览器输入m.maybellinechina.com，你随时随地都能快捷方便的了解美宝莲详情。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;找产品，找专柜，想了解更多明星产品资讯，自主搜索轻松帮你达成!还可一键进入潮妆学院APP下载，海量美妆教程,真人手把手示范，和美宝莲一起无妆不潮!更多产品还有网上商城等你随心淘哦!<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;你还可以通过手机版官网关注美宝莲新浪微博!从此潮妆资讯一手掌控!<br/>");
			news1.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news1);
			newsItemDao.saveNewsSearchRstItem(news1);
			news.add(news1);

			NewsSearchRstItem news2 = new NewsSearchRstItem();
			news2.set_id("000000000000000000000012");
			news2.setProgram_wiki_keys(Arrays.asList("美宝莲"));
			news2.setTitle("睫毛膏诞生100年全能之作 美宝莲纽约飞箭睫毛膏上市");
			news2.setLink("http://fashion.ifeng.com/beauty/detail_2013_04/25/24646016_0.shtml");
			news2.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("导语：许多人或许知道世界上第一支睫毛膏诞生于美宝莲，却不知它的诞生源于一段美丽的爱情故事：<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;时光回溯至100年前，1913年，托马斯*威廉姆斯（Thomas Williams）是芝加哥一位年轻的化学师，他的妹妹美宝*威廉姆斯（Mabel Williams）爱上了一位男士切特，然而当时的切特却心有所属。为了帮助心爱的妹妹美宝（Mabel ）赢得心上人，托马斯发明了一种以碳粉和凡士林搭配成的美睫“魔棒”，令美宝拥有了撩人的双眼，次年和心上人终成眷属。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;威廉姆斯由此触发灵感，开始致力于彩妆事业。而美宝莲品牌的得名也是源自妹妹名字“Mabel” 和凡士林的拼写组合，借以向他的妹妹美宝和她最心爱的美容用品致敬。威廉姆斯也成为日后享誉国际的美宝莲彩妆王国的创始人。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;自1913年首支美睫魔棒的诞生至今，100年间，美宝莲睫毛膏在全世界女性美的演绎中承担着举足轻重的作用，为不同时代的女性精神与女性美代言，不断引领美容潮流，推动着美容产业的发展，呈现出一幅幅兼具时代感且瑰丽动人的美丽画卷。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;女性对双眸的追求，也是对自我，自由，自信的追求，让我们一起掀开这些经典画卷，重温睫毛膏的百年征程。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1917年，全球首款简装饼状睫毛膏诞生，掀起了“魅惑双眸，让双眸闪耀动人光彩”的全新美容理念”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;20世纪20～30年代，饼状睫毛膏风靡全美，这支当时只售10美分的美睫鼻祖，如今已成为收藏家的爱物。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;20世纪40～50年代，明眸美睫是当时女性们追逐的最时尚的潮流，四十年代被好莱坞誉为世界最美的女人海蒂*拉玛（Hedy Lamarr）曾感谢美宝莲为她带来最闪耀的明眸；一些大红大紫的影视明星如琼*克劳馥( Joan Crawford)、丽塔*海华丝(Rita Hayworth)等都纷纷为美宝莲的睫毛膏海报代言出镜。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;20世纪六十年代， 全球首款防水型管装睫毛膏Ultra Lash诞生，革命性的创新见证了60年代时尚潮流的到来。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1971年，享誉全球半个多世纪的Great Lash问世，从充满传奇色彩的昨日走来的Great Lash至今仍是全球市场上排行第一的热销睫毛膏，保持着每1.5秒一支的销售速度。它的制胜秘方得到严格保密，其时尚魅力有口皆碑。该款睫毛膏是化妆师的必备法宝，在时装发布会的后台随处可见。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1990年，由一支睫毛膏起家的美宝莲品牌，已经拥有涵盖眼，脸，唇，甲全方位产品线的全球最大彩妆王国。美宝莲“美来自内心，美来自美宝莲”成为享誉全球的口号，致力于让每一位女性展现自我魅力，绽放于身俱来的美。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;至今，美宝莲在中国拥有11支不同的睫毛膏配方，集高科技与流行时尚于一身。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;今天，时隔睫毛膏的浪漫起点已有100年，当全球女性对美的追求从100年前的“魅惑双眸”发展为全方位的自我完善和美丽绽放之际，美宝莲在打造女性完美眼妆的历程上也将迎来全新的篇章。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;2013年，备受瞩目的“飞箭睫毛膏” 全球同步震撼上市，集浓密，干净，快速功效于一身的独特配方和革命性“超音速不结块”刷头，将再次引领全球美睫新潮流！ 这是对创始人威廉姆斯和其妹妹美宝的致敬，更是对追求完美，绽放自我，不断超越精神的最好献礼！<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;产品特性：<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;飞箭睫毛膏可以打造浓密、干净、快速的睫毛妆效，革命性超音速不结块刷头同时拥有硬质核心和柔软刷毛，硬质核心可以更多量地沾取膏体，而柔软刷毛刺头则能均匀包裹睫毛，刷出爆炸性浓密，根根分明的妆效；柔滑者哩状膏体令上妆流畅顺滑，火箭般立现完美妆效，保持全天候不晕染，温水即可卸。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;产品功效：<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;快速：革命性超音速刷头， 火箭般速现纤长浓密妆效；干净不结块；全天候不晕染；温水即卸<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;价格/毫升：99元/ 10 ml<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;上市时间：2013年2月<br/>");
			news2.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news2);
			newsItemDao.saveNewsSearchRstItem(news2);
			news.add(news2);

			NewsSearchRstItem news3 = new NewsSearchRstItem();
			news3.set_id("000000000000000000000013");
			news3.setProgram_wiki_keys(Arrays.asList("美宝莲"));
			news3.setTitle("美宝莲纽约携手上海时装周 掀起新潮流");
			news3.setLink("http://beauty.pclady.com.cn/96/966874.html");
			news3.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("上海时装周优秀本土设计师精彩秀场及后台妆容<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;4月11日，上海时尚地标新天地星光璀璨，上海时装周迎来中国本土独立设计师之夜。吉承，邱昊，这些最杰出的中国设计力量代表的2013秋冬作品先后登上时装周舞台，影后巩俐等众多国内一线明星和时尚界人士纷纷前来成为秀场头排客，为中国设计师加油助威。让我们一起来领略崛起中的中国时尚力量的经典代表和美宝莲纽约带来的潮流妆容。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;妆容解析： 出水芙蓉是本次吉承秀场妆容的灵感来源，运用薄透的底妆和橘色唇妆突出女性柔润清透的特质。运用BB霜打造水润肌肤，橘色绝色唇膏妆点唇部。眉宇之间添加上的闪片点亮了整个妆容，晶亮，活力又不失秀气。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;吉承，植根于上海的年轻本土设计师，她的设计往往颠覆中国传统风格，又将中国元素与优雅, 嬉皮混合在一起，创造出独特的风格。她曾求学于意大利米兰著名的“Marangoni”时装设计学院，先后担任“ Basic Krizia”和“Missoni Sports两大品牌在香港的设计师，之后作为意大利女装品牌“D’A”的视觉艺术总监。她创立的个人品牌La Vie将中国元素与西式剪裁相揉合，致力于表达独特的个人气质，为全球女性提供了高品质的审美标准与无限自由的创意。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;妆容解析：简洁，清透中性风是本次秋昊秀场妆容的特色所在，硬朗的粗眉和裸透的底妆是妆容的重点。用飞箭睫毛膏刷起眉头的每根眉毛，塑造眉型，再用BB霜涂抹于全脸打造通透光泽感的肤质。帅气的同时仍然保留了女性的柔媚。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;邱昊，华裔新锐服装设计师，毕业于英国伦敦中央圣马丁艺术学院。2007年在伦敦注册了设计师品牌Qiu Hao，2008年赢得澳大利亚美丽诺奖毛标志大奖，与卡尔大帝，YSL和Giorgio Armani等设计巨匠一起，入主羊毛标志大奖名人堂。邱昊用他那不断发人深省及看似随意的设计风格为自己赢得了中国时装界“好男孩”的美誉。他有一双翻云覆雨手，他有大胆的创意，他的恢弘一瞥便是一个经典之作。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;妆容解析：本次妆容以“行云”为主题，突出女性独有的轻盈，灵动气息。运用精纯矿物粉底打造白皙立体底妆，涂抹少量遮暇弱化眉毛的部分。以宝蓓护唇膏打底之后用角色唇膏橙色和浆果色涂抹于唇部，突出渐变唇色效果，完美演绎现代女孩的时尚与俏皮感。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;韩璐璐，曾游学于加拿大及意大利，在米兰顶级设计学院Instituto Marango获得硕士学位。曾参与多家高端品牌的设计项目，其中更以自由设计师身份加入世界顶级礼服桂由美Yumi Katsura的设计工作，成为其合作的首位华人设计师。擅长将典雅的东方气质与利落的西式剪裁相融和，强调女性精致，优雅的特质。作为“新式轻礼服”创导者，她将礼服的精致和成衣的实穿完美结合，提倡一衣多变及无季节性的穿搭理念，力求在设计感与实穿性上找到最佳契合点。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;妆容解析：本次妆容灵感来源于“月亮”，清澈，简洁是妆容的基调。运用精纯矿物粉底液打造立体无暇的肌肤，选择睛彩造型眼线膏勾勒下眼线，打造完美眼行，最后选择裸色唇膏轻抹于唇部尽显低调大气的特质!<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;李鸿雁，新锐设计师的领衔人物，新兴时尚领域的领军人之一。2006年被时装巨头H&M杂志誉为中国最有潜力的年轻设计师和中国时尚感念“街头装女王”称号。HELEN LEE是以设计师李鸿雁命名的高端成衣品牌，定位高端是因为用料考究，细节精致。而李鸿雁却不是一个循规蹈矩的人，她致力于有趣的设计，以混搭的手法运用面料，以生活中的点滴贯穿设计细节，并揉入国内外各个时期不同的服装风格，加以整合并再创造，使整个设计富于历史影像又具个人思想。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;妆容解析：本次妆容灵感源自部落刺青图腾，模特妆容宛如一位部落中的女战士，桀骜，纯粹，打破常规。运用精纯矿物质粉底液打造健康自然肤色，使用钻石眼影勾勒眼部自然轮廓，眼线膏勾勒鼻翼以下线条，最后用宝蓓护唇膏在唇部打底。野性，原始，充满个性!<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;王海震，毕业于中央圣马丁艺术与设计学院，曾先后在Max Mara和All Saints时装公司工作，2010年在英国创立自己的个人时装品牌“Haizhen Wang”。2013年伦敦春夏时装周上，王海震发布秀作为压轴秀在伦敦时装周主会场Somerset house举行，并在当天举行的Fashion Fringe时装大赛中拔得头筹，获得大奖。他的作品剪裁立体，线条感强，风格简洁硬朗，同时融入亚洲元素，以修饰线条展现女性美，并借由设计表达出对生活的感受。<br/>");
			news3.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news3);
			newsItemDao.saveNewsSearchRstItem(news3);
			news.add(news3);

			NewsSearchRstItem news4 = new NewsSearchRstItem();
			news4.set_id("000000000000000000000014");
			news4.setProgram_wiki_keys(Arrays.asList("美宝莲"));
			news4.setTitle("美宝莲眼线笔的使用方法介绍");
			news4.setLink("http://news.xkb.com.cn/life/chuanyidb/145.html");
			news4.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("作为一个国际知名的化妆品品牌，美宝莲在中国可以说得上是非常的受欢迎的，很多人在购买化妆品的时候都会选择美宝莲，就是因为美宝莲化妆品的品质效果都是很好的。美宝莲眼线笔是很多爱美女性必备的，而且价格并不贵，只需要39元，那么美宝莲眼线笔怎么用呢？下面小编就来向大家介绍一下美宝莲眼线笔的使用方法。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲眼线笔的使用方法第一步：一只手提拉上眼皮，另一只手从内眼角向外眼角分四五段，紧贴着睫毛根部反复描画上眼线。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲眼线笔的使用方法第二步：完成后，可以看到眼睛两端的眼线比较细，中央部分眼线较粗一些。有增大黑眼球宽度的效果。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲眼线笔的使用方法第三步：眼睛向上看，从外眼向内眼球中央画下眼线，同样分三段画。在外眼角的地方要与上眼线连接自然。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲眼线笔的使用方法第四步：闭眼后，用棉签来回晕染上眼线，一般要擦拭4-5下，尽量在眼中央晕染的宽一些。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲眼线笔的使用方法第五步：晕染下眼线很关键。向前看，用面前来回擦拭4-5下，稍微向眼线外晕染出深浅感。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;相信在看完上面的介绍之后，大家都已经知道了美宝莲眼线笔的使用方法。我们能够看出美宝莲眼线笔的使用方法是很简单的，当然了一般来说只要是经常化妆的朋友都是知道怎么用的，不过对于新手而言，就可以参考上面的方法来画眼线了，效果还是很不错的。<br/>");
			news4.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news4);
			newsItemDao.saveNewsSearchRstItem(news4);
			news.add(news4);

			NewsSearchRstItem news5 = new NewsSearchRstItem();
			news5.set_id("000000000000000000000015");
			news5.setProgram_wiki_keys(Arrays.asList("美宝莲"));
			news5.setTitle("美宝莲睫毛膏标价89却卖99");
			news5.setLink("http://news.e23.cn/content/2013-04-02/2013040200214.html");
			news5.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("摘 要：　市民刘女士向女报反映，她在屈臣氏美宝莲专柜购买一支巨密睫毛膏，标价89元，售货员却告知标价有错，是99元。刘女士交钱后去另一家屈臣氏专柜，售货员却告知此款睫毛膏全国统一售价89元。对此，美宝莲公司鲁西市场负责人田女士称，美容顾问是临时招的，区别不开三种产品。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;市民刘女士向女报反映，她在屈臣氏美宝莲专柜购买一支巨密睫毛膏，标价89元，售货员却告知标价有错，是99元。刘女士交钱后去另一家屈臣氏专柜，售货员却告知此款睫毛膏全国统一售价89元。对此，美宝莲公司鲁西市场负责人田女士称，美容顾问是临时招的，区别不开三种产品。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;30日下午，刘女士在屈臣氏美宝莲专柜购买了一支巨密睫毛膏。“巨密睫毛膏标价是89元，售货员说是99元。”刘女士说，当时她和朋友都质疑，货架上明明标着89元，为啥说99元?刘女士询问售货员，售货员说，柜台上的标码牌是刚换的，标错了，其实是99元。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;30日晚，刘女士来到另一家屈臣氏美宝莲专柜，看到巨密睫毛膏的标价是89元。“这家售货员说巨密睫毛膏89元，我把新买的产品拿来给她看，她很确定说89元，并说这是全国统一零售价。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;刘女士再次来到消费过的屈臣氏美宝莲专柜，“我又问她这支睫毛膏到底多少钱，对方还说99元。”刘女士告诉对方，已在另一家屈臣氏美宝莲专柜询问，被告知巨密睫毛膏是89元。“她这才说打电话问问，问后说是89元并退了10元。”“10元钱不多，还不够我开车来回的油钱。但这么有名的牌子怎么会出现这种事情?”刘女士说，“如果我不回去找，这名售货员可能继续以99元的价格卖给其他消费者。”该商场屈臣氏店长刘女士称，她已跟顾客联系处理此事。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲公司鲁西市场相关负责人田女士称，顾客买的睫毛膏有三个系列，有巨密睫毛膏、巨密美睫梳、巨密猫眼三个产品。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;她说：“这三个产品的价格是不一样的，屈臣氏美宝莲专柜临时招了一名美容顾问上班，这三种产品她区别不开，都在架子上，把产品当成99元的卖给顾客了。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;田女士说，该美容顾问并未把此事反馈给主管。“这也给我们一个教训，对上岗人员的培训以后会注意。”<br/>");
			news5.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news5);
			newsItemDao.saveNewsSearchRstItem(news5);
			news.add(news5);

			NewsSearchRstItem news6 = new NewsSearchRstItem();
			news6.set_id("000000000000000000000016");
			news6.setProgram_wiki_keys(Arrays.asList("美宝莲"));
			news6.setTitle("美宝莲BB霜真伪判断方法介绍");
			news6.setLink("http://news.xkb.com.cn/life/chuanyidb/144.html");
			news6.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("估计很多人都想要知道美宝莲BB霜的真伪判断方法，因为美宝莲BB霜可以说是非常受人欢迎的一个产品，因此市场上就难免会出现一些假冒产品危害消费者的权益，甚至会危害消费者的健康，所以鉴别方法就变得非常的重要，那么美宝莲bb霜真伪判断方法是什么呢？下面小编就来向大家介绍一下美宝莲BB霜的鉴别方法。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲BB霜的鉴别方法1、看外包装：正品美宝莲BB霜外包装处的蓝色圆形区域的边缘为晕染效果，而假冒产品此处没有晕染效果。再来看包装背面，正品底色的蓝更鲜艳明亮，印刷细腻规整，而仿货的底色比较暗沉，有的存在掉色现象。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲BB霜的鉴别方法2、看BB霜瓶身：正品美宝莲BB霜瓶身正面的圆形区域底色并非纯黑色，微微有些棕红。而假货此处的底色为纯黑色，没有任何其他颜色。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲BB霜的鉴别方法3、看瓶口：正品美宝莲BB霜的瓶口内颜色与瓶身颜色同为蓝色，而仿品瓶口内则为透明状。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲BB霜的鉴别方法4、看瓶盖：正品的瓶盖的锁扣为凹陷的内嵌式，而仿品的锁扣是简单的外凸式，这一点很明显与正品不同。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;美宝莲BB霜的鉴别方法5、看BB霜膏体：正品美宝莲BB霜膏体颜色与肌肤本色接近，膏体本身细腻略有发亮。而仿品膏体的颜色则明显偏褐色，涂抹后皮肤会显得暗沉，效果可想而知。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;相信在看完小编对美宝莲BB霜真伪判断方法介绍之后，大家都已经知道了怎么鉴别美宝莲BB霜的真假，因此在我们选择购买美宝莲BB霜的时候就可以参考着上面的方法来进行判断，另外小编提示大家购买美宝莲产品的时候一定要到专柜进行购买，那里的产品比较有保障。<br/>");
			news6.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news6);
			newsItemDao.saveNewsSearchRstItem(news6);
			news.add(news6);

			try {
				Serializer.writeObject(news, newsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return news;
	}

	@SuppressWarnings("unchecked")
	private List<NewsSearchRstItem> getDemoNews_fcwr() {
		List<NewsSearchRstItem> news = new ArrayList<NewsSearchRstItem>();
		File newsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_NEWS_FCWR);
		if (newsFile.exists() && newsFile.isFile()) {
			try {
				news = (List<NewsSearchRstItem>) Serializer.readObject(newsFile);
			} catch (Exception e) {
				news = new ArrayList<NewsSearchRstItem>();
				logger.error("", e);
			}
		} else {
			NewsSearchRstItem news1 = new NewsSearchRstItem();
			news1.set_id("000000000000000000000021");
			news1.setProgram_wiki_keys(Arrays.asList("非诚勿扰"));
			news1.setTitle("非诚勿扰51岁女嘉宾气质迷人 成功牵手小17岁帅哥");
			news1.setLink("http://weifang.dzwww.com/yl/201306/t20130603_8452541.htm");
			news1.setUpdateTime(new Date());
			StringBuffer des = new StringBuffer();
			des.append("[提要]在《非诚勿扰》的舞台上再现惊人一幕，在舞台上一位51岁的女嘉宾吴愤奋同一位17岁的小帅哥牵手成功，女嘉宾用坦诚打动了小帅哥，不求得到关怀，只求能一起走到最后，成熟的爱情观获得观众认可。据悉，51岁女嘉宾吴愤奋有一个大学已经毕业的女儿，而34岁的男嘉宾伍逸则是一位有着自己公司的企业家，在新西兰生活了12年。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;在《非诚勿扰》的舞台上再现惊人一幕，在舞台上一位51岁的女嘉宾吴愤奋同一位17岁的小帅哥牵手成功，女嘉宾用坦诚打动了小帅哥，不求得到关怀，只求能一起走到最后，成熟的爱情观获得观众认可。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;据悉，51岁女嘉宾吴愤奋有一个大学已经毕业的女儿，而34岁的男嘉宾伍逸则是一位有着自己公司的企业家，在新西兰生活了12年。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;由于太过惊讶，惹得当时三位现场老师激动地语无伦次、几度凝噎，不少女嘉宾更是感动得当场飙泪。<br/>");
			news1.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news1);
			newsItemDao.saveNewsSearchRstItem(news1);
			news.add(news1);

			NewsSearchRstItem news2 = new NewsSearchRstItem();
			news2.set_id("000000000000000000000022");
			news2.setProgram_wiki_keys(Arrays.asList("非诚勿扰"));
			news2.setTitle("盘点非诚勿扰奇葩女嘉宾：杀夫 曝艳照 骗婚");
			news2.setLink("http://finance.china.com.cn/roll/20130601/1519136.shtml");
			news2.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("《非诚勿扰》女嘉宾王佳两刀砍死丈夫<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1月13日，多位网友在微博上爆料称，《非诚勿扰》前女嘉宾王佳因杀夫被西安警方逮捕。据网友透露，王佳于2012年5月结婚，7月就两刀砍死了熟睡中的老公，还企图制作家暴假象，自砍两刀，但被警方识破。8月她在警方监护下产下一男婴，目前被西安警方拘押中。9月11日，被她砍死的老公遗体在西安三兆殡仪馆火化。 <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;拜金女马诺<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;马诺是一名来自北京的平面模特，因其在江苏卫视《非诚勿扰》中对大胆、犀利的言论而迅速在网络上蹿红，她因为那句“我宁愿坐在宝马车里哭，也不愿意坐在自行车上笑”的金句被网友们称作“拜金女”。 <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;在《非常勿扰》节目中，马诺实在表现突出，经常和男嘉宾爆发“冲突”，造成不少话题，也炒热了该节目。马诺也因此成为了网络热议的主角。 <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;闫凤娇曝艳照门<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;闫凤娇，1989年出生，模特、化妆师，曾获瑞丽第七届封面女孩大赛季军。因在《非诚勿扰》上美丽动人而成为网络红人。其后网络相继流出闫凤娇的不雅照，引起社会广泛争议。闫凤娇随后发出声明，称照片是遭人胁迫拍下的。后在江苏卫视的陪同下向警方报案。 <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;孙雅莉”骗婚门<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;孙雅莉，又名孙莉子，业余演员，江苏卫视《非诚勿扰》的节目嘉宾，因为参加这个节目而在网络上走红；2010年9月鄂皆豪和孙雅莉因《非诚勿扰》相识发展为恋人关系，后以结婚为目的，鄂皆豪应孙雅莉要求赠与其夏普32英寸高清液晶电视一台、宝马318车一辆，并登记在孙雅莉名下；但后来孙雅莉拒绝和鄂皆豪结婚，同时也拒绝返还宝马车，因此2011年5月底，鄂皆豪将孙雅莉告上法庭；2011年11月16日，案件进入实体审理，孙雅莉的律师首次承认宝马车确属鄂皆豪出资购买。 <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;马伊咪——被男嘉宾追求最多女嘉宾<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;马伊咪，女，河南人。中学毕业于郑州市第四十七中学，2007年进入北京现代音乐研修学院学习。因参加江苏卫视《非诚勿扰》而成为网络红人，马伊咪是《非诚勿扰》舞台上早期最久的一位女嘉宾，也是被男嘉宾追求最多的一位女嘉宾。经过20多期的选择，最终牵手李安元。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;俞夏<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;俞夏，女，江苏卫视《非诚勿扰》节目嘉宾，因为参加这个节目时，极有个性的她提问犀利，头脑灵活，而且早前因为又胖又丑被男友抛弃为了“复仇”誓要变瘦变美，而在网络上走红。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;许秀琴<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;许秀琴，女，江苏卫视《非诚勿扰》节目嘉宾，因为参加这个节目而在网络上走红。她的搞笑方式近乎“无厘头”，又不扭捏做作，真实说话、真诚做人的性格让她大受追捧。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;谢佳<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;原为北京交通大学的学生，后来退学，考取北京现代音乐学院，目前就读大二。因其在江苏卫视《非诚勿扰》中说话非常有内涵，打扮中性化，语出惊人加之经纪人炒作而大红网络。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;张奀宁<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;女，原名张恩宁，江苏卫视《非诚勿扰》节目嘉宾，因为参加这个节目，长相漂亮，网友觉得长得水灵与演员胡静、模特兽兽神似，而在网络上走红。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;王茜被曝职业造假<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;《非诚勿扰》15号王茜，自称是小学舞蹈老师，在非诚勿扰上，从5月4号开始到15号，腰身变法，从严密型突然转变成清纯爆乳型，在5月21、22两期非诚勿扰中，王茜都主动发言，就是主动晒出自己的最新清纯打扮，同时把两个白白的F杯爆乳展现给观众，这引起了网友对王茜的极力关注。经网友人搜搜索，王茜居然是惊艳的汽车模特 <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;《非诚》第一外籍美女韩国MM郑孝美<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;面容姣好、姿态甜美、气质知性，江苏卫视《非诚勿扰》韩国女嘉宾郑孝美宛若韩剧中温婉美丽的女主角，言谈举止迅速征服众多观众，网友将她称为“非诚勿扰最美外籍女嘉宾”，“落落大方，媳妇品质十足”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;张蕾<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;非诚勿扰10号女嘉宾张蕾是医疗器械的企业法人，家族企业，是个富家女，因为在非诚勿扰台上很久，而且长相萝莉，被许多人观众关注。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;日本女嘉宾佐藤爱成孟非最爱<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;佐藤爱是日本籍的单亲妈妈。她在台上说的“看中国男人下厨房就觉得可怜”、“不让未来老公做一切家务”。一上台迅速走红，让她的人气值飙升。她的直爽幽默的风格大受追捧，连孟非都忍不住侧目，台下称她是“迄今为止最喜欢的女嘉宾”。台上，孟非遇到每个问题都会询问她的看法，还强调要为他专门“调一批喜欢小孩、不介意已婚的大龄优质男”。孟非表示，33岁的佐藤爱受人追捧合乎情理，她直爽真诚的性格、丰富的人生经历、成熟的婚姻态度，对众多单身男人“很有杀伤力”。网友更是将他评为做老婆的最佳人选，并列出“最爱”佐藤爱的N种理由。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;刘婷婷<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;《非诚勿扰》舞台上的话题女王，即“五朵金花”之一的知性女生刘婷婷，获得了“三好”女的称号，私下里没有浓妆艳抹，长相平凡没有一点特色，穿着也很随意。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;薛璐曝全裸照<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;继闫凤娇之后，江苏卫视非诚勿扰女嘉宾薛璐又流出全套私拍艳照，并在网上疯传。经过查证，当事人竟是江苏卫视非诚勿扰的女嘉宾，薛璐。但在这次薛璐风波的背后，却有网友特别指出，这很有可能是本人或背后的团队精心设计的一场炒作，目的就是象兽兽、闫凤娇一般利用不雅照事件出名。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;梁爽与清华教师曝床上艳照<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;清华大学教师和《非诚勿扰》24号女嘉宾床照引起网友的热议。网友称男主角姜来和梁爽在江苏卫视《非诚勿扰》相识，更有知情者爆料称男主角原是清华大学体育教师，姜来瞒婚上《非诚勿扰》，生活非常不检点。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;谷慧子，江苏卫视《非诚勿扰》节目12号女嘉宾，因为参加这个节目而在网络上走红。谷慧子2011年12月18日被返场男嘉宾杨凯带走。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;徐仔婷<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;徐仔婷，女，江苏卫视《非诚勿扰》节目女嘉宾，虽然站在比较偏远的2号位置，但在镜头一扫而过的瞬间，她清纯淡雅的形象总能让人留下深刻的印象，让她在网络上迅速走红。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;常小娟因长相被封1号神女<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;制造了诸多话题跟捧红了多位网络红人的江苏卫视相亲节目《非诚勿扰》，近期又成功让一位女嘉宾走红，但，这一次真的很不一样。她，没有马诺的性感妖娆，没有马伊咪的气质，没有闫凤娇的清纯，更没有谢佳的中性美，她，就是常小娟，被称为《非诚勿扰》史上最丑女嘉宾的1号“神女”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;一开始网友们是带着怜悯跟爱惜的眼光看待这个女孩的，但是，常小娟一次次娇柔造作的表现终于让大部分网友反胃了，对于这种有如“芙蓉姐姐”出道时的自我炒作，连主持人孟非也有点看不下去。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;吴喻<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;吴瑜是非诚勿扰一代钉子户，她的尖酸刻薄损人利己得意忘形博得关注的个性给人以深刻的印象，全无阴柔之美。广大公众也是在强烈的忍受着她带来的神经紧张。指责之声风起云涌。<br/>");
			news2.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news2);
			newsItemDao.saveNewsSearchRstItem(news2);
			news.add(news2);

			NewsSearchRstItem news3 = new NewsSearchRstItem();
			news3.set_id("000000000000000000000023");
			news3.setProgram_wiki_keys(Arrays.asList("非诚勿扰"));
			news3.setTitle("\"非诚\"男谈牵手大15岁女子：她是我知己和小姐姐");
			news3.setLink("http://www.chinanews.com/yl/2013/06-04/4889173.shtml");
			news3.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("上周日晚江苏卫视《非诚勿扰》51岁的吴愤奋牵手小17岁帅哥一幕，在网络上引发了轩然大波。在错愕惊讶之余，众人也纷纷向两人送上了祝福，同时，在网上也引发了争议，面对种种疑问，男嘉宾伍逸希望大家停止猜测，前日大方回应：“我们没有分开！”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;伍逸：我们没有分开！ <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;这段不可思议的“忘年恋”，人们关注的焦点是男嘉宾伍逸最后牵手比自己大17岁的女嘉宾，是否有炒作自己的嫌疑。对此，伍逸回忆当时的牵手，说道：“芳子(指女嘉宾吴愤奋)的确是节目场上最令我感动的女嘉宾，她欣赏我鼓励我，我牵她是很正确的选择。”而在走下舞台之后，对于两人是否还在联系，这也是大家十分关心的话题，伍逸也希望大家停止猜测，他说：“下了节目，我们没有分开，我喜欢和芳子喝酒聊天，可以一谈几个小时，我觉得，她是一个既优雅高贵又有传奇故事的女人。我们在非诚勿扰收获了，所以彼此都很珍惜我们之间的感情。我希望芳子既是我红颜知己，也是我的小姐姐，在心灵上可以亲近，在智慧上可以帮助我。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;吴愤奋：要找个心灵上互相体贴的人！ <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;而“忘年恋”的另一主角也是大家关注的焦点，有很多人关心这个“史上最大龄女嘉宾”。记者也采访到了吴愤奋，了解了她来《非诚勿扰》的缘由。谈及自己上一段婚姻，芳子没有抱怨、没有忧郁。“这段婚姻虽然给我带来了一些坎坷的经历，但它也让我更清楚的认识到自己，知道自己想要什么。”而有意思的是，“来参加非诚勿扰也是女儿的爸爸提出的建议，我们现在再见面仍然是朋友。”以前因为孩子小怕她不接受，所以就一直没有结婚。“现在女儿也长大了，大学毕业，生活完全可以自理。我想找一个从心灵上可以给予关爱的另一个人共同经营生活，相互体贴。”正因为这样她才来到《非诚勿扰》，寻找那个“注重外表、孝顺、爱女人”的男人。<br/>");
			news3.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news3);
			newsItemDao.saveNewsSearchRstItem(news3);
			news.add(news3);

			NewsSearchRstItem news4 = new NewsSearchRstItem();
			news4.set_id("000000000000000000000024");
			news4.setProgram_wiki_keys(Arrays.asList("非诚勿扰"));
			news4.setTitle("南充女生《非诚勿扰》\"牵手\"美国帅哥 异地恋已搁浅");
			news4.setLink("http://sichuan.scol.com.cn/ncxw/content/2013-06/04/content_5348491.htm");
			news4.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("爱转角<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;“爱转角”是江苏卫视《非诚勿扰》节目中的一个特色，没有成为台上正式女嘉宾的单身女生，可以坐在这里争取与男嘉宾牵手的机会。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;遇到爱<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;当来自美国的24岁帅哥朱可夫遭遇“全场灭灯”后，坐在“爱转角”里的南充22岁女大学生曾燕站起来大胆表白，最终牵手成功。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;“5月中下旬的时候，我接到了《非诚勿扰》栏目组的通知，让我去参加他们的节目。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;曾燕在电话里谈到，大二的时候，她和同学出于好奇，就在一交友网站上注册了会员，填写了交友信息。“没想到都到了大四快毕业的时候，《非诚勿扰》栏目组会给我打电话。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;“当时只是想到要毕业了，去参加《非诚勿扰》，就当旅游一圈。”在6月1日播出的江苏卫视《非诚勿扰》节目中，南充22岁女大学生曾燕成功“牵手”一位来自美国的24岁帅哥。3日，华西城市读本记者电话连线了曾燕，聆听了这名大四女生的“相亲”故事。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;在6月1日播出的《非诚勿扰》中，曾燕身着白色网状上衣和碎花裙，坐在“爱转角”的女嘉宾席上，靓丽的外表十分抢眼。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;身着白色衬衣，个子高大的美国男孩朱可夫一上台，便引来了现场阵阵尖叫。不过，因为各种原因，这个阳光大男孩遭遇全场“灭灯”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;“我觉得这个美国的男孩子还不错，也很符合我的审美要求。”抱着先从朋友做起的心态，曾燕站起来，表示愿意和朱可夫“牵手”。“牵手”成功后，两人私下进行了约会交流，不过两人最终没有走到一起。“我是四川人，想留在四川工作，他已经在北京上班了，也没办法因为我来到四川，“都是年轻人，做不成恋人，做朋友也不错。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;华西城市读本实习记者钱双<br/>");
			news4.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news4);
			newsItemDao.saveNewsSearchRstItem(news4);
			news.add(news4);

			NewsSearchRstItem news5 = new NewsSearchRstItem();
			news5.set_id("000000000000000000000025");
			news5.setProgram_wiki_keys(Arrays.asList("非诚勿扰"));
			news5.setTitle("丁东丽《非诚勿扰》爆灯后反悔 姓名域名被抢注");
			news5.setLink("http://news.ename.cn/yuming_20130604_45166_1.html");
			news5.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("易名中国（eName.cn）6月4日讯，据悉，在江苏卫视《非诚勿扰》最新一期节目中，“爆灯姐”爆灯后突然反悔，令全场陷入尴尬境地。主持人孟非也忍不住“动怒”。丁东丽姓名域名也于昨日被抢注。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;网友发布了一条坐在观众席拍摄的“爆灯姐丁东丽爆灯后反悔“的视频片段，这也让这期原在6月2日播出的《非诚勿扰》节目未播先火。丁东丽也再次引起了大家的关注，其姓名域名dingdongli.com在6月3日被抢注。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;“爆灯姐”丁东丽为一位来自新西兰的男嘉宾爆灯又突然反悔，令全场都陷入了尴尬境地。首度遭遇此种状况的孟非也很为难，忍不住“动怒”，“我希望每个人都能遵守节目规则，另外，不是你爆灯，人家就一定愿意带你走。”网上也是炸开了锅，网友纷纷表示，“像她这种缺乏真诚的爆灯，爆的再多也没意义。”<br/>");
			news5.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news5);
			newsItemDao.saveNewsSearchRstItem(news5);
			news.add(news5);

			NewsSearchRstItem news6 = new NewsSearchRstItem();
			news6.set_id("000000000000000000000026");
			news6.setProgram_wiki_keys(Arrays.asList("非诚勿扰"));
			news6.setTitle("《非诚勿扰》34岁优质男牵手51岁熟女");
			news6.setLink("http://ent.sina.com.cn/v/m/2013-06-04/12383935782.shtml");
			news6.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("在前晚播出的《非诚勿扰》[微博]新西兰专场第二场中，51岁的女嘉宾吴愤奋成功牵手34岁男嘉宾伍逸，让全场惊讶沸腾，也让很多观众质疑他们会不会下台就分手。如今距节目录制已有大半个月，这对年龄相差17岁的男女嘉宾感情发展是否顺利？昨日，重庆晚报记者从江苏卫视[微博]方面了解到，身在新西兰的伍逸通过网络回答了观众的质疑，明确表示他和吴愤奋没有分开，“我很珍惜我们之间的感情，牵手是很正确的选择”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;现场回放<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;事后回应节目组评价<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;34岁优质男拒绝了优质爆灯女<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;51岁的吴愤奋报名身份为私营业主和作家，大学已毕业的女儿怂恿她报名参加《非诚勿扰》相亲。伍逸曾在新西兰打过工，卖过广告，现在拥有三家公司。在节目中，虽然吴愤奋明确表达了对伍逸的欣赏，但爆灯女生李宁宇和心动女生李莉娜都是条件出众的竞争对手，因此，当伍逸说出自己的决定时，全场都震惊并沸腾了。主持嘉宾黄菡非常赞赏吴愤奋的择偶标准“注重形象、孝顺、爱女人”，称“这才是经历过家庭生活的女人给出的答案”。<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;两人牵手后接受采访时，伍逸说：“年龄没什么，我感觉她现在状态非常好，我在20多岁眼里也算老的。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;事后回应<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;我们没有分开，牵她是正确的选择<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;这段忘年恋在收获祝福的同时，也遭到了不少质疑，一些观众怀疑这是否是节目组安排的炒作，想知道他们在走下舞台后是否还在一起。对此，伍逸通过网络回答说：“我是个凡事都往好处想的人，经常会做一些乐观的决定，这一次我也做了。芳子(指女嘉宾吴愤奋)是节目场上最令我感动的女嘉宾，她欣赏我鼓励我，我牵她是很正确的选择。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;至于两人是否还在联系，伍逸称“没有分开”。他说，他喜欢和芳子喝酒聊天，而且一谈就几个小时。“我觉得她是一个既优雅高贵又有传奇故事的女人。我很珍惜我们之间的感情，我们也许比每对年轻男女都会更长久。我希望芳子既是我红颜知己，也是我的小姐姐，在心灵上可以亲近，在智慧上可以帮助我。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;节目组评价<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;他们对年龄的宽容度更高<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;昨日，重庆晚报记者致电了江苏卫视《非诚勿扰》节目宣传张毅，他告诉记者，吴愤奋是节目中最年长的一位女嘉宾。对于她和伍逸跨越年龄的牵手，张毅说：“他俩都在国外生活了很长时间，西方文化中对配偶年龄的宽容度更高一些。而且，他俩都是思想独立、精神成熟的人，我们都相信他们在做决定之前已做好充足的心理准备。”<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;本栏稿件由重庆晚报记者 周裕昶 采写<br/>");
			news6.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news6);
			newsItemDao.saveNewsSearchRstItem(news6);
			news.add(news6);

			try {
				Serializer.writeObject(news, newsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return news;
	}

	@SuppressWarnings("unchecked")
	private List<YihaodianProductBean> getDemoProducts_xinwen() {
		List<YihaodianProductBean> products = new ArrayList<YihaodianProductBean>();
		File productsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_PRODUCT_XINWEN);
		if (productsFile.exists() && productsFile.isFile()) {
			try {
				products = (List<YihaodianProductBean>) Serializer.readObject(productsFile);
			} catch (Exception e) {
				products = new ArrayList<YihaodianProductBean>();
				logger.error("", e);
			}
		} else {
			YihaodianProductBean prod1 = new YihaodianProductBean();
			prod1.setKeyType(Arrays.asList(KeyType.CONTENT));
			prod1.setId("000000000000000000000001");
			prod1.setBrand_name("蒂梵之家");
			prod1.setUpdatetime(new Date());
			prod1.setSearchKey("领带");
			prod1.setPic_url("http://d6.yihaodianimg.com/N00/M05/34/AD/CgQCtlE__9aAPWAgAAEcSJEHV_s70500_450x450.jpg");
			prod1.setProduct_url_m("http://m.yihaodian.com/product/7405825");
			prod1.setTitle("Evanhome/艾梵之家 纳米防水韩版窄领带休闲窄纯色领带L5001均码");
			YihaodianSale_price price = new YihaodianSale_price("广东", 98.0);
			prod1.setSale_price(Arrays.asList(price));
			products.add(prod1);
			yihaodianProductDao.removeYihaodianProductById(prod1.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod1);

			YihaodianProductBean prod2 = new YihaodianProductBean();
			prod2.setId("000000000000000000000002");
			prod2.setBrand_name("海e家");
			prod2.setUpdatetime(new Date());
			prod2.setSearchKey("领带");
			prod2.setPic_url("http://d6.yihaodianimg.com/N03/M07/2F/81/CgQCs1E-wv-APdSsAACBd8rzvAU61500_450x450.jpg");
			prod2.setProduct_url_m("http://m.yihaodian.com/product/7386012");
			prod2.setTitle("海e家 145*6cm 提花领带 男士商务休闲 100%桑蚕丝 浅紫色 E00244浅紫均码");
			prod2.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;高贵的深紫色上镶嵌有浅紫色星状提花，让原本的深紫色面料变成了深浅交替的星的海洋。男士佩戴更有高贵典雅的气质。海e家领带面料采用100%桑蚕丝制作，柔软丝滑的手感，让人穿戴都觉得倍感舒适，这款领带搭配任何衬衫都能显示出男士的优雅风范。高品质高追求    面料采用100%桑蚕丝，标准45°斜角剪裁。真丝光泽柔和，手感柔软，质地细腻。内衬采用纯手工缝制，选用50%羊毛，50%涤纶，羊毛既保持了领带的蓬松和饱满，涤纶又增强了领带的弹性，彻底区别于低端纯涤纶衬领带的粗糙，尽显高贵气息。领带背面衬里，内侧含有“生命线”，这条线具备良好的伸缩及缓冲功能，保持领带弹性，使领带延展自如，同时可防止领带因拉扯而变形。<br/>&nbsp;&nbsp;&nbsp;&nbsp;保养说明<br/>&nbsp;&nbsp;&nbsp;&nbsp;1.使用后请您即刻解开领结，并轻轻从结口解下，避免用力拉扯表布及衬，以免纤维断裂造成永久性皱折。<br/>&nbsp;&nbsp;&nbsp;&nbsp;2.每次戴完结口解开后，请将领带平放或用衣架将领带拦腰垂挂起来，并留意放置处是否平滑以免刮伤领带。<br/>&nbsp;&nbsp;&nbsp;&nbsp;3.开车系上安全带时，勿将领带绑在安全带里面，以免产生皱折。请尽量不要折叠，避免褶皱。如已有褶皱，请将领带拉紧圈在干净的酒瓶上，隔一天褶皱即可消除。<br/>&nbsp;&nbsp;&nbsp;&nbsp; 4.同一条领带戴完一次后，请隔几天后戴，并先将领带放置于潮湿的地方或喷少许水，使其皱折处恢复原状后，再收至干燥处平放或吊立。<br/>&nbsp;&nbsp;&nbsp;&nbsp;5.领带不能放置在阳光下暴晒，否则会造成丝质泛黄，影响外观。<br/>&nbsp;&nbsp;&nbsp;&nbsp;6.沾染污垢时，应立即干洗，为保持领带色泽，请不要水洗，不可漂白，处理结上褶皱请以蒸气熨斗低温烫平，熨烫最高温度150°，速度要快。水洗及高温熨烫容易造成变形而受损。");
			price = new YihaodianSale_price("广东", 89.0);
			prod2.setSale_price(Arrays.asList(price));
			products.add(prod2);
			yihaodianProductDao.removeYihaodianProductById(prod2.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod2);

			YihaodianProductBean prod3 = new YihaodianProductBean();
			prod3.setKeyType(Arrays.asList(KeyType.CONTENT));
			prod3.setId("000000000000000000000003");
			prod3.setBrand_name("日驰尼");
			prod3.setUpdatetime(new Date());
			prod3.setSearchKey("西装");
			prod3.setPic_url("http://d6.yihaodianimg.com/N01/M00/15/D1/CgQCrlDsE1-AJ_rnAAGh7azUJdg38800_450x450.jpg");
			prod3.setProduct_url_m("http://m.yihaodian.com/product/6574334");
			prod3.setTitle("Richini/日驰尼 2013新款男士全棉西装款夹克衫 商务休闲时尚个性-32823黑色2024XXL");
			price = new YihaodianSale_price("广东", 990.0);
			prod3.setSale_price(Arrays.asList(price));
			products.add(prod3);
			yihaodianProductDao.removeYihaodianProductById(prod3.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod3);

			YihaodianProductBean prod4 = new YihaodianProductBean();
			prod4.setId("000000000000000000000004");
			prod4.setBrand_name("RXT");
			prod4.setUpdatetime(new Date());
			prod4.setSearchKey("白衬衫");
			prod4.setPic_url("http://d6.yihaodianimg.com/N01/M0B/5B/42/CgQCr1GRqcSANUD0AANpcNNr4cw87600_450x450.jpg");
			prod4.setProduct_url_m("http://m.yihaodian.com/product/8876398");
			prod4.setTitle("RXT 新款衬衫 男士长袖白衬衫正装衬衣 西装衬衫男装白色43");
			price = new YihaodianSale_price("广东", 89.0);
			prod4.setSale_price(Arrays.asList(price));
			products.add(prod4);
			yihaodianProductDao.removeYihaodianProductById(prod4.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod4);

			YihaodianProductBean prod5 = new YihaodianProductBean();
			prod5.setId("000000000000000000000005");
			prod5.setBrand_name("卡尔兰奴/CARLLANNU");
			prod5.setUpdatetime(new Date());
			prod5.setSearchKey("白衬衫");
			prod5.setPic_url("http://d6.yihaodianimg.com/N01/M04/3F/87/CgQCrlFWjISAe-Q6AAE5QGqPyZg94300_450x450.jpg");
			prod5.setProduct_url_m("http://m.yihaodian.com/product/8876398");
			prod5.setTitle("Carl lannu /卡尔兰奴 13款女款百搭款宽松白衬衫 长袖女衬衫OL通勤 B330-9042白色M");
			prod5.setProduct_desc("编号：9042属性：衬衣   面料：合成纤维尺寸：国际标准尺寸：36-40");
			price = new YihaodianSale_price("广东", 76.0);
			prod5.setSale_price(Arrays.asList(price));
			products.add(prod5);
			yihaodianProductDao.removeYihaodianProductById(prod5.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod5);

			YihaodianProductBean prod6 = new YihaodianProductBean();
			prod6.setId("000000000000000000000006");
			prod6.setBrand_name("亿超");
			prod6.setUpdatetime(new Date());
			prod6.setSearchKey("眼镜");
			prod6.setPic_url("http://d6.yihaodianimg.com/N00/M06/04/B4/CgQCtlDOsMmAUDoOAAECdpFRzZE46900_380x380.jpg");
			prod6.setProduct_url_m("http://m.yihaodian.com/product/6761033");
			prod6.setTitle("亿超 纯钛眼镜架近视眼镜架 全框眼睛框 赠树脂防辐射镜片 1109c3BG银色 赠送1.60非球镜片常规");
			price = new YihaodianSale_price("广东", 399.0);
			prod6.setSale_price(Arrays.asList(price));
			products.add(prod6);
			yihaodianProductDao.removeYihaodianProductById(prod6.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod6);

			try {
				Serializer.writeObject(products, productsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return products;
	}

	@SuppressWarnings("unchecked")
	private List<YihaodianProductBean> getDemoProducts_ad() {
		List<YihaodianProductBean> products = new ArrayList<YihaodianProductBean>();
		File productsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_PRODUCT_AD);
		if (productsFile.exists() && productsFile.isFile()) {
			try {
				products = (List<YihaodianProductBean>) Serializer.readObject(productsFile);
			} catch (Exception e) {
				products = new ArrayList<YihaodianProductBean>();
				logger.error("", e);
			}
		} else {
			YihaodianProductBean prod1 = new YihaodianProductBean();
			prod1.setId("000000000000000000000011");
			prod1.setBrand_name("Maybelline 美宝莲");
			prod1.setUpdatetime(new Date());
			prod1.setSearchKey("美宝莲");
			prod1.setPic_url("http://d11.yihaodianimg.com/t1/2012/1128/199/237/9d0efb63c8938d3cc77fecedee40a191_380x380.jpg");
			prod1.setProduct_url_m("http://m.yihaodian.com/product/5607882");
			prod1.setTitle("Maybelline 美宝莲 纽约宝贝粉嫩光采蜜乳30ml");
			YihaodianSale_price price = new YihaodianSale_price("广东", 79.0);
			prod1.setSale_price(Arrays.asList(price));
			prod1.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;产品规格：30ml<br/>&nbsp;&nbsp;&nbsp;&nbsp;产品特点：配方富含天然樱桃精萃和抗氧化维他命C。粉润质地，顷刻间，肌肤告别黯沉，时刻闪耀白皙、透亮、粉嫩光采。持续保湿+防护。肌肤Q弹水感****，健康粉润。宝贝粉嫩,使命必达!秘密是?天然樱桃精萃和维他命C!<br/>&nbsp;&nbsp;&nbsp;&nbsp;使用方法:作为日间护肤的最后一步。无需卸妆。品牌介绍美宝莲品牌口号：Maybesheisbornwithit,MaybeitisMaybelline美来自内心，美来自美宝莲（1991至今，通常为广告的最后一句话）“Thepowerisinyourhands”–“把握属于你的美”。<br/>&nbsp;&nbsp;&nbsp;&nbsp;品牌故事：Maybelline(美宝莲纽约)的名字是由他的妹妹的名字(Maybel)以及凡士林(Vaseline)的后半部分组成的。美宝莲纽约于1917年成立之时，生产出了世界上第一支现代眼部化妆品：美宝莲纽约块状睫毛膏(MaybellineCakeMascara)。如今，美宝莲纽约已经成为了一个具有传奇色彩的全球化妆品先驱公司。世界90多个国家及城市中，美宝莲纽约已经成为第一个针对女性生产化妆及护肤品的公司。至今已经有了97年品牌历史。美宝莲纽约(MaybellineNewYork)提供包括专业脸部彩妆、眼部彩妆、唇部彩妆产品。");
			products.add(prod1);
			yihaodianProductDao.removeYihaodianProductById(prod1.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod1);

			YihaodianProductBean prod2 = new YihaodianProductBean();
			prod2.setId("000000000000000000000012");
			prod2.setBrand_name("Maybelline 美宝莲");
			prod2.setUpdatetime(new Date());
			prod2.setSearchKey("美宝莲");
			prod2.setPic_url("http://d6.yihaodianimg.com/N02/M06/29/4C/CgQCsFEkJrmAd2GVAAYkIpvLTcg94800_380x380.jpg");
			prod2.setProduct_url_m("http://m.yihaodian.com/product/4834745");
			prod2.setTitle("Maybelline/美宝莲 完美裸妆组合套装 （宝蓓粉嫩光采蜜乳礼盒装+ 40ml卸妆液答谢品） 全场4.5折起，满299元送卸妆液40ml");
			price = new YihaodianSale_price("广东", 81.0);
			prod2.setSale_price(Arrays.asList(price));
			products.add(prod2);
			yihaodianProductDao.removeYihaodianProductById(prod2.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod2);

			YihaodianProductBean prod3 = new YihaodianProductBean();
			prod3.setId("000000000000000000000013");
			prod3.setBrand_name("Maybelline 美宝莲");
			prod3.setUpdatetime(new Date());
			prod3.setSearchKey("美宝莲");
			prod3.setPic_url("http://d6.yihaodianimg.com/N02/M06/29/4C/CgQCsFEkJrmAd2GVAAYkIpvLTcg94800_380x380.jpg");
			prod3.setProduct_url_m("http://m.yihaodian.com/product/5602337");
			prod3.setTitle("Maybelline 美宝莲 轻松画柔滑自动眼线笔 黑色 0.35g");
			prod3.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;产品规格：0.35g产品特点：<br/>&nbsp;&nbsp;&nbsp;&nbsp;柔滑质地，易上色轻松描画流畅线条，色泽浓郁防水配方，持久不晕染。<br/>&nbsp;&nbsp;&nbsp;&nbsp;品牌介绍美宝莲品牌口号：Maybesheisbornwithit,MaybeitisMaybelline美来自内心，美来自美宝莲（1991至今，通常为广告的最后一句话）“Thepowerisinyourhands”–“把握属于你的美”。<br/>&nbsp;&nbsp;&nbsp;&nbsp;品牌故事：Maybelline(美宝莲纽约)的名字是由他的妹妹的名字(Maybel)以及凡士林(Vaseline)的后半部分组成的。美宝莲纽约于1917年成立之时，生产出了世界上第一支现代眼部化妆品：美宝莲纽约块状睫毛膏(MaybellineCakeMascara)。<br/>&nbsp;&nbsp;&nbsp;&nbsp;如今，美宝莲纽约已经成为了一个具有传奇色彩的全球化妆品先驱公司。世界90多个国家及城市中，美宝莲纽约已经成为第一个针对女性生产化妆及护肤品的公司。至今已经有了97年品牌历史。美宝莲纽约(MaybellineNewYork)提供包括专业脸部彩妆、眼部彩妆、唇部彩妆产品。");
			price = new YihaodianSale_price("广东", 49.0);
			prod3.setSale_price(Arrays.asList(price));
			products.add(prod3);
			yihaodianProductDao.removeYihaodianProductById(prod3.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod3);

			YihaodianProductBean prod4 = new YihaodianProductBean();
			prod4.setId("000000000000000000000014");
			prod4.setBrand_name("Maybelline 美宝莲");
			prod4.setUpdatetime(new Date());
			prod4.setSearchKey("美宝莲");
			prod4.setPic_url("http://d13.yihaodianimg.com/t1/2012/1128/257/238/0c6aef4afe6ddbd1c77fecedee40a191_380x380.jpg");
			prod4.setProduct_url_m("http://m.yihaodian.com/product/5602349");
			prod4.setTitle("Maybelline 美宝莲 宝蓓爱炫彩护唇膏-瑚光珊色控1.9g");
			prod4.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;产品规格：1.9g<br/>&nbsp;&nbsp;&nbsp;&nbsp;产品特点：美宝莲纽约开启炫彩护唇新体验!一抹鲜活淡彩双唇宝贝般亮泽水嫩一抹鲜活淡彩:注入至纯清透色泽,带给双唇自然鲜活的透明感淡彩。全天候亮泽水嫩:充沛乳木果精华融入霍霍巴油唇部柔润延展，持续滋润清透双唇。<br/>&nbsp;&nbsp;&nbsp;&nbsp;使用方法:只需旋转膏体2-3毫米;多次涂抹更能抗衡户外伤害,绽现炫彩水嫩。<br/>&nbsp;&nbsp;&nbsp;&nbsp;色系：珊瑚红属于偏红色系比橘红色略红！<br/>&nbsp;&nbsp;&nbsp;&nbsp;建议咨询品牌客服：400-821-5878品牌介绍美宝莲品牌口号：Maybesheisbornwithit,MaybeitisMaybelline美来自内心，美来自美宝莲（1991至今，通常为广告的最后一句话）“Thepowerisinyourhands”–“把握属于你的美”。<br/>&nbsp;&nbsp;&nbsp;&nbsp;品牌故事：Maybelline(美宝莲纽约)的名字是由他的妹妹的名字(Maybel)以及凡士林(Vaseline)的后半部分组成的。美宝莲纽约于1917年成立之时，生产出了世界上第一支现代眼部化妆品：美宝莲纽约块状睫毛膏(MaybellineCakeMascara)。如今，美宝莲纽约已经成为了一个具有传奇色彩的全球化妆品先驱公司。世界90多个国家及城市中，美宝莲纽约已经成为第一个针对女性生产化妆及护肤品的公司。至今已经有了97年品牌历史。美宝莲纽约(MaybellineNewYork)提供包括专业脸部彩妆、眼部彩妆、唇部彩妆产品。");
			price = new YihaodianSale_price("广东", 25.0);
			prod4.setSale_price(Arrays.asList(price));
			products.add(prod4);
			yihaodianProductDao.removeYihaodianProductById(prod4.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod4);

			YihaodianProductBean prod5 = new YihaodianProductBean();
			prod5.setId("000000000000000000000015");
			prod5.setBrand_name("Maybelline 美宝莲");
			prod5.setUpdatetime(new Date());
			prod5.setSearchKey("美宝莲");
			prod5.setPic_url("http://d12.yihaodianimg.com/t20/2012/0623/462/390/cb58c2dbd9fb7eeaYY_380x380.jpg");
			prod5.setProduct_url_m("http://m.yihaodian.com/product/3737521");
			prod5.setTitle("Maybelline/美宝莲 睛采持久魅影眼线膏 不晕染2.5g （最新包装） 默认发黑色 全场4.5折起，满299元送卸妆液40ml");
			price = new YihaodianSale_price("广东", 69.0);
			prod5.setSale_price(Arrays.asList(price));
			products.add(prod5);
			yihaodianProductDao.removeYihaodianProductById(prod5.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod5);

			YihaodianProductBean prod6 = new YihaodianProductBean();
			prod6.setId("000000000000000000000016");
			prod6.setBrand_name("Maybelline 美宝莲");
			prod6.setUpdatetime(new Date());
			prod6.setSearchKey("美宝莲");
			prod6.setPic_url("http://d6.yihaodianimg.com/N00/M03/31/B9/CgMBmFE2812AZUWfAANFFqD-mWc92200_380x380.jpg");
			prod6.setProduct_url_m("http://m.yihaodian.com/product/4244946");
			prod6.setTitle("Maybelline/美宝莲 精纯矿物新颜乳液BB霜 防晒隔离紫外线 美白遮瑕30ml倍润型30ml 全场4.5折起，满299元送卸妆液40ml");
			price = new YihaodianSale_price("广东", 89.0);
			prod6.setSale_price(Arrays.asList(price));
			products.add(prod6);
			yihaodianProductDao.removeYihaodianProductById(prod6.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod6);

			try {
				Serializer.writeObject(products, productsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return products;
	}

	@SuppressWarnings("unchecked")
	private List<YihaodianProductBean> getDemoProducts_fcwr() {
		List<YihaodianProductBean> products = new ArrayList<YihaodianProductBean>();
		File productsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_PRODUCT_FCWR);
		if (productsFile.exists() && productsFile.isFile()) {
			try {
				products = (List<YihaodianProductBean>) Serializer.readObject(productsFile);
			} catch (Exception e) {
				products = new ArrayList<YihaodianProductBean>();
				logger.error("", e);
			}
		} else {
			YihaodianProductBean prod1 = new YihaodianProductBean();
			prod1.setId("000000000000000000000021");
			prod1.setBrand_name("博库");
			prod1.setUpdatetime(new Date());
			prod1.setSearchKey("非诚勿扰");
			prod1.setPic_url("http://d6.yihaodianimg.com/N00/M07/50/45/CgMBmVF2aVWAYpPcAANBJJwIy4w54700_380x380.jpg");
			prod1.setProduct_url_m("http://m.yihaodian.com/product/8420378");
			prod1.setTitle("CD-HD非诚勿扰的士高(2碟装)（博库） ");
			YihaodianSale_price price = new YihaodianSale_price("广东", 40.0);
			prod1.setSale_price(Arrays.asList(price));
			prod1.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;基本信息商品名称：CD-HD非诚勿扰的士高(2碟装)开本：其他作者：广州市新时代影音公司页数：2印次：1出版时间：2008-01-01ISBN号：F210841000印刷时间：2008-01-01出版社：广州市新时代影音版次：1商品类型：图书印次：1");
			products.add(prod1);
			yihaodianProductDao.removeYihaodianProductById(prod1.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod1);

			YihaodianProductBean prod2 = new YihaodianProductBean();
			prod2.setId("000000000000000000000022");
			prod2.setBrand_name("新华文轩");
			prod2.setUpdatetime(new Date());
			prod2.setSearchKey("非诚勿扰");
			prod2.setPic_url("http://d6.yihaodianimg.com/N03/M03/4A/5F/CgQCs1F1-c6ATw2QAABGyMSDAiE17600_380x380.jpg");
			prod2.setProduct_url_m("http://m.yihaodian.com/product/4384108");
			prod2.setTitle("色眼再识人（文轩） 全国收视率第一的综艺节目——江苏卫视《非诚勿扰》心理学专家乐嘉四年倾心打造！继实用心理学畅销书《色眼识人》后再推力作");
			price = new YihaodianSale_price("广东", 19.4);
			prod2.setSale_price(Arrays.asList(price));
			prod2.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;王映霞为何没有选择郁达夫？翁美玲缘何会因汤镇业另觅新欢而自杀？希拉里性格中的什么成分促使他能和自己的政敌交朋友……乐嘉将在新书《色眼再识人》中为你给出答案。<br/>&nbsp;&nbsp;&nbsp;&nbsp;作者一改《色眼识人》中一半优点和一半缺点的风格，集中火力对不同性格的缺点进行深度的剖析和猛烈的批判，借助了近200个生活中真实案例和50个古今中外的名人案例将读者置身于性格的黑暗面。读完本书中你能理解其他性格是如何伤害自己的，也能了解自己是如何在无意中伤害他人的。<br/>&nbsp;&nbsp;&nbsp;&nbsp;这是一本专门为那些希望洞悉性格来寻找真实生命最高境界的人而写的书，只有了解了人性内心的真相，才有可能追求内在的和谐宁静与自由自在的喜悦生活。 ");
			products.add(prod2);
			yihaodianProductDao.removeYihaodianProductById(prod2.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod2);

			YihaodianProductBean prod3 = new YihaodianProductBean();
			prod3.setId("000000000000000000000023");
			prod3.setBrand_name("博库");
			prod3.setUpdatetime(new Date());
			prod3.setSearchKey("非诚勿扰");
			prod3.setPic_url("http://d6.yihaodianimg.com/N04/M0A/0E/C6/CgQDrlF2RdSAc-sxAAoGWHguDWU78400_380x380.jpg");
			prod3.setProduct_url_m("http://m.yihaodian.com/product/8410920");
			prod3.setTitle("非诚勿扰(Ⅲ结婚进行曲)（博库）");
			price = new YihaodianSale_price("广东", 21.8);
			prod3.setSale_price(Arrays.asList(price));
			prod3.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;《非诚勿扰(Ⅲ结婚进行曲)》是一本关于结婚的指导书。<br/>&nbsp;&nbsp;&nbsp;&nbsp;从相识、相知、相爱，到最后步入婚姻的殿堂，每对男女都希望自己 的婚姻生括幸福美满。结婚是人生大事，每对情侣都希望将自己的结婚典 礼办得成功完美，这份美好的回忆，将为以后的家庭生活打下坚实的感情基础。<br/>&nbsp;&nbsp;&nbsp;&nbsp;相爱多年，如何牵手走进婚姻的殿堂？ 婚前婚后，怎样经营美好的幸福婚姻？ 月老凡间所著的这本《非诚勿扰(Ⅲ结婚进行曲)》全方位解答了婚前 婚后容易面临的各种问题，详细阐述了婚礼仪式的各项流程和技巧。你适 合什么样的婚姻？如何进行婚前心理准备与调适？怎样做一个结婚成本的 预算？如何打造一个专属于你的个性化婚礼？怎样安排一个难忘的旅行结 婚？婚姻中性爱有多重要？保持爱情天长地久的方式有哪些？本书将为你 解决所有的困惑和疑问，让你心如明镜，面对婚姻不再迷茫。<br/>&nbsp;&nbsp;&nbsp;&nbsp;书中已经为你谱写好了一曲曲最优美的旋律，让你和你的爱人踏着幸 福的节拍，携手走进婚姻幸福美满的大门。本书既温柔体贴，又实用关怀 ，从而保证新婚生活不会出现一丝纰漏，让你尽享幸福和温馨。<br/>&nbsp;&nbsp;&nbsp;&nbsp;全程指导新婚生活，让终身大事万无一失。<br/>&nbsp;&nbsp;&nbsp;&nbsp;经营好自己的婚姻，让婚姻生活时时保鲜。");
			products.add(prod3);
			yihaodianProductDao.removeYihaodianProductById(prod3.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod3);

			YihaodianProductBean prod4 = new YihaodianProductBean();
			prod4.setId("000000000000000000000024");
			prod4.setBrand_name("博库");
			prod4.setUpdatetime(new Date());
			prod4.setSearchKey("非诚勿扰");
			prod4.setPic_url("http://d6.yihaodianimg.com/N00/M08/4F/8E/CgMBmFF14_uAPzvAAAn5J9Fg0FY67600_380x380.jpg");
			prod4.setProduct_url_m("http://m.yihaodian.com/product/8385748");
			prod4.setTitle("非诚勿扰(Ⅰ相亲指南针)（博库）");
			price = new YihaodianSale_price("广东", 21.8);
			prod4.setSale_price(Arrays.asList(price));
			prod4.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;本书借你一双慧眼，帮你辨别“真假王子”！ 如果你是泥捏的，本书给你爱情智囊，助你找到“白雪公主”！ 《非诚勿扰Ⅰ(相亲指南针)》是一部关于相亲的技巧书。<br/>&nbsp;&nbsp;&nbsp;&nbsp;一部能帮助你相亲成功的最佳实用指南。<br/>&nbsp;&nbsp;&nbsp;&nbsp;千千万万的单身男女从被迫相亲，到被动相亲，再到主动相亲，相亲 的观念与现象越来越普遍，大有“生命不息，相亲不止”之势。<br/>&nbsp;&nbsp;&nbsp;&nbsp;如何提高相亲的效率？如何找到自己的意中人？ 如何把握稍纵即逝的机会？如何遇到就不错过？ 月老凡间，王泽所著的这本《非诚勿扰Ⅰ(相亲指南针)》有针对性地 分析了相亲过程中容易遇到的各种问题，并给出了有效的解决之道，以便 相亲者花很少的时间，就能迅速掌握其中最关键的制胜法宝，从而帮助大 量单身人士改善情感现状，大幅度提升相亲质量和效率。<br/>&nbsp;&nbsp;&nbsp;&nbsp;本书坚持以“实用主义，常识至上”为行文主线，精心选取了大量男 女相亲的实例，阐述了各种相亲心理以及其细节，通过简要分析其优劣， 让读者知道自己在相亲时应该做什么以及怎么做。<br/>&nbsp;&nbsp;&nbsp;&nbsp;本书还对时下流行的婚恋观点，给予了合理分析，从而引导读者在相 亲时做出适合自己的正确选择。<br/>&nbsp;&nbsp;&nbsp;&nbsp;献给所有在漫漫相亲路上追寻爱情的朋友们，为了你的幸福生活，让 我们将相亲进行到底！");
			products.add(prod4);
			yihaodianProductDao.removeYihaodianProductById(prod4.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod4);

			YihaodianProductBean prod5 = new YihaodianProductBean();
			prod5.setId("000000000000000000000025");
			prod5.setBrand_name("文轩");
			prod5.setUpdatetime(new Date());
			prod5.setSearchKey("非诚勿扰");
			prod5.setPic_url("http://d6.yihaodianimg.com/N00/M0A/3B/B5/CgMBmVFNGu6AM_jJAAIjQrTwAEE25900_380x380.jpg");
			prod5.setProduct_url_m("http://m.yihaodian.com/product/2904807");
			prod5.setTitle("男人爱播种.女人爱筑巢 非诚勿扰2（文轩）");
			price = new YihaodianSale_price("广东", 22.4);
			prod5.setSale_price(Arrays.asList(price));
			prod5.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;男性与女性是完全不同的生物，从人类社会与生物的角度来看，男性的功能是尽可能地“多播种”，而女性的功能则是尽可能接受“优良的种子”。因此，男性和女性在思维上、评价方式上、性爱心理上都有着非常明显的差异，如果不能够意识到这种差异，男性与女性在“沟通”、“恋爱”、“结婚”时就会碰到很多问题，更谈不上做异性眼中有魅力的人。从这样的角度出发，本书除介绍男女之间思维差异、评价方式差异、性爱心理差异之外，还收录了提升异性缘的技巧，怎样拉进与异性之间的距离，如何克服自卑心理，如何创造受欢迎的外部环境、怎样掌握受欢迎的谈话技巧以及学会如何高效地恋爱、甚至恋爱失败时应该如何终结等等。本书是一本教给我们如何理解异性的思维，如何获得异性欢迎以及提升自己异性魅力的书，对正在恋爱或即将恋爱的人们相信更会有较大的启发 ");
			products.add(prod5);
			yihaodianProductDao.removeYihaodianProductById(prod5.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod5);

			YihaodianProductBean prod6 = new YihaodianProductBean();
			prod6.setId("000000000000000000000026");
			prod6.setBrand_name("文轩");
			prod6.setUpdatetime(new Date());
			prod6.setSearchKey("非诚勿扰");
			prod6.setPic_url("http://d6.yihaodianimg.com/N01/M0B/3D/47/CgQCrVFShByAadhTAAGhkyw6KPY02100_380x380.jpg");
			prod6.setProduct_url_m("http://m.yihaodian.com/product/7614042");
			prod6.setTitle("相亲红宝书:\"非诚勿扰\"百战胜经(公主版)（文轩）");
			price = new YihaodianSale_price("广东", 22.5);
			prod6.setSale_price(Arrays.asList(price));
			prod6.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;全书内容按照《非诚勿扰》节目的环节来编排，4个篇目依次为“往前一步是幸福”、“爱之初体验”、“爱之再判断”、“爱之终决选”，这也是一个标准的男女双方从相知到相恋的相亲过程。 ");
			products.add(prod6);
			yihaodianProductDao.removeYihaodianProductById(prod6.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod6);

			try {
				Serializer.writeObject(products, productsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return products;
	}

	@SuppressWarnings("unchecked")
	private List<RecommendObject> getDemoVods_xinwen() {
		List<RecommendObject> vods = new ArrayList<RecommendObject>();
		File vodsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_VOD_XINWEN);
		if (vodsFile.exists() && vodsFile.isFile()) {
			try {
				vods = (List<RecommendObject>) Serializer.readObject(vodsFile);
			} catch (Exception e) {
				vods = new ArrayList<RecommendObject>();
				logger.error("", e);
			}
		} else {
			RecommendObject vod1 = new RecommendObject();
			vod1.setId("tcl_demo_vod_xinwen_1");
			vod1.setTitle("李克强离京对印度、巴基斯坦、瑞士、德国进行正式访问 130519");
			vod1.setCover("http://g1.ykimg.com/01270F1F465198B8ABEFAE0123193C822B45AA-6BE0-69AA-E841-961DD0A10B04");
			vods.add(vod1);

			RecommendObject vod2 = new RecommendObject();
			vod2.setId("tcl_demo_vod_xinwen_2");
			vod2.setTitle("国务院总理李克强访问瑞士 130525");
			vod2.setCover("http://g2.ykimg.com/01270F1F4651A05DC9EBE30123193C541215EF-184A-A27C-99E4-3BC3E311E912");
			vods.add(vod2);

			RecommendObject vod3 = new RecommendObject();
			vod3.setId("tcl_demo_vod_xinwen_3");
			vod3.setTitle("李克强抵达苏黎世开始对瑞士进行正式访问");
			vod3.setCover("http://g3.ykimg.com/0100641F464B5EDF989CE50029CD26242D0A6B-DEE2-E69F-E5B4-4AD6CF9A5D3A");
			vods.add(vod3);

			RecommendObject vod4 = new RecommendObject();
			vod4.setId("tcl_demo_vod_xinwen_4");
			vod4.setTitle("李克强参观瑞士爱因斯坦博物馆时强调 创新是人类活力的源泉");
			vod4.setCover("http://g2.ykimg.com/01270F1F4651A1A196B6940123193C591047EB-A3FA-B282-1B5A-36BE08B938F4");
			vods.add(vod4);

			RecommendObject vod5 = new RecommendObject();
			vod5.setId("tcl_demo_vod_xinwen_5");
			vod5.setTitle("李克强参观瑞士家庭农庄强调 加强中瑞合作 促农业现代化");
			vod5.setCover("http://g3.ykimg.com/01270F1F4651A0A36D72230123193C8875E433-5436-866F-6DEA-A2C75E30800F");
			vods.add(vod5);

			RecommendObject vod6 = new RecommendObject();
			vod6.setId("tcl_demo_vod_xinwen_6");
			vod6.setTitle("李克强圆满结束对瑞士访问");
			vod6.setCover("http://g2.ykimg.com/01270F1F4651A1EFABCBF00123193CD6825B8F-4D06-E944-FEE6-56D9A88A4987");
			vods.add(vod6);

			try {
				Serializer.writeObject(vods, vodsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return vods;
	}

	@SuppressWarnings("unchecked")
	private List<RecommendObject> getDemoVods_ad() {
		List<RecommendObject> vods = new ArrayList<RecommendObject>();
		File vodsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_VOD_AD);
		if (vodsFile.exists() && vodsFile.isFile()) {
			try {
				vods = (List<RecommendObject>) Serializer.readObject(vodsFile);
			} catch (Exception e) {
				vods = new ArrayList<RecommendObject>();
				logger.error("", e);
			}
		} else {
			RecommendObject vod1 = new RecommendObject();
			vod1.setId("tcl_demo_vod_ad_1");
			vod1.setTitle("美宝莲潮妆学院-清新约会妆");
			vod1.setCover("http://g3.ykimg.com/0100641F4650A3D5FBD3D1058E2FDCC33134ED-3DEB-8104-CB92-75B53C543B76");
			vods.add(vod1);

			RecommendObject vod2 = new RecommendObject();
			vod2.setId("tcl_demo_vod_ad_2");
			vod2.setTitle("中性帅气的春日妆容（无妆不潮） 美宝莲中国");
			vod2.setCover("http://g1.ykimg.com/0100641F464F680DB0AEF704677344D677BEFD-7163-836D-707C-19228B7FE192");
			vods.add(vod2);

			RecommendObject vod3 = new RecommendObject();
			vod3.setId("tcl_demo_vod_ad_3");
			vod3.setTitle("美寶蓮-早春明亮動人渡假風妝容");
			vod3.setCover("http://g2.ykimg.com/0100641F464F669F63557805A70FCB1014D3B1-712D-B1CA-073D-9FDBE86BA444");
			vods.add(vod3);

			RecommendObject vod4 = new RecommendObject();
			vod4.setId("tcl_demo_vod_ad_4");
			vod4.setTitle("美宝莲潮妆学院呈献: 职场裸妆");
			vod4.setCover("http://g3.ykimg.com/0100641F46511697C7813A058E2FDC64C44A17-AFB7-AC00-EB05-C3676BABE969");
			vods.add(vod4);

			RecommendObject vod5 = new RecommendObject();
			vod5.setId("tcl_demo_vod_ad_5");
			vod5.setTitle("美宝莲潮妆学院-迷人大眼妆");
			vod5.setCover("http://g1.ykimg.com/0100641F4650D35FFB6020058E2FDCE9D2B4FF-9D5B-067E-6707-133521A75A1D");
			vods.add(vod5);

			RecommendObject vod6 = new RecommendObject();
			vod6.setId("tcl_demo_vod_ad_6");
			vod6.setTitle("美宝莲潮妆学院-风格职场妆");
			vod6.setCover("http://g2.ykimg.com/0100641F4650D9045D5F70058E2FDC99FFAE12-57A5-3446-D649-E7A690D608B3");
			vods.add(vod6);

			try {
				Serializer.writeObject(vods, vodsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return vods;
	}

	@SuppressWarnings("unchecked")
	private List<RecommendObject> getDemoVods_fcwr() {
		List<RecommendObject> vods = new ArrayList<RecommendObject>();
		File vodsFile = new File(PATH_DEMO_SERIALIZE + FILE_DEMO_VOD_FCWR);
		if (vodsFile.exists() && vodsFile.isFile()) {
			try {
				vods = (List<RecommendObject>) Serializer.readObject(vodsFile);
			} catch (Exception e) {
				vods = new ArrayList<RecommendObject>();
				logger.error("", e);
			}
		} else {
			RecommendObject vod1 = new RecommendObject();
			vod1.setId("tcl_demo_vod_fcwr_1");
			vod1.setTitle("非诚勿扰51岁女嘉宾成功牵手小17岁男！不可思议的牵手！");
			vod1.setCover("http://g2.ykimg.com/0100641F4651A5EABEFC9F0759279D312A5980-8654-E05E-95B5-C803732DB3B5");
			vods.add(vod1);

			RecommendObject vod2 = new RecommendObject();
			vod2.setId("tcl_demo_vod_fcwr_2");
			vod2.setTitle("《非诚勿扰》官晶晶朱峰 发现王国浪漫童话婚礼秀");
			vod2.setCover("http://g1.ykimg.com/0100401F46517909CF70D406E26A7A664B3DF6-F3D3-7767-BE8E-16766D32397D");
			vods.add(vod2);

			RecommendObject vod3 = new RecommendObject();
			vod3.setId("tcl_demo_vod_fcwr_3");
			vod3.setTitle("《非诚勿扰》“富二代”澄清事实");
			vod3.setCover("http://g2.ykimg.com/0100641F465154897C3030019C3C1C61522747-1880-998A-5232-2580DE01ABB2");
			vods.add(vod3);

			RecommendObject vod4 = new RecommendObject();
			vod4.setId("tcl_demo_vod_fcwr_4");
			vod4.setTitle("乐嘉请辞江苏卫视 《非诚勿扰》暂不受影响");
			vod4.setCover("http://g3.ykimg.com/0100641F46512722D93D71088EF54334B95813-442F-433C-8F5D-71CFB3921403");
			vods.add(vod4);

			RecommendObject vod5 = new RecommendObject();
			vod5.setId("tcl_demo_vod_fcwr_5");
			vod5.setTitle("《非诚勿扰》新西兰专场，江一燕助场");
			vod5.setCover("http://g1.ykimg.com/0100641F4651A89F46A88A073505BF00E712DE-9107-0D72-09C0-D94242A9AEF4");
			vods.add(vod5);

			RecommendObject vod6 = new RecommendObject();
			vod6.setId("tcl_demo_vod_fcwr_6");
			vod6.setTitle("《非诚勿扰》韩国专场");
			vod6.setCover("http://g2.ykimg.com/0100641F46502315490D7506A592F9EBB04E06-5729-4AD1-6881-DE8F72D1A19F");
			vods.add(vod6);

			try {
				Serializer.writeObject(vods, vodsFile);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return vods;
	}
}
