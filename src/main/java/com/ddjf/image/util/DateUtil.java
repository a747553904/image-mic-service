package com.ddjf.image.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	
	/**
	 * 格式化时间
	 * @param time
	 * @param formate
	 * @return
	 */
	public static Date parse(String date, String formate){
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
	
	public static String formate(String formate){
		return formate(new Date(), formate);
	}
	
	public static String formate(Date date){
		return formate(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String formate(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 获取失效时间
	 * @param minutes 间隔分钟
	 * @return
	 */
	public static Date getExpireTime(int minutes){

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE,minutes);

		return  calendar.getTime();

	}
}
