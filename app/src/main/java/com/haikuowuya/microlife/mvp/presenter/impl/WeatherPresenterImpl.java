package com.haikuowuya.microlife.mvp.presenter.impl;

import android.webkit.URLUtil;

import com.haikuowuya.microlife.WeatherActivity;
import com.haikuowuya.microlife.mvp.model.Weather;
import com.haikuowuya.microlife.mvp.presenter.WeatherPresenter;
import com.haikuowuya.microlife.mvp.view.WeatherView;
import com.haikuowuya.microlife.util.OkHttpUtils;
import com.haikuowuya.microlife.util.WeatherUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by raiyi-suzhou on 2015/5/21 0021.
 */
public class WeatherPresenterImpl implements WeatherPresenter
{
    private WeatherView mWeatherView;
    private Request mRequest;
    public WeatherPresenterImpl(WeatherView weatherView ,String weatherId)
    {
        this.mWeatherView = weatherView;
        mRequest = new Request.Builder().url(WeatherUtils.getWeatherUrl(weatherId)).build();
    }
    @Override
    public void doGetWeather(    )
    {
        mWeatherView.showProgressDialog();
        doGetWeatherJsonUrl();
    }

     private void doGetWeatherJsonUrl()
    {
        OkHttpUtils.asyncExecute(mRequest, new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {
                ((Callback) mWeatherView).onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                String jsonUrl = response.body().string();
                try
                {
                    Thread.sleep(1600L);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                executeWeather(jsonUrl);
            }
        });
    }

    private void  executeWeather(String jsonUrl )
    {
        if(URLUtil.isValidUrl(jsonUrl))
        {
            Request request = new Request.Builder().url(jsonUrl).build();
            OkHttpUtils.asyncExecute(request, (Callback) mWeatherView);
        }
    }
}
