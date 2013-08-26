package com.tcl.smart.server.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询关键字的与或拼接
 * 
 * @author fanjie
 * @date 2013-4-9
 */
public class QueryString4Rss {
	private List<String> andStrs = new ArrayList<String>();
	private List<String> orStrs = new ArrayList<String>();

	public void and(String key) {
		if (key != null)
			andStrs.add(key);
	}

	public void or(String key) {
		if (key != null)
			orStrs.add(key);
	}

	public String toQueryStr() {
		if (andStrs.size() == 0 && orStrs.size() == 0) {
			return null;
		}
		StringBuffer queryStr = new StringBuffer();
		for (int i = 0; i < andStrs.size(); i++) {
			String andStr = andStrs.get(i);
			queryStr.append(andStr);
			if (i != andStrs.size() - 1)
				queryStr.append(" ");
		}
		if (orStrs.size() > 0) {
			if (andStrs.size() > 0)
				queryStr.append(" ");
			queryStr.append("(");
			for (int i = 0; i < orStrs.size(); i++) {
				String orStr = orStrs.get(i);
				if (i != 0)
					queryStr.append(" ");
				queryStr.append(orStr);
				if (i != orStrs.size() - 1)
					queryStr.append(" |");
			}
			queryStr.append(")");
		}
		return queryStr.toString();
	}

	public List<String> toKeyList() {
		List<String> rtnStrs = new ArrayList<String>();
		if (andStrs.size() == 0 && orStrs.size() == 0) {
			return rtnStrs;
		}
		if (andStrs.size() > 0) {
			rtnStrs.addAll(andStrs);
		}
		if (orStrs.size() > 0) {
			rtnStrs.addAll(orStrs);
		}
		return rtnStrs;
	}
}
