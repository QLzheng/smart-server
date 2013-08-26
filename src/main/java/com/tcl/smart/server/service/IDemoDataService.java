package com.tcl.smart.server.service;

/**
 * 
 * @author fanjie
 * @date 2013-5-30
 */
public interface IDemoDataService {
	public String currentProgramTitle(long timestamp);

	public String currentEpgHTML(long timestamp);

	public String currentEpgInformationHTML(long timestamp);

	public String getDemoVodUrl(String itemId);

	public String getDemoVodInformationHTML(String itemId);

	public String generateDemoBaikesHTML(long timestamp);

	public String generateDemoNewsHTML(long timestamp);

	public String generateDemoProductsHTML(long timestamp);

	public String generateDemoVodsHTML(long timestamp);
}
