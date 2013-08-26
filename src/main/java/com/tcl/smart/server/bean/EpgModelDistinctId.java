package com.tcl.smart.server.bean;

import java.util.ArrayList;
import java.util.List;

import com.tcl.smart.server.dao.IMovieDao;

/**
 * @author fanjie
 * @date 2013-4-11
 */
public class EpgModelDistinctId {
	private String name;
	private String wikiId;
	private Double count = new Double(1);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWikiId() {
		return wikiId;
	}

	public void setWikiId(String wikiId) {
		this.wikiId = wikiId;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public List<String> getKeys(IMovieDao movieDao) {
		List<String> keys = new ArrayList<String>();
		MovieModel model = movieDao.getMovieById(wikiId);
		if (model != null) {
			if (model.getModel() != null) {
				keys = model.getKeyQueryItems();
			}
		} else {
			keys.add(parseName(name));
		}
		return keys;
	}

	private String parseName(String name) {
		if (name.contains("：")) {
			if (name.contains("剧场：")) {
				name = name.substring(name.indexOf("：") + 1, name.length());
			} else if (name.contains("连续剧：")) {
				name = name.substring(name.indexOf("：") + 1, name.length());
			} else if (name.contains("影院：")) {
				name = name.substring(name.indexOf("：") + 1, name.length());
			} else if (name.contains("剧院：")) {
				name = name.substring(name.indexOf("：") + 1, name.length());
			} else if (name.contains("电影：")) {
				name = name.substring(name.indexOf("：") + 1, name.length());
			} else if (name.contains("养生")) {
				name = name.substring(name.indexOf("：") + 1, name.length());
			} else if (name.contains("故事")) {
				name = name.substring(name.indexOf("：") + 1, name.length());
			}
		}
		return name;
	}

	public String getParsedName() {
		return parseName(name);
	}

	@Override
	public String toString() {
		return name + "(" + wikiId + "): " + count;
	}
}
