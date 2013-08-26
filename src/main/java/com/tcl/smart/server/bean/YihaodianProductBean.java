package com.tcl.smart.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.tcl.smart.server.util.KeyType;

public class YihaodianProductBean implements Serializable {
	private static final long serialVersionUID = -3943578978837134839L;

	private static final int MAX_TITLE_LEN = 4;

	@Id
	private String id;
	// 产品id
	private String product_id;
	// 产品名称
	private String title;
	// 产品副标题
	private String subtitle;
	// 品牌名称
	private String brand_name;
	// 商品条形码
	private List<String> bar_codes;
	// 商品属性
	private List<YihaodianCharacter> character;
	// 商品主图片地址
	private String pic_url;
	// 类目id
	private String category_id;
	// 商品详情地址
	private String product_url;
	// 商品详情地址 for moblie
	private String product_url_m;
	// 打折信息，discount有打折，fixed没有打折
	private String sale_type;
	// 文件更新时间
	private Date updatetime;
	// 1号店价格及其对应的省份
	private List<YihaodianSale_price> sale_price;
	// 是否有库存及其省份，1表示有库存，0表示没库存
	private List<YihaodianStorage> storage;

	private String product_desc;

	/** 搜索关键字 */
	private String searchKey;

	/** 推荐关键字类型 */
	private List<KeyType> keyType;

	public List<YihaodianSale_price> getSale_price() {
		return sale_price;
	}

	public void setSale_price(List<YihaodianSale_price> sale_price) {
		this.sale_price = sale_price;
	}

	public List<YihaodianStorage> getStorage() {
		return storage;
	}

	public void setStorage(List<YihaodianStorage> storage) {
		this.storage = storage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleEtc() {
		if (title == null)
			return null;
		if (title.length() > MAX_TITLE_LEN) {
			return title.substring(0, MAX_TITLE_LEN) + "..";
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public List<String> getBar_codes() {
		return bar_codes;
	}

	public void setBar_codes(List<String> bar_codes) {
		this.bar_codes = bar_codes;
	}

	public List<YihaodianCharacter> getCharacter() {
		return character;
	}

	public void setCharacter(List<YihaodianCharacter> character) {
		this.character = character;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getProduct_url() {
		return product_url;
	}

	public void setProduct_url(String product_url) {
		this.product_url = product_url;
	}

	public String getProduct_url_m() {
		return product_url_m;
	}

	public void setProduct_url_m(String product_url_m) {
		this.product_url_m = product_url_m;
	}

	public String getSale_type() {
		return sale_type;
	}

	public void setSale_type(String sale_type) {
		this.sale_type = sale_type;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getProduct_desc() {
		return product_desc;
	}

	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}

	public List<KeyType> getKeyType() {
		return keyType;
	}

	public void setKeyType(List<KeyType> keyType) {
		this.keyType = keyType;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getIgnoredImgDescription() {
		if (product_desc == null || product_desc.trim().equals(""))
			return null;
		String regEx = "<img[^>]*>";
		return product_desc.replaceAll(regEx, "");
	}

	public String getSource() {
		StringBuffer des = new StringBuffer();
		if (keyType != null) {
			for (KeyType type : keyType) {
				if (type != KeyType.TAG) {
					des.append(type.toString());
					des.append(" ");
				}
			}
		}
		return des.toString();
	}
}
