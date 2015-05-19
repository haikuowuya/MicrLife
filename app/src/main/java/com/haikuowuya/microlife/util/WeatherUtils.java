package com.haikuowuya.microlife.util;

import com.haikuowuya.microlife.URLConstants;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class WeatherUtils
{
    public static String getWeatherUrl(String weatherId )
    {
        return URLConstants.BASE_QIHU_URL+weatherId+"?pkg=net.qihoo.launcher.widget.clockweather&cver=42&ver=1&token=UNnm1X3X%2BkQhMlfpQD7Fog%3D%3D&t=1421635358396";
    }
}
