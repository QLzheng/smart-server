package com.tcl.smart.server.crawler.custom;

import java.util.Locale;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;

/**
 * 属性值相似比较过滤器
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class HasAttributeLikeFilter implements NodeFilter {
	private static final long serialVersionUID = -2391463271766765341L;
	protected String mAttribute;
	protected String mValue;

	public HasAttributeLikeFilter(String attribute, String value) {
		mAttribute = attribute.toUpperCase(Locale.ENGLISH);
		mValue = value;
	}

	public boolean accept(Node node) {
		Tag tag;
		Attribute attribute;
		boolean ret;

		ret = false;
		if (node instanceof Tag) {
			tag = (Tag) node;
			attribute = tag.getAttributeEx(mAttribute);
			ret = null != attribute;
			if (ret && (null != mValue))
				ret = attribute.getValue().contains(mValue);
		}
		return ret;
	}
}
