package com.tcl.smart.server.dao;

import java.util.List;

import com.tcl.smart.server.bean.YihaodianProductBean;

public interface IYihaodianProductDao {
	public List<YihaodianProductBean> findAllYihaodianProducts();

	public YihaodianProductBean findYihaodianProductById(String id);

	public void removeYihaodianProductById(String id);

	public List<YihaodianProductBean> findYihaodianProductByIds(List<String> ids);

	public List<YihaodianProductBean> findYihaodianProductByTitle(String title);

	public List<YihaodianProductBean> findYihaodianProductByFeature(String feature);

	public List<YihaodianProductBean> findYihaodianProductByFeature(String feature, int size);

	/* only return the id of product */
	public List<String> findYihaodianProductIdsByFeature(String feature);

	public void insertYihaodianProductBean(YihaodianProductBean baike);

	// get Product_desc from xml and update to database
	public void updateProduct_desc(String id, String desc);

	public List<YihaodianProductBean> getDefaultRecProducts(int number);

	/* clear database,drop and rebuild the index */
	public void resetDB();
}
