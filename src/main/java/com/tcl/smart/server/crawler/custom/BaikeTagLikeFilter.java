package com.tcl.smart.server.crawler.custom;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.TagNameFilter;

/**
 * 百科标签过滤器
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeTagLikeFilter implements NodeFilter {
	private static final long serialVersionUID = -1453871394285314477L;

	private List<NodeFilter> predicates = new ArrayList<NodeFilter>();

	public BaikeTagLikeFilter(String tagType, String id, String className) {
		predicates.add(new TagNameFilter(tagType));
		if (id != null)
			predicates.add(new HasAttributeLikeFilter("id", id));
		if (className != null)
			predicates.add(new HasAttributeLikeFilter("class", className));
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
