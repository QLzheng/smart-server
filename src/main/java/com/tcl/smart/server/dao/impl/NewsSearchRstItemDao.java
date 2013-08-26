package com.tcl.smart.server.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.bean.NewsSearchRstItem;
import com.tcl.smart.server.dao.INewsSearchRstItemDao;

/**
 * 新闻搜索条目操作
 * 
 * @author fanjie
 * @date 2013-4-1
 */
public class NewsSearchRstItemDao implements INewsSearchRstItemDao {
	public static final int MAX_NEWS_SIZE_EVERY_KEY = 100;

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	private String col = "NewsSearchRstItem";

	private DBCollection dbCol;

	public void init() {
		dbCol = mongoTemplate.getCollection(col);
		dbCol.ensureIndex(new BasicDBObject().append("program_name", 1).append("program_wiki_id", 1).append("title", 1));
		dbCol.ensureIndex(new BasicDBObject().append("title", 1).append("description", 1));
		dbCol.ensureIndex(new BasicDBObject().append("program_wiki_keys", 1));
	}

	public boolean existNewsSearchRstItem(NewsSearchRstItem newsItem) {
		Query query = new Query(Criteria.where("program_name").is(newsItem.getProgram_name()).and("program_wiki_id").is(newsItem.getProgram_wiki_id()).and("title")
				.is(newsItem.getTitle()));
		long count = mongoTemplate.count(query, col);
		return count > 0;
	}

	public boolean existSameContentNewsSearchRstItemForProgram(NewsSearchRstItem newsItem) {
		return countSameContentNewsSearchRstItemForProgram(newsItem) > 0;
	}

	public long countSameContentNewsSearchRstItemForProgram(NewsSearchRstItem newsItem) {
		Query query = new Query(Criteria.where("title").is(newsItem.getTitle()).and("description").is(newsItem.getDescription()).and("program_name").is(newsItem.getProgram_name())
				.and("program_wiki_id").is(newsItem.getProgram_wiki_id()));
		long count = mongoTemplate.count(query, col);
		return count;
	}

	@Override
	public List<EpgModelDistinctId> groupByEpgsIfCountTooMuch() {
		GroupBy groupBy = GroupBy.key("program_name", "program_wiki_id").initialDocument("{count:0}").reduceFunction("function(doc, prev){prev.count+=1}");
		GroupByResults<NewsSearchRstItem> results = mongoTemplate.group(col, groupBy, NewsSearchRstItem.class);
		List<EpgModelDistinctId> distincts = new ArrayList<EpgModelDistinctId>();
		if (results == null)
			return distincts;
		BasicDBList list = (BasicDBList) results.getRawResults().get("retval");
		for (int i = 0; i < list.size(); i++) {
			BasicDBObject obj = (BasicDBObject) list.get(i);
			EpgModelDistinctId distinctId = new EpgModelDistinctId();
			distinctId.setName((String) obj.get("program_name"));
			distinctId.setWikiId((String) obj.get("program_wiki_id"));
			distinctId.setCount((Double) obj.get("count"));
			if (distinctId.getCount() > MAX_NEWS_SIZE_EVERY_KEY) {
				distincts.add(distinctId);
			}
		}
		return distincts;
	}

