package com.tcl.smart.server.crawler.custom;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;

/**
 * 百科标签过滤器
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeTagFilter implements NodeFilter {
	private static final long serialVersionUID = -1453871394285314477L;

	private List<NodeFilter> predicates = new ArrayList<NodeFilter>();

	public BaikeTagFilter(String tagType, String id, String className) {
		if (tagType != null)
			predicates.add(new TagNameFilter(tagType));
		if (id != null)
			predicates.add(new HasAttributeFilter("id", id));
		if (className != null)
			predicates.add(new HasAttributeFilter("class", className));
	}

	@Override
	public boolean accept(Node node) {
		boolean ret = true;
		for (int i = 0; ret && (i < predicates.size()); i++)
			if (!predicates.get(i).accept(node))
				ret = false;
		return ret;
	}
}
