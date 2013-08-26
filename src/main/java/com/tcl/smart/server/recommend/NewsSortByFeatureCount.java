package com.tcl.smart.server.recommend;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.service.INewsSortService;
import com.tcl.smart.server.util.StringUtil;

public class NewsSortByFeatureCount implements INewsSortService{

	@Override
	public List<NewsSearchRstItem> sort(List<NewsSearchRstItem> newsRstItem) {
		Collections.sort(newsRstItem,COMPARATOR_NEWS_BY_FEATURE_COUNT);
		return newsRstItem;
	}

	/**
	 * compare by feature count;descending order
	 */
	private Comparator<NewsSearchRstItem> COMPARATOR_NEWS_BY_FEATURE_COUNT = new Comparator<NewsSearchRstItem>(){
		@Override
		public int compare(NewsSearchRstItem item1, NewsSearchRstItem item2){
			long t1 = countFeatureNumber(item1);
			long t2 = countFeatureNumber(item2);
			return t1<t2 ? 1 : t1>t2 ? -1 : 0;
		}
	};
	
	private long countFeatureNumber(NewsSearchRstItem newsItem){
		long number=0;
		String program_wiki_name=newsItem.getProgram_wiki_name();
		List<String> program_wiki_keys=newsItem.getProgram_wiki_keys();
		String description = newsItem.getDescription();
		if(program_wiki_name!=null){
			number+=StringUtil.countStrAInStrB(program_wiki_name,description);
		}
		if(program_wiki_keys!=null&&program_wiki_keys.size()>0){
			for(String key:program_wiki_keys){
				number+=StringUtil.countStrAInStrB(key,description);
			}
		}
		return number;
	}
}
