package com.haikuowuya.microlife.mvp.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Weather implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String province;
	public String city;
	public String county;
	public String cityCode;
	public LifeItem lifeItem;
	public LinkedList<WeatherItem> weatherItems;
	public RealtimeItem realtimeItem;
	public LinkedList<HourItem> hourItems;
	public long weatherUpdateTime = 0;

	@Override
	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("省:" + province);
		stringBuffer.append(" 市:" + city);
		stringBuffer.append(" 地区:" + county);
		stringBuffer.append(" 城市代码:" + cityCode);
		if (null != lifeItem)
		{
			stringBuffer.append(lifeItem.toString());
		}
		if (null != weatherItems)
		{
			for (WeatherItem item : weatherItems)
			{
				stringBuffer.append(item.toString());
			}
		}
		if (null != hourItems)
		{
			for (HourItem item : hourItems)
			{
				stringBuffer.append(item.toString());
			}
		}
		if (null != realtimeItem)
		{
			stringBuffer.append(realtimeItem.toString());
		}
		return stringBuffer.toString();
	}

	public static class RealtimeItem
	{
		public String windspeed;
		public String direct;
		public String power;
		public String time;
		public String pressure;
		public String humidity;
		public String img;
		public String info;
		public String temperature;
		public String feelslike_c;
		public String dataUptime;
		public String date;

		@Override
		public String toString()
		{
			return "\n风速:" + windspeed + " 风向:" + direct + " 风力:" + power
				+ " 时间:" + time + " 湿度:" + humidity + " 气压:" + pressure
				+ " 天气:" + info + " 温度:" + temperature + " 日期:" + date;
		}
	}

	public static class HourItem
	{
		public String img;
		public String hour;
		public String temperature;
		public String info;
		public boolean  isNight = false;

		@Override
		public String toString()
		{
			return "\nimg:" + img + " hour:" + hour + " temperature:"
				+ temperature + " info:" + info;
		}
	}

	public static class WeatherItem
	{
		public String date;
		public String week;
		public Item dayItem;
		public Item nightItem;
		public Item dawnItem;

		@Override
		public String toString()
		{
			 return "\n日期:"+ date  +" 星期:"+ week
				 +"\n黎明信息:"+(null ==dawnItem?"":dawnItem.toString())
				 +"\n白天信息:"+ (null ==dayItem?"":dayItem.toString())
				 +"\n夜晚信息:"+ (null ==nightItem?"":nightItem.toString());
		}
		public static class Item
		{
			public String img;
			public String info;
			public String temperature;
			public String direct;
			public String power;
			public String time;
			@Override
			public String toString()
			{
				 return " img:"+ img + " info:"+ info + " temperature:"+ temperature
					 +" direct:"+ direct + " power:"+ power + " time:"+ time;
			}
		}
	}

	public static class LifeItem
	{
		/**空调指数*/
		public String kongTiaoExp;
		public String kongTiaoDesc;

		public String yunDongExp;
		public String yunDongDesc;

		public String ziWaiXianExp;
		public String ziWaiXianDesc;
		public String ganMaoExp;
		public String ganMaoDesc;

		public String xiCheExp;
		public String xiCheDesc;

		public String wuRanExp;
		public String wuRanDesc;
		public String chuanYiExp;
		public String chuanyiDesc;

		@Override
		public String toString()
		{
			return "\n空调指数:" + kongTiaoExp + " 描述:" + kongTiaoDesc + "\n穿衣指数:"
				+ chuanYiExp + " 描述:" + chuanyiDesc + "\n污染指数:" + wuRanExp
				+ " 描述:" + wuRanDesc + "\n洗车指数:" + xiCheExp + " 描述:"
				+ xiCheDesc + "\n运动指数:" + yunDongExp + " 描述:" + yunDongDesc
				+ "\n紫外线指数:" + ziWaiXianExp + " 描述:" + ziWaiXianDesc;
		}
	}
}
