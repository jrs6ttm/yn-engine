package com.util;

import java.util.UUID;

public class Seq {
	/**
	 * 
	 * @Title: createSeqStr 
	 * @Description: 生成字符串唯一码
	 * @author 张龙龙
	 * @date 2018年3月3日 下午6:04:34
	 * @param @return     
	 * @return String     
	 * @throws 
	 */
	public static String createSeqStr() {
		return UUID.randomUUID().toString().replaceAll("-","");
	}

	/**
	 * 
	 * @Title: createSeqNum 
	 * @Description: 生成数字唯一码
	 * @author 张龙龙
	 * @date 2018年3月3日 下午6:04:39
	 * @param @return     
	 * @return long     
	 * @throws 
	 */
	public static long createSeqNum(){
		return System.currentTimeMillis();
	}
}
