package com.tcl.smart.server.update;

public interface IUpdate {
	
	void update(); 
	
	/** Update epg recommendation by epgId */
	void updateByEpgId(String id);
	
}
