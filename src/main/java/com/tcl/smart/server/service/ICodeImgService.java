package com.tcl.smart.server.service;

/**
 * 二维码自动生成服务
 * 
 * @author fanjie
 * @date 2013-5-26
 */
public interface ICodeImgService {
	public String generateCodeImgByDefaultSetting(String data);

	public String generateCodeImg(int width, int height, String data, String encoding, String error_level);
}
