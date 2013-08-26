package com.tcl.smart.server.dao;

import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import com.tcl.smart.server.model.RecName;
import com.tcl.smart.server.model.TrainerLog;

public interface ITrainerLogInfo {
	
	void putTrainerLogInfo(TrainerLog logInfo);
	List<TrainerLog> getTrainerLogInfoByName(RecName name);
	Object2ObjectOpenHashMap<String, List<TrainerLog>> getAllTrainerLogInfo();
	
}
