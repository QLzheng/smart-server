package com.tcl.smart.server.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * �ٶȰٿ��е�һ�ض���
 * 
 * @author fanjie
 * @date 2013-4-28
 */
public class BaikeSection implements Serializable {
	private static final long serialVersionUID = 2774456840912906794L;

	/** ����������Ϊ�գ��������ֻ��һ������ʱ��ͬʱҲ�����ж༶���� */
	private List<String> heads;

	private List<BaikePara> paras;

	public List<BaikePara> getParas() {
		return paras;
	}

	public void setParas(List<BaikePara> paras) {
		this.paras = paras;
	}

	public List<String> getHeads() {
		return heads;
	}

	public void setHeads(List<String> heads) {
		this.heads = heads;
	}

	public void addHead(String head) {
		if (heads == null)
			heads = new ArrayList<String>();
		heads.add(head);
	}

	public void addPara(BaikePara para) {
		if (paras == null)
			paras = new ArrayList<BaikePara>();
		paras.add(para);
	}

	@Override
	public String toString() {
		if (heads != null)
			return Arrays.toString(heads.toArray()) + ": " + (paras == null ? 0 : paras.size()) + " paras.";
		return (paras == null ? 0 : paras.size()) + " paras.";
	}
}
