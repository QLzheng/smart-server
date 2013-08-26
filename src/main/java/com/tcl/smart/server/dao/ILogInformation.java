package com.tcl.smart.server.dao;

import java.util.Date;
import java.util.List;

import com.tcl.smart.server.model.LogModel;

public interface ILogInformation {
	
	void putLogInfo(LogModel log);
	List<LogModel> getLogByParam(Date time, String type, String operation, String level);

}
