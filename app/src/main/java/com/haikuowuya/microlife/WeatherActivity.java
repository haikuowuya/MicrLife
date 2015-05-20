package com.haikuowuya.microlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.haikuowuya.microlife.base.BaseActivity;
import com.haikuowuya.microlife.util.OkHttpUtils;
import com.haikuowuya.microlife.util.ParamsUtils;
import com.haikuowuya.microlife.util.ToastUtils;
import com.haikuowuya.microlife.util.WeatherUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class WeatherActivity extends BaseActivity
{

    public static void actionWeather(Activity activity)
    {
        Intent intent = new Intent(activity, WeatherActivity.class);
        activity.startActivity(intent);
    }

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String city = mPreferences.getString(Constants.PREF_LOCATION_CITY, Constants.DEFAULT_CITY);
        if (city.contains(Constants.SHI) && city.substring(city.length() - 1).equals(Constants.SHI))
        {
            city = city.substring(0, city.length() - 1);
        }
        setTitleText(city + "天气");
        initView();
    }

    private void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Request request = new Request.Builder().url(WeatherUtils.getWeatherUrl("101010100")).build();

        OkHttpUtils.asyncExecute(request, this);

    }

    @Override
    public void onFailure(Request request, IOException e)
    {
        super.onFailure(request, e);
    }

    @Override
    public void onResponse(Response response) throws IOException
    {
        super.onResponse(response);
        ToastUtils.showCrouton(mActivity, response.body().string(), getContentViewGroup());
        System.out.println("params.toString = " + ParamsUtils.paramsToString(mActivity ));
    }
}
