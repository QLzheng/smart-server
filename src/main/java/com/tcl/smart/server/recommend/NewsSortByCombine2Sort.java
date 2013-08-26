package com.tcl.smart.server.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.service.INewsSortService;

public class NewsSortByCombine2Sort implements INewsSortService{
	
	private INewsSortService sortMethod_1;
	private INewsSortService sortMethod_2;
	private double valueForMethod_1=0.5;
	private Logger logger = LoggerFactory.getLogger(NewsSortByCombine2Sort.class);
	
	public NewsSortByCombine2Sort(INewsSortService sortMethod_1,INewsSortService sortMethod_2,
			double valueForMethod_1){
		this.sortMethod_1=sortMethod_1;
		this.sortMethod_2=sortMethod_2;
		this.valueForMethod_1=valueForMethod_1;
	}

	@Override
	public List<NewsSearchRstItem> sort(List<NewsSearchRstItem> newsRstItem) {
		List<NewsSearchRstItem> list_1=sortMethod_1.sort(newsRstItem);
		List<NewsSearchRstItem> list_2=sortMethod_2.sort(newsRstItem);
		if(list_1==null||list_2==null || list_1.size()!=list_2.size()){
			logger.error("sort error,can't get sort list.");
			return null;
		}
		

		Map<NewsSearchRstItem, Double> map = new HashMap<NewsSearchRstItem, Double>();
		for(int i=0;i<list_1.size();i++){
			NewsSearchRstItem item = list_1.get(i);
			int j=list_2.indexOf(item);
			double value = (double)i*valueForMethod_1+(double)j*(1-valueForMethod_1);
			map.put(item, value);
		}
		
		List<Map.Entry<NewsSearchRstItem, Double>> valuedItems =
			    new ArrayList<Map.Entry<NewsSearchRstItem, Double>>(map.entrySet());
		
		Collections.sort(valuedItems, new Comparator<Map.Entry<NewsSearchRstItem, Double>>() {   
		    public int compare(Map.Entry<NewsSearchRstItem, Double> o1, Map.Entry<NewsSearchRstItem, Double> o2) {
		    	double t1 = o1.getValue();
		    	double t2 = o2.getValue();
				return t1<t2 ? 1 : t1>t2 ? -1 : 0;
		    }
		});
		
		List<NewsSearchRstItem> results = new ArrayList<NewsSearchRstItem>();
		for (int i = 0; i < valuedItems.size(); i++) {
			results.add(valuedItems.get(i).getKey());
		}
		return results;
	}

}
