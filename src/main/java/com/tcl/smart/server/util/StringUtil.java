package com.tcl.smart.server.util;


/**
 * Some String methods
 * @author Jerryangly
 *
 */
public class StringUtil {
	/**
	 * count the number of String A in String B
	 * @param strA String A 
	 * @param strB String B
	 * @return int x = the number of A in B
	 */
	public static int countStrAInStrB(String strA,String strB){
		if (strA==null||strB==null||strA.length()==0) return 0;
		int x = strB.length()-strB.replaceAll(strA, "").length();
		x=x/strA.length();
		return x;
	}
}
