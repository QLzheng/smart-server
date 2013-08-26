package com.tcl.smart.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化与反序列化工具
 * 
 * @author fanjie
 * @date 2013-5-30
 */
public class Serializer {
	public static void writeObject(Object o, File f) throws Exception {
		if (f.exists()) {
			f.delete();
		}
		FileOutputStream os = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(o);
		oos.close();
		os.close();
	}

	public static Object readObject(File file) throws Exception {
		InputStream is = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(is);
		Object obj = ois.readObject();
		ois.close();
		return obj;
	}
}
