package com.tcl.smart.server;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tcl.smart.server.bean.EpgModelDistinctId;
import com.tcl.smart.server.dao.IEpgModelDao;

public class StringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String s = "����糡���ɻ���ӥ22";
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring-config.xml");
		IEpgModelDao epgDao = (IEpgModelDao) context.getBean("epgModelDao");
		List<EpgModelDistinctId> ids = epgDao.groupByNameAndWikiId();
		for (EpgModelDistinctId id : ids) {
			String name = id.getName();
			if (name.contains("��")) {
				if (name.contains("�糡��")) {
					name = name.substring(name.indexOf("��") + 1, name.length());
				} else if (name.contains("�����磺")) {
					name = name.substring(name.indexOf("��") + 1, name.length());
				} else if (name.contains("ӰԺ��")) {
					name = name.substring(name.indexOf("��") + 1, name.length());
				} else if (name.contains("��Ӱ��")) {
					name = name.substring(name.indexOf("��") + 1, name.length());
				} else if (name.contains("����")) {
					name = name.substring(name.indexOf("��") + 1, name.length());
				} else if (name.contains("����")) {
					name = name.substring(name.indexOf("��") + 1, name.length());
				}
			}
		}
	}
}
