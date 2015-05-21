package com.haikuowuya.microlife.mvp.presenter.impl;

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
    public WeatherPresenterImpl(WeatherView weatherView)
    {
        this.mWeatherView = weatherView;
          mRequest = new Request.Builder().url(WeatherUtils.getWeatherUrl("101010100")).build();
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
        Request request = new Request.Builder().url(jsonUrl).build();
        OkHttpUtils.asyncExecute(request, (Callback) mWeatherView) ;
    }
}
