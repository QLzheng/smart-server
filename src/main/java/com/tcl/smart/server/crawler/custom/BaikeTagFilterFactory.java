package com.tcl.smart.server.crawler.custom;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;

/**
 * 百科标签过滤器工厂
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeTagFilterFactory {
	public static final int TERM_BODY = 1;
	public static final int SEC_CONTENT = 2;
	public static final int LEMMA_CONTENT = 3;
	public static final int PARA = 4;
	public static final int BAIKECARD = 5;
	public static final int BAIKECARD_TITLE = 6;
	public static final int BAIKECARD_IMG = 7;
	public static final int BAIKE_TITLE = 8;
	public static final int PARA_HEADER = 9;
	public static final int CONTENT_HEADER_OR_PARA = 10;
	public static final int SEC_HEADER = 11;
	public static final int MULTI_SEC_CONTENT = 12;
	public static final int MULTI_HEADER_OR_CONTENT = 13;
	public static final int MULTI_HEADER_INDEX = 14;
	public static final int MULTI_HEADER_CONTENT = 15;
	public static final int BAIKE_TITLE_IN_MULTI = 16;
	public static final int BAIKE_CATEGORY_TAG = 17;
	public static final int BAIKE_CATEGORY = 18;

	private static BaikeTagFilter termBodyDivfilter = new BaikeTagFilter("div", null, "content-bd main-body");
	private static BaikeTagLikeFilter secDivFilter = new BaikeTagLikeFilter("div", "sec-content", null);
	private static BaikeTagLikeFilter lemmaDivFilter = new BaikeTagLikeFilter("div", "lemmaContent-", null);
	private static BaikeTagFilter paraDivFilter = new BaikeTagFilter("div", null, "para");
	private static BaikeTagFilter baikeCardFilter = new BaikeTagFilter("div", null, "mod-top");
	private static HasAttributeFilter baikeCardTitleFilter = new HasAttributeFilter("class", "card-title");
	private static BaikeTagLikeFilter baikeCardImgFilter = new BaikeTagLikeFilter("img", null, "card-image");
	private static HasAttributeFilter baikeTitleFilter = new HasAttributeFilter("class", "title");
	private static HasAttributeFilter paraHeaderFilter = new HasAttributeFilter("class", "headline-content");
	private static OrFilter contentHeaderOrParaFilter = new OrFilter(paraHeaderFilter, paraDivFilter);
	private static BaikeTagLikeFilter secHeaderDivFilter = new BaikeTagLikeFilter("div", "sec-header", null);
	private static BaikeTagLikeFilter multiContentDivFilter = new BaikeTagLikeFilter("div", "mul-content", null);
	private static OrFilter multiHeaderOrContentFilter = new OrFilter(secHeaderDivFilter, secDivFilter);
	private static BaikeTagFilter multiHeaderIndexFilter = new BaikeTagFilter("span", null, "headline-index");
	private static BaikeTagFilter multiHeaderContentFilter = new BaikeTagFilter("span", null, "headline-content");
	private static HasAttributeFilter baikeTitleInMultiFilter = new HasAttributeFilter("class", "title sub-title");
	private static BaikeTagFilter categoryTagFilter = new BaikeTagFilter("dl", "viewExtCati", null);
	private static BaikeTagFilter categoryFilter = new BaikeTagFilter("a", null, null);

	public static final NodeFilter getFilter(int type) {
		switch (type) {
		case TERM_BODY:
			return termBodyDivfilter;
		case SEC_CONTENT:
			return secDivFilter;
		case LEMMA_CONTENT:
			return lemmaDivFilter;
		case PARA:
			return paraDivFilter;
		case BAIKECARD:
			return baikeCardFilter;
		case BAIKECARD_TITLE:
			return baikeCardTitleFilter;
		case BAIKECARD_IMG:
			return baikeCardImgFilter;
		case BAIKE_TITLE:
			return baikeTitleFilter;
		case PARA_HEADER:
			return paraHeaderFilter;
		case CONTENT_HEADER_OR_PARA:
			return contentHeaderOrParaFilter;
		case SEC_HEADER:
			return secHeaderDivFilter;
		case MULTI_SEC_CONTENT:
			return multiContentDivFilter;
		case MULTI_HEADER_OR_CONTENT:
			return multiHeaderOrContentFilter;
		case MULTI_HEADER_INDEX:
			return multiHeaderIndexFilter;
		case MULTI_HEADER_CONTENT:
			return multiHeaderContentFilter;
		case BAIKE_TITLE_IN_MULTI:
			return baikeTitleInMultiFilter;
		case BAIKE_CATEGORY_TAG:
			return categoryTagFilter;
		case BAIKE_CATEGORY:
			return categoryFilter;
		default:
			return null;
		}
	}
}
