package com.tcl.smart.server.service;

/**
 * ��ά���Զ����ɷ���
 * 
 * @author fanjie
 * @date 2013-5-26
 */
public interface ICodeImgService {
	public String generateCodeImgByDefaultSetting(String data);

	public String generateCodeImg(int width, int height, String data, String encoding, String error_level);
}
