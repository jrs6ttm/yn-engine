package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * function:Get properties file attributes value
 * author：shaojiang
 * date：2011.7.20
 */
public class PropertiesUtil {
	
	private static Properties pros = null;//Properties Object
	
	static{
		pros = new Properties();//Create a Properties object
	}
	
	/**
	 * Loading files to Properties object
	 * @param fileName
	 */
	public static void load(String fileName){	
        InputStream in = PropertiesUtil.class.getResourceAsStream(fileName);
        try {
			pros.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从文件加载属性值
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static String loadValue(String fileName,String key){
		load(fileName);
		return get(key);
	}	

	/**
	 * 获得配置文件属性值
	 * @param key
	 * @return
	 */
	public static String get(String key){
		try {
			if(pros.getProperty(key)!=null){
				//return new String(pros.getProperty(key).getBytes("ISO8859-1"),"UTF-8");
				return pros.getProperty(key);
			}else{
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 从文件加载一个字符串数组
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static String[] loadStrArray(String fileName,String key){
		load(fileName);
		return getStrArray(key);
	}
	
	/**
	 * 将配置文件中","分割的字符串转换成字符串数组
	 * @param key
	 * @return
	 */
	public static String[] getStrArray(String key){
		return get(key).split(",");
	}
}

 
