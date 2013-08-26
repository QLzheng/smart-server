package com.tcl.smart.server.service;

import com.tcl.smart.server.bean.YihaodianIndex;

public interface IYihaodianApiService {
	public static enum STATE{RUN,STOP};
	public YihaodianIndex getYihaodianIndex();
	public void getAndStroeProducts();
	public void changeState(STATE state);
}
