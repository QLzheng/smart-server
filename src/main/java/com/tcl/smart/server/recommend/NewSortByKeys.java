package com.tcl.smart.server.recommend;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.service.INewsSortService;

public class NewSortByKeys implements INewsSortService{

	private INewsSortService newsSortService;
	
	public NewSortByKeys(INewsSortService newsSortService) {
		this.newsSortService = newsSortService;
	}

	@Override
	public List<NewsSearchRstItem> sort(List<NewsSearchRstItem> newsRstItem) {
		
		if(newsRstItem ==null || newsRstItem.isEmpty())
			return Collections.emptyList();
		
		Object2ObjectOpenHashMap<String,List<NewsSearchRstItem>> keyItemMap = new Object2ObjectOpenHashMap<String,List<NewsSearchRstItem>>();
		
		/*First,sort the newsRstItem by time*/
		newsRstItem = newsSortService.sort(newsRstItem);
		
		/*Separate the news by wiki's keys and epg's name */
		for(NewsSearchRstItem item : newsRstItem){
			List<String> keys = item.getProgram_wiki_keys();
			if(keys != null && !keys.isEmpty()){
				for(String key : keys){
					if(keyItemMap.containsKey(key)){
						keyItemMap.get(key).add(item);
					}else{
						List<NewsSearchRstItem> newsList = new ArrayList<NewsSearchRstItem>();
						newsList.add(item);
						keyItemMap.put(key, newsList);
					}
				}
			}
		}
		
		List<NewsSearchRstItem> newsList = new ArrayList<NewsSearchRstItem>();
		ArrayList<List<NewsSearchRstItem>> separatedList = new ArrayList<List<NewsSearchRstItem>>();
		Iterator<Object2ObjectOpenHashMap.Entry<String, List<NewsSearchRstItem>>> iter = keyItemMap.object2ObjectEntrySet().fastIterator();
		while(iter.hasNext()){
			Object2ObjectOpenHashMap.Entry<String, List<NewsSearchRstItem>> entry = iter.next();
			separatedList.add(entry.getValue());
		}
		
		int i = 0, j = 0, maxSize = 0, len = separatedList.size();
		int sizeArray[] = new int[len];
		for(List<NewsSearchRstItem> list : separatedList){
			sizeArray[i++] = list.size();
		}
		for(i = 0; i<len; i++)
			if(sizeArray[i] > maxSize)
				maxSize = sizeArray[i];
		
		/*Circulate getting two items from each separation list */
		while(j < maxSize){
			for(int index = 0; index<len; index++){
				if(sizeArray[index]>j){
					List<NewsSearchRstItem> subList = separatedList.get(index).subList(j, Math.min(sizeArray[index],j+2));
					newsList.addAll(subList);
				}
				else
					continue;
			}
			j+=2;
		}
		
		return newsList;
	}

}
