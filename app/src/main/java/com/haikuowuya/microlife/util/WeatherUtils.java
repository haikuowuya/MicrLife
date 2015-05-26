package com.haikuowuya.microlife.util;

import android.text.TextUtils;

import com.haikuowuya.microlife.URLConstants;
import com.haikuowuya.microlife.mvp.model.Weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class WeatherUtils
{
    private static final String TAG_AREA = "area";
    private static final String TAG_LIFE = "life";
    private static final String TAG_INFO = "info";
    private static final String TAG_DAY = "day";
    private static final String TAG_NIGHT = "night";
    private static final String TAG_DAWN = "dawn";
    private static final String TAG_WEATHER = "weather";
    private static final String TAG_KONG_TIAO = "kongtiao";
    private static final String TAG_YUN_DONG = "yundong";
    private static final String TAG_ZI_WAI_XIAN = "ziwaixian";
    private static final String TAG_GAN_MAO = "ganmao";
    private static final String TAG_XI_CHE = "xiche";
    private static final String TAG_WU_RAN = "wuran";
    private static final String TAG_CHUAN_YI = "chuanyi";
    private static final String TAG_TIME = "time";
    private static final String TAG_WIND = "wind";
    private static final String TAG_DATA_UPTIME = "dataUptime";
    private static final String TAG_FEELS_LIKE_C = "feelslike_c";
    private static final String TAG_POWER = "power";
    private static final String TAG_DIRECT = "direct";
    private static final String TAG_WIND_SPEED = "windspeed";
    private static final String TAG_PRESSURE = "pressure";
    private static final String TAG_HUMIDITY = "humidity";
    private static final String TAG_DATE = "date";
    private static final String TAG_IMG = "img";
    private static final String TAG_HOUR = "hour";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_HOURLY_FORECAST = "hourly_forecast";
    private static final String TAG_REAL_TIME = "realtime";
    private static final String TAG_PM25 ="pm25" ;
    private static final String TAG_SO2="so2";
    private static final String TAG_O3="o3";
    private static final String TAG_CO="co";
    private static final String TAG_LEVEL="level";
    private static final String TAG_COLOR="color";
    private static final String TAG_NO2="no2";
    private static final String TAG_AQI="aqi";
    private static final String TAG_QUALITY="quality";
    private static final String TAG_PM10="pm10";

    private static final String TAG_ADVICE="advice";
    private static final String TAG_CHIEF="chief";
    private static final String TAG_UPDATE_TIME="upDateTime";

    public static String getWeatherUrl(String weatherId )
    {
        return URLConstants.BASE_QIHU_URL+weatherId+"?pkg=net.qihoo.launcher.widget.clockweather&cver=42&ver=1&token=UNnm1X3X%2BkQhMlfpQD7Fog%3D%3D&t=1421635358396";
    }


    public static Weather parseWeatherJson(String json)
    {
        Weather item = null;
        if (!TextUtils.isEmpty(json))
        {
            // System.out.println("json = " + json);
            item = new Weather();
            try
            {
                String province = null;
                String city = null;
                String county = null;
                String cityCode = null;
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.optJSONArray(TAG_AREA);
                if (null != jsonArray)
                {
                    province = jsonArray.optJSONArray(0).optString(0);
                    city = jsonArray.optJSONArray(1).optString(0);
                    county = jsonArray.optJSONArray(2).optString(0);
                    cityCode = jsonArray.optJSONArray(2).optString(1);
                    item.province = province;
                    item.city = city;
                    item.county = county;
                    item.cityCode = cityCode;
                }
                // 解析生活指数
                JSONObject lifeJsonObject = jsonObject.optJSONObject(TAG_LIFE);
                if (null != lifeJsonObject)
                {
                    Weather.LifeItem lifeItem = new Weather.LifeItem();
                    String chuanYiDesc, chuanYiExp, wuRanDesc, wuRanExp, kongTiaoExp, kongTiaoDesc, xiCheExp, xiCheDesc, yunDongExp, yunDongDesc, ganMaoExp, ganMaoDesc, ziWaiXianExp, ziWaiXianDesc;
                    kongTiaoDesc = kongTiaoExp = ganMaoDesc = ganMaoExp = ziWaiXianDesc = ziWaiXianExp = null;
                    yunDongDesc = yunDongExp = xiCheDesc = xiCheExp = null;
                    wuRanDesc = wuRanExp = chuanYiDesc = chuanYiExp = null;
                    lifeJsonObject = lifeJsonObject.getJSONObject(TAG_INFO);
                    // 空调指数
                    JSONArray lifeJsonArray = lifeJsonObject
                            .optJSONArray(TAG_KONG_TIAO);
                    kongTiaoExp = "空调指数:"+ lifeJsonArray.getString(0);
                    kongTiaoDesc = lifeJsonArray.getString(1);
                    // 运动指数
                    lifeJsonArray = lifeJsonObject.optJSONArray(TAG_YUN_DONG);
                    yunDongExp ="运动指数:"+ lifeJsonArray.getString(0);
                    yunDongDesc = lifeJsonArray.getString(1);
                    // 紫外线指数
                    lifeJsonArray = lifeJsonObject
                            .optJSONArray(TAG_ZI_WAI_XIAN);
                    ziWaiXianExp = "紫外线指数:"+lifeJsonArray.getString(0);
                    ziWaiXianDesc = lifeJsonArray.getString(1);
                    // 感冒指数
                    lifeJsonArray = lifeJsonObject.optJSONArray(TAG_GAN_MAO);
                    ganMaoExp = "感冒指数:"+lifeJsonArray.getString(0);
                    ganMaoDesc = lifeJsonArray.getString(1);
                    // 洗车指数
                    lifeJsonArray = lifeJsonObject.optJSONArray(TAG_XI_CHE);
                    xiCheExp = "洗车指数:"+lifeJsonArray.getString(0);
                    xiCheDesc = lifeJsonArray.getString(1);
                    // 污染指数
                    lifeJsonArray = lifeJsonObject.optJSONArray(TAG_WU_RAN);
                    wuRanExp = "污染指数:"+lifeJsonArray.getString(0);
                    wuRanDesc = lifeJsonArray.getString(1);
                    // 穿衣指数
                    lifeJsonArray = lifeJsonObject.optJSONArray(TAG_CHUAN_YI);
                    chuanYiExp = "穿衣指数:"+lifeJsonArray.getString(0);
                    chuanYiDesc = lifeJsonArray.getString(1);
                    lifeItem.kongTiaoDesc = kongTiaoDesc;
                    lifeItem.kongTiaoExp = kongTiaoExp;
                    lifeItem.yunDongDesc = yunDongDesc;
                    lifeItem.yunDongExp = yunDongExp;
                    lifeItem.ziWaiXianDesc = ziWaiXianDesc;
                    lifeItem.ziWaiXianExp = ziWaiXianExp;
                    lifeItem.ganMaoDesc = ganMaoDesc;
                    lifeItem.ganMaoExp = ganMaoExp;
                    lifeItem.xiCheDesc = xiCheDesc;
                    lifeItem.xiCheExp = xiCheExp;
                    lifeItem.wuRanDesc = wuRanDesc;
                    lifeItem.wuRanExp = wuRanExp;
                    lifeItem.chuanyiDesc = chuanYiDesc;
                    lifeItem.chuanYiExp = chuanYiExp;
                    item.lifeItem = lifeItem;
                }

                // 解析生活指数
                // 解析天气信息
                JSONArray weatherJsonArray = jsonObject
                        .optJSONArray(TAG_WEATHER);
                if (null != weatherJsonArray && weatherJsonArray.length() > 0)
                {
                    LinkedList<Weather.WeatherItem> weatherItems = new LinkedList<Weather.WeatherItem>();
                    for (int i = 0; i < weatherJsonArray.length(); i++)
                    {
                        Weather.WeatherItem weatherItem = new Weather.WeatherItem();
                        JSONObject weatherJsonObject = weatherJsonArray
                                .getJSONObject(i);
                        String date = weatherJsonObject.optString(TAG_DATE);
                        weatherItem.date = date;
                        if (!TextUtils.isEmpty(date) && date.contains("-")
                                && date.split("-").length == 3)
                        {
                            int year = Integer.parseInt(date.split("-")[0]);
                            //月份不要做处理
                            int month = Integer.parseInt(date.split("-")[1]);
                            int day = Integer.parseInt(date.split("-")[2]);
                            String week = DateUtils.getWeek(year, month, day);
                            weatherItem.week = week;
                        }
                        // System.out.println("date = "+ date);
                        weatherJsonObject = weatherJsonObject
                                .optJSONObject(TAG_INFO);
                        JSONArray dayJsonArray = weatherJsonObject
                                .optJSONArray(TAG_DAY);
                        JSONArray nightJsonArray = weatherJsonObject
                                .optJSONArray(TAG_NIGHT);
                        JSONArray dawnJsonArray = weatherJsonObject
                                .optJSONArray(TAG_DAWN);
                        if (null != dayJsonArray && dayJsonArray.length() == 6)
                        {
                            Weather.WeatherItem.Item dayItem = new Weather.WeatherItem.Item();
                            dayItem.img = dayJsonArray.optString(0);
                            dayItem.info = dayJsonArray.optString(1);
                            dayItem.temperature = dayJsonArray.optString(2);
                            dayItem.direct = dayJsonArray.optString(3);
                            dayItem.power = dayJsonArray.optString(4);
                            dayItem.time = dayJsonArray.optString(5);

                            weatherItem.dayItem = dayItem;
                        }
                        if (null != nightJsonArray
                                && nightJsonArray.length() == 6)
                        {
                            Weather.WeatherItem.Item nightItem = new Weather.WeatherItem.Item();
                            nightItem.img = nightJsonArray.optString(0);
                            nightItem.info = nightJsonArray.optString(1);
                            nightItem.temperature = nightJsonArray.optString(2);
                            nightItem.direct = nightJsonArray.optString(3);
                            nightItem.power = nightJsonArray.optString(4);
                            nightItem.time = nightJsonArray.optString(5);

                            weatherItem.nightItem = nightItem;
                        }
                        if (null != dawnJsonArray
                                && dawnJsonArray.length() == 6)
                        {
                            Weather.WeatherItem.Item dawnItem = new Weather.WeatherItem.Item();
                            dawnItem.img = dawnJsonArray.optString(0);
                            dawnItem.info = dawnJsonArray.optString(1);
                            dawnItem.temperature = dawnJsonArray.optString(2);
                            dawnItem.direct = dawnJsonArray.optString(3);
                            dawnItem.power = dawnJsonArray.optString(4);
                            dawnItem.time = dawnJsonArray.optString(5);

                            weatherItem.dawnItem = dawnItem;
                        }

                        weatherItems.add(weatherItem);
                    }
                    item.weatherItems = weatherItems;
                }
                // 解析天气信息

                // 解析每一个小时信息
                JSONArray hourJsonArray = jsonObject
                        .optJSONArray(TAG_HOURLY_FORECAST);
                if (null != hourJsonArray && hourJsonArray.length() > 0)
                {
                    LinkedList<Weather.HourItem> hourItems = new LinkedList<Weather.HourItem>();
                    for (int i = 0; i < hourJsonArray.length(); i++)
                    {
                        Weather.HourItem hourItem = new Weather.HourItem();
                        JSONObject hourJsonObject = hourJsonArray
                                .getJSONObject(i);
                        String img, temperature, hour, info;
                        boolean isNight = false;
                        img = temperature = hour = info = null;
                        img = hourJsonObject.optString(TAG_IMG);
                        temperature = hourJsonObject.optString(TAG_TEMPERATURE);
                        hour = hourJsonObject.optString(TAG_HOUR) ;
                        info = hourJsonObject.optString(TAG_INFO);
                        isNight = (TextUtils.isDigitsOnly(hour) && (Integer.parseInt(hour)>18|| Integer.parseInt(hour)< 7));
                        hour +=":00";

                        hourItem.isNight = isNight;
                        hourItem.hour = hour;
                        hourItem.img = img;
                        hourItem.temperature = temperature;
                        hourItem.info = info;
                        hourItems.add(hourItem);
                    }
                    item.hourItems = hourItems;
                }
                //解析PM信息
                JSONObject pmJsonObject = jsonObject.optJSONObject(TAG_PM25);
                if(null != pmJsonObject)
                {
                    Weather.Pm25 pm25Item = new Weather.Pm25();
                      int so2;
                      int o3;
                      String co;
                      int level;
                      String color;
                      int no2;
                      int aqi;
                      String quality;
                      int pm10;
                      int pm25;
                      String advice;
                      String chief;
                      String upDateTime;
                    so2 = pmJsonObject.optInt(TAG_SO2);
                    o3 = pmJsonObject.optInt(TAG_O3);
                    co = pmJsonObject.optString(TAG_CO);
                    level = pmJsonObject.optInt(TAG_LEVEL);
                    color = pmJsonObject.optString(TAG_COLOR);
                    no2 = pmJsonObject.optInt(TAG_NO2);
                    aqi = pmJsonObject.optInt(TAG_AQI);
                    quality = pmJsonObject.optString(TAG_QUALITY);
                    pm10 = pmJsonObject.optInt(TAG_PM10);
                    pm25 = pmJsonObject.optInt(TAG_PM25);
                    advice = pmJsonObject.optString(TAG_ADVICE);
                    chief = pmJsonObject.optString(TAG_CHIEF);
                    upDateTime = pmJsonObject.optString(TAG_UPDATE_TIME);
                    pm25Item.so2 = so2;
                    pm25Item.o3 = o3;
                    pm25Item.level = level;
                    pm25Item.co = co;
                    pm25Item.color = color;
                    pm25Item.no2 = no2;
                    pm25Item.aqi = aqi;
                    pm25Item.quality = quality;
                    pm25Item.pm10 = pm10;
                    pm25Item.pm25 = pm25;
                    pm25Item.advice = advice;
                    pm25Item.chief = chief;
                    pm25Item.upDateTime = upDateTime;
                   item.pm25 = pm25Item;
                }
                // 解析每一个小时信息
                // 解析实时信息
                JSONObject realtimeJsonObject = jsonObject
                        .optJSONObject(TAG_REAL_TIME);
                if (null != realtimeJsonObject)
                {
                    Weather.RealtimeItem realtimeItem = new Weather.RealtimeItem();
                    String windspeed, direct, power, time, pressure, humidity, img, info, temperature, feelslike_c, dataUptime, date;
                    windspeed = direct = power = time = pressure = humidity = img = null;
                    info = temperature = feelslike_c = dataUptime = date = null;
                    time = realtimeJsonObject.optString(TAG_TIME);
                    dataUptime = realtimeJsonObject.optString(TAG_DATA_UPTIME);
                    pressure = realtimeJsonObject.optString(TAG_PRESSURE);
                    feelslike_c = realtimeJsonObject
                            .optString(TAG_FEELS_LIKE_C);
                    date = realtimeJsonObject.optString(TAG_DATE);
                    JSONObject windJsonObject = realtimeJsonObject
                            .optJSONObject(TAG_WIND);
                    if (null != windJsonObject)
                    {
                        windspeed = windJsonObject.optString(TAG_WIND_SPEED);
                        direct = windJsonObject.optString(TAG_DIRECT);
                        power = windJsonObject.optString(TAG_POWER);
                    }
                    JSONObject weatherJsonObject = realtimeJsonObject
                            .optJSONObject(TAG_WEATHER);
                    if (null != weatherJsonObject)
                    {
                        humidity = weatherJsonObject.optString(TAG_HUMIDITY);
                        img = weatherJsonObject.optString(TAG_IMG);
                        info = weatherJsonObject.optString(TAG_INFO);
                        temperature = weatherJsonObject
                                .optString(TAG_TEMPERATURE);
                    }
                    realtimeItem.dataUptime = dataUptime;
                    realtimeItem.date = date;
                    realtimeItem.direct = direct;
                    realtimeItem.feelslike_c = feelslike_c;
                    realtimeItem.humidity = humidity;
                    realtimeItem.img = img;
                    realtimeItem.info = info;
                    realtimeItem.power = power;
                    realtimeItem.pressure = pressure;
                    realtimeItem.temperature = temperature;
                    realtimeItem.time = time;
                    realtimeItem.windspeed = windspeed;
                    item.realtimeItem = realtimeItem;
                }
                // 解析实时信息
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return item;
    }

    /**
     * 天气是否已经过期
     * @return true 过期  ; false 没有过期
     */
    public static  boolean isWeatherExpired(long lastUpdateTime)
    {
        boolean flag = true;
        long currentTime = System.currentTimeMillis();
        flag =  Math.abs(currentTime - lastUpdateTime) >8*60*1000;  // 8分钟一更新
        return flag;
    }
    public static boolean isCurrentCityWeather(String cacheWeatherId, String currentCityWeatherId)
    {
        return  cacheWeatherId.equals(currentCityWeatherId);
    }
}
