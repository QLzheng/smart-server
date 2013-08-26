package com.tcl.smart.server.crawler.custom;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.bean.BaikeCard;
import com.tcl.smart.server.bean.BaikeCategory;
import com.tcl.smart.server.bean.BaikeHeader;
import com.tcl.smart.server.bean.BaikeImage;
import com.tcl.smart.server.bean.BaikePara;
import com.tcl.smart.server.bean.BaikeSection;
import com.tcl.smart.server.dao.impl.BaikeBeanDao;

/**
 * �ٶȰٿƴ���������
 * 
 * @author fanjie
 * @date 2013-4-27
 */
public class BaiduBaikeParserTool {
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		BaikeBeanDao baikeBeanDao = (BaikeBeanDao) context.getBean("baikeBeanDao");
		// BaikeBean bb = new BaikeBean();
		// BaikeHeader bh = new BaikeHeader();
		// bh.setIndex("3.");
		// bh.setContent("��ʤ���ݳ�����");
		// bb.setName("����Ļ���");
		// bb.setHeader(bh);
		// System.out.println(baikeBeanDao.existStrictBaikeBean(bb));
		// List<BaikeBean> baikes = extractText("E://baike//182228.htm");
		// for (BaikeBean baike : baikes) {
		// if (!baikeBeanDao.existStrictBaikeBean(baike))
		// baikeBeanDao.insertBaikeBean(baike);
		// }
		File baikePath = new File("E://baike/");
		File[] baikeFiles = baikePath.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith("htm") && pathname.canRead() && pathname.canWrite())
					return true;
				return false;
			}
		});
		int size = baikeFiles.length;
		int i = 1;
		for (File file : baikeFiles) {
			System.out.println("(" + i++ + "/" + size + ")" + file.getAbsolutePath());
			List<BaikeBean> baikes = extractText(file.getAbsolutePath());
			for (BaikeBean baike : baikes) {
				if (!baikeBeanDao.existStrictBaikeBean(baike)) {
					baikeBeanDao.insertBaikeBean(baike);
					System.out.println("Saved new baike: " + baike);
				}
			}
		}
	}

	// <div class="content-bd main-body"> �˽ڵ���Ϊ�ô�����������

	// <div id="sec-content*"> ��ֻ��һ���������������ܽڵ���Ϊ�˽ڵ�
	// <div id="sec-header*"> ���ж����������˽ڵ�Ϊ�ô����ı�־

	// <div class="mod-top"> ���аٿ���Ƭ�����ڴ˽ڵ��£���lemmaContent-*�ڵ�ƽ��
	// <div class="card-summary-content"> ���аٿ���Ƭ����˽ڵ��µ�Ϊ������
	// <div class="para">

	// <div id="lemmaContent-*"> �˽ڵ���Ϊ�����������м������
	// <div class="para"> ÿ���˽ڵ�Ϊһ������

	// <h1 class="title">**</h1>

	// <h2 class="headline-1">��
	// <span class="headline-content">**</span> �˽ڵ���Ϊ�ö���ı���
	public static List<BaikeBean> extractText(String url) {
		if (url == null)
			return null;
		Parser parser;
		try {
			parser = new Parser(url);
			parser.setEncoding("UTF-8");
			NodeFilter filter = BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.TERM_BODY);
			NodeList termBody = parser.parse(filter);
			if (termBody.size() > 0) {
				Div termBodyDiv = (Div) termBody.elements().nextNode();
				List<BaikeBean> baikes = extractTermsByDiv(termBodyDiv);
				return baikes;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		return null;
	}

	public static List<BaikeBean> extractTermsByDiv(Div termBodyDiv) {
		NodeList termBodyContents = termBodyDiv.getChildren();
		NodeList secContent = termBodyContents.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.SEC_CONTENT));
		// ����ֻ��һ������
		if (secContent.size() > 0) {
			BaikeBean baike = extractSingleBaikeBySecContent(secContent);
			return Arrays.asList(baike);
		} else {
			NodeList multiSecContent = termBodyContents.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.MULTI_SEC_CONTENT));
			if (multiSecContent.size() > 0) {
				List<BaikeBean> baikes = extractMultiBaikeBySecContent(multiSecContent);
				return baikes;
			} else {
				System.err.println("Error when parsing.");
				return null;
			}
		}
	}

	public static BaikeBean extractSingleBaikeBySecContent(NodeList secContent) {
		BaikeBean baike = new BaikeBean();

		// �����ٿƴ�������
		NodeList baikeTitleNode = secContent.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.BAIKE_TITLE), true);
		if (baikeTitleNode.size() > 0) {
			TagNode titleNode = (TagNode) baikeTitleNode.elements().nextNode();
			baike.setName(titleNode.getChildren().elementAt(0).toPlainTextString().trim());
		} else {
			System.err.println("No title.");
		}

		// �����ٿ���Ƭ
		NodeList baikeCardNode = secContent.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.BAIKECARD), true);
		if (baikeCardNode.size() > 0) {
			BaikeCard card = extractCardByCardNode(baikeCardNode);
			baike.setCard(card);
		}

		// ��������
		List<BaikeSection> sections = extractSectionsBySecContentNode(secContent);
		baike.setSections(sections);

		// �������ŷ���
		List<BaikeCategory> categories = extractCategoriesBySecContentNode(secContent);
		baike.setCategories(categories);

		return baike;
	}

	public static BaikeBean extractSingleBaikeInMultiBySecContent(NodeList secContent) {
		BaikeBean baike = new BaikeBean();

		// �����ٿƴ�������
		NodeList baikeTitleNode = secContent.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.BAIKE_TITLE_IN_MULTI), true);
		if (baikeTitleNode.size() > 0) {
			TagNode titleNode = (TagNode) baikeTitleNode.elements().nextNode();
			baike.setName(titleNode.toPlainTextString().trim());
		} else {
			System.err.println("No title.");
		}

		// �����ٿ���Ƭ
		NodeList baikeCardNode = secContent.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.BAIKECARD), true);
		if (baikeCardNode.size() > 0) {
			BaikeCard card = extractCardByCardNode(baikeCardNode);
			baike.setCard(card);
		}

		// ��������
		List<BaikeSection> sections = extractSectionsBySecContentNode(secContent);
		baike.setSections(sections);

		// �������ŷ���
		List<BaikeCategory> categories = extractCategoriesBySecContentNode(secContent);
		baike.setCategories(categories);

		return baike;
	}

	public static BaikeCard extractCardByCardNode(NodeList baikeCardNode) {
		if (baikeCardNode.size() == 0)
			return null;
		BaikeCard card = new BaikeCard();
		NodeList baikeCardTitleNode = baikeCardNode.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.BAIKECARD_TITLE), true);
		if (baikeCardTitleNode.size() > 0) {
			card.setTitle(baikeCardTitleNode.asString());
		}
		NodeList baikeCardImgNode = baikeCardNode.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.BAIKECARD_IMG), true);
		if (baikeCardImgNode.size() > 0) {
			ImageTag imgTag = (ImageTag) baikeCardImgNode.elements().nextNode();
			BaikeImage img = new BaikeImage();
			img.setAlt(imgTag.getAttribute("alt"));
			img.setTitle(imgTag.getAttribute("title"));
			img.setWidth(imgTag.getAttribute("width"));
			img.setHeight(imgTag.getAttribute("height"));
			img.setUrl(imgTag.getAttribute("src"));
			card.setImg(img);
		}
		NodeList baikeCardSummaryNode = baikeCardNode.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.PARA), true);
		if (baikeCardSummaryNode.size() > 0) {
			Div paraDiv = (Div) baikeCardSummaryNode.elements().nextNode();
			BaikePara para = new BaikePara();
			para.setHtml(paraDiv.toHtml());
			para.setText(paraDiv.toPlainTextString());
			card.setSummary(para);
		}
		return card;
	}

	public static List<BaikeSection> extractSectionsBySecContentNode(NodeList secContent) {
		NodeList contentNode = secContent.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.LEMMA_CONTENT), true);
		if (contentNode.size() > 0) {
			NodeList paraNodes = contentNode.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.CONTENT_HEADER_OR_PARA), true);
			SimpleNodeIterator iterator = paraNodes.elements();
			List<BaikeSection> sections = new ArrayList<BaikeSection>();
			Queue<String> headerQueue = new Queue<String>();
			Queue<BaikePara> paraQueue = new Queue<BaikePara>();
			while (iterator.hasMoreNodes()) {
				TagNode node = (TagNode) iterator.nextNode();
				if (node.getAttribute("class") != null) {
					if (node.getAttribute("class").equals("headline-content")) {
						if (!paraQueue.isQueueEmpty()) {
							BaikeSection section = new BaikeSection();
							while (!headerQueue.isQueueEmpty()) {
								section.addHead(headerQueue.deQueue());
							}
							while (!paraQueue.isQueueEmpty()) {
								section.addPara(paraQueue.deQueue());
							}
							sections.add(section);
						}
						headerQueue.enQueue(node.toPlainTextString());
					} else if (node.getTagName().equals("DIV") && node.getAttribute("class").equals("para")) {
						BaikePara para = new BaikePara();
						para.setHtml(node.toHtml());
						para.setText(node.toPlainTextString());
						paraQueue.enQueue(para);
					}
				}
			}
			BaikeSection section = new BaikeSection();
			while (!headerQueue.isQueueEmpty()) {
				section.addHead(headerQueue.deQueue());
			}
			while (!paraQueue.isQueueEmpty()) {
				section.addPara(paraQueue.deQueue());
			}
			sections.add(section);
			return sections;
		}
		return null;
	}

	public static List<BaikeCategory> extractCategoriesBySecContentNode(NodeList secContent) {
		NodeList cateNode = secContent.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.BAIKE_CATEGORY_TAG), true);
		if (cateNode.size() > 0) {
			NodeList categoryNodes = cateNode.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.BAIKE_CATEGORY), true);
			SimpleNodeIterator iterator = categoryNodes.elements();
			List<BaikeCategory> categories = new ArrayList<BaikeCategory>();
			while (iterator.hasMoreNodes()) {
				TagNode node = (TagNode) iterator.nextNode();
				BaikeCategory category = new BaikeCategory();
				category.setClassName(node.getAttribute("class"));
				category.setHref(node.getAttribute("href"));
				category.setContent(node.toPlainTextString().trim());
				categories.add(category);
			}
			return categories;
		}
		return null;
	}

	public static List<BaikeBean> extractMultiBaikeBySecContent(NodeList multiSecContent) {
		NodeList multiHeaderOrContent = multiSecContent.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.MULTI_HEADER_OR_CONTENT), true);
		if (multiHeaderOrContent.size() > 0) {
			SimpleNodeIterator iterator = multiHeaderOrContent.elements();
			List<BaikeBean> baikes = new ArrayList<BaikeBean>();
			BaikeBean baike = null;
			BaikeHeader header = null;
			while (iterator.hasMoreNodes()) {
				TagNode node = (TagNode) iterator.nextNode();
				if (node.getAttribute("id") != null) {
					if (node.getAttribute("id").contains("sec-header")) {
						NodeList multiHeaderIndex = node.getChildren().extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.MULTI_HEADER_INDEX), true);
						NodeList multiHeaderContent = node.getChildren()
								.extractAllNodesThatMatch(BaikeTagFilterFactory.getFilter(BaikeTagFilterFactory.MULTI_HEADER_CONTENT), true);
						if (multiHeaderIndex.size() > 0 || multiHeaderContent.size() > 0) {
							header = new BaikeHeader();
							header.setIndex(multiHeaderIndex.asString());
							header.setContent(multiHeaderContent.asString());
						}
					} else if (node.getAttribute("id").contains("sec-content")) {
						baike = extractSingleBaikeInMultiBySecContent(node.getChildren());
						if (baike != null) {
							if (header != null) {
								baike.setHeader(header);
								baikes.add(baike);
								header = null;
								baike = null;
							}
						}
					}
				}
			}
			return baikes;
		}
		return null;
	}

	public static boolean isBlock(String url) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("GBK");
			TagNameFilter divFilter = new TagNameFilter("div");
			NodeList collectionList = parser.parse(divFilter);
			for (SimpleNodeIterator e = collectionList.elements(); e.hasMoreNodes();) {
				Div totalDiv = (Div) e.nextNode();
				String result = totalDiv.toPlainTextString();
				if (result.indexOf("���ĵ��Ի����ڵľ����������쳣�ķ���") != -1) {
					return true;
				}
			}
		} catch (ParserException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	public static boolean noTerm(String url) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("GBK");
			TagNameFilter divFilter = new TagNameFilter("div");
			NodeList collectionList = parser.parse(divFilter);
			for (SimpleNodeIterator e = collectionList.elements(); e.hasMoreNodes();) {
				Div totalDiv = (Div) e.nextNode();
				String result = totalDiv.toPlainTextString();
				if (result.indexOf("��������Ĵ���������") != -1) {
					return true;
				}
			}
		} catch (ParserException e1) {
			e1.printStackTrace();
		}
		return false;
	}
}
