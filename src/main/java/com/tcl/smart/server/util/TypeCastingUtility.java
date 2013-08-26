package com.tcl.smart.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TypeCastingUtility {
	
	private static Logger logger = LoggerFactory.getLogger(TypeCastingUtility.class);

	public static boolean getBoolean(Object obj){
		if(obj instanceof Boolean){
			return (Boolean) obj;
		}
		throw new NumberFormatException();				
	}
	
	public static double getDouble(Object obj){
		if(obj instanceof String){
			return Double.parseDouble((String)obj);
		}
		else if(obj instanceof Number){
			return ((Number)obj).doubleValue();
		}
		throw new NumberFormatException();				
	}

	public static int getInt(Object obj){
		if(obj instanceof String){
			return Integer.parseInt((String)obj);
		}
		else if(obj instanceof Number){
			return ((Number)obj).intValue();
		}
		throw new NumberFormatException();
	}

	public static long getLong(Object obj){
		if(obj instanceof String){
			return Long.parseLong((String)obj);
		}
		else if(obj instanceof Number){
			return ((Number)obj).longValue();
		}
		throw new NumberFormatException();				
	}

	public static short getShort(Object obj){
		if(obj instanceof String){
			return Short.parseShort((String)obj);
		}
		else if(obj instanceof Number){
			return ((Number)obj).shortValue();
		}
		throw new NumberFormatException();					
	}

	public static float getFloat(Object obj){
		if(obj instanceof String){
			return Float.parseFloat((String)obj);
		}
		else if(obj instanceof Number){
			return ((Number)obj).floatValue();
		}
		throw new NumberFormatException();					
	}

	public static byte getByte(Object obj){
		if(obj instanceof String){
			return Byte.parseByte((String)obj);
		}
		else if(obj instanceof Number){
			return ((Number)obj).byteValue();
		}
		throw new NumberFormatException();					
	}

	public static String getString(Object obj){
		return (String) obj;
	}

	/**
	 * @param dateString representing a date in the format "YYYY-[M]M-[D]D" or "YYYY-[M]M-[D]D HH:mm:ss". 
	 *          The leading zero for [M]M and [D]D cann't be omitted but HH and mm and ss my be omitted.
	 * @return
	 *        
	 */
	public static Date stringToDate(String dateString){
		if(dateString.length()==10){
			try{ 
				SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd"); 
				Date date = sdf.parse(dateString); 
				return date;
			}catch (ParseException e){ 
				logger.error("Can't parse " + dateString + " as date."); 
			}
		}else if(dateString.length() == 4){
			try{ 
				SimpleDateFormat sdf = new SimpleDateFormat( "yyyy"); 
				Date date = sdf.parse(dateString); 
				return date;
			}catch (ParseException   e){ 
				logger.error("Can't parse " + dateString + " as date."); 
			}
		}else 
		{
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				Date date = sdf.parse(dateString); 
				return date;
			}
			catch (ParseException e)
			{
				logger.error("Can't parse " + dateString + " as date."); 
			}
		}

		return null; 
	}	
}
