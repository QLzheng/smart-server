package com.tcl.smart.server.util;

import java.io.File;

public class PropertiesBean {

	private String recommendIp;

	private String recommendPort;

	private String recommendProj;

	private String recommendBehavior;

	private String recommendRecommend;

	private String recommendReset;

	private String recommendResetall;

	private String recommendPicWebPrefix;

	private String recommendPicFolder1;

	private String recommendPicFolder2;

	private String recommendPicDefault;

	private String imageFolder;

	private String codeImgFolder;

	private String serializeFolder;

	private String recommendQueryItem;

	private String recommendCliveIp;

	private String recommendClivePort;

	private String recommendCliveProj;

	private String recommendCliveRecommend;

	private String recommendCliveTrainOnce;

	private String viewPage;

	private String defaultVideoUrl;

	private String baikeFolder;

	private String hotlinkingImgFolder;

	public void init() {
		try {
			File baikeFolderFile = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator + baikeFolder);
			if (!baikeFolderFile.exists() || !baikeFolderFile.isDirectory())
				baikeFolderFile.mkdirs();
			File hotlinkingImgFolderFile = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator + hotlinkingImgFolder);
			if (!hotlinkingImgFolderFile.exists() || !hotlinkingImgFolderFile.isDirectory())
				hotlinkingImgFolderFile.mkdirs();
			File codeImgFolderFile = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator + codeImgFolder);
			if (!codeImgFolderFile.exists() || !codeImgFolderFile.isDirectory())
				codeImgFolderFile.mkdirs();
			File serializeFolderFile = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator + serializeFolder);
			if (!serializeFolderFile.exists() || !serializeFolderFile.isDirectory())
				serializeFolderFile.mkdirs();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public String getRecommendIp() {
		return recommendIp;
	}

	public void setRecommendIp(String recommendIp) {
		this.recommendIp = recommendIp;
	}

	public String getRecommendPort() {
		return recommendPort;
	}

	public void setRecommendPort(String recommendPort) {
		this.recommendPort = recommendPort;
	}

	public String getRecommendProj() {
		return recommendProj;
	}

	public void setRecommendProj(String recommendProj) {
		this.recommendProj = recommendProj;
	}

	public String getRecommendBehavior() {
		return recommendBehavior;
	}

	public void setRecommendBehavior(String recommendBehavior) {
		this.recommendBehavior = recommendBehavior;
	}

	public String getRecommendRecommend() {
		return recommendRecommend;
	}

	public void setRecommendRecommend(String recommendRecommend) {
		this.recommendRecommend = recommendRecommend;
	}

	public String getRecommendQueryItem() {
		return recommendQueryItem;
	}

	public void setRecommendQueryItem(String recommendQueryItem) {
		this.recommendQueryItem = recommendQueryItem;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	public String getRecommendPicDefault() {
		return recommendPicDefault;
	}

	public void setRecommendPicDefault(String recommendPicDefault) {
		this.recommendPicDefault = recommendPicDefault;
	}

	public String getRecommendPicWebPrefix() {
		return recommendPicWebPrefix;
	}

	public void setRecommendPicWebPrefix(String recommendPicWebPrefix) {
		this.recommendPicWebPrefix = recommendPicWebPrefix;
	}

	public String getRecommendPicFolder1() {
		return recommendPicFolder1;
	}

	public void setRecommendPicFolder1(String recommendPicFolder1) {
		this.recommendPicFolder1 = recommendPicFolder1;
	}

	public String getRecommendPicFolder2() {
		return recommendPicFolder2;
	}

	public void setRecommendPicFolder2(String recommendPicFolder2) {
		this.recommendPicFolder2 = recommendPicFolder2;
	}

	public String getDefaultVideoUrl() {
		return defaultVideoUrl;
	}

	public void setDefaultVideoUrl(String defaultVideoUrl) {
		this.defaultVideoUrl = defaultVideoUrl;
	}

	public String getRecommendReset() {
		return recommendReset;
	}

	public void setRecommendReset(String recommendReset) {
		this.recommendReset = recommendReset;
	}

	public String getRecommendResetall() {
		return recommendResetall;
	}

	public void setRecommendResetall(String recommendResetall) {
		this.recommendResetall = recommendResetall;
	}

	public String getRecommendCliveIp() {
		return recommendCliveIp;
	}

	public void setRecommendCliveIp(String recommendCliveIp) {
		this.recommendCliveIp = recommendCliveIp;
	}

	public String getRecommendClivePort() {
		return recommendClivePort;
	}

	public void setRecommendClivePort(String recommendClivePort) {
		this.recommendClivePort = recommendClivePort;
	}

	public String getRecommendCliveProj() {
		return recommendCliveProj;
	}

	public void setRecommendCliveProj(String recommendCliveProj) {
		this.recommendCliveProj = recommendCliveProj;
	}

	public String getRecommendCliveRecommend() {
		return recommendCliveRecommend;
	}

	public void setRecommendCliveRecommend(String recommendCliveRecommend) {
		this.recommendCliveRecommend = recommendCliveRecommend;
	}

	public String getBaikeFolder() {
		return baikeFolder;
	}

	public void setBaikeFolder(String baikeFolder) {
		this.baikeFolder = baikeFolder;
	}

	public String getRecommendCliveTrainOnce() {
		return recommendCliveTrainOnce;
	}

	public void setRecommendCliveTrainOnce(String recommendCliveTrainOnce) {
		this.recommendCliveTrainOnce = recommendCliveTrainOnce;
	}

	public String getHotlinkingImgFolder() {
		return hotlinkingImgFolder;
	}

	public void setHotlinkingImgFolder(String hotlinkingImgFolder) {
		this.hotlinkingImgFolder = hotlinkingImgFolder;
	}

	public String getImageFolder() {
		return imageFolder;
	}

	public void setImageFolder(String imageFolder) {
		this.imageFolder = imageFolder;
	}

	public String getCodeImgFolder() {
		return codeImgFolder;
	}

	public void setCodeImgFolder(String codeImgFolder) {
		this.codeImgFolder = codeImgFolder;
	}

	public String getSerializeFolder() {
		return serializeFolder;
	}

	public void setSerializeFolder(String serializeFolder) {
		this.serializeFolder = serializeFolder;
	}

}
