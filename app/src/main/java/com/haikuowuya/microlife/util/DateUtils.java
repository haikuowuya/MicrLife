package com.haikuowuya.microlife.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils
{
	private static final String TODAY = "今天";
	public static final String[] WEEKS = { "", "星期天", "星期一", "星期二", "星期三",
		"星期四", "星期五", "星期六" };

	/***
	 * 根据年月日来获取对应的星期,注意月为正常的月份
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getWeek(int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		String week = WEEKS[calendar.get(Calendar.DAY_OF_WEEK)];
		calendar.setTimeInMillis(System.currentTimeMillis());
		if (calendar.get(Calendar.YEAR) == year
			&& calendar.get(Calendar.MONTH) == month - 1
			&& calendar.get(Calendar.DAY_OF_MONTH) == day)
		{
			 
			week = TODAY;
		}
		return week;
	} 
	/**根据秒获取更新时间 */
	public static String getUpdateTime(String second)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		return simpleDateFormat.format(new Date(Long.parseLong(second)*1000));
	}
}
