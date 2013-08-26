package com.tcl.smart.server.service.impl;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tcl.smart.server.bean.cmd.TvListData;
import com.tcl.smart.server.bean.cmd.TvListRequest;
import com.tcl.smart.server.bean.cmd.TvListRequestAck;
import com.tcl.smart.server.bean.cmd.TvWikInfoRequestAck;
import com.tcl.smart.server.bean.cmd.TvWikiInfoRequest;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.service.IHuanApiService;
import com.tcl.smart.server.util.HttpClientUtil;

/**
 * »¶ÍøSOAP·þÎñ
 * 
 * @author fanjie
 * @date 2013-4-10
 */
public class HuanApiService implements IHuanApiService {
	private static final Logger logger = LoggerFactory.getLogger(HuanApiService.class);

	@Autowired
	@Qualifier("epgModelDao")
	private IEpgModelDao epgModelDao;

	@Override
	public TvWikInfoRequestAck getWikiInfo(String wiki_id) {
		if (wiki_id == null || wiki_id.trim().equals(""))
			return null;
		TvWikiInfoRequest req = new TvWikiInfoRequest(wiki_id);
		String url = String.format("http://www.epg.huan.tv/api/interface/");
		StringWriter contentWriter = new StringWriter();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(TvWikiInfoRequest.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(req, contentWriter);

			NameValuePair[] parameters = new NameValuePair[1];
			parameters[0] = new NameValuePair("xmlString", contentWriter.toString());
			String postAck = HttpClientUtil.postHttp(url, parameters);
			StringReader xmlReader = new StringReader(postAck);
			JAXBContext ackContext = JAXBContext.newInstance(TvWikInfoRequestAck.class);
			Unmarshaller unmarshaller = ackContext.createUnmarshaller();
			TvWikInfoRequestAck ack = (TvWikInfoRequestAck) unmarshaller.unmarshal(xmlReader);
			return ack;
		} catch (Exception e) {
			logger.error("Error in getting wiki info from huan.", e);
			return null;
		}
	}

	@Override
	public TvListRequestAck getTodayChannelPrograms() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String day = format.format(new Date());
		return getChannelPrograms(day);
	}

	@Override
	public TvListRequestAck getTomorrowChannelPrograms() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		String day = format.format(calendar.getTime());
		return getChannelPrograms(day);
	}

	private TvListRequestAck getChannelPrograms(String day) {
		TvListRequest req = new TvListRequest();
		TvListData date = new TvListData();
		date.setProvince("ÉîÛÚ");
		date.setDate(day);
		req.getParameter().setData(date);
		String url = String.format("http://www.epg.huan.tv/api/interface/");
		StringWriter contentWriter = new StringWriter();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(TvListRequest.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(req, contentWriter);

			NameValuePair[] parameters = new NameValuePair[1];
			parameters[0] = new NameValuePair("xmlString", contentWriter.toString());
			String postAck = HttpClientUtil.postHttp(url, parameters);
			StringReader xmlReader = new StringReader(postAck);
			JAXBContext ackContext = JAXBContext.newInstance(TvListRequestAck.class);
			Unmarshaller unmarshaller = ackContext.createUnmarshaller();
			TvListRequestAck ack = (TvListRequestAck) unmarshaller.unmarshal(xmlReader);
			ack.getData().setDay(day);
			return ack;
		} catch (Exception e) {
			logger.error("Error in getting program list from huan.", e);
			return null;
		}
	}
}
