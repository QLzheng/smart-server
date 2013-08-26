import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.DataTableJson;
import com.tcl.smart.server.bean.DistinctChannel;
import com.tcl.smart.server.bean.EpgModel;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.bean.SpecifiedKeyword;
import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.dao.IEpgModelDao;
import com.tcl.smart.server.dao.ISpecifiedKeywordDao;
import com.tcl.smart.server.dao.IYihaodianProductDao;
import com.tcl.smart.server.dao.impl.NewsSearchRstItemDao;
import com.tcl.smart.server.service.IBaikeSearchService;
import com.tcl.smart.server.service.impl.BaikeSearchService;
import com.tcl.smart.server.service.impl.BlockException;
import com.tcl.smart.server.util.DateJsonValueProcessor;
import com.tcl.smart.server.util.Serializer;

public class StringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		// IEpgModelDao epgModelDao = (IEpgModelDao)
		// context.getBean("epgModelDao");
		// // List<DistinctChannel> channels = epgModelDao.group2Channels();
		// // System.out.println(channels.size());
		// // for (DistinctChannel channel : channels) {
		// // System.out.println(channel);
		// // }
		//
		// List<EpgModel> f = epgModelDao.findEpgModelsByChannelCode("cctv1");
		// System.out.println(f.size());
		// for(EpgModel m : f){
		// System.out.println(m);
		// }

		// NewsSearchRstItemDao newsSearchRstItemDao = (NewsSearchRstItemDao)
		// context.getBean("newsSearchRstItemDao");
		// // List<EpgModelDistinctId> ids =
		// // newsSearchRstItemDao.mapreduceByEpgsIfCountTooMuch();
		// // System.out.println(ids.size());
		// long t1 = System.currentTimeMillis();
		// List<NewsSearchRstItem> items =
		// newsSearchRstItemDao.findNewsSearchRstItemsByKeyword("李娜",20);
		// long t2 = System.currentTimeMillis();
		// for (NewsSearchRstItem item : items) {
		// System.out.println(item);
		// }

		JsonConfig cfg = new JsonConfig();
		cfg.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor());
		IEpgModelDao epgModelDao = (IEpgModelDao) context.getBean("epgModelDao");
		ISpecifiedKeywordDao specifiedKeywordDao = (ISpecifiedKeywordDao) context.getBean("specifiedKeywordDao");
		List<SpecifiedKeyword> specifiedKeywords = specifiedKeywordDao.findAllSpecifiedKeywords();
		DataTableJson data = new DataTableJson();
		for (int i = 0; i < specifiedKeywords.size(); i++) {
			SpecifiedKeyword keyword = specifiedKeywords.get(i);
			EpgModel epg = epgModelDao.findEpgModelById(keyword.getEpgId());
			data.addRecord(new String[] { epg.getChannelName() + "(" + epg.getChannelCode() + ")", epg.getName(), keyword.getKeyword() });
		}
		System.out.println(JSONObject.fromObject(data, cfg).toString());

		// IYihaodianProductDao dao = (IYihaodianProductDao)
		// context.getBean("yihaodianProductDao");
		// List<YihaodianProductBean> beans =
		// dao.findYihaodianProductByFeature("李东生");
		// for (YihaodianProductBean bean : beans) {
		// System.out.println(beans);
		// }
	}
}
