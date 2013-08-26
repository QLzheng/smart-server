package com.tcl.smart.server.dao;

import java.util.Date;
import java.util.List;

import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.NewsSearchRstItem;

/**
 * @author fanjie
 * @date 2013-4-10
 */
public interface INewsSearchRstItemDao {
	public boolean existNewsSearchRstItem(String program_name, String program_wiki_id, String title, Date updateTime);

	public boolean existSameContentNewsSearchRstItemForProgram(NewsSearchRstItem newsItem);

	public long countSameContentNewsSearchRstItemForProgram(NewsSearchRstItem newsItem);

	public List<NewsSearchRstItem> findAll();

	public List<NewsSearchRstItem> findNewsSearchRstItemsByKeyword(String keyword, int size);

	public List<EpgModelDistinctId> groupByEpgsIfCountTooMuch();

	public List<NewsSearchRstItem> findNewsSearchRstItemsInHours(int hours);

	public NewsSearchRstItem findNewsSearchRstItemsById(String id);

	public List<NewsSearchRstItem> findNewsSearchRstItemsByIds(List<String> ids);

	public List<NewsSearchRstItem> findNewsSearchRstItemsByProgram(String program_name, String program_wiki_id);

	public List<NewsSearchRstItem> findNewsSearchRstItemsByProgramSortByTime(String program_name, String program_wiki_id);

	public boolean saveNewsSearchRstItem(NewsSearchRstItem newsItem);

	public boolean saveNewsSearchRstItemReplaceSameNews(NewsSearchRstItem newsItem);

	public void updateNewsSearchRstItem(NewsSearchRstItem newsItem);

	public void removeNewsSearchRstItem(NewsSearchRstItem newsItem);

	public void removeOldestNewsSearchRstItems(String program_name, String program_wiki_id, int removeSize);

	public void removeOldestNewsSearchRstItemsIfCountTooMuch(String program_name, String program_wiki_id, int count);

	public void removeNewsSearchRstItemsByContent(String title, String description);
}
