package com.tcl.smart.server.recommend;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.service.INewsSortService;

public class NewsSortByTimeDesc implements INewsSortService{

	@Override
	public List<NewsSearchRstItem> sort(List<NewsSearchRstItem> newsRstItem) {
		Collections.sort(newsRstItem,COMPARATOR_NEWS_BYTIME_DESC);
		return newsRstItem;
	}

	private Comparator<NewsSearchRstItem> COMPARATOR_NEWS_BYTIME_DESC = new Comparator<NewsSearchRstItem>(){
		@Override
		public int compare(NewsSearchRstItem item1, NewsSearchRstItem item2){
			long t1 = item1.getUpdateTime().getTime();
			long t2 = item2.getUpdateTime().getTime();
			return t1<t2 ? 1 : t1>t2 ? -1 : 0;
		}
	};
}
