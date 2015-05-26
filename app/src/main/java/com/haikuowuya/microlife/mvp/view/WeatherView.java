package com.haikuowuya.microlife.mvp.view;

import com.haikuowuya.microlife.mvp.model.Weather;

/**
 * Created by raiyi-suzhou on 2015/5/21 0021.
 */
public interface WeatherView  extends  BaseView
{

    public void onWeatherFinished(Weather weather);
}
