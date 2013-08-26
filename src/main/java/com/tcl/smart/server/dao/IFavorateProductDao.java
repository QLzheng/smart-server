package com.tcl.smart.server.dao;

import com.tcl.smart.server.bean.FavorateProduct;

/**
 * @author fanjie
 * @date 2013-5-20
 */
public interface IFavorateProductDao {
	public FavorateProduct findFavorateProductsByDefaultUser();

	public void insertFavorateProductByDefaultUser(String productId);

	public void removeFavorateProductByDefaultUser(String productId);

	public long countFavorateProductsByDefaultUser();

	public long countFavorateProductsByUser(String user);

	public FavorateProduct findFavorateProductsByUser(String user);

	public void insertFavorateProduct(String user, String productId);

	public void removeFavorateProductByUser(String user, String productId);
}
