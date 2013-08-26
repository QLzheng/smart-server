package com.tcl.smart.server.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.smart.server.bean.YihaodianIndex;
import com.tcl.smart.server.bean.YihaodianProductBean;
import com.tcl.smart.server.bean.YihaodianCharacter;
import com.tcl.smart.server.bean.YihaodianProductDesc;
import com.tcl.smart.server.bean.YihaodianSale_price;
import com.tcl.smart.server.bean.YihaodianStorage;
import com.tcl.smart.server.dao.IYihaodianProductDao;
import com.tcl.smart.server.service.IYihaodianApiService;

public class YihaodianApiService implements IYihaodianApiService {
	
	private final String indexUrl = "http://union.yihaodian.com/api/productInfo/yihaodian/index.xml";
	private static final Logger logger = LoggerFactory.getLogger(YihaodianApiService.class);
	private IYihaodianProductDao productDao;
	private volatile STATE state = STATE.RUN;
	
	public YihaodianApiService(IYihaodianProductDao productDao){
		this.productDao = productDao;
	}
	
	@Override
	public YihaodianIndex getYihaodianIndex() {
		YihaodianIndex index = getAllIndexes();
		return index;
	}

	@Override
	public void getAndStroeProducts() {
		YihaodianIndex index = getAllIndexes();
		getProducts(index);
		getProductsDesc(index);
	}

	
	/**
	 * get all the products through Yihaodian product API
	 * @param index
	 */
	private void getProducts(YihaodianIndex index){
		
		SAXReader reader = new SAXReader();
		URL urlPath;
		try {
			urlPath = new URL(index.getProduct_path());
		} catch (MalformedURLException e) {
			logger.error("get total Product_path fail");
			e.printStackTrace();
			return;
		}
		List<String> urlxmls = index.getProducts();
		int totalxml=urlxmls.size();
		int xmlnow=0;
		for(String urlxml:urlxmls){
			long t0 = System.currentTimeMillis();
			if(state==STATE.STOP){
				logger.info("STOP getting Products!");
				break;
			}
			xmlnow++;
			//get products from one xml
			List<YihaodianProductBean> products = new ArrayList<YihaodianProductBean>();
			URL url;
			try {
				url = new URL(urlPath,urlxml);
			} catch (MalformedURLException e) {
				logger.error("get Product_path fail,try another!");
				e.printStackTrace();
				continue;
			}
			Document document = null;
			try {
				document = reader.read(url);
			} catch (Exception e) {
				logger.error("parser xml fail!try another!");
				e.printStackTrace();
				continue;
			}
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("product"); i.hasNext() ;) {
			    Element prod = (Element) i.next();
			    YihaodianProductBean product = parserProductElement(prod);
			    if(null!=product) products.add(product);
			}
			
			//store products to database
			for(YihaodianProductBean product:products){
				productDao.insertYihaodianProductBean(product);
			}
			
			double t3 = (System.currentTimeMillis() - t0) / 1000.0;
			System.out.println(""+xmlnow+"/"+totalxml+" Finish " + urlxml + " ,got " + products.size()
					+ " products."+" Use " + t3 + " seconds!");
		}
	}
	
	/**
	 * get all the products through Yihaodian product_desc API
	 * and store to the product database
	 * @param index
	 */
	private void getProductsDesc(YihaodianIndex index){
		SAXReader reader = new SAXReader();
		URL urlPath;
		try {
			urlPath = new URL(index.getProduct_desc_path());
		} catch (MalformedURLException e) {
			logger.error("get total getProduct_desc_path fail");
			e.printStackTrace();
			return;
		}
		List<String> urlxmls = index.getProduct_descs();
		int totalxml=urlxmls.size();
		int xmlnow=0;
		for(String urlxml:urlxmls){
			long t0 = System.currentTimeMillis();
			if(state==STATE.STOP){
				logger.info("STOP getting Product_descs!");
				break;
			}
			xmlnow++;
			//get product_descs from one xml
			List<YihaodianProductDesc> descs = new ArrayList<YihaodianProductDesc>();
			URL url;
			try {
				url = new URL(urlPath,urlxml);
			} catch (MalformedURLException e) {
				logger.error("get getProduct_desc_path fail,try another!");
				e.printStackTrace();
				continue;
			}
			Document document = null;
			try {
				document = reader.read(url);
			} catch (Exception e) {
				logger.error("parser xml fail!try another!");
				e.printStackTrace();
				continue;
			}
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("desc"); i.hasNext() ;) {
			    Element prod = (Element) i.next();
			    YihaodianProductDesc desc = parserProduct_descElement(prod);
			    if(null!=descs) descs.add(desc);
			}
			
			//store products to database
			for(YihaodianProductDesc desc:descs){
				productDao.updateProduct_desc(desc.getProduct_id(), desc.getProduct_desc());
			}
			
			double t3 = (System.currentTimeMillis() - t0) / 1000.0;
			System.out.println(""+xmlnow+"/"+totalxml+" Finish " + urlxml + " ,got " + descs.size()
					+ " product_descs."+" Use " + t3 + " seconds!");
		}
	}
	
	

	/**
	 * parser an Element to YihaodianProductBean
	 * @param Element e
	 * @return YihaodianProductBean
	 */
	private YihaodianProductBean parserProductElement(Element e){
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
		product.setCategory_id(e.elementText("category_id"));
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
		
		Element echaracter = e.element("character");
		List<YihaodianCharacter> character = new ArrayList<YihaodianCharacter>();
		for ( Iterator i = echaracter.elementIterator(); i.hasNext();) {
		       Element prod = (Element) i.next();
		       String name = prod.getText();
		       prod = (Element) i.next();
		       String value = prod.getText();
		       character.add(new YihaodianCharacter(name,value));
		}
		product.setCharacter(character);
		
		Element esale_price = e.element("sale_price");
		List<YihaodianSale_price> sale_price = new ArrayList<YihaodianSale_price>();
		for ( Iterator i = esale_price.elementIterator(); i.hasNext();) {
		       Element prod = (Element) i.next();
		       String priceString = prod.getText();
		       Double price = Double.valueOf(priceString);
		       String regions = prod.attribute("region").getText();
		       String[] regionList = regions.split(",");
		       for(int j=0;j<regionList.length;j++){
		    	   sale_price.add(new YihaodianSale_price(regionList[j],price));
		       }
		}
		product.setSale_price(sale_price);
		
		Element estorage = e.element("storage");
		List<YihaodianStorage> storage = new ArrayList<YihaodianStorage>();
		for ( Iterator i = estorage.elementIterator(); i.hasNext();) {
		       Element prod = (Element) i.next();
		       String storageString = prod.getText();
		       boolean value = true;
		       if(storageString.equals("0")) value = false;
		       String regions = prod.attribute("region").getText();
		       String[] regionList = regions.split(",");
		       for(int j=0;j<regionList.length;j++){
		    	   storage.add(new YihaodianStorage(regionList[j], value));
		       }
		}
		product.setStorage(storage);
		
		return product;
	}
	
	/**
	 * parser an Element to YihaodianProductDesc
	 * @param e
	 * @return
	 */
	private YihaodianProductDesc parserProduct_descElement(Element e){
		YihaodianProductDesc desc = new YihaodianProductDesc();
		desc.setProduct_id(e.elementText("product_id"));
		desc.setProduct_desc(e.elementText("product_desc"));
		return desc;		
	}
	

	/**
	 * get all the index informations of Yihaodian
	 * @return YihaodianIndex
	 */
	private YihaodianIndex getAllIndexes(){
		YihaodianIndex index = new YihaodianIndex();
		SAXReader reader = new SAXReader();
		try {
			URL url = new URL(indexUrl);
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

	@Override
	public void changeState(STATE state) {
		this.state=state;
	}


}
