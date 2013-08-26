package com.tcl.smart.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.YihaodianIndex;
import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.dao.IBaikeBeanDao;
import com.tcl.smart.server.dao.ITvListDao;
import com.tcl.smart.server.dao.IYihaodianProductDao;

public class ProductTest {
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IBaikeBeanDao baikeDao = (IBaikeBeanDao) context.getBean("baikeBeanDao");
		IYihaodianProductDao dao = (IYihaodianProductDao) context.getBean("yihaodianProductDao");

//		List<YihaodianProductBean> list = dao.findYihaodianProductByName("Æ»¹û");
//		for(YihaodianProductBean a :list){
//			System.out.println(a.toString());
//		}
		
		
        YihaodianIndex a = getAllIndexes();
        try {
			List<YihaodianProductBean> list = getProducts(a);
			for(YihaodianProductBean item : list){
				dao.insertYihaodianProductBean(item);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        System.out.println(a.toString());

	}
	
	/**
	 * get all the products through Yihaodian API
	 * @param index
	 * @return List<YihaodianProductBean>
	 * @throws MalformedURLException 
	 * @throws DocumentException 
	 */
	private static List<YihaodianProductBean> getProducts(YihaodianIndex index) throws MalformedURLException, DocumentException{
		List<YihaodianProductBean> products = new ArrayList<YihaodianProductBean>();
		SAXReader reader = new SAXReader();
		URL urlPath = new URL(index.getProduct_path());
		List<String> urlxmls = index.getProducts();
		for(String urlxml:urlxmls){
			URL url = new URL(urlPath,urlxml);
			Document  document = reader.read(url);
			Element root = document.getRootElement();
//			Element eleProducts = root.element("products");
			int j=0;//TODO
			for (Iterator i = root.elementIterator("product"); i.hasNext() ;) {
				if(j>20) break;//TODO
			    Element prod = (Element) i.next();
			    YihaodianProductBean product = parserProductElement(prod);
			    if(null!=product) products.add(product);
			    j++;//TODO
			}
			
			break;//TODO
		}
		return products;
	}
	
	private static YihaodianProductBean parserProductElement(Element e){
		if(null==e) return null;
		YihaodianProductBean product = new YihaodianProductBean();
		product.setId(e.elementText("product_id"));
		product.setProduct_id(e.elementText("product_id"));
		product.setBrand_name(e.elementText("brand_name"));
		product.setPic_url(e.elementText("pic_url"));
		product.setProduct_url(e.elementText("product_url"));
		product.setProduct_url_m(e.elementText("product_url_m"));
		product.setSale_type(e.elementText("sale_type"));
		product.setSubtitle(e.elementText("subtitle"));
		product.setTitle(e.elementText("title"));
		//parser update time string to Date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = e.element("updatetime").getText();
		Date date = null;
		try {
			date = simpleDateFormat.parse(dateString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		product.setUpdatetime(date);
		//parser bar_codes to list of string
		Element ebar_codes = e.element("bar_codes");
		List<String> bar_codes = new ArrayList<String>();
		for ( Iterator i = ebar_codes.elementIterator("bar_code"); i.hasNext();) {
		       Element prod = (Element) i.next();
		       bar_codes.add(prod.getText());
		}
		product.setBar_codes(bar_codes);
		

		return product;
	}
	
	/**
	 * get all the index informations of Yihaodian
	 * @return YihaodianIndex
	 */
	private static YihaodianIndex getAllIndexes(){
		YihaodianIndex index = new YihaodianIndex();
		SAXReader reader = new SAXReader();
		try {
			URL url = new URL("http://union.yihaodian.com/api/productInfo/yihaodian/index.xml");
			Document  document = reader.read(url);
			Element root = document.getRootElement();
			index.setVersion(root.element("version").getText());
			index.setCategory_path(root.element("category_path").getText());
			index.setPromotion_path(root.element("promotion_path").getText());
			index.setProduct_desc_path(root.element("product_desc_path").getText());
			index.setProduct_path(root.element("product_path").getText());
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = root.element("updatetime").getText();
			Date date = simpleDateFormat.parse(dateString);
			index.setUpdatetime(date);
			
			Element eleProducts = root.element("products");
			List<String> products = new ArrayList<String>();
			for ( Iterator i = eleProducts.elementIterator("product"); i.hasNext();) {
			       Element prod = (Element) i.next();
			       products.add(prod.getText());
			}
			index.setProducts(products);
			
			Element eleProduct_descs = root.element("product_descs");
			List<String> product_descs = new ArrayList<String>();
			for ( Iterator i = eleProduct_descs.elementIterator("product_desc"); i.hasNext();) {
			       Element prod = (Element) i.next();
			       product_descs.add(prod.getText());
			}
			index.setProduct_descs(product_descs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return index;
	}
}