	public List<EpgModelDistinctId> mapreduceByEpgsIfCountTooMuch() {
		String map = "function() { emit({name:this.program_name,wiki:this.program_wiki_id},1);}";
		String reduce = "function(key, values) { var x = 0; values.forEach(function (v) {x += v;}); return x;} ";
		BasicDBObject q = new BasicDBObject();
		q.put("program_wiki_id", "516627d9ed454b0124000000");
		MapReduceCommand cmd = new MapReduceCommand(dbCol, map, reduce, "test", MapReduceCommand.OutputType.REPLACE, q);
		cmd.setOutputDB("mapreduce_test");
		MapReduceOutput output = dbCol.mapReduce(cmd);

		List<EpgModelDistinctId> ids = new ArrayList<EpgModelDistinctId>();
		BasicDBObject query = new BasicDBObject();
		query.append("count", new BasicDBObject("$gt", MAX_NEWS_SIZE_EVERY_KEY));
		DBCursor cursor = output.getOutputCollection().find(query);
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			String programName = (String) dbObj.get("name");
			String wikiId = (String) dbObj.get("wiki");
			String count = (String) dbObj.get("count");
			EpgModelDistinctId id = new EpgModelDistinctId();
			id.setName(programName);
			id.setWikiId(wikiId);
			id.setCount(Double.valueOf(count));
			ids.add(id);
		}
		return ids;
	}

	@Override
	public boolean existNewsSearchRstItem(String program_name, String program_wiki_id, String title, Date updateTime) {
		Query query = new Query(Criteria.where("program_name").is(program_name).and("program_wiki_id").is(program_wiki_id).and("title").is(title));
		long count = mongoTemplate.count(query, col);
		return count > 0;
	}

	@Override
	public List<NewsSearchRstItem> findAll() {
		List<NewsSearchRstItem> newsList = mongoTemplate.findAll(NewsSearchRstItem.class, col);
		return newsList;
	}

	@Override
	public List<NewsSearchRstItem> findNewsSearchRstItemsByKeyword(String keyword, int size) {
		Query query = new Query(Criteria.where("program_wiki_keys").is(keyword)).limit(size);
		List<NewsSearchRstItem> newsList = mongoTemplate.find(query, NewsSearchRstItem.class, col);
		return newsList;
	}

	@Override
	public List<NewsSearchRstItem> findNewsSearchRstItemsInHours(int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -hours);
		Date startFrom = calendar.getTime();
		Query query = new Query(Criteria.where("updateTime").gte(startFrom));
		List<NewsSearchRstItem> newsList = mongoTemplate.find(query, NewsSearchRstItem.class, col);
		return newsList;
	}

	@Override
	public List<NewsSearchRstItem> findNewsSearchRstItemsByProgram(String program_name, String program_wiki_id) {
		Query query = new Query(Criteria.where("program_name").is(program_name).and("program_wiki_id").is(program_wiki_id));
		List<NewsSearchRstItem> newsList = mongoTemplate.find(query, NewsSearchRstItem.class, col);
		return newsList;
	}

	@Override
	public List<NewsSearchRstItem> findNewsSearchRstItemsByProgramSortByTime(String program_name, String program_wiki_id) {
		Query query = new Query(Criteria.where("program_name").is(program_name).and("program_wiki_id").is(program_wiki_id));
		query.sort().on("updateTime", Order.ASCENDING);
		List<NewsSearchRstItem> newsList = mongoTemplate.find(query, NewsSearchRstItem.class, col);
		return newsList;
	}

	@Override
	public List<NewsSearchRstItem> findNewsSearchRstItemsByIds(List<String> ids) {
		if (ids == null || ids.size() == 0)
			return null;
		Query query = new Query(Criteria.where("_id").in(ids));
		List<NewsSearchRstItem> news = mongoTemplate.find(query, NewsSearchRstItem.class, col);
		return news;
	}

	@Override
	public NewsSearchRstItem findNewsSearchRstItemsById(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		NewsSearchRstItem news = mongoTemplate.findOne(query, NewsSearchRstItem.class, col);
		return news;
	}

	@Override
	public boolean saveNewsSearchRstItem(NewsSearchRstItem newsItem) {
		if (!existNewsSearchRstItem(newsItem)) {
			mongoTemplate.save(newsItem, col);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean saveNewsSearchRstItemReplaceSameNews(NewsSearchRstItem newsItem) {
		if (existSameContentNewsSearchRstItemForProgram(newsItem)) {
			removeNewsSearchRstItemsByContent(newsItem.getTitle(), newsItem.getDescription());
		}
		mongoTemplate.save(newsItem, col);
		return true;
	}

	@Override
	public void updateNewsSearchRstItem(NewsSearchRstItem newsItem) {
		NewsSearchRstItem news = findNewsSearchRstItemsById(newsItem.get_id());
		if (news != null) {
			Query query = new Query(Criteria.where("_id").is(newsItem.get_id()));
			mongoTemplate.remove(query, col);
			mongoTemplate.save(newsItem, col);
		}
	}

	@Override
	public void removeNewsSearchRstItem(NewsSearchRstItem newsItem) {
		Query query = new Query(Criteria.where("_id").is(newsItem.get_id()));
		mongoTemplate.remove(query, col);
	}

	@Override
	public void removeOldestNewsSearchRstItems(String program_name, String program_wiki_id, int removeSize) {
		List<NewsSearchRstItem> items = findNewsSearchRstItemsByProgramSortByTime(program_name, program_wiki_id);
		removeSize = removeSize > items.size() ? items.size() : removeSize;
		for (int i = 0; i < removeSize; i++) {
			NewsSearchRstItem item = items.get(i);
			mongoTemplate.remove(item, col);
		}
	}

	@Override
	public void removeOldestNewsSearchRstItemsIfCountTooMuch(String program_name, String program_wiki_id, int count) {
		if (count > MAX_NEWS_SIZE_EVERY_KEY) {
			removeOldestNewsSearchRstItems(program_name, program_wiki_id, count - MAX_NEWS_SIZE_EVERY_KEY);
		}
	}

	@Override
	public void removeNewsSearchRstItemsByContent(String title, String description) {
		Query query = new Query(Criteria.where("title").is(title).and("description").is(description));
		mongoTemplate.remove(query, col);
	}
}
