package com.tcl.smart.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fanjie
 * @date 2012-11-20
 */
public class FileOperation {

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static StringBuffer readFileByLines(String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			reader.close();
			return sb;
		} catch (IOException e) {
			e.printStackTrace();
			return sb;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static List<String> readFileByLinesForArray(String fileName) {
		List<String> ss = new ArrayList<String>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				ss.add(tempString);
			}
			reader.close();
			return ss;
		} catch (IOException e) {
			e.printStackTrace();
			return ss;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void appendStrInFile(String fileName, String content) {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8");
			writer.write(content + "\r\n");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}