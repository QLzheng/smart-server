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
			String programTitle = "CCTV-13���ţ���������";
			return programTitle;
		} else if (timestamp <= TIMESTAMP_AD) {
			String programTitle = "��棺������";
			return programTitle;
		} else {
			String programTitle = "�������ӣ��ǳ�����";
			return programTitle;
		}
	}

	public String currentEpgHTML(long timestamp) {
		if (timestamp <= TIMESTAMP_XINWEN) {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img src='../../clive/images/xinwen.jpg' />");
			sb.append("<div class=\"ps_desc\"><h3>��������</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
			return sb.toString();
		} else if (timestamp <= TIMESTAMP_AD) {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img src='../../clive/images/ad.jpg' />");
			sb.append("<div class=\"ps_desc\"><h3>���</h3></div>");
			sb.append("<div class=\"z-movie-playmask\"></div>");
			sb.append("</div>");
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"ps_album\" style=\"opacity:0;\">");
			sb.append("<img src='../../clive/images/fcwr.jpg' />");
			sb.append("<div class=\"ps_desc\"><h3>�ǳ�����</h3></div>");
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
			sb.append("<h2>��Ŀ���ƣ�<b><font color='red'>" + movie.getTitle() + "</font></b></h2>");
			if (movie.getUpdated_at() != null)
				sb.append("<h2>����ʱ�䣺" + Constants.parseTime(movie.getUpdated_at()) + "</h2>");
			if (movie.getHost() != null && movie.getHost().size() > 0)
				sb.append("<h2>�����ˣ�" + movie.getHost() + "</h2>");
			if (movie.getGuest() != null && movie.getGuest().size() > 0)
				sb.append("<h2>�α���" + movie.getGuest() + "</h2>");
			if (movie.getDirector() != null && movie.getDirector().size() > 0)
				sb.append("<h2>���ݣ�" + movie.getDirector() + "</h2>");
			if (movie.getStarring() != null && movie.getStarring().size() > 0)
				sb.append("<h2>��Ա��" + movie.getStarring() + "</h2>");
			if (movie.getTags() != null && movie.getTags().size() > 0)
				sb.append("<h2>��Ŀ���ͣ�" + movie.getTags() + "</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;" + movie.getContent() + "</h2>");
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font color='red'>DEMO������Ƶ</font></b></h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;һ�����Ҿ��õ�����������һ��������ҵ��׳��\"����\"�ڸĸ￪��֮����TCL�����ѳɳ�Ϊ�б�����\"����\"����Ϊ���ȹ��ʻ����й���ҵ��TCLû���κο��Խ������������ȫ��Χ��̽����һ���й�Ʒ�ƴ�δ�߹��ĵ�·���������ʻ����Ͼ�����Ͳ�и̽���������ڹ��ʻ���·�ϵĲ������Ӽᶨ��ͨ�����ʻ�������TCLʵ�����������������й���ҵ�����ȫ��Ĺ��ʻ���ҵ����ʷ��ת�䣬������ȫ�·�չ�׶Ρ�����ѽ�����TCL�Ĺ��ʻ�̽�������Ǳ�����Ӧ���ʻ������ĳ������ս,��ô�������ڿ�ʼ�����Ǿ��Ѿ����뵽һ������ı�����ʻ�δ����չĿ���������½׶Ρ����ǽ�վ���µ���ʷ����ϣ�������TCL����һ�����о������Ĺ��ʻ���ҵ����δ����������TCL��չ������ս��˼·�У�����Ҫ�̱�ǿ�����������£�ʵ���¿�Խ��\"�̱�ǿ��\"������Ҫ���̻�������֮����,ǿ����������֮������\"��������\"������Ҫ�Լ��������̺��Ļ��ı��ͻ�ƣ�����ҵ�ľ�Ӫ����ˮƽ���������ʻ�Ҫ����¸߶ȡ����Ǽ��ţ�\"��Ϊ������\"��TCL�ˣ�����ƾ���ﴴ�µ��ǻۡ����۲��ӵ�����,��дTCL��ʷ����ƪ�£�ʵ��TCL��չ���¿�Խ��</h2>");
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
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǿ�뾩��ӡ�ȡ��ͻ�˹̹����ʿ���¹�������ʽ����</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-5-16 19:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�ź���]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;��Ƶ: ���ǿ�뾩��ӡ�ȡ��ͻ�˹̹����ʿ���¹�������ʽ���� 130519</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_2".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>����Ժ�������ǿ������ʿ</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-5-25 19:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�ź���]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;��Ƶ: ����Ժ�������ǿ������ʿ 130525</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_3".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǿ�ִ���������ʼ����ʿ������ʽ����</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2012-1-25 19:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�ź���]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;��Ƶ: ���ǿ�ִ���������ʼ����ʿ������ʽ����</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_4".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǿ�ι���ʿ����˹̹�����ʱǿ�� ���������������ԴȪ</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-5-26 19:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�ź���]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;��Ƶ: ���ǿ�ι���ʿ����˹̹�����ʱǿ�� ���������������ԴȪ</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_5".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǿ�ι���ʿ��ͥũׯǿ�� ��ǿ������� ��ũҵ�ִ���</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-5-25 19:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�ź���]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;��Ƶ: ���ǿ�ι���ʿ��ͥũׯǿ�� ��ǿ������� ��ũҵ�ִ��� 130525</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_xinwen_6".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǿԲ����������ʿ����</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-5-26 19:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�ź���]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;��Ƶ: ���ǿԲ����������ʿ���� 130526</h2>");
			return sb.toString();
		}

		else if ("tcl_demo_vod_ad_1".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>��������ױѧԺ-����Լ��ױ</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2012-5-6 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>��Ŀ���ͣ�[ʱ��]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;������ŦԼ������1920�꣬��ȫ�����ױƷ��˾ŷ���ż������й���չ��Ϊ�ɹ���Ʒ��֮һ����ȫ�򳬹�129�����Һ͵����������ۡ�2012������γ�Ϊ÷����˹-����ŦԼʱװ�ܵĹٷ���ױƷ�����̡������ڰ����й�Ů�Ը��õ�չʾ�����Լ��ġ����������ڵ�����������������ŵ�����2011�꣬�Ƴ���ױѧԺ���ù��Ů������ױ��ѧ��ױ������ױ��ȫ��һ����ױ�������� </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_2".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>����˧���Ĵ���ױ�ݣ���ױ������ �������й�</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2012-5-6 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>��Ŀ���ͣ�[ʱ��]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;������ŦԼ������1920�꣬��ȫ�����ױƷ��˾ŷ���ż������й���չ��Ϊ�ɹ���Ʒ��֮һ����ȫ�򳬹�129�����Һ͵����������ۡ�2012������γ�Ϊ÷����˹-����ŦԼʱװ�ܵĹٷ���ױƷ�����̡������ڰ����й�Ů�Ը��õ�չʾ�����Լ��ġ����������ڵ�����������������ŵ�����2011�꣬�Ƴ���ױѧԺ���ù��Ů������ױ��ѧ��ױ������ױ��ȫ��һ����ױ�������� </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_3".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>����ɏ-�紺�������˶ɼ��L�y��</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2012-5-7 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>��Ŀ���ͣ�[ʱ��]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;������ϵ�в�Ʒ ��<br/>&nbsp;&nbsp;&nbsp;&nbsp;������BB˪�� ����ȥ֮��Ƥ�w�ǳ��й�� ҕ�X�Ͽ������w�|�ܼ��@M �m��Ǭ��Ƥ�w�����������ݵ�Ů��ʹ��<br/>&nbsp;&nbsp;&nbsp;&nbsp;���j��Ŀ��ɫ��Ӱ�̣���N�ɫ���ǻ����y�ݱ؂���ɫ�܌��� ��ĩ�O�� Ҳ���@ɫ<br/>&nbsp;&nbsp;&nbsp;&nbsp;���Ƚ�ë�ࣺ�Y�溬���S�����w�S ��ëϡ�ٵ���Ҳ����׃�ú��L�ܝ��� ���Ҝ�ˮ��ж ��ж�yҺжҲ�ܺ�ж �����ڻ� �������ĩ�� ��һ�l�l��Ó��~<br/>&nbsp;&nbsp;&nbsp;&nbsp;�V��ˮ���H�w��t�� ɢ�۠����t ��ĩ���� �ַ��㶨�y<br/>&nbsp;&nbsp;&nbsp;&nbsp;�^ɫ�־ô��ࣺ �ɫM ���@ɫ ���Ӳ�����������c��ĸ��X</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_4".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>��������ױѧԺ����: ְ����ױ</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2012-6-21 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>��Ŀ���ͣ�[ʱ��]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;������ŦԼ������1920�꣬��ȫ�����ױƷ��˾ŷ���ż������й���չ��Ϊ�ɹ���Ʒ��֮һ����ȫ�򳬹�129�����Һ͵����������ۡ�2012������γ�Ϊ÷����˹-����ŦԼʱװ�ܵĹٷ���ױƷ�����̡������ڰ����й�Ů�Ը��õ�չʾ�����Լ��ġ����������ڵ�����������������ŵ�����2011�꣬�Ƴ���ױѧԺ���ù��Ů������ױ��ѧ��ױ������ױ��ȫ��һ����ױ�������� </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_5".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>��������ױѧԺ-���˴���ױ</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2012-4-2 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>��Ŀ���ͣ�[ʱ��]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;������ŦԼ������1920�꣬��ȫ�����ױƷ��˾ŷ���ż������й���չ��Ϊ�ɹ���Ʒ��֮һ����ȫ�򳬹�129�����Һ͵����������ۡ�2012������γ�Ϊ÷����˹-����ŦԼʱװ�ܵĹٷ���ױƷ�����̡������ڰ����й�Ů�Ը��õ�չʾ�����Լ��ġ����������ڵ�����������������ŵ�����2011�꣬�Ƴ���ױѧԺ���ù��Ů������ױ��ѧ��ױ������ױ��ȫ��һ����ױ�������� </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_ad_6".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>��������ױѧԺ-���ְ��ױ</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2012-8-16 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>��Ŀ���ͣ�[ʱ��]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;������ŦԼ������1920�꣬��ȫ�����ױƷ��˾ŷ���ż������й���չ��Ϊ�ɹ���Ʒ��֮һ����ȫ�򳬹�129�����Һ͵����������ۡ�2012������γ�Ϊ÷����˹-����ŦԼʱװ�ܵĹٷ���ױƷ�����̡������ڰ����й�Ů�Ը��õ�չʾ�����Լ��ġ����������ڵ�����������������ŵ�����2011�꣬�Ƴ���ױѧԺ���ù��Ů������ױ��ѧ��ױ������ױ��ȫ��һ����ױ�������� </h2>");
			return sb.toString();
		}

		else if ("tcl_demo_vod_fcwr_1".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>�ǳ�����51��Ů�α��ɹ�ǣ��С17���У�����˼���ǣ�֣�</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-3-16 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�Ϸ�]</h2>");
			sb.append("<h2>�α���[�ּΣ�����]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������������Ŀ]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;��Ƶ: �ǳ�����51��Ů�α��ɹ�ǣ��С17���У�����˼���ǣ�֣�</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_2".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǳ����š��پ������ ������������ͯ��������</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-1-16 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�Ϸ�]</h2>");
			sb.append("<h2>�α���[�ּΣ�����]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������������Ŀ]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;�����������������޼��ޣ����ǳ����š�ʷ������˵����£��پ�������壬���밮�ĵ��ã������ͯ��������������</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_3".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǳ����š�����������������ʵ</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-4-12 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�Ϸ�]</h2>");
			sb.append("<h2>�α���[�ּΣ�����]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������������Ŀ]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;���ǳ����š��ǽ�������һ����Ӧ�ִ��������Ĵ��ͻ������ѽ�Ŀ��Ϊ�������Ů�ṩ�����Ļ�������ƽ̨�������Ľ�Ŀ������ȫ�µĻ�������ģʽ�õ����ں����ѹ㷺��ע�� </h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_4".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>�ּ���ǽ������� ���ǳ����š��ݲ���Ӱ��</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-3-16 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�Ϸ�]</h2>");
			sb.append("<h2>�α���[�ּΣ�����]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������������Ŀ]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;��Ƶ: �ּ���ǽ������� ���ǳ����š��ݲ���Ӱ�죨������</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_5".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǳ����š�������ר������һ������</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-4-7 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�Ϸ�]</h2>");
			sb.append("<h2>�α���[�ּΣ�����]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������������Ŀ]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;���ǳ����š���2010��1��15���й���½��������������һ�����������������Ŀ���ɽ��յ���̨���Ž�Ŀ�������Ϸ����֣�ǰ�����ּκͻ��������˷���������2013��4��8-7���������������ּ�[1]��2013��4��13���������Ӻ�������������Ŀ�ײ�ʱ��Ϊÿ���������յ�21:10�����Ϊ21:20��ͬ��23:00�����ز�����������12:30���ҵڶ����ز������Ϊ11:30���ֱ��ز������������գ���2011��7���Ƴ�����ר����2011��10���Ƴ�����ר����2012��1���Ƴ�Ӣ��ר����2012��6���Ƴ�����ר����201</h2>");
			return sb.toString();
		} else if ("tcl_demo_vod_fcwr_6".equals(itemId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("<h2>��Ŀ���ƣ�<b><font id='key_title'>���ǳ����š�����ר��</font></b></h2>");
			sb.append("<h2>����ʱ�䣺2013-5-8 12:00:00</h2>");
			sb.append("<h2>ƬԴ���ſ���-���ƬԴ</h2>");
			sb.append("<h2>�����ˣ�[�Ϸ�]</h2>");
			sb.append("<h2>�α���[�ּΣ�����]</h2>");
			sb.append("<h2>��Ŀ���ͣ�[��������������Ŀ]</h2>");
			sb.append("<h2>&nbsp;&nbsp;&nbsp;&nbsp;�й��ڵ���𱬵����ս�Ŀ���ǳ����š�����ר������������ʽ��8��8�������ں������η�չ��¡�������Ļ�� 8��1��-9��15�գ����׶�����ɽ�ȵؽ��ܱ��� 9��16��-9��30�գ��Ա����߽������� 10����Ѯ���ڽ������ӽ���¼�� 10��ĩ����ʽ����</h2>");
			return sb.toString();
		}

		else {
			return "������Ϣ";
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
				return "<span style='position:absolute;text-align:center;line-height:177px;width:325px;'><h2>�޲�Ʒ�Ƽ���</h2></span>";
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
				baikes.addAll(baikeSearchService.search("��������"));
				baikes.addAll(baikeSearchService.search("���ǿ"));
				baikes.addAll(baikeSearchService.search("�������ء�����˹̹"));
				baikes.addAll(baikeSearchService.search("��ʿ"));
				baikes.addAll(baikeSearchService.search("������"));
				baikes.addAll(baikeSearchService.search("����˹̹�����"));
				baikes.add(baikeSearchService.search("����ٸ").get(0));

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
				baikes.addAll(baikeSearchService.search("������"));
				baikes.add(baikeSearchService.search("ӣ��").get(0));
				baikes.add(baikeSearchService.search("����").get(0));
				baikes.addAll(baikeSearchService.search("Ƥ������"));
				baikes.addAll(baikeSearchService.search("��ë��"));
				baikes.add(baikeSearchService.search("����ķ˹").get(1));
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
				baikes.add(baikeSearchService.search("�ǳ�����").get(0));
				baikes.addAll(baikeSearchService.search("�Ϸ�"));
				baikes.addAll(baikeSearchService.search("�ּ�"));
				baikes.add(baikeSearchService.search("����").get(0));
				baikes.addAll(baikeSearchService.search("�ٺ���"));
				baikes.addAll(baikeSearchService.search("����"));

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
			news1.setProgram_wiki_keys(Arrays.asList("���ǿ"));
			news1.setTitle("���ǿ�ι���ʿ����˹̹�����ʱǿ�����������������ԴȪ");
			news1.setLink("http://www.ccdy.cn/wenhuabao/yb/201305/t20130527_653395.htm");
			news1.setUpdateTime(new Date());
			StringBuffer des = new StringBuffer();
			des.append("�»��粮����5��25�յ� (��������ά ����)����ʱ��25�����磬������ʿ���ʵĹ���Ժ�������ǿ�ι���λ�ڲ�����İ���˹̹����ݡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ĩ�Ĳ�����ֵ����������ꡣ�����ǿ�ִ�ʱ����ʿ�Ƽ��봴�²��������鰲���޻��¡��������г������غͲ�������ʷ����ݹݳ�÷�����ڲ������ڴ�ӭ�ӡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����˹̹������ǲ�������ʷ����ݵ�һ���֣�չƷ���ݷḻ�����ǿ��ϸ��ȡ�˹��ڰ���˹̹�ڲ���������о��ر��������������۵��йؽ��ܡ����ǿ˵������˹̹�ǲ�����Ľ�����Ҳ��ȫ����Ĺ��١�����˹̹�ľ���������û������������û�д�������ֻ���ڷܡ���ѧ�����п��ܴ���ΰ��ĳɾ͡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����֪��������ʷ��������ھٰ��й���ʼ�ʱ���ٸչ��ʱ�����ǿ��ʾ������˵����ʼ�ʱ���ٸ���й���ʷ�Ļ�������һ���֡�����չ��ͬʱչ�����ö������Ļ����֣������������Ļ�����ʵ���˿�Խʱ�յ���ײ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������ǿ���ڹ��ڲι۵ĵ������ڻ���������ѯ�����ǵ�ѧϰ���������������֪�����е�һЩ���й����й���ѧ�������ľ���ʱ�����ǿ�������������������ʹ�ߡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;һλ��ʿ��ѧ�������ǿ������Ϊ�й������Ƿ���ʱ����飬���ǿЦ�Żش����۹�����æ����Ҫ���ʱ����顣��������飬��������˼�����˸��Ҳ�����˽������������̡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������������й�����ƶ��Ƽ����µ�����ʱ�����ǿ˵������������ĲƸ����ɹ�����Ҫ��ǿ֪ʶ��Ȩ���������������߲��м��飬���ܵõ�Ӧ�лر�����ҵ֮�佻��Ҫע�ؼ���������ʹ��Ʒ���пƼ������������ʺϵ����г��������й�������̨��һϵ���ƶ��Ƽ����µ����ߣ���ӭ��ʿ��ŷ����ҵ���й�Ͷ�ʡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ʱ���ڻ���Ц���з��š��ٱ�ǰ��÷�����ݳ��������ǿ�����Բ������ԡ����ǿ���д�£������������������ԴȪ����Ӯ���ڳ���ʿ������������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���죬���ǿ��������й�פ��ʿʹ�ݹ�����Ա�����ʻ��������Ȼ��˺���ѧ������<br/>");
			news1.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news1);
			newsItemDao.saveNewsSearchRstItem(news1);
			news.add(news1);

			NewsSearchRstItem news2 = new NewsSearchRstItem();
			news2.set_id("000000000000000000000002");
			news2.setProgram_wiki_keys(Arrays.asList("�й�", "��ʿ", "����"));
			news2.setTitle("�й�����ʿ���ں����ռ�޴�");
			news2.setLink("http://intl.ce.cn/specials/zxgjzh/201305/29/t20130529_24429684.shtml");
			news2.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("5��24�գ�����ǩ�������óЭ��̸�е��½ⱸ��¼�������ش�ɹ������������й�����ʿ������ó�����������չ�����������ڴٽ��й�ͬŷ��֮��Ĺ�ϵ���й��������ᶨ���Ƶظĸ￪�ţ��������Ҳ���ڻ��������з����й�������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ʿ������������˿�Ҳ���٣�ȴ���������ǿ��������ҵ������ҵ��֤ȯ�г����ƽ��г�����ʿ����ҵ���Ĵ�֧��������ʿ���õ���Ҫ���棬��20�����ҵ����ʿ�����������׽�����֮һ����ʿ����ҵ��ʷ�ƾá������ƶ��ϸ񡢷���רҵ�����ʻ��̶ȸߣ���ȫ�����ҵ�ж���һ�ġ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ʿ����ҵ������16�������ڣ�18�����γɹ�ģ��������ʿ��1815���������������ߣ�����Ϊ���������ȫ���ʱ���ŵأ����ϵ�����λ���ƣ��Լ�˰���ºͼ���ʿ���ɱ�ֵ�ȶ�����Ϊȫ������ʲ���֮���͵ıܷ�ۡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ʿ������������˽������һֱ����Ϊ���������ȫ�����У������ƾõĲƸ�������ʷ�ͷḻ�Ľ��ڹ����顣��ʿ����312������2011���ֵ��350����ʿ���ɣ���ʿ����ҵ���ʲ�����Ϊ����ҵ�񣬹����ʲ��ܶ��5.3������ʿ���ɡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ʿ��228�ұ��չ�˾���ʲ�Ͷ���ܶ��5100����ʿ���ɣ���������Լ545����ʿ���ɣ�ռȫ���г��ݶ��5.5%���˾�����ŷ�޵�һ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1995�꣬��ʿ֤ȯ��������ȫ���������������ߵ��ӽ���ϵͳ��2012�꽻�׶��9000����ʿ���ɡ���ʿ�����������������˾��ƽ𴢱�ȫ���һ��ȫ��40%�Ļƽ�������ʿ���У���������ȫ����ڶ���ƽ����г���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;2008����ʽ���Σ������ʿ����ҵ�����ϴ�����������ʿ���ڷ��ɺͼ����ϵ����걸����������д����ۺ񡢾�Ӫ�������������к�����������ҵ�ڹ��ʽ���Σ������Ӫ�Ƚ���ΪӦ�Թ��ʽ���Σ������������ʿ��ȫ�����ϵ�����Ʒ��ɿ�ܣ�Ŭ��ά����ʿ���ʽ������ĵ�λ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ʿ������ͬ���й���������������֮һ��Ҳ���й���ŷ�޵���Ҫ���ں������֮һ����ʿ���ڽ��ڣ�������ḻ������Ϊ�����ܶ���ߵĹ��ҡ����𷽼�ǿ���ڼ�ܡ�������ߺ������ʱ��г���ϵ�ȷ���ĺ��������й������;��÷�չ�Ŀ͹���Ҫ���й����������ҵ�ĸ￪�ţ������Ȳ��ƽ������г����ĸ��Լ�������ʱ���Ŀ�ɶһ�����������Ͷ���߾���Ͷ���ƶȡ����ƽ��ڼ�ܻ��Ƶȣ��⽫Ϊ����������ҵ������չ�ṩ�µĻ�����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������������ʿ����ҵһֱʮ�ֿ����й������������ŵ���ʿ�������ı���һ��������ٷ�չ���й������г������������й�����ҵ��չ��ͷǿ�����������ڷ��������Ӵ󣬾���Ͷ��������������������й���������Ƹĸ�����Ŷ����ƽ��г����ĸ����ӿ��߳�ȥ��������ս����ʿ����ϵͳ����Ͻ�ֵ���й�ѧϰ����������������й�����ҵ�����������;������������ڽ��ڷ�������ž޴󻥲��ռ䣬�����ǿ������������������ڹ��ʽ��ڻ����м�ǿЭ����ϣ��Դٽ��й�����ҵ��չ���ƶ��й�������ҵ��һ�����߳�ȥ��������Ҫ���塣<br/>");
			news2.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news2);
			newsItemDao.saveNewsSearchRstItem(news2);
			news.add(news2);

			NewsSearchRstItem news3 = new NewsSearchRstItem();
			news3.set_id("000000000000000000000003");
			news3.setProgram_wiki_keys(Arrays.asList("����˹̹�����"));
			news3.setTitle("����˹̹����� ");
			news3.setLink("http://news.xinhuanet.com/2013-05/25/c_115907608.htm");
			news3.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("�»����������£����յ� λ����ʿ��������ʷ����ݶ���İ���˹̹����ݣ����������ꣲ�£�����ʽ���ݡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����˹̹�������Ϊ�����˹̹����������������������衣���������������������ꡣΪ����������갮��˹̹�ڲ�����ȡ�õľ�����ͻ�ƣ����������겮����ٰ��ˡ�����ۣ������꡷��ʱչ�������ڵ���ι����ڶ࣬��ʷ����ݾ�������ʱ��չ��������ɳ���չ�ݡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����˹̹����ݹ�չ�����������ʵ���д��Ӱӡ���飬�ع˰���˹̹��һ�������ְ���˹̹ʱ������ʷ��չ��������˹̹�ɳ�����չ�����Ӹ��׵�������ѧ��ʱ����������ӣ�������������Ӱ�졣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;չƷ�м��а���˹̹���ֱ�ʹ�ù�����Ʒ�����е�ʱ�Ĵ��ֻ����绰��ˮ������С�̵��ʵ����⣬չ�������������ִ����������˴�����ɹ��������ΪͶӰ��Ļ�����е�˵�����õ¡�����Ӣ�������֣����⻹�����������Ե�������˵¼������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ڰ���˹̹�����ù�����Щ��Ʒ֮�䣬���˸о��·�Խ����ʷ��ʱ�գ�����λ��ѧ�޽�������һ�������ĶԻ���<br/>");
			news3.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news3);
			newsItemDao.saveNewsSearchRstItem(news3);
			news.add(news3);

			NewsSearchRstItem news4 = new NewsSearchRstItem();
			news4.set_id("000000000000000000000004");
			news4.setProgram_wiki_keys(Arrays.asList("����˹̹�����", "��ʿ"));
			news4.setTitle("�ҹ�99.7%������ʿ��Ʒ�����˰ �ٿ���ŷ���г�");
			news4.setLink("http://www.chinadaily.com.cn/micro-reading/dzh/2013-05-26/content_9136386_2.html");
			news4.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("[��Ҫ]  24�գ����ǿ�ڲ������ϯ��ʿ������ϯë�׶����еĻ�ӭ��ʽ���߻���ָ��������֮��Ĳ�ҵ�ṹ��ó�׽ṹ������ǿ��˫������Ǳ������óЭ���Ĵ�ɣ�����һ���˫����ó�������ƶ�˫����ó��ϵ������̨�ס�������óЭ����Ϊ��ŷó�׿����µĻ��ᣬ��Ϊ�й���ŷ�˽������ӽ��ܵľ�ó��ϵ��ʾ�����á�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����Ժ�������ǿ24���ڲ���������ʿ������ϯë�׶����л�̸��˫��ǩ���˽���������óЭ��̸�е��½ⱸ��¼�����־��˫����2010����������ó��̸�л��������䶨����Э��ǩ�����׼ʵʩ��һ��֮ң��˫���������������ڶԻ����ơ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ǿ˵������ǰ�ҷ�����ʿ�ڼ䣬��������������ó̸�С�����˫����ֲ�и��Ŭ�������մ����һ����ˮƽ�������������ݻ�����Э���������й���ŷ�޴�½���ҵĵ�һ����óЭ��������չ�������������ŷ��ó��ϵ������Ҫ����ʵ����ͳ�ԶӰ�졣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ǿ˵������˫��Ӧץס����������Ͷ��ó�׹�ģ���Ż��ṹ����ǿ�ڸ߶˻�е���졢����������������ҩ�����ܻ������ִ�ũҵ������Ļ����������ƶ�������ó��������ȫ���������úý��ڶԻ�����ƽ̨����ڽ��ڼ�ܡ�������ߡ������ʱ��г���ϵ�ȷ���ĺ�������ǿ�ڹ��ʻ��һ�����֯���������еȹ��ʽ��ڻ����е�Э����ϣ���ͬ�ƶ�������ƽ�����������ݡ�����Ĺ��ʽ���������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ë�׶�˵�����ǿ����˷óɹ���˶�����������óЭ������չ���ڶԻ�����������ϵ��һ��̱�����ʿ���ҳ�Ϊ��һ�����й�������ó���ͽ��ڶԻ����Ƶ�ŷ�޴�½���ң����Ž������ٽ���ʿ�ķ�չ����Ϊ���й�ϵ��Զ��չ�춨���Ӽ�ʵ�Ļ�������Ը���з��ȫ��λ�������ƶ�˫�߹�ϵ������̨�ס�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>���</b><br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>99.7%������ʿ��Ʒ���˰</b><br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�й����񲿳��߻���24���ڲ������ʾ��������óЭ����һ��ȫ��ġ���ˮƽ�ĺͻ������ݵ�Э����ϣ������˫��ץ��������������ɸ��Թ��ڳ���ʹЭ�龡��ʵʩ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�߻��Ǳ�ʾ��������óЭ������˵�ǽ�Щ���й������ɵ�ˮƽ��ߡ���Ϊȫ�����óЭ��֮һ��Э�������˰�����ܸߣ��𷽽����з�99.7%�ĳ�������ʵʩ���˰���з�������84.2%�ĳ�������ʵʩ���˰��������ϲ��ֽ�˰�Ĳ�Ʒ����ʿ���뽵˰�Ĳ�Ʒ������99.99%���з���96.5%��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�߻���ָ��������֮��Ĳ�ҵ�ṹ��ó�׽ṹ������ǿ��˫������Ǳ������óЭ���Ĵ�ɣ�����һ���˫����ó�������ƶ�˫����ó��ϵ������̨�ס�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ƣ�����ó�����Դ��ھ����ԣ��й�ӵ�о޴���г�����ͷ�չǱ�����й���һЩ����ҵҲ���߾���������ʿ��ŷ�޷�����ң�ӵ���ۺ�ĿƼ�ʵ���Ͳ�ҵ���ƣ��ھ�ϸ�������ӱ�����;����Ǳ������ӵ�нϴ����ơ���ǿ�ľ��û�����ʹ����ó��ϵ���ڱ��ֽ���ƽ�ȷ�չ��˫��ó�׶�ӽ���֮����680����Ԫ��չ��2011�괴��¼��308����Ԫ��2012�꣬���ŷծΣ���������ͺ����羭�ö����Ĳ������أ�����˫��ó�׶���Ȼ�ﵽ263����Ԫ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>�������ҹ�����ŷ���г�</b><br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�������ǿ����24������ʿ���ڽ���ʿ��ͻ����ݽ�ʱ��˵��������ó����������岻���������������������й�ͬŷ�޴�½���ҵĵ�һ����ó�������й�ͬ���羭��20ǿ���ҵĵ�һ����ó�����������������ش����ã��Է�չ��ŷ��ϵ���ش����ã����������Ҳ�������Ҫ��ʾ�����������á�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������óЭ����Ϊ��ŷó�׿����µĻ��ᣬ��Ϊ�й���ŷ�˽������ӽ��ܵľ�ó��ϵ��ʾ�����á���ʿ�Ƿ�ŷ�˹��ң�����ŷ��ͬ��ŷ�޾��������໥֮��ǩ���˴�Լ120������Э�飬ʵ������ó�ס���Ȼ����ԭ�������ƣ���������ó��������ζ���й���Ʒ��������ʿ���������ɽ���ŷ���г�������Ϊ���й����ڰ��Ʒ����ʿ�ӹ���������ŷ���ṩ�˿��ܣ��Ӷ���һ���̶�����չ��ŷ���г���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���⣬��ʿ����������Ͷ�ʷ��滹�������ȶ�����ҵ��Χ�Ѻá��Ͷ����г���������ʩ���á����л�����������ơ���ʿһֱ���Ҷ�λΪ�й���ҵ����ŷ�޵��Ż���ϣ��ƾ����һ��������Խ��Խ����й���ҵ�仧��ʿ�����˽⣬Ŀǰ�Ѿ���65�����ҵ��й���ҵ����ʿ�����˷�֧�����������޽���óЭ�����ɻ�ʹ��ʿ����һ���Ƹ������ԡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>����˹̹�����</b><br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;λ����ʿ��������ʷ����ݶ���İ���˹̹����ݣ�2007��2��1����ʽ���ݡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����˹̹�������Ϊ�����˹̹��������100��������衣2005�������������ꡣΪ����1905�갮��˹̹�ڲ�����ȡ�õľ�����ͻ�ƣ�2005�겮����ٰ��ˡ������100�꡷��ʱչ�������ڵ���ι����ڶ࣬��ʷ����ݾ�������ʱ��չ��������ɳ���չ�ݡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����˹̹����ݹ�չ��200���ʵ���д��Ӱӡ���飬�ع˰���˹̹��һ�������ְ���˹̹ʱ������ʷ��չ��������˹̹�ɳ�����չ�����Ӹ��׵�������ѧ��ʱ����������ӣ�������������Ӱ�졣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;չƷ�м��а���˹̹���ֱ�ʹ�ù�����Ʒ�����е�ʱ�Ĵ��ֻ����绰��ˮ������С�̵��ʵ����⣬չ�������������ִ����������˴�����ɹ��������ΪͶӰ��Ļ�����е�˵�����õ¡�����Ӣ�������֣����⻹�����������Ե�������˵¼������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ڰ���˹̹�����ù�����Щ��Ʒ֮�䣬���˸о��·�Խ����ʷ��ʱ�գ�����λ��ѧ�޽�������һ�������ĶԻ���<br/>");
			news4.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news4);
			newsItemDao.saveNewsSearchRstItem(news4);
			news.add(news4);

			NewsSearchRstItem news5 = new NewsSearchRstItem();
			news5.set_id("000000000000000000000005");
			news5.setProgram_wiki_keys(Arrays.asList("�й�", "��ʿ", "ó��"));
			news5.setTitle("������óЭ����ζ��ʲô��");
			news5.setLink("http://bjyouth.ynet.com/3.1/1305/26/8033983.html");
			news5.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("����ǩ�������óЭ��̸���½ⱸ��¼ ��Э��ǩ�����׼ʵʩ��һ��֮ң��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;5��24�գ����ǿ��������ʿ����������ʿ������ϯë�׶����л�̸��˫��ǩ���˽���������óЭ��̸�е��½ⱸ��¼���������������ڶԻ����ƣ����־��˫����2010����������ó��̸�л��������䶨����Э��ǩ�����׼ʵʩ��һ��֮ң��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ǿ��������ʿ���ڽ���ʿ��ͻ����ݽ�ʱָ����������óЭ���ɹ��ḻ����һ����ˮƽ�����ݹ㷺��Э����������ó����������岻���������������������й�ͬŷ�޴�½���ҵĵ�һ����ó�������й�ͬ���羭��20ǿ���ҵĵ�һ����ó�����������������ش����ã��Է�չ��ŷ��ϵ���ش����ã����������Ҳ�������Ҫ��ʾ�����������á�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����̸�У��ӷ�ѩ�������������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ǰ��2010��1��26�գ���ʿ�����ᡣʱ�ι���Ժ���������ǿ����ʿ������ϯ�����ع��¾��л�̸��˫��һ�±�ʾ�����ڵ���2�¾��е�һ���й�����������ó�����Ͽ������о��Ļ��飬��ȡ����ȡ�óɹ�������������ʽ����̸�С�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�»��統ʱ�ı���������������Ĳ����ᣬ��ѩ����������������������˫���ӿ��ƶ���ó���������о���̸�н��̵���Ϣ���ܵ��㷺��ע������Ϊ������������ů�⡣��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�������2013��5��24�գ����������ֵļ��̸�к�������ó̸�����տ��������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��óЭ�������ٽ�˫����������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�й��ִ����ʹ�ϵ�о�Ժŷ���������Ž���Ϊ������ǩ����óЭ�������Դٽ�˫������������Ҳ����Ϊ˫�������ߴ��������˵Ĳ�Ʒ���й����´��ڲ�ҵת�͵Ĺؼ�ʱ�̣�ͨ������ʿ���¼������߶˲�ҵ�ĺ������������й��Ĳ�ҵת�͡���������ʿ������С�;�������˵���й��Ӵ���г����������Ʒ�ľ�������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���⣬���й�����ڸĸ�Ȳ��ƽ������г����ĸ�Ĵ󱳾��£�����ҵ�߶ȷ������ʿ��Ϊ̽���е��й����ڸĸ���������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�������� ���ͷ���ó��̸��·��ͼ�ź�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�Ž���ר����Ϊ��������ó���ļ�����������������̸�е��������ң����������о�ǩ����ó���Ĺ�����˵���ƶ��������ԡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��Ϥ�����ڲ������й�����ȫ�г����õ�λ��ŷ�ˡ������ȶ����������Ŀǰ��δ���й�ǩ����óЭ��������һ������ǰ���й��ͱ���ǩ����óЭ���������й��״κ�ŷ�޹���ǩ����óЭ�顣����ܺ;��õ�λ������Ҫ����ʿǩ����óЭ���������ֽ�ǰ��һ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���۾ݴ���Ϊ������ͨ�����֡���С���󡱡������׵��ѡ���·��ͼ�������ɹ����壬�𲽽����������ó������Ȼ��ʿ����ŷ�˹��ң�����ʿ��ŷ����ǩ����˫����óЭ�����й�����ʿ�����óЭ����Ϊ�й���ŷ��չ��˫����ó̸���ṩ�˾���Ϳ����ԡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��/�������ߡ������<br/>");
			news5.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news5);
			newsItemDao.saveNewsSearchRstItem(news5);
			news.add(news5);

			NewsSearchRstItem news6 = new NewsSearchRstItem();
			news6.set_id("000000000000000000000006");
			news6.setProgram_wiki_keys(Arrays.asList("����˹̹", "��ʿ"));
			news6.setTitle("��ʿ����ô������˹̹");
			news6.setLink("http://www.stdaily.com/kjrb/content/2010-06/09/content_196470.htm");
			news6.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("������˹̹����ʿ�ˣ���������һ������仰�ĵ�һ��Ӧ���ǳԾ���ֻ֪������˹̹�ǳ����ڵ¹�����������������������������ʿ��������ʷ����ݹݳ���������ʿ�����й����ߣ�����˹̹��������ʿ��������ʿ����������˫�ع�������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����й��˶�֪������ʿ���ֱ��������磬Ҳ֪����ʿ�ľ���ȫ����������ʵ��ʿ�������Ժ��Ļ������������Ĺ��񡪡�ΰ�������ѧ�Ұ������ء�����˹̹��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;2005�꣬�ǰ���˹̹���������100���ꡣΪ�˼�����һ�����ѧ��չʷ���ش���ʷ�¼�����������ʷ������صؾٰ��ˡ�����˹̹չ������Ϊ100��ǰ������˹̹���������ʱ���Ǿ�ס�ڲ����ᣬ��ר���ֵ�һ��СְԱ��δ���뵽������˹̹չ�����˴�������ʿ����ǰ���ιۡ�����770���˿ڵ���ʿ�����У�����35�����߽���������ʷ����ݣ��߽�����˹̹��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����ʿ�����У�����˹̹����һ����ͨ�ˣ���һ��������ѧ��������ΰ���׵���ͨ�ˡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;³���򡤰������о�����˹̹��ר�ҡ���λ�Ѿ����ݵ�����������ѧ����ѧ�������ڵ�ְ���ǡ�����˹̹�о���᳤����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��1896��1900�꣬����˹̹������������ѧ�Ͷ�����69��İ������й����߽����˰���˹̹�Ĺ��¡�����˹̹���ϴ�ѧʱʮ��ϲ����������������ʵ������û��ʲô��˼��������ѧ������ִ�����Ŀγ̣���Ӱ���������γ̵�ѧϰ���ɼ������ã�������ʦһ����������߳�ȥ������ʦ��û��ע�⵽���ѧ���Ѿ�֪������ô����ִ�����֪ʶ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������ѧ��һ��������˹̹���ϴ�ѧʱҲ�����ӿΣ�������ѧ�Σ�������Ժ������ѧ�о�����������˹̹��1905���������������ۣ���������ۣ������Ž�һ�����о����������˹�������۵��뷨���ݰ��ؽ��ڽ��ܣ�����˹̹��Ϊ��ѧ�е㲻���ã����ֻ������ѧ�ر�õ�����һ���о�������Ȼ������˹̹�ܺ����������������ѧ�ڼ�û�кú�ѧ��ѧ������ֻ�á��񲹡���ѧ������������˹̹������ʽ�����˹�������۵����ġ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����˹̹Ҳ�ӿΣ���ʿ�˽������ǵĽܳ���ѧ��ʱ�����ܻ���һ�㡣���ǲ����񻯰���˹̹�����ǲ����й������Լ���ѧ�ҵĳɳ�ʱ����ϲ�����조�ߴ�ȫ��ʽ�����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����˹̹��ȷ�й��ӿε���ʷ�����������Ǹ�����ѧϰ���о����ˡ����ӿβ���Ϊ���������֣�����Ҫ�����пα���û�л�δ����������ѧǰ��֪ʶ��ʵ���ϣ������ӿΣ���������ƫ�Ƶ�һ�ֱ��֡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;5��30�գ�����˹̹չ���й�������Ļ������һ��ý��Դ�������ʱ���ˡ�����˹̹��ѧ����ù�1�֡�����Ŀ���ⲻ֪������˹̹���Ե�ȷ�ù�1�֣����������������δ˵����һ����1��Ϊ6���Ƶ�1�֣����ǰ���˹̹���Ǵ�ѧ�����1�֣�����ʵ������ù�1�֡������й���ϲ���ӡ����Ե�1�ֵ�ŵ�������������ĽǶȰѰ���˹̹���Ϊ����š�������ʿ��ֻ�ǰѰ���˹̹�������š�������ͨ�ˡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�߽���������ʷ����ݣ�������¥����˹̹չ�Ĵ���������ӳ����������һ���޴����Ƭ������˹̹�뼸λ��ѧ�ҵĺ�Ӱ�����˰���˹̹���⣬������λ����װ���ģ�ϵ�������ֻ�а���˹̹����һ�ģ����Ĵ��²�ϵ�ۣ�������һ�������Ҳ������������΢Ц�ؿ���ÿһλǰ���ι۵��ˡ���Ƭ�Աߵ�����˵����д�ţ�����˹̹����ע���������ݵĽ���Ա�����й����ߣ����ǰ���˹̹�ĵ��ͷ�񡪡�����˲�һ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ڲι۹����У�һֱ��Ѱ�Ұ���˹̹��������Щ�ط�����˲�һ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1885�ꡪ1894�꣬����˹̹��Ľ�����ѧ��ȫ��52��ͬѧһ������һ�ź�Ӱ������Ա���й�������һ����һλ��С����˹̹���������ƺ�������������С��������Ĵ�����ѧ�����񡣽���Ա���ѣ����������Ǹ����ڲ�ͬ�ĺ��ӡ�51�����Ӷ������ע���������ֻ��һ�����ӳ��ž�ͷ΢Ц����ĺ��Ӷ��¹ڳ�����ֻ���Ǹ�΢Ц�ĺ��Ӵ���ֻϵ���ϱ�����Ŧ�ۣ��±߿ճ��š�������Ӿ���С����˹̹�����ź�Ӱ��ǰ�����Ű���˹̹�������ơ�������Ȼ�����������������һ����һ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ѧ�����������顢��������顢���ӡ��ٻ顢���С��񽱡�������˹̹����������˲�һ���أ���ָ�������������ۣ��ֻ���ָ�����ŵ��������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;û���ҵ�����˹̹������˵Ĳ�һ�������෴��������ʿȴ�˽��˲��ٰ���˹̹������˴���һ���ĵط����ڰ���˹̹�ʾӣ�����˹̹����ݹݳ���������һ��1901��ı�ֽ��ӡ�������й����ߣ����갮��˹̹û�й��������ڲ����ᵱ�صı�ֽ�Ͽ��ǹ���ҹ�����ϣ������ͥ��ʦ����Ǯ��������˹̹��ѧ��ҵ��Ҳ���Ҳ���������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1900�꣬����˹̹��ѧ��ҵʱ������������ѧУ���������û�гɹ���8��������ڻ�׼�ڲ������ѧ�������ˡ����ܿ�ϧ��û�м���ѧ��ϲ�������ĿΡ���������ʷ�����ݹݳ�Ƥ�ز�ʿ�ر����й�����չʾ�˵��갮��˹̹�ġ���������1908��7��6�յ�һ�ݰ���˹̹�Ͽεļ�¼���������������������������ۡ��γ�ʱ��ֻ��3��ѧ�������ĿΡ���һ��ļ�¼��4��ѧ���������Ľ��Ρ�����У�����ò�ȡ�������Ŀγ̡�������˹̹�Ľ�ѧ����Ҳ��������ǡ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ι۰���˹̹չ�����߽�����˹̹�ʾӣ�̽�ð���˹̹����ѧ���ν̵�����������ѧ��һ����С�£�һ�������£������������������ʿ�����а���˹̹������˲�һ��������ʵ���壺����˹̹����Щ�򲻹���Ц���ظ�����Ŀ�ѧ���ǲ�һ��������ȴ������ͨ�İ�������̫�����ͬ���������������Ǹ���ͨ�ˡ����񲮶�����ʷ����ݹݳ���������ʿ��˵������˹̹��������ھ�һ�����С�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����˹̹�����缶�Ĵ�����ѧ�ҡ���ʿ������Ϊ�Ժ���������ʿ�˲�û�а������񻯡��������й����߽��ܰ���˹̹ʱ�������Ҳ���������������������̫�٣���������ĳЩ���ﶼû�б��������������ǿ�����һ����ʵ�İ���˹̹��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����ʿ�ι�̽��֮�ʣ�������������һ����֮��ȥ�����ʣ�Ϊʲô���й���ý�������������й���ѧ���Ƕ��ǡ��񡱻����ˡ���Ҫô�ǿ�ѧ�ұ��˳��˿�ѧ�Ĵ����ˡ������ʣ�Ҫô�ǿ�ѧ���ڴ��¿�ѧ�о��Ĺ������ƺ�Ҳ����˵��¼�����ġ��𻯡���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ô��͸����ʿ�˿�����ѧ�ҵ�̬�ȣ������Ƿ���Щ˼���أ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�������л����񹲺͹�����ʿ�����60���ꡣ��ʿפ��ʹ�ݺ���ʿ��������ʷ������ڱ����ر�ٰ��ˡ��������ء�����˹̹��1879��1955����չ��������ף��������60���ꡣչ����չ����200���չƷ�����а�������˹̹��ǰʹ�ù�����ص���Ʒ�����ܰ���˹̹�������������ʷ������Ӱ�����ϣ��Լ�һЩ��Ⱞ��˹̹��ѧ����Ļ���չƷ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;Ϊ�����й��˸�ȫ����˽Ⱞ��˹̹���˽���ʿ����ǰ����ʿ��������ίԱ�������й����ߵ���ʿ�ι۷��ʣ����е�һ����Ҫ���ݾ����˽Ⱞ��˹̹����ʿ����ʷ��<br/>");
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
			news1.setProgram_wiki_keys(Arrays.asList("������"));
			news1.setTitle("�������ֻ������ȫ�����ߣ���ױ��Ѷһ���ƿ�!");
			news1.setLink("http://beauty.pclady.com.cn/fashion/1305/971161.html");
			news1.setUpdateTime(new Date());
			StringBuffer des = new StringBuffer();
			des.append("��Ҫ�˽���ϲ���ĳ�ױƷ����Ҫ��������?�򿪵��ԡ�������������ҹ������������,���ܵ��л���Ҫ�������롭����ʵ���鷳�˵㣬������?������4������ȫ���ֻ�����������ڿ�ʼ��ֻҪ���ֻ��������������������ֻ����������m.maybellinechina.com������ʱ��ض��ܿ�ݷ�����˽����������顣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�Ҳ�Ʒ����ר�����˽�������ǲ�Ʒ��Ѷ�������������ɰ�����!����һ�����볱ױѧԺAPP���أ�������ױ�̳�,�����ְ���ʾ������������һ����ױ����!�����Ʒ���������̳ǵ���������Ŷ!<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�㻹����ͨ���ֻ��������ע����������΢��!�Ӵ˳�ױ��Ѷһ���ƿ�!<br/>");
			news1.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news1);
			newsItemDao.saveNewsSearchRstItem(news1);
			news.add(news1);

			NewsSearchRstItem news2 = new NewsSearchRstItem();
			news2.set_id("000000000000000000000012");
			news2.setProgram_wiki_keys(Arrays.asList("������"));
			news2.setTitle("��ë�൮��100��ȫ��֮�� ������ŦԼ�ɼ���ë������");
			news2.setLink("http://fashion.ifeng.com/beauty/detail_2013_04/25/24646016_0.shtml");
			news2.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("�������˻���֪�������ϵ�һ֧��ë�൮������������ȴ��֪���ĵ���Դ��һ�������İ�����£�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ʱ�������100��ǰ��1913�꣬����˹*����ķ˹��Thomas Williams����֥�Ӹ�һλ����Ļ�ѧʦ��������������*����ķ˹��Mabel Williams��������һλ��ʿ���أ�Ȼ����ʱ������ȴ����������Ϊ�˰����İ�������������Mabel ��Ӯ�������ˣ�����˹������һ����̼�ۺͷ�ʿ�ִ���ɵ����ޡ�ħ������������ӵ�������˵�˫�ۣ�������������ճɾ�����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����ķ˹�ɴ˴�����У���ʼ�����ڲ�ױ��ҵ����������Ʒ�Ƶĵ���Ҳ��Դ���������֡�Mabel�� �ͷ�ʿ�ֵ�ƴд��ϣ��������������������������İ���������Ʒ�¾�������ķ˹Ҳ��Ϊ�պ��������ʵ���������ױ�����Ĵ�ʼ�ˡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��1913����֧����ħ���ĵ�������100��䣬��������ë����ȫ����Ů�����������ге��ž������ص����ã�Ϊ��ͬʱ����Ů�Ծ�����Ů�������ԣ������������ݳ������ƶ������ݲ�ҵ�ķ�չ�����ֳ�һ�������ʱ�����ҹ������˵���������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;Ů�Զ�˫����׷��Ҳ�Ƕ����ң����ɣ����ŵ�׷��������һ���ƿ���Щ���仭�����½�ë��İ������̡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1917�꣬ȫ���׿��װ��״��ë�൮���������ˡ��Ȼ�˫������˫����ҫ���˹�ʡ���ȫ�����������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;20����20��30�������״��ë�����ȫ������֧��ʱֻ��10���ֵ����ޱ��棬����ѳ�Ϊ�ղؼҵİ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;20����40��50��������������ǵ�ʱŮ����׷�����ʱ�еĳ�������ʮ�������������Ϊ����������Ů�˺���*���꣨Hedy Lamarr������л������Ϊ����������ҫ��������һЩ�����ϵ�Ӱ����������*�����( Joan Crawford)������*����˿(Rita Hayworth)�ȶ��׷�Ϊ�������Ľ�ë�ຣ�����Գ�����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;20������ʮ����� ȫ���׿��ˮ�͹�װ��ë��Ultra Lash�����������ԵĴ��¼�֤��60���ʱ�г����ĵ�����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1971�꣬����ȫ���������͵�Great Lash�������ӳ�������ɫ�ʵ�����������Great Lash��������ȫ���г������е�һ��������ë�࣬������ÿ1.5��һ֧�������ٶȡ�������ʤ�ط��õ��ϸ��ܣ���ʱ�������пڽԱ����ÿ��ë���ǻ�ױʦ�ıر���������ʱװ������ĺ�̨�洦�ɼ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1990�꣬��һ֧��ë����ҵ�������Ʒ�ƣ��Ѿ�ӵ�к����ۣ�����������ȫ��λ��Ʒ�ߵ�ȫ������ױ�����������������������ģ�����������������Ϊ����ȫ��Ŀںţ���������ÿһλŮ��չ�����������������������������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�������������й�ӵ��11֧��ͬ�Ľ�ë���䷽�����߿Ƽ�������ʱ����һ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���죬ʱ����ë��������������100�꣬��ȫ��Ů�Զ�����׷���100��ǰ�ġ��Ȼ�˫������չΪȫ��λ���������ƺ���������֮�ʣ��������ڴ���Ů��������ױ��������Ҳ��ӭ��ȫ�µ�ƪ�¡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;2013�꣬������Ŀ�ġ��ɼ���ë�ࡱ ȫ��ͬ�������У���Ũ�ܣ��ɾ������ٹ�Ч��һ��Ķ����䷽�͸����ԡ������ٲ���顱ˢͷ�����ٴ�����ȫ�������³����� ���ǶԴ�ʼ������ķ˹���������������¾������Ƕ�׷���������������ң����ϳ�Խ������������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��Ʒ���ԣ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ɼ���ë����Դ���Ũ�ܡ��ɾ������ٵĽ�ëױЧ�������Գ����ٲ����ˢͷͬʱӵ��Ӳ�ʺ��ĺ�����ˢë��Ӳ�ʺ��Ŀ��Ը�������մȡ���壬������ˢë��ͷ���ܾ��Ȱ�����ë��ˢ����ը��Ũ�ܣ�����������ױЧ���Ử����״��������ױ����˳�����������������ױЧ������ȫ�����Ⱦ����ˮ����ж��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��Ʒ��Ч��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���٣������Գ�����ˢͷ�� ����������˳�Ũ��ױЧ���ɾ�����飻ȫ�����Ⱦ����ˮ��ж<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�۸�/������99Ԫ/ 10 ml<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����ʱ�䣺2013��2��<br/>");
			news2.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news2);
			newsItemDao.saveNewsSearchRstItem(news2);
			news.add(news2);

			NewsSearchRstItem news3 = new NewsSearchRstItem();
			news3.set_id("000000000000000000000013");
			news3.setProgram_wiki_keys(Arrays.asList("������"));
			news3.setTitle("������ŦԼЯ���Ϻ�ʱװ�� �����³���");
			news3.setLink("http://beauty.pclady.com.cn/96/966874.html");
			news3.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("�Ϻ�ʱװ�����㱾�����ʦ�����㳡����̨ױ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;4��11�գ��Ϻ�ʱ�еر�������ǹ��貣��Ϻ�ʱװ��ӭ���й������������ʦ֮ҹ�����У���껣���Щ��ܳ����й�������������2013�ﶬ��Ʒ�Ⱥ����ʱװ����̨��Ӱ�������ڶ����һ�����Ǻ�ʱ�н���ʿ�׷�ǰ����Ϊ�㳡ͷ�ſͣ�Ϊ�й����ʦ����������������һ�������������е��й�ʱ�������ľ�������������ŦԼ�����ĳ���ױ�ݡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ױ�ݽ����� ��ˮܽ���Ǳ��μ����㳡ױ�ݵ������Դ�����ñ�͸�ĵ�ױ����ɫ��ױͻ��Ů��������͸�����ʡ�����BB˪����ˮ�󼡷�����ɫ��ɫ����ױ�㴽����ü��֮������ϵ���Ƭ����������ױ�ݣ������������ֲ�ʧ������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���У�ֲ�����Ϻ������᱾�����ʦ��������������߸��й���ͳ����ֽ��й�Ԫ��������, ��Ƥ�����һ�𣬴�������صķ��������ѧ����������������ġ�Marangoni��ʱװ���ѧԺ���Ⱥ��Ρ� Basic Krizia���͡�Missoni Sports����Ʒ������۵����ʦ��֮����Ϊ�����ŮװƷ�ơ�D��A�����Ӿ������ܼࡣ�������ĸ���Ʒ��La Vie���й�Ԫ������ʽ��������ϣ������ڱ����صĸ������ʣ�Ϊȫ��Ů���ṩ�˸�Ʒ�ʵ�������׼���������ɵĴ��⡣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ױ�ݽ�������࣬��͸���Է��Ǳ�������㳡ױ�ݵ���ɫ���ڣ�Ӳ�ʵĴ�ü����͸�ĵ�ױ��ױ�ݵ��ص㡣�÷ɼ���ë��ˢ��üͷ��ÿ��üë������ü�ͣ�����BB˪ͿĨ��ȫ������ͨ͸����еķ��ʡ�˧����ͬʱ��Ȼ������Ů�Ե����ġ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��껣����������װ���ʦ����ҵ��Ӣ���׶�����ʥ������ѧԺ��2007�����׶�ע�������ʦƷ��Qiu Hao��2008��Ӯ�ðĴ���������ŵ��ë��־�󽱣��뿨����ۣ�YSL��Giorgio Armani����ƾ޽�һ��������ë��־�������á���������ǲ��Ϸ�����ʡ�������������Ʒ��Ϊ�Լ�Ӯ�����й�ʱװ�硰���к���������������һ˫���Ƹ����֣����д󵨵Ĵ��⣬���Ļֺ�һƳ����һ������֮����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ױ�ݽ���������ױ���ԡ����ơ�Ϊ���⣬ͻ��Ů�Զ��е���ӯ���鶯��Ϣ�����þ�������۵״����������ױ��ͿĨ������Ͼ����üë�Ĳ��֡��Ա���������֮���ý�ɫ�����ɫ�ͽ���ɫͿĨ�ڴ�����ͻ�����䴽ɫЧ�������������ִ�Ů����ʱ������Ƥ�С�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���责�����ѧ�ڼ��ô���������������������ѧԺInstituto Marango���˶ʿѧλ���������Ҹ߶�Ʒ�Ƶ������Ŀ�����и����������ʦ��ݼ������綥�����������Yumi Katsura����ƹ�������Ϊ���������λ�������ʦ���ó������ŵĶ����������������ʽ�������ںͣ�ǿ��Ů�Ծ��£����ŵ����ʡ���Ϊ����ʽ������������ߣ���������ľ��ºͳ��µ�ʵ��������ϣ��ᳫһ�¶�估�޼����ԵĴ��������������Ƹ���ʵ�������ҵ�������ϵ㡣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ױ�ݽ���������ױ�������Դ�ڡ����������峺�������ױ�ݵĻ��������þ�������۵�Һ����������Ͼ�ļ�����ѡ�񾦲��������߸๴�������ߣ������������У����ѡ����ɫ������Ĩ�ڴ������Ե͵�����������!<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����㣬�������ʦ�������������ʱ������������֮һ��2006�걻ʱװ��ͷH&M��־��Ϊ�й�����Ǳ�����������ʦ���й�ʱ�и����ͷװŮ�����ƺš�HELEN LEE�������ʦ����������ĸ߶˳���Ʒ�ƣ���λ�߶�����Ϊ���Ͽ�����ϸ�ھ��¡��������ȴ����һ��ѭ�浸�ص��ˣ�����������Ȥ����ƣ��Ի����ַ��������ϣ��������еĵ�ιᴩ���ϸ�ڣ���������������ʱ�ڲ�ͬ�ķ�װ��񣬼������ϲ��ٴ��죬ʹ������Ƹ�����ʷӰ���־߸���˼�롣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ױ�ݽ���������ױ�����Դ�Բ������ͼ�ڣ�ģ��ױ������һλ�����е�Ůսʿ�����񣬴��⣬���Ƴ��档���þ��������ʷ۵�Һ���콡����Ȼ��ɫ��ʹ����ʯ��Ӱ�����۲���Ȼ���������߸๴�ձ�����������������ñ��������ڴ�����ס�Ұ�ԣ�ԭʼ����������!<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����𣬱�ҵ������ʥ�����������ѧԺ�����Ⱥ���Max Mara��All Saintsʱװ��˾������2010����Ӣ�������Լ��ĸ���ʱװƷ�ơ�Haizhen Wang����2013���׶ش���ʱװ���ϣ������𷢲�����Ϊѹ�������׶�ʱװ�����᳡Somerset house���У����ڵ�����е�Fashion Fringeʱװ�����аε�ͷ���ô󽱡�������Ʒ�������壬������ǿ�������Ӳ�ʣ�ͬʱ��������Ԫ�أ�����������չ��Ů��������������Ʊ���������ĸ��ܡ�<br/>");
			news3.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news3);
			newsItemDao.saveNewsSearchRstItem(news3);
			news.add(news3);

			NewsSearchRstItem news4 = new NewsSearchRstItem();
			news4.set_id("000000000000000000000014");
			news4.setProgram_wiki_keys(Arrays.asList("������"));
			news4.setTitle("���������߱ʵ�ʹ�÷�������");
			news4.setLink("http://news.xkb.com.cn/life/chuanyidb/145.html");
			news4.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("��Ϊһ������֪���Ļ�ױƷƷ�ƣ����������й�����˵�����Ƿǳ����ܻ�ӭ�ģ��ܶ����ڹ���ױƷ��ʱ�򶼻�ѡ����������������Ϊ��������ױƷ��Ʒ��Ч�����Ǻܺõġ����������߱��Ǻܶమ��Ů�Աر��ģ����Ҽ۸񲢲���ֻ��Ҫ39Ԫ����ô���������߱���ô���أ�����С��������ҽ���һ�����������߱ʵ�ʹ�÷�����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���������߱ʵ�ʹ�÷�����һ����һֻ����������Ƥ����һֻ�ִ����۽������۽Ƿ�����Σ������Ž�ë���������軭�����ߡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���������߱ʵ�ʹ�÷����ڶ�������ɺ󣬿��Կ����۾����˵����߱Ƚ�ϸ�����벿�����߽ϴ�һЩ��������������ȵ�Ч����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���������߱ʵ�ʹ�÷������������۾����Ͽ��������������������뻭�����ߣ�ͬ�������λ��������۽ǵĵط�Ҫ��������������Ȼ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���������߱ʵ�ʹ�÷������Ĳ������ۺ�����ǩ������Ⱦ�����ߣ�һ��Ҫ����4-5�£���������������Ⱦ�Ŀ�һЩ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���������߱ʵ�ʹ�÷������岽����Ⱦ�����ߺܹؼ�����ǰ��������ǰ���ز���4-5�£���΢����������Ⱦ����ǳ�С�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ڿ�������Ľ���֮�󣬴�Ҷ��Ѿ�֪�������������߱ʵ�ʹ�÷����������ܹ��������������߱ʵ�ʹ�÷����Ǻܼ򵥵ģ���Ȼ��һ����˵ֻҪ�Ǿ�����ױ�����Ѷ���֪����ô�õģ������������ֶ��ԣ��Ϳ��Բο�����ķ������������ˣ�Ч�����Ǻܲ���ġ�<br/>");
			news4.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news4);
			newsItemDao.saveNewsSearchRstItem(news4);
			news.add(news4);

			NewsSearchRstItem news5 = new NewsSearchRstItem();
			news5.set_id("000000000000000000000015");
			news5.setProgram_wiki_keys(Arrays.asList("������"));
			news5.setTitle("��������ë����89ȴ��99");
			news5.setLink("http://news.e23.cn/content/2013-04-02/2013040200214.html");
			news5.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("ժ Ҫ����������Ůʿ��Ů����ӳ������������������ר����һ֧���ܽ�ë�࣬���89Ԫ���ۻ�Աȴ��֪����д���99Ԫ����Ůʿ��Ǯ��ȥ��һ��������ר���ۻ�Աȴ��֪�˿��ë��ȫ��ͳһ�ۼ�89Ԫ���Դˣ���������˾³���г���������Ůʿ�ƣ����ݹ�������ʱ�еģ����𲻿����ֲ�Ʒ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������Ůʿ��Ů����ӳ������������������ר����һ֧���ܽ�ë�࣬���89Ԫ���ۻ�Աȴ��֪����д���99Ԫ����Ůʿ��Ǯ��ȥ��һ��������ר���ۻ�Աȴ��֪�˿��ë��ȫ��ͳһ�ۼ�89Ԫ���Դˣ���������˾³���г���������Ůʿ�ƣ����ݹ�������ʱ�еģ����𲻿����ֲ�Ʒ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;30�����磬��Ůʿ��������������ר������һ֧���ܽ�ë�ࡣ�����ܽ�ë������89Ԫ���ۻ�Ա˵��99Ԫ������Ůʿ˵����ʱ�������Ѷ����ɣ���������������89Ԫ��Ϊɶ˵99Ԫ?��Ůʿѯ���ۻ�Ա���ۻ�Ա˵����̨�ϵı������Ǹջ��ģ�����ˣ���ʵ��99Ԫ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;30������Ůʿ������һ��������������ר�񣬿������ܽ�ë��ı����89Ԫ��������ۻ�Ա˵���ܽ�ë��89Ԫ���Ұ�����Ĳ�Ʒ����������������ȷ��˵89Ԫ����˵����ȫ��ͳһ���ۼۡ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��Ůʿ�ٴ��������ѹ���������������ר�񣬡�����������֧��ë�ൽ�׶���Ǯ���Է���˵99Ԫ������Ůʿ���߶Է���������һ��������������ר��ѯ�ʣ�����֪���ܽ�ë����89Ԫ���������˵��绰���ʣ��ʺ�˵��89Ԫ������10Ԫ������10ԪǮ���࣬�������ҿ������ص���Ǯ������ô������������ô�������������?����Ůʿ˵��������Ҳ���ȥ�ң������ۻ�Ա���ܼ�����99Ԫ�ļ۸��������������ߡ������̳������ϵ곤��Ůʿ�ƣ����Ѹ��˿���ϵ������¡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��������˾³���г���ظ�������Ůʿ�ƣ��˿���Ľ�ë��������ϵ�У��о��ܽ�ë�ࡢ���������ᡢ����è��������Ʒ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��˵������������Ʒ�ļ۸��ǲ�һ���ģ�������������ר����ʱ����һ�����ݹ����ϰ࣬�����ֲ�Ʒ�����𲻿������ڼ����ϣ��Ѳ�Ʒ����99Ԫ�������˿��ˡ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��Ůʿ˵�������ݹ��ʲ�δ�Ѵ��·��������ܡ�����Ҳ������һ����ѵ�����ϸ���Ա����ѵ�Ժ��ע�⡣��<br/>");
			news5.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news5);
			newsItemDao.saveNewsSearchRstItem(news5);
			news.add(news5);

			NewsSearchRstItem news6 = new NewsSearchRstItem();
			news6.set_id("000000000000000000000016");
			news6.setProgram_wiki_keys(Arrays.asList("������"));
			news6.setTitle("������BB˪��α�жϷ�������");
			news6.setLink("http://news.xkb.com.cn/life/chuanyidb/144.html");
			news6.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("���ƺܶ��˶���Ҫ֪��������BB˪����α�жϷ�������Ϊ������BB˪����˵�Ƿǳ����˻�ӭ��һ����Ʒ������г��Ͼ���������һЩ��ð��ƷΣ�������ߵ�Ȩ�棬������Σ�������ߵĽ��������Լ��𷽷��ͱ�÷ǳ�����Ҫ����ô������bb˪��α�жϷ�����ʲô�أ�����С��������ҽ���һ��������BB˪�ļ��𷽷���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������BB˪�ļ��𷽷�1�������װ����Ʒ������BB˪���װ������ɫԲ������ı�ԵΪ��ȾЧ��������ð��Ʒ�˴�û����ȾЧ������������װ���棬��Ʒ��ɫ����������������ӡˢϸ����������»��ĵ�ɫ�Ƚϰ������еĴ��ڵ�ɫ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������BB˪�ļ��𷽷�2����BB˪ƿ����Ʒ������BB˪ƿ�������Բ�������ɫ���Ǵ���ɫ��΢΢��Щ�غ졣���ٻ��˴��ĵ�ɫΪ����ɫ��û���κ�������ɫ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������BB˪�ļ��𷽷�3����ƿ�ڣ���Ʒ������BB˪��ƿ������ɫ��ƿ����ɫͬΪ��ɫ������Ʒƿ������Ϊ͸��״��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������BB˪�ļ��𷽷�4����ƿ�ǣ���Ʒ��ƿ�ǵ�����Ϊ���ݵ���Ƕʽ������Ʒ�������Ǽ򵥵���͹ʽ����һ�����������Ʒ��ͬ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������BB˪�ļ��𷽷�5����BB˪���壺��Ʒ������BB˪������ɫ�뼡����ɫ�ӽ������屾��ϸ�����з���������Ʒ�������ɫ������ƫ��ɫ��ͿĨ��Ƥ�����Եð�����Ч�������֪��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ڿ���С���������BB˪��α�жϷ�������֮�󣬴�Ҷ��Ѿ�֪������ô����������BB˪����٣����������ѡ����������BB˪��ʱ��Ϳ��Բο�������ķ����������жϣ�����С����ʾ��ҹ�����������Ʒ��ʱ��һ��Ҫ��ר����й�������Ĳ�Ʒ�Ƚ��б��ϡ�<br/>");
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
			news1.setProgram_wiki_keys(Arrays.asList("�ǳ�����"));
			news1.setTitle("�ǳ�����51��Ů�α��������� �ɹ�ǣ��С17��˧��");
			news1.setLink("http://weifang.dzwww.com/yl/201306/t20130603_8452541.htm");
			news1.setUpdateTime(new Date());
			StringBuffer des = new StringBuffer();
			des.append("[��Ҫ]�ڡ��ǳ����š�����̨�����־���һĻ������̨��һλ51���Ů�α���߷�ͬһλ17���С˧��ǣ�ֳɹ���Ů�α���̹�ϴ���С˧�磬����õ��ػ���ֻ����һ���ߵ���󣬳���İ���ۻ�ù����Ͽɡ���Ϥ��51��Ů�α���߷���һ����ѧ�Ѿ���ҵ��Ů������34����мα���������һλ�����Լ���˾����ҵ�ң���������������12�ꡣ<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ڡ��ǳ����š�����̨�����־���һĻ������̨��һλ51���Ů�α���߷�ͬһλ17���С˧��ǣ�ֳɹ���Ů�α���̹�ϴ���С˧�磬����õ��ػ���ֻ����һ���ߵ���󣬳���İ���ۻ�ù����Ͽɡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��Ϥ��51��Ů�α���߷���һ����ѧ�Ѿ���ҵ��Ů������34����мα���������һλ�����Լ���˾����ҵ�ң���������������12�ꡣ<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����̫�����ȣ��ǵõ�ʱ��λ�ֳ���ʦ�����������״Ρ�������ҭ������Ů�α����Ǹж��õ�����ᡣ<br/>");
			news1.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news1);
			newsItemDao.saveNewsSearchRstItem(news1);
			news.add(news1);

			NewsSearchRstItem news2 = new NewsSearchRstItem();
			news2.set_id("000000000000000000000022");
			news2.setProgram_wiki_keys(Arrays.asList("�ǳ�����"));
			news2.setTitle("�̵�ǳ���������Ů�α���ɱ�� ������ ƭ��");
			news2.setLink("http://finance.china.com.cn/roll/20130601/1519136.shtml");
			news2.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("���ǳ����š�Ů�α��������������ɷ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;1��13�գ���λ������΢���ϱ��ϳƣ����ǳ����š�ǰŮ�α�������ɱ����������������������͸¶��������2012��5�½�飬7�¾�������������˯�е��Ϲ�������ͼ�����ұ������Կ���������������ʶ�ơ�8�����ھ����໤�²���һ��Ӥ��Ŀǰ������������Ѻ�С�9��11�գ������������Ϲ������������������ǹݻ𻯡� <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ݽ�Ů��ŵ<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ŵ��һ�����Ա�����ƽ��ģ�أ������ڽ������ӡ��ǳ����š��жԴ󵨡�Ϭ�������۶�Ѹ���������ϴں죬����Ϊ�Ǿ䡰����Ը���ڱ�����ޣ�Ҳ��Ը���������г���Ц���Ľ�䱻�����ǳ������ݽ�Ů���� <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ڡ��ǳ����š���Ŀ�У���ŵʵ�ڱ���ͻ�����������мα���������ͻ������ɲ��ٻ��⣬Ҳ�����˸ý�Ŀ����ŵҲ��˳�Ϊ��������������ǡ� <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�Ʒｿ��������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�Ʒｿ��1989�������ģ�ء���ױʦ�������������߽����Ů���������������ڡ��ǳ����š����������˶���Ϊ������ˡ����������������Ʒｿ�Ĳ����գ��������㷺���顣�Ʒｿ��󷢳�����������Ƭ������в�����µġ����ڽ������ӵ���ͬ���򾯷������� <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������ƭ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���������������ӣ�ҵ����Ա���������ӡ��ǳ����š��Ľ�Ŀ�α�����Ϊ�μ������Ŀ�����������ߺ죻2010��9�¶��Ժ����������򡶷ǳ����š���ʶ��չΪ���˹�ϵ�����Խ��ΪĿ�ģ����Ժ�Ӧ������Ҫ������������32Ӣ�����Һ������һ̨������318��һ�������Ǽ������������£�������������ܾ��Ͷ��Ժ���飬ͬʱҲ�ܾ��������������2011��5�µף����Ժ�����������Ϸ�ͥ��2011��11��16�գ���������ʵ���������������ʦ�״γ��ϱ���ȷ�����Ժ����ʹ��� <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����䡪�����мα�׷�����Ů�α�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����䣬Ů�������ˡ���ѧ��ҵ��֣���е���ʮ����ѧ��2007����뱱���ִ���������ѧԺѧϰ����μӽ������ӡ��ǳ����š�����Ϊ������ˣ��������ǡ��ǳ����š���̨��������õ�һλŮ�α���Ҳ�Ǳ��мα�׷������һλŮ�α�������20���ڵ�ѡ������ǣ���Ԫ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ģ�Ů���������ӡ��ǳ����š���Ŀ�α�����Ϊ�μ������Ŀʱ�����и��Ե�������Ϭ����ͷ����������ǰ��Ϊ�����ֳ���������Ϊ�ˡ�������Ҫ���ݱ����������������ߺ졣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����٣�Ů���������ӡ��ǳ����š���Ŀ�α�����Ϊ�μ������Ŀ�����������ߺ졣���ĸ�Ц��ʽ����������ͷ�����ֲ�Ť����������ʵ˵����������˵��Ը���������׷����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;л��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;ԭΪ������ͨ��ѧ��ѧ����������ѧ����ȡ�����ִ�����ѧԺ��Ŀǰ�Ͷ�����������ڽ������ӡ��ǳ����š���˵���ǳ����ں���������Ի���������˼�֮�����˳�����������硣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ŊC��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;Ů��ԭ���Ŷ������������ӡ��ǳ����š���Ŀ�α�����Ϊ�μ������Ŀ������Ư�������Ѿ��ó���ˮ������Ա������ģ���������ƣ������������ߺ졣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���类��ְҵ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ǳ����š�15�����磬�Գ���Сѧ�赸��ʦ���ڷǳ������ϣ���5��4�ſ�ʼ��15�ţ�����䷨����������ͻȻת����崿�����ͣ���5��21��22���ڷǳ������У����綼�������ԣ���������ɹ���Լ��������崿��磬ͬʱ�������װ׵�F������չ�ָ����ڣ������������Ѷ�����ļ�����ע�����������������������Ȼ�Ǿ��޵�����ģ�� <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ǳϡ���һ�⼮��Ů����MM֣Т��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����毺á���̬����������֪�ԣ��������ӡ��ǳ����š�����Ů�α�֣Т����������������������Ů���ǣ���̸��ֹѸ�������ڶ���ڣ����ѽ�����Ϊ���ǳ����������⼮Ů�α�����������󷽣�ϱ��Ʒ��ʮ�㡱��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ǳ�����10��Ů�α�������ҽ����е����ҵ���ˣ�������ҵ���Ǹ�����Ů����Ϊ�ڷǳ�����̨�Ϻܾã����ҳ������򣬱�����˹��ڹ�ע��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ձ�Ů�α����ٰ����Ϸ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ٰ����ձ����ĵ������衣����̨��˵�ġ����й������³����;��ÿ�������������δ���Ϲ���һ�м��񡱡�һ��̨Ѹ���ߺ죬����������ֵ���������ֱˬ��Ĭ�ķ�����׷�������ϷǶ��̲�ס��Ŀ��̨�³����ǡ�����Ϊֹ��ϲ����Ů�α�����̨�ϣ��Ϸ�����ÿ�����ⶼ��ѯ�����Ŀ�������ǿ��ҪΪ��ר�š���һ��ϲ��С�����������ѻ�Ĵ��������С����ϷǱ�ʾ��33������ٰ�����׷���Ϻ�������ֱˬ��ϵ��Ը񡢷ḻ����������������Ļ���̬�ȣ����ڶ൥�����ˡ�����ɱ�����������Ѹ��ǽ�����Ϊ�����ŵ������ѡ�����г���������ٰ���N�����ɡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ǳ����š���̨�ϵĻ���Ů�����������𻨡�֮һ��֪��Ů�������ã�����ˡ����á�Ů�ĳƺţ�˽����û��Ũױ��Ĩ������ƽ��û��һ����ɫ������Ҳ�����⡣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;Ѧ���ȫ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���Ʒｿ֮�󣬽������ӷǳ�����Ů�α�Ѧ�������ȫ��˽�����գ��������Ϸ贫��������֤�������˾��ǽ������ӷǳ����ŵ�Ů�α���Ѧ贡��������Ѧ贷粨�ı���ȴ�������ر�ָ��������п����Ǳ��˻򱳺���ŶӾ�����Ƶ�һ��������Ŀ�ľ��������ޡ��Ʒｿһ�����ò������¼�������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��ˬ���廪��ʦ�ش�������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�廪��ѧ��ʦ�͡��ǳ����š�24��Ů�α������������ѵ����顣���ѳ������ǽ�������ˬ�ڽ������ӡ��ǳ����š���ʶ������֪���߱��ϳ�������ԭ���廪��ѧ������ʦ�����������ϡ��ǳ����š�������ǳ�����㡣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�Ȼ��ӣ��������ӡ��ǳ����š���Ŀ12��Ů�α�����Ϊ�μ������Ŀ�����������ߺ졣�Ȼ���2011��12��18�ձ������мα�����ߡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ã�Ů���������ӡ��ǳ����š���ĿŮ�α�����Ȼվ�ڱȽ�ƫԶ��2��λ�ã����ھ�ͷһɨ������˲�䣬���崿���ŵ�������������������̵�ӡ��������������Ѹ���ߺ졣<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��С�����౻��1����Ů<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��������໰��������˶�λ������˵Ľ����������׽�Ŀ���ǳ����š��������ֳɹ���һλŮ�α��ߺ죬������һ����ĺܲ�һ��������û����ŵ���Ը���欣�û������������ʣ�û���Ʒｿ���崿����û��л�ѵ����������������ǳ�С�꣬����Ϊ���ǳ����š�ʷ�����Ů�α���1�š���Ů����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;һ��ʼ�������Ǵ�����������ϧ���۹⿴�����Ů���ģ����ǣ���С��һ�δν��������ı��������ô󲿷����ѷ�θ�ˣ������������硰ܽ�ؽ�㡱����ʱ�����ҳ��������������Ϸ�Ҳ�е㿴����ȥ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����Ƿǳ�����һ�����ӻ������ļ���̱����������������β��ù�ע�ĸ��Ը�������̵�ӡ��ȫ������֮���������Ҳ����ǿ�ҵ����������������񾭽��š�ָ��֮��������ӿ��<br/>");
			news2.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news2);
			newsItemDao.saveNewsSearchRstItem(news2);
			news.add(news2);

			NewsSearchRstItem news3 = new NewsSearchRstItem();
			news3.set_id("000000000000000000000023");
			news3.setProgram_wiki_keys(Arrays.asList("�ǳ�����"));
			news3.setTitle("\"�ǳ�\"��̸ǣ�ִ�15��Ů�ӣ�������֪����С���");
			news3.setLink("http://www.chinanews.com/yl/2013/06-04/4889173.shtml");
			news3.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("�������������ӡ��ǳ����š�51�����߷�ǣ��С17��˧��һĻ������������������Ȼ�󲨡��ڴ�㵾���֮�࣬����Ҳ�׷�������������ף����ͬʱ��������Ҳ���������飬����������ʣ��мα�����ϣ�����ֹͣ�²⣬ǰ�մ󷽻�Ӧ��������û�зֿ�����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ݣ�����û�зֿ��� <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��β���˼��ġ��������������ǹ�ע�Ľ������мα��������ǣ�ֱ��Լ���17���Ů�α����Ƿ��г����Լ������ɡ��Դˣ����ݻ��䵱ʱ��ǣ�֣�˵����������(ָŮ�α���߷�)��ȷ�ǽ�Ŀ���������Ҹж���Ů�α����������ҹ����ң���ǣ���Ǻ���ȷ��ѡ�񡣡�����������̨֮�󣬶��������Ƿ�����ϵ����Ҳ�Ǵ��ʮ�ֹ��ĵĻ��⣬����Ҳϣ�����ֹͣ�²⣬��˵�������˽�Ŀ������û�зֿ�����ϲ���ͷ��ӺȾ����죬����һ̸����Сʱ���Ҿ��ã�����һ�������Ÿ߹����д�����µ�Ů�ˡ������ڷǳ������ջ��ˣ����Ա˴˶�����ϧ����֮��ĸ��顣��ϣ�����Ӽ����Һ���֪����Ҳ���ҵ�С��㣬�������Ͽ����׽������ǻ��Ͽ��԰����ҡ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��߷ܣ�Ҫ�Ҹ������ϻ����������ˣ� <br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����������������һ����Ҳ�Ǵ�ҹ�ע�Ľ��㣬�кܶ��˹��������ʷ�������Ů�α���������Ҳ�ɷõ�����߷ܣ��˽����������ǳ����š���Ե�ɡ�̸���Լ���һ�λ���������û�б�Թ��û������������λ�����Ȼ���Ҵ�����һЩ�����ľ���������Ҳ���Ҹ��������ʶ���Լ���֪���Լ���Ҫʲô����������˼���ǣ������μӷǳ�����Ҳ��Ů���İְ�����Ľ��飬���������ټ�����Ȼ�����ѡ�����ǰ��Ϊ����С���������ܣ����Ծ�һֱû�н�顣������Ů��Ҳ�����ˣ���ѧ��ҵ��������ȫ��������������һ���������Ͽ��Ը���ذ�����һ���˹�ͬ��Ӫ����໥������������Ϊ���������������ǳ����š���Ѱ���Ǹ���ע�����Т˳����Ů�ˡ������ˡ�<br/>");
			news3.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news3);
			newsItemDao.saveNewsSearchRstItem(news3);
			news.add(news3);

			NewsSearchRstItem news4 = new NewsSearchRstItem();
			news4.set_id("000000000000000000000024");
			news4.setProgram_wiki_keys(Arrays.asList("�ǳ�����"));
			news4.setTitle("�ϳ�Ů�����ǳ����š�\"ǣ��\"����˧�� ������Ѹ�ǳ");
			news4.setLink("http://sichuan.scol.com.cn/ncxw/content/2013-06/04/content_5348491.htm");
			news4.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("��ת��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����ת�ǡ��ǽ������ӡ��ǳ����š���Ŀ�е�һ����ɫ��û�г�Ϊ̨����ʽŮ�α��ĵ���Ů������������������ȡ���мα�ǣ�ֵĻ��ᡣ<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������������24��˧����ɷ�������ȫ����ơ������ڡ���ת�ǡ�����ϳ�22��Ů��ѧ������վ�����󵨱�ף�����ǣ�ֳɹ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��5������Ѯ��ʱ���ҽӵ��ˡ��ǳ����š���Ŀ���֪ͨ������ȥ�μ����ǵĽ�Ŀ����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ڵ绰��̸���������ʱ������ͬѧ���ں��棬����һ������վ��ע���˻�Ա����д�˽�����Ϣ����û�뵽�����˴��Ŀ��ҵ��ʱ�򣬡��ǳ����š���Ŀ�����Ҵ�绰����<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����ʱֻ���뵽Ҫ��ҵ�ˣ�ȥ�μӡ��ǳ����š����͵�����һȦ������6��1�ղ����Ľ������ӡ��ǳ����š���Ŀ�У��ϳ�22��Ů��ѧ������ɹ���ǣ�֡�һλ����������24��˧�硣3�գ��������ж������ߵ绰���������࣬��������������Ů���ġ����ס����¡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��6��1�ղ����ġ��ǳ����š��У��������Ű�ɫ��״���º��黨ȹ�����ڡ���ת�ǡ���Ů�α�ϯ�ϣ����������ʮ�����ۡ�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���Ű�ɫ���£����Ӹߴ�������к���ɷ�һ��̨�����������ֳ������С���������Ϊ����ԭ�����������к�����ȫ������ơ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���Ҿ�������������к��ӻ�����Ҳ�ܷ����ҵ�����Ҫ�󡣡������ȴ������������̬������վ��������ʾԸ�����ɷ�ǣ�֡�����ǣ�֡��ɹ�������˽�½�����Լ�ύ����������������û���ߵ�һ�𡣡������Ĵ��ˣ��������Ĵ����������Ѿ��ڱ����ϰ��ˣ�Ҳû�취��Ϊ�������Ĵ��������������ˣ����������ˣ�������Ҳ������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�������ж���ʵϰ����Ǯ˫<br/>");
			news4.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news4);
			newsItemDao.saveNewsSearchRstItem(news4);
			news.add(news4);

			NewsSearchRstItem news5 = new NewsSearchRstItem();
			news5.set_id("000000000000000000000025");
			news5.setProgram_wiki_keys(Arrays.asList("�ǳ�����"));
			news5.setTitle("���������ǳ����š����ƺ󷴻� ������������ע");
			news5.setLink("http://news.ename.cn/yuming_20130604_45166_1.html");
			news5.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("�����й���eName.cn��6��4��Ѷ����Ϥ���ڽ������ӡ��ǳ����š�����һ�ڽ�Ŀ�У������ƽ㡱���ƺ�ͻȻ���ڣ���ȫ���������ξ��ء��������Ϸ�Ҳ�̲�ס����ŭ������������������Ҳ�����ձ���ע��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���ѷ�����һ�����ڹ���ϯ����ġ����ƽ㶡�������ƺ󷴻ڡ�����ƵƬ�Σ���Ҳ������ԭ��6��2�ղ����ġ��ǳ����š���Ŀδ���Ȼ𡣶�����Ҳ�ٴ������˴�ҵĹ�ע������������dingdongli.com��6��3�ձ���ע��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�����ƽ㡱������Ϊһλ�������������мα�������ͻȻ���ڣ���ȫ�������������ξ��ء��׶���������״�����Ϸ�Ҳ��Ϊ�ѣ��̲�ס����ŭ��������ϣ��ÿ���˶������ؽ�Ŀ�������⣬�����㱬�ƣ��˼Ҿ�һ��Ը������ߡ�������Ҳ��ը���˹������ѷ׷ױ�ʾ������������ȱ����ϵı��ƣ������ٶ�Ҳû���塣��<br/>");
			news5.setDescription(des.toString());
			newsItemDao.removeNewsSearchRstItem(news5);
			newsItemDao.saveNewsSearchRstItem(news5);
			news.add(news5);

			NewsSearchRstItem news6 = new NewsSearchRstItem();
			news6.set_id("000000000000000000000026");
			news6.setProgram_wiki_keys(Arrays.asList("�ǳ�����"));
			news6.setTitle("���ǳ����š�34��������ǣ��51����Ů");
			news6.setLink("http://ent.sina.com.cn/v/m/2013-06-04/12383935782.shtml");
			news6.setUpdateTime(new Date());
			des = new StringBuffer();
			des.append("��ǰ�����ġ��ǳ����š�[΢��]������ר���ڶ����У�51���Ů�α���߷ܳɹ�ǣ��34���мα����ݣ���ȫ�����ȷ��ڣ�Ҳ�úܶ�����������ǻ᲻����̨�ͷ��֡������Ŀ¼�����д����£�����������17�����Ů�α����鷢չ�Ƿ�˳�������գ����������ߴӽ�������[΢��]�����˽⵽������������������ͨ������ش��˹��ڵ����ɣ���ȷ��ʾ������߷�û�зֿ������Һ���ϧ����֮��ĸ��飬ǣ���Ǻ���ȷ��ѡ�񡱡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�ֳ��ط�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�º��Ӧ��Ŀ������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;34�������оܾ������ʱ���Ů<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;51�����߷ܱ������Ϊ˽Ӫҵ�������ң���ѧ�ѱ�ҵ��Ů�������������μӡ��ǳ����š����ס����������������������������棬����ӵ�����ҹ�˾���ڽ�Ŀ�У���Ȼ��߷���ȷ����˶����ݵ����ͣ�������Ů����������Ķ�Ů�������ȶ����������ڵľ������֣���ˣ�������˵���Լ��ľ���ʱ��ȫ�����𾪲������ˡ����ּα����շǳ�������߷ܵ���ż��׼��ע������Т˳����Ů�ˡ����ơ�����Ǿ�������ͥ�����Ů�˸����Ĵ𰸡���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����ǣ�ֺ���ܲɷ�ʱ������˵��������ûʲô���Ҹо�������״̬�ǳ��ã�����20��������Ҳ���ϵġ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;�º��Ӧ<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;����û�зֿ���ǣ������ȷ��ѡ��<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������������ջ�ף����ͬʱ��Ҳ�⵽�˲������ɣ�һЩ���ڻ������Ƿ��ǽ�Ŀ�鰲�ŵĳ�������֪��������������̨���Ƿ���һ�𡣶Դˣ�����ͨ������ش�˵�������Ǹ����¶����ô�����ˣ���������һЩ�ֹ۵ľ�������һ����Ҳ���ˡ�����(ָŮ�α���߷�)�ǽ�Ŀ���������Ҹж���Ů�α����������ҹ����ң���ǣ���Ǻ���ȷ��ѡ�񡣡�<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���������Ƿ�����ϵ�����ݳơ�û�зֿ�������˵����ϲ���ͷ��ӺȾ����죬����һ̸�ͼ���Сʱ�����Ҿ�������һ�������Ÿ߹����д�����µ�Ů�ˡ��Һ���ϧ����֮��ĸ��飬����Ҳ���ÿ��������Ů��������á���ϣ�����Ӽ����Һ���֪����Ҳ���ҵ�С��㣬�������Ͽ����׽������ǻ��Ͽ��԰����ҡ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;��Ŀ������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���Ƕ�����Ŀ��ݶȸ���<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;���գ������������µ��˽������ӡ��ǳ����š���Ŀ�������㣬�����߼��ߣ���߷��ǽ�Ŀ�����곤��һλŮ�α��������������ݿ�Խ�����ǣ�֣�����˵�����������ڹ��������˺ܳ�ʱ�䣬�����Ļ��ж���ż����Ŀ��ݶȸ���һЩ�����ң���������˼����������������ˣ����Ƕ�����������������֮ǰ�����ó��������׼������<br/>");
			des.append("&nbsp;&nbsp;&nbsp;&nbsp;������������������� ��ԣ�� ��д<br/>");
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
			prod1.setBrand_name("����֮��");
			prod1.setUpdatetime(new Date());
			prod1.setSearchKey("���");
			prod1.setPic_url("http://d6.yihaodianimg.com/N00/M05/34/AD/CgQCtlE__9aAPWAgAAEcSJEHV_s70500_450x450.jpg");
			prod1.setProduct_url_m("http://m.yihaodian.com/product/7405825");
			prod1.setTitle("Evanhome/����֮�� ���׷�ˮ����խ�������խ��ɫ���L5001����");
			YihaodianSale_price price = new YihaodianSale_price("�㶫", 98.0);
			prod1.setSale_price(Arrays.asList(price));
			products.add(prod1);
			yihaodianProductDao.removeYihaodianProductById(prod1.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod1);

			YihaodianProductBean prod2 = new YihaodianProductBean();
			prod2.setId("000000000000000000000002");
			prod2.setBrand_name("��e��");
			prod2.setUpdatetime(new Date());
			prod2.setSearchKey("���");
			prod2.setPic_url("http://d6.yihaodianimg.com/N03/M07/2F/81/CgQCs1E-wv-APdSsAACBd8rzvAU61500_450x450.jpg");
			prod2.setProduct_url_m("http://m.yihaodian.com/product/7386012");
			prod2.setTitle("��e�� 145*6cm �Ứ��� ��ʿ�������� 100%ɣ��˿ ǳ��ɫ E00244ǳ�Ͼ���");
			prod2.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;�߹������ɫ����Ƕ��ǳ��ɫ��״�Ứ����ԭ��������ɫ���ϱ������ǳ������ǵĺ�����ʿ������и߹���ŵ����ʡ���e��������ϲ���100%ɣ��˿����������˿�����ָУ����˴��������ñ������ʣ������������κγ���������ʾ����ʿ�����ŷ緶����Ʒ�ʸ�׷��    ���ϲ���100%ɣ��˿����׼45��б�Ǽ��á���˿������ͣ��ָ������ʵ�ϸ�塣�ڳĲ��ô��ֹ����ƣ�ѡ��50%��ë��50%���ڣ���ë�ȱ�������������ɺͱ�������������ǿ������ĵ��ԣ����������ڵͶ˴����ڳ�����Ĵֲڣ����Ը߹���Ϣ������������ڲຬ�С������ߡ��������߾߱����õ����������幦�ܣ�����������ԣ�ʹ�����չ���磬ͬʱ�ɷ�ֹ��������������Ρ�<br/>&nbsp;&nbsp;&nbsp;&nbsp;����˵��<br/>&nbsp;&nbsp;&nbsp;&nbsp;1.ʹ�ú��������̽⿪��ᣬ������ӽ�ڽ��£������������������ģ�������ά����������������ۡ�<br/>&nbsp;&nbsp;&nbsp;&nbsp;2.ÿ�δ����ڽ⿪���뽫���ƽ�Ż����¼ܽ��������������������������ô��Ƿ�ƽ��������������<br/>&nbsp;&nbsp;&nbsp;&nbsp;3.����ϵ�ϰ�ȫ��ʱ����������ڰ�ȫ�����棬����������ۡ��뾡����Ҫ�۵����������塣���������壬�뽫�������Ȧ�ڸɾ��ľ�ƿ�ϣ���һ�����弴��������<br/>&nbsp;&nbsp;&nbsp;&nbsp; 4.ͬһ���������һ�κ���������������Ƚ���������ڳ�ʪ�ĵط���������ˮ��ʹ�����۴��ָ�ԭ״�����������ﴦƽ�Ż������<br/>&nbsp;&nbsp;&nbsp;&nbsp;5.������ܷ����������±�ɹ����������˿�ʷ��ƣ�Ӱ����ۡ�<br/>&nbsp;&nbsp;&nbsp;&nbsp;6.մȾ�۹�ʱ��Ӧ������ϴ��Ϊ�������ɫ���벻Ҫˮϴ������Ư�ף���������������������ٶ�������ƽ����������¶�150�㣬�ٶ�Ҫ�졣ˮϴ����������������ɱ��ζ�����");
			price = new YihaodianSale_price("�㶫", 89.0);
			prod2.setSale_price(Arrays.asList(price));
			products.add(prod2);
			yihaodianProductDao.removeYihaodianProductById(prod2.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod2);

			YihaodianProductBean prod3 = new YihaodianProductBean();
			prod3.setKeyType(Arrays.asList(KeyType.CONTENT));
			prod3.setId("000000000000000000000003");
			prod3.setBrand_name("�ճ���");
			prod3.setUpdatetime(new Date());
			prod3.setSearchKey("��װ");
			prod3.setPic_url("http://d6.yihaodianimg.com/N01/M00/15/D1/CgQCrlDsE1-AJ_rnAAGh7azUJdg38800_450x450.jpg");
			prod3.setProduct_url_m("http://m.yihaodian.com/product/6574334");
			prod3.setTitle("Richini/�ճ��� 2013�¿���ʿȫ����װ��п��� ��������ʱ�и���-32823��ɫ2024XXL");
			price = new YihaodianSale_price("�㶫", 990.0);
			prod3.setSale_price(Arrays.asList(price));
			products.add(prod3);
			yihaodianProductDao.removeYihaodianProductById(prod3.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod3);

			YihaodianProductBean prod4 = new YihaodianProductBean();
			prod4.setId("000000000000000000000004");
			prod4.setBrand_name("RXT");
			prod4.setUpdatetime(new Date());
			prod4.setSearchKey("�׳���");
			prod4.setPic_url("http://d6.yihaodianimg.com/N01/M0B/5B/42/CgQCr1GRqcSANUD0AANpcNNr4cw87600_450x450.jpg");
			prod4.setProduct_url_m("http://m.yihaodian.com/product/8876398");
			prod4.setTitle("RXT �¿���� ��ʿ����׳�����װ���� ��װ������װ��ɫ43");
			price = new YihaodianSale_price("�㶫", 89.0);
			prod4.setSale_price(Arrays.asList(price));
			products.add(prod4);
			yihaodianProductDao.removeYihaodianProductById(prod4.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod4);

			YihaodianProductBean prod5 = new YihaodianProductBean();
			prod5.setId("000000000000000000000005");
			prod5.setBrand_name("������ū/CARLLANNU");
			prod5.setUpdatetime(new Date());
			prod5.setSearchKey("�׳���");
			prod5.setPic_url("http://d6.yihaodianimg.com/N01/M04/3F/87/CgQCrlFWjISAe-Q6AAE5QGqPyZg94300_450x450.jpg");
			prod5.setProduct_url_m("http://m.yihaodian.com/product/8876398");
			prod5.setTitle("Carl lannu /������ū 13��Ů��ٴ����ɰ׳��� ����Ů����OLͨ�� B330-9042��ɫM");
			prod5.setProduct_desc("��ţ�9042���ԣ�����   ���ϣ��ϳ���ά�ߴ磺���ʱ�׼�ߴ磺36-40");
			price = new YihaodianSale_price("�㶫", 76.0);
			prod5.setSale_price(Arrays.asList(price));
			products.add(prod5);
			yihaodianProductDao.removeYihaodianProductById(prod5.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod5);

			YihaodianProductBean prod6 = new YihaodianProductBean();
			prod6.setId("000000000000000000000006");
			prod6.setBrand_name("�ڳ�");
			prod6.setUpdatetime(new Date());
			prod6.setSearchKey("�۾�");
			prod6.setPic_url("http://d6.yihaodianimg.com/N00/M06/04/B4/CgQCtlDOsMmAUDoOAAECdpFRzZE46900_380x380.jpg");
			prod6.setProduct_url_m("http://m.yihaodian.com/product/6761033");
			prod6.setTitle("�ڳ� �����۾��ܽ����۾��� ȫ���۾��� ����֬�����侵Ƭ 1109c3BG��ɫ ����1.60����Ƭ����");
			price = new YihaodianSale_price("�㶫", 399.0);
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
			prod1.setBrand_name("Maybelline ������");
			prod1.setUpdatetime(new Date());
			prod1.setSearchKey("������");
			prod1.setPic_url("http://d11.yihaodianimg.com/t1/2012/1128/199/237/9d0efb63c8938d3cc77fecedee40a191_380x380.jpg");
			prod1.setProduct_url_m("http://m.yihaodian.com/product/5607882");
			prod1.setTitle("Maybelline ������ ŦԼ�������۹������30ml");
			YihaodianSale_price price = new YihaodianSale_price("�㶫", 79.0);
			prod1.setSale_price(Arrays.asList(price));
			prod1.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;��Ʒ���30ml<br/>&nbsp;&nbsp;&nbsp;&nbsp;��Ʒ�ص㣺�䷽������Ȼӣ�Ҿ��ͺͿ�����ά����C�������ʵأ���̼䣬�������������ʱ����ҫ��𪡢͸�������۹�ɡ�������ʪ+����������Q��ˮ��****���������󡣱�������,ʹ���ش�!������?��Ȼӣ�Ҿ��ͺ�ά����C!<br/>&nbsp;&nbsp;&nbsp;&nbsp;ʹ�÷���:��Ϊ�ռ令�������һ��������жױ��Ʒ�ƽ���������Ʒ�ƿںţ�Maybesheisbornwithit,MaybeitisMaybelline���������ģ���������������1991����ͨ��Ϊ�������һ�仰����Thepowerisinyourhands���C�������������������<br/>&nbsp;&nbsp;&nbsp;&nbsp;Ʒ�ƹ��£�Maybelline(������ŦԼ)�����������������õ�����(Maybel)�Լ���ʿ��(Vaseline)�ĺ�벿����ɵġ�������ŦԼ��1917�����֮ʱ���������������ϵ�һ֧�ִ��۲���ױƷ��������ŦԼ��״��ë��(MaybellineCakeMascara)�����������ŦԼ�Ѿ���Ϊ��һ�����д���ɫ�ʵ�ȫ��ױƷ������˾������90������Ҽ������У�������ŦԼ�Ѿ���Ϊ��һ�����Ů��������ױ������Ʒ�Ĺ�˾�������Ѿ�����97��Ʒ����ʷ��������ŦԼ(MaybellineNewYork)�ṩ����רҵ������ױ���۲���ױ��������ױ��Ʒ��");
			products.add(prod1);
			yihaodianProductDao.removeYihaodianProductById(prod1.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod1);

			YihaodianProductBean prod2 = new YihaodianProductBean();
			prod2.setId("000000000000000000000012");
			prod2.setBrand_name("Maybelline ������");
			prod2.setUpdatetime(new Date());
			prod2.setSearchKey("������");
			prod2.setPic_url("http://d6.yihaodianimg.com/N02/M06/29/4C/CgQCsFEkJrmAd2GVAAYkIpvLTcg94800_380x380.jpg");
			prod2.setProduct_url_m("http://m.yihaodian.com/product/4834745");
			prod2.setTitle("Maybelline/������ ������ױ�����װ ��������۹���������װ+ 40mlжױҺ��лƷ�� ȫ��4.5������299Ԫ��жױҺ40ml");
			price = new YihaodianSale_price("�㶫", 81.0);
			prod2.setSale_price(Arrays.asList(price));
			products.add(prod2);
			yihaodianProductDao.removeYihaodianProductById(prod2.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod2);

			YihaodianProductBean prod3 = new YihaodianProductBean();
			prod3.setId("000000000000000000000013");
			prod3.setBrand_name("Maybelline ������");
			prod3.setUpdatetime(new Date());
			prod3.setSearchKey("������");
			prod3.setPic_url("http://d6.yihaodianimg.com/N02/M06/29/4C/CgQCsFEkJrmAd2GVAAYkIpvLTcg94800_380x380.jpg");
			prod3.setProduct_url_m("http://m.yihaodian.com/product/5602337");
			prod3.setTitle("Maybelline ������ ���ɻ��Ử�Զ����߱� ��ɫ 0.35g");
			prod3.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;��Ʒ���0.35g��Ʒ�ص㣺<br/>&nbsp;&nbsp;&nbsp;&nbsp;�Ử�ʵأ�����ɫ�����軭����������ɫ��Ũ����ˮ�䷽���־ò���Ⱦ��<br/>&nbsp;&nbsp;&nbsp;&nbsp;Ʒ�ƽ���������Ʒ�ƿںţ�Maybesheisbornwithit,MaybeitisMaybelline���������ģ���������������1991����ͨ��Ϊ�������һ�仰����Thepowerisinyourhands���C�������������������<br/>&nbsp;&nbsp;&nbsp;&nbsp;Ʒ�ƹ��£�Maybelline(������ŦԼ)�����������������õ�����(Maybel)�Լ���ʿ��(Vaseline)�ĺ�벿����ɵġ�������ŦԼ��1917�����֮ʱ���������������ϵ�һ֧�ִ��۲���ױƷ��������ŦԼ��״��ë��(MaybellineCakeMascara)��<br/>&nbsp;&nbsp;&nbsp;&nbsp;���������ŦԼ�Ѿ���Ϊ��һ�����д���ɫ�ʵ�ȫ��ױƷ������˾������90������Ҽ������У�������ŦԼ�Ѿ���Ϊ��һ�����Ů��������ױ������Ʒ�Ĺ�˾�������Ѿ�����97��Ʒ����ʷ��������ŦԼ(MaybellineNewYork)�ṩ����רҵ������ױ���۲���ױ��������ױ��Ʒ��");
			price = new YihaodianSale_price("�㶫", 49.0);
			prod3.setSale_price(Arrays.asList(price));
			products.add(prod3);
			yihaodianProductDao.removeYihaodianProductById(prod3.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod3);

			YihaodianProductBean prod4 = new YihaodianProductBean();
			prod4.setId("000000000000000000000014");
			prod4.setBrand_name("Maybelline ������");
			prod4.setUpdatetime(new Date());
			prod4.setSearchKey("������");
			prod4.setPic_url("http://d13.yihaodianimg.com/t1/2012/1128/257/238/0c6aef4afe6ddbd1c77fecedee40a191_380x380.jpg");
			prod4.setProduct_url_m("http://m.yihaodian.com/product/5602349");
			prod4.setTitle("Maybelline ������ �����Ųʻ�����-����ɺɫ��1.9g");
			prod4.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;��Ʒ���1.9g<br/>&nbsp;&nbsp;&nbsp;&nbsp;��Ʒ�ص㣺������ŦԼ�����Ųʻ���������!һĨ�ʻ��˫������������ˮ��һĨ�ʻ��:ע��������͸ɫ��,����˫����Ȼ�ʻ��͸���е��ʡ�ȫ�������ˮ��:������ľ����������������ʹ���������չ������������͸˫����<br/>&nbsp;&nbsp;&nbsp;&nbsp;ʹ�÷���:ֻ����ת����2-3����;���ͿĨ���ܿ��⻧���˺�,�����Ų�ˮ�ۡ�<br/>&nbsp;&nbsp;&nbsp;&nbsp;ɫϵ��ɺ��������ƫ��ɫϵ���ٺ�ɫ�Ժ죡<br/>&nbsp;&nbsp;&nbsp;&nbsp;������ѯƷ�ƿͷ���400-821-5878Ʒ�ƽ���������Ʒ�ƿںţ�Maybesheisbornwithit,MaybeitisMaybelline���������ģ���������������1991����ͨ��Ϊ�������һ�仰����Thepowerisinyourhands���C�������������������<br/>&nbsp;&nbsp;&nbsp;&nbsp;Ʒ�ƹ��£�Maybelline(������ŦԼ)�����������������õ�����(Maybel)�Լ���ʿ��(Vaseline)�ĺ�벿����ɵġ�������ŦԼ��1917�����֮ʱ���������������ϵ�һ֧�ִ��۲���ױƷ��������ŦԼ��״��ë��(MaybellineCakeMascara)�����������ŦԼ�Ѿ���Ϊ��һ�����д���ɫ�ʵ�ȫ��ױƷ������˾������90������Ҽ������У�������ŦԼ�Ѿ���Ϊ��һ�����Ů��������ױ������Ʒ�Ĺ�˾�������Ѿ�����97��Ʒ����ʷ��������ŦԼ(MaybellineNewYork)�ṩ����רҵ������ױ���۲���ױ��������ױ��Ʒ��");
			price = new YihaodianSale_price("�㶫", 25.0);
			prod4.setSale_price(Arrays.asList(price));
			products.add(prod4);
			yihaodianProductDao.removeYihaodianProductById(prod4.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod4);

			YihaodianProductBean prod5 = new YihaodianProductBean();
			prod5.setId("000000000000000000000015");
			prod5.setBrand_name("Maybelline ������");
			prod5.setUpdatetime(new Date());
			prod5.setSearchKey("������");
			prod5.setPic_url("http://d12.yihaodianimg.com/t20/2012/0623/462/390/cb58c2dbd9fb7eeaYY_380x380.jpg");
			prod5.setProduct_url_m("http://m.yihaodian.com/product/3737521");
			prod5.setTitle("Maybelline/������ ���ɳ־���Ӱ���߸� ����Ⱦ2.5g �����°�װ�� Ĭ�Ϸ���ɫ ȫ��4.5������299Ԫ��жױҺ40ml");
			price = new YihaodianSale_price("�㶫", 69.0);
			prod5.setSale_price(Arrays.asList(price));
			products.add(prod5);
			yihaodianProductDao.removeYihaodianProductById(prod5.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod5);

			YihaodianProductBean prod6 = new YihaodianProductBean();
			prod6.setId("000000000000000000000016");
			prod6.setBrand_name("Maybelline ������");
			prod6.setUpdatetime(new Date());
			prod6.setSearchKey("������");
			prod6.setPic_url("http://d6.yihaodianimg.com/N00/M03/31/B9/CgMBmFE2812AZUWfAANFFqD-mWc92200_380x380.jpg");
			prod6.setProduct_url_m("http://m.yihaodian.com/product/4244946");
			prod6.setTitle("Maybelline/������ ��������������ҺBB˪ ��ɹ���������� �������30ml������30ml ȫ��4.5������299Ԫ��жױҺ40ml");
			price = new YihaodianSale_price("�㶫", 89.0);
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
			prod1.setBrand_name("����");
			prod1.setUpdatetime(new Date());
			prod1.setSearchKey("�ǳ�����");
			prod1.setPic_url("http://d6.yihaodianimg.com/N00/M07/50/45/CgMBmVF2aVWAYpPcAANBJJwIy4w54700_380x380.jpg");
			prod1.setProduct_url_m("http://m.yihaodian.com/product/8420378");
			prod1.setTitle("CD-HD�ǳ����ŵ�ʿ��(2��װ)�����⣩ ");
			YihaodianSale_price price = new YihaodianSale_price("�㶫", 40.0);
			prod1.setSale_price(Arrays.asList(price));
			prod1.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;������Ϣ��Ʒ���ƣ�CD-HD�ǳ����ŵ�ʿ��(2��װ)�������������ߣ���������ʱ��Ӱ����˾ҳ����2ӡ�Σ�1����ʱ�䣺2008-01-01ISBN�ţ�F210841000ӡˢʱ�䣺2008-01-01�����磺��������ʱ��Ӱ����Σ�1��Ʒ���ͣ�ͼ��ӡ�Σ�1");
			products.add(prod1);
			yihaodianProductDao.removeYihaodianProductById(prod1.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod1);

			YihaodianProductBean prod2 = new YihaodianProductBean();
			prod2.setId("000000000000000000000022");
			prod2.setBrand_name("�»�����");
			prod2.setUpdatetime(new Date());
			prod2.setSearchKey("�ǳ�����");
			prod2.setPic_url("http://d6.yihaodianimg.com/N03/M03/4A/5F/CgQCs1F1-c6ATw2QAABGyMSDAiE17600_380x380.jpg");
			prod2.setProduct_url_m("http://m.yihaodian.com/product/4384108");
			prod2.setTitle("ɫ����ʶ�ˣ������� ȫ�������ʵ�һ�����ս�Ŀ�����������ӡ��ǳ����š�����ѧר���ּ��������Ĵ��죡��ʵ������ѧ�����顶ɫ��ʶ�ˡ�����������");
			price = new YihaodianSale_price("�㶫", 19.4);
			prod2.setSale_price(Arrays.asList(price));
			prod2.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;��ӳϼΪ��û��ѡ�������������Ե�λ�������ҵ�����»�����ɱ��ϣ�����Ը��е�ʲô�ɷִ�ʹ���ܺ��Լ������н����ѡ����ּν������顶ɫ����ʶ�ˡ���Ϊ������𰸡�<br/>&nbsp;&nbsp;&nbsp;&nbsp;����һ�ġ�ɫ��ʶ�ˡ���һ���ŵ��һ��ȱ��ķ�񣬼��л����Բ�ͬ�Ը��ȱ�������ȵ����������ҵ����У������˽�200����������ʵ������50���Ž���������˰����������������Ը�ĺڰ��档���걾����������������Ը�������˺��Լ��ģ�Ҳ���˽��Լ���������������˺����˵ġ�<br/>&nbsp;&nbsp;&nbsp;&nbsp;����һ��ר��Ϊ��Щϣ����Ϥ�Ը���Ѱ����ʵ������߾�����˶�д���飬ֻ���˽����������ĵ����࣬���п���׷�����ڵĺ�г�������������ڵ�ϲ����� ");
			products.add(prod2);
			yihaodianProductDao.removeYihaodianProductById(prod2.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod2);

			YihaodianProductBean prod3 = new YihaodianProductBean();
			prod3.setId("000000000000000000000023");
			prod3.setBrand_name("����");
			prod3.setUpdatetime(new Date());
			prod3.setSearchKey("�ǳ�����");
			prod3.setPic_url("http://d6.yihaodianimg.com/N04/M0A/0E/C6/CgQDrlF2RdSAc-sxAAoGWHguDWU78400_380x380.jpg");
			prod3.setProduct_url_m("http://m.yihaodian.com/product/8410920");
			prod3.setTitle("�ǳ�����(���������)�����⣩");
			price = new YihaodianSale_price("�㶫", 21.8);
			prod3.setSale_price(Arrays.asList(price));
			prod3.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;���ǳ�����(���������)����һ�����ڽ���ָ���顣<br/>&nbsp;&nbsp;&nbsp;&nbsp;����ʶ����֪���మ�������������ĵ��ã�ÿ����Ů��ϣ���Լ� �Ļ��������Ҹ�������������������£�ÿ�����¶�ϣ�����Լ��Ľ��� ���óɹ�������������õĻ��䣬��Ϊ�Ժ�ļ�ͥ������¼�ʵ�ĸ��������<br/>&nbsp;&nbsp;&nbsp;&nbsp;�మ���꣬���ǣ���߽������ĵ��ã� ��ǰ���������Ӫ���õ��Ҹ������� ���Ϸ����������Ȿ���ǳ�����(���������)��ȫ��λ����˻�ǰ ����������ٵĸ������⣬��ϸ�����˻�����ʽ�ĸ������̺ͼ��ɡ����� ��ʲô���Ļ�������ν��л�ǰ����׼������ʣ�������һ�����ɱ��� Ԥ�㣿��δ���һ��ר������ĸ��Ի�������������һ�����������н� �飿�������԰��ж���Ҫ�����ְ����쳤�ؾõķ�ʽ����Щ�����齫Ϊ�� ������е���������ʣ�����������������Ի���������ã��<br/>&nbsp;&nbsp;&nbsp;&nbsp;�����Ѿ�Ϊ����д����һ���������������ɣ��������İ���̤���� ���Ľ��ģ�Я���߽������Ҹ������Ĵ��š������������������ʵ�ùػ� ���Ӷ���֤�»���������һ˿�©�����㾡���Ҹ�����ܰ��<br/>&nbsp;&nbsp;&nbsp;&nbsp;ȫ��ָ���»�����������������һʧ��<br/>&nbsp;&nbsp;&nbsp;&nbsp;��Ӫ���Լ��Ļ������û�������ʱʱ���ʡ�");
			products.add(prod3);
			yihaodianProductDao.removeYihaodianProductById(prod3.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod3);

			YihaodianProductBean prod4 = new YihaodianProductBean();
			prod4.setId("000000000000000000000024");
			prod4.setBrand_name("����");
			prod4.setUpdatetime(new Date());
			prod4.setSearchKey("�ǳ�����");
			prod4.setPic_url("http://d6.yihaodianimg.com/N00/M08/4F/8E/CgMBmFF14_uAPzvAAAn5J9Fg0FY67600_380x380.jpg");
			prod4.setProduct_url_m("http://m.yihaodian.com/product/8385748");
			prod4.setTitle("�ǳ�����(������ָ����)�����⣩");
			price = new YihaodianSale_price("�㶫", 21.8);
			prod4.setSale_price(Arrays.asList(price));
			prod4.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;�������һ˫���ۣ�������������ӡ��� �����������ģ�������㰮�����ң������ҵ�����ѩ�������� ���ǳ����Ţ�(����ָ����)����һ���������׵ļ����顣<br/>&nbsp;&nbsp;&nbsp;&nbsp;һ���ܰ��������׳ɹ������ʵ��ָ�ϡ�<br/>&nbsp;&nbsp;&nbsp;&nbsp;ǧǧ����ĵ�����Ů�ӱ������ף����������ף��ٵ��������ף����� �Ĺ���������Խ��Խ�ձ飬���С�������Ϣ�����ײ�ֹ��֮�ơ�<br/>&nbsp;&nbsp;&nbsp;&nbsp;���������׵�Ч�ʣ�����ҵ��Լ��������ˣ� ��ΰ������ݼ��ŵĻ��᣿��������Ͳ������ ���Ϸ��䣬�����������Ȿ���ǳ����Ţ�(����ָ����)��������Ե� ���������׹��������������ĸ������⣬����������Ч�Ľ��֮�����Ա� �����߻����ٵ�ʱ�䣬����Ѹ������������ؼ�����ʤ�������Ӷ������� ��������ʿ���������״���������������������Ч�ʡ�<br/>&nbsp;&nbsp;&nbsp;&nbsp;�������ԡ�ʵ�����壬��ʶ���ϡ�Ϊ�������ߣ�����ѡȡ�˴����� Ů���׵�ʵ���������˸������������Լ���ϸ�ڣ�ͨ����Ҫ���������ӣ� �ö���֪���Լ�������ʱӦ����ʲô�Լ���ô����<br/>&nbsp;&nbsp;&nbsp;&nbsp;���黹��ʱ�����еĻ����۵㣬�����˺���������Ӷ������������� ��ʱ�����ʺ��Լ�����ȷѡ��<br/>&nbsp;&nbsp;&nbsp;&nbsp;�׸���������������·��׷Ѱ����������ǣ�Ϊ������Ҹ������ ���ǽ����׽��е��ף�");
			products.add(prod4);
			yihaodianProductDao.removeYihaodianProductById(prod4.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod4);

			YihaodianProductBean prod5 = new YihaodianProductBean();
			prod5.setId("000000000000000000000025");
			prod5.setBrand_name("����");
			prod5.setUpdatetime(new Date());
			prod5.setSearchKey("�ǳ�����");
			prod5.setPic_url("http://d6.yihaodianimg.com/N00/M0A/3B/B5/CgMBmVFNGu6AM_jJAAIjQrTwAEE25900_380x380.jpg");
			prod5.setProduct_url_m("http://m.yihaodian.com/product/2904807");
			prod5.setTitle("���˰�����.Ů�˰����� �ǳ�����2��������");
			price = new YihaodianSale_price("�㶫", 22.4);
			prod5.setSale_price(Arrays.asList(price));
			prod5.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;������Ů������ȫ��ͬ��������������������ĽǶ����������ԵĹ����Ǿ����ܵء��ಥ�֡�����Ů�ԵĹ������Ǿ����ܽ��ܡ����������ӡ�����ˣ����Ժ�Ů����˼ά�ϡ����۷�ʽ�ϡ��԰������϶����ŷǳ����ԵĲ��죬������ܹ���ʶ�����ֲ��죬������Ů���ڡ���ͨ������������������顱ʱ�ͻ������ܶ����⣬��̸�����������������������ˡ��������ĽǶȳ����������������Ů֮��˼ά���졢���۷�ʽ���졢�԰��������֮�⣬����¼����������Ե�ļ��ɣ���������������֮��ľ��룬��ο˷��Ա�������δ����ܻ�ӭ���ⲿ���������������ܻ�ӭ��̸�������Լ�ѧ����θ�Ч����������������ʧ��ʱӦ������ս�ȵȡ�������һ���̸��������������Ե�˼ά����λ�����Ի�ӭ�Լ������Լ������������飬�����������򼴽��������������Ÿ����нϴ������ ");
			products.add(prod5);
			yihaodianProductDao.removeYihaodianProductById(prod5.getId());
			yihaodianProductDao.insertYihaodianProductBean(prod5);

			YihaodianProductBean prod6 = new YihaodianProductBean();
			prod6.setId("000000000000000000000026");
			prod6.setBrand_name("����");
			prod6.setUpdatetime(new Date());
			prod6.setSearchKey("�ǳ�����");
			prod6.setPic_url("http://d6.yihaodianimg.com/N01/M0B/3D/47/CgQCrVFShByAadhTAAGhkyw6KPY02100_380x380.jpg");
			prod6.setProduct_url_m("http://m.yihaodian.com/product/7614042");
			prod6.setTitle("���׺챦��:\"�ǳ�����\"��սʤ��(������)��������");
			price = new YihaodianSale_price("�㶫", 22.5);
			prod6.setSale_price(Arrays.asList(price));
			prod6.setProduct_desc("&nbsp;&nbsp;&nbsp;&nbsp;ȫ�����ݰ��ա��ǳ����š���Ŀ�Ļ��������ţ�4��ƪĿ����Ϊ����ǰһ�����Ҹ���������֮�����顱������֮���жϡ�������֮�վ�ѡ������Ҳ��һ����׼����Ů˫������֪�����������׹��̡� ");
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
			vod1.setTitle("���ǿ�뾩��ӡ�ȡ��ͻ�˹̹����ʿ���¹�������ʽ���� 130519");
			vod1.setCover("http://g1.ykimg.com/01270F1F465198B8ABEFAE0123193C822B45AA-6BE0-69AA-E841-961DD0A10B04");
			vods.add(vod1);

			RecommendObject vod2 = new RecommendObject();
			vod2.setId("tcl_demo_vod_xinwen_2");
			vod2.setTitle("����Ժ�������ǿ������ʿ 130525");
			vod2.setCover("http://g2.ykimg.com/01270F1F4651A05DC9EBE30123193C541215EF-184A-A27C-99E4-3BC3E311E912");
			vods.add(vod2);

			RecommendObject vod3 = new RecommendObject();
			vod3.setId("tcl_demo_vod_xinwen_3");
			vod3.setTitle("���ǿ�ִ���������ʼ����ʿ������ʽ����");
			vod3.setCover("http://g3.ykimg.com/0100641F464B5EDF989CE50029CD26242D0A6B-DEE2-E69F-E5B4-4AD6CF9A5D3A");
			vods.add(vod3);

			RecommendObject vod4 = new RecommendObject();
			vod4.setId("tcl_demo_vod_xinwen_4");
			vod4.setTitle("���ǿ�ι���ʿ����˹̹�����ʱǿ�� ���������������ԴȪ");
			vod4.setCover("http://g2.ykimg.com/01270F1F4651A1A196B6940123193C591047EB-A3FA-B282-1B5A-36BE08B938F4");
			vods.add(vod4);

			RecommendObject vod5 = new RecommendObject();
			vod5.setId("tcl_demo_vod_xinwen_5");
			vod5.setTitle("���ǿ�ι���ʿ��ͥũׯǿ�� ��ǿ������� ��ũҵ�ִ���");
			vod5.setCover("http://g3.ykimg.com/01270F1F4651A0A36D72230123193C8875E433-5436-866F-6DEA-A2C75E30800F");
			vods.add(vod5);

			RecommendObject vod6 = new RecommendObject();
			vod6.setId("tcl_demo_vod_xinwen_6");
			vod6.setTitle("���ǿԲ����������ʿ����");
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
			vod1.setTitle("��������ױѧԺ-����Լ��ױ");
			vod1.setCover("http://g3.ykimg.com/0100641F4650A3D5FBD3D1058E2FDCC33134ED-3DEB-8104-CB92-75B53C543B76");
			vods.add(vod1);

			RecommendObject vod2 = new RecommendObject();
			vod2.setId("tcl_demo_vod_ad_2");
			vod2.setTitle("����˧���Ĵ���ױ�ݣ���ױ������ �������й�");
			vod2.setCover("http://g1.ykimg.com/0100641F464F680DB0AEF704677344D677BEFD-7163-836D-707C-19228B7FE192");
			vods.add(vod2);

			RecommendObject vod3 = new RecommendObject();
			vod3.setId("tcl_demo_vod_ad_3");
			vod3.setTitle("����ɏ-�紺�������˶ɼ��L�y��");
			vod3.setCover("http://g2.ykimg.com/0100641F464F669F63557805A70FCB1014D3B1-712D-B1CA-073D-9FDBE86BA444");
			vods.add(vod3);

			RecommendObject vod4 = new RecommendObject();
			vod4.setId("tcl_demo_vod_ad_4");
			vod4.setTitle("��������ױѧԺ����: ְ����ױ");
			vod4.setCover("http://g3.ykimg.com/0100641F46511697C7813A058E2FDC64C44A17-AFB7-AC00-EB05-C3676BABE969");
			vods.add(vod4);

			RecommendObject vod5 = new RecommendObject();
			vod5.setId("tcl_demo_vod_ad_5");
			vod5.setTitle("��������ױѧԺ-���˴���ױ");
			vod5.setCover("http://g1.ykimg.com/0100641F4650D35FFB6020058E2FDCE9D2B4FF-9D5B-067E-6707-133521A75A1D");
			vods.add(vod5);

			RecommendObject vod6 = new RecommendObject();
			vod6.setId("tcl_demo_vod_ad_6");
			vod6.setTitle("��������ױѧԺ-���ְ��ױ");
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
			vod1.setTitle("�ǳ�����51��Ů�α��ɹ�ǣ��С17���У�����˼���ǣ�֣�");
			vod1.setCover("http://g2.ykimg.com/0100641F4651A5EABEFC9F0759279D312A5980-8654-E05E-95B5-C803732DB3B5");
			vods.add(vod1);

			RecommendObject vod2 = new RecommendObject();
			vod2.setId("tcl_demo_vod_fcwr_2");
			vod2.setTitle("���ǳ����š��پ������ ������������ͯ��������");
			vod2.setCover("http://g1.ykimg.com/0100401F46517909CF70D406E26A7A664B3DF6-F3D3-7767-BE8E-16766D32397D");
			vods.add(vod2);

			RecommendObject vod3 = new RecommendObject();
			vod3.setId("tcl_demo_vod_fcwr_3");
			vod3.setTitle("���ǳ����š�����������������ʵ");
			vod3.setCover("http://g2.ykimg.com/0100641F465154897C3030019C3C1C61522747-1880-998A-5232-2580DE01ABB2");
			vods.add(vod3);

			RecommendObject vod4 = new RecommendObject();
			vod4.setId("tcl_demo_vod_fcwr_4");
			vod4.setTitle("�ּ���ǽ������� ���ǳ����š��ݲ���Ӱ��");
			vod4.setCover("http://g3.ykimg.com/0100641F46512722D93D71088EF54334B95813-442F-433C-8F5D-71CFB3921403");
			vods.add(vod4);

			RecommendObject vod5 = new RecommendObject();
			vod5.setId("tcl_demo_vod_fcwr_5");
			vod5.setTitle("���ǳ����š�������ר������һ������");
			vod5.setCover("http://g1.ykimg.com/0100641F4651A89F46A88A073505BF00E712DE-9107-0D72-09C0-D94242A9AEF4");
			vods.add(vod5);

			RecommendObject vod6 = new RecommendObject();
			vod6.setId("tcl_demo_vod_fcwr_6");
			vod6.setTitle("���ǳ����š�����ר��");
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
