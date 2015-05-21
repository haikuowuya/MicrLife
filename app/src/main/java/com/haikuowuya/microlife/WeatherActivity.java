package com.haikuowuya.microlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.haikuowuya.microlife.adapter.weather.WeatherPagerAdapter;
import com.haikuowuya.microlife.base.BaseActivity;
import com.haikuowuya.microlife.mvp.model.Weather;
import com.haikuowuya.microlife.mvp.presenter.WeatherPresenter;
import com.haikuowuya.microlife.mvp.presenter.impl.WeatherPresenterImpl;
import com.haikuowuya.microlife.mvp.view.WeatherView;
import com.haikuowuya.microlife.util.ParamsUtils;
import com.haikuowuya.microlife.util.WeatherUtils;
import com.haikuowuya.microlife.view.TabViewPagerIndicator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class WeatherActivity extends BaseActivity implements WeatherView
{
    public static void actionWeather(Activity activity)
    {
        Intent intent = new Intent(activity, WeatherActivity.class);
        activity.startActivity(intent);
    }

    private WeatherPresenter mWeatherPresenter;
    private RecyclerView mRecyclerView;

    private TabViewPagerIndicator mTabViewPagerIndicator ;
    private Weather mWeather;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);    //TODO
        initView();
        setMenuResId(R.mipmap.ic_refresh);
        setListener();
        mWeatherPresenter = new WeatherPresenterImpl(this);
        initWeather();
    }
    private void initWeather()
    {
        mWeather = WeatherUtils.parseWeatherJson(mPreferences.getString(Constants.PREF_WEATHER_JSON,""));
        if(null != mWeather)
        {
            onWeatherFinished(mWeather);
        }
        else
        {
            mWeatherPresenter.doGetWeather();
        }
    }
    private void setListener()
    {
        onMenuClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mWeatherPresenter.doGetWeather();
            }
        });
    }

    private void initView()
    {

        mTabViewPagerIndicator = (TabViewPagerIndicator) findViewById(R.id.vpi_viewpager);
        mTabViewPagerIndicator.getIndicator().setVisibility(View.GONE);
        mTabViewPagerIndicator.getTabStrip().setUnderlineColor(0xFFFFFFFF);
        mTabViewPagerIndicator.getTabStrip().setSelectedTextColor(0xFFFFFFFF);
    }

    @Override
    public void onFailure(Request request, IOException e)
    {
        super.onFailure(request, e);
        hideProgressDialog();
    }

    @Override
    public void onResponse(Response response) throws IOException
    {
        super.onResponse(response);
        hideProgressDialog();
        if(response.isSuccessful())
        {
            String json = response.body().string();
            if(!TextUtils.isEmpty(json))
            {
                Weather weather = WeatherUtils.parseWeatherJson(json);
                if(null != weather)
                {
                    mPreferences.edit().putString(Constants.PREF_WEATHER_JSON, json).commit();
                    onWeatherFinished(weather);
                }
            }
        }
    }

    @Override
    public CharSequence getActivityTitle()
    {
        String city = mPreferences.getString(Constants.PREF_LOCATION_CITY, Constants.DEFAULT_CITY);
        if (city.contains(Constants.SHI) && city.substring(city.length() - 1).equals(Constants.SHI))
        {
            city = city.substring(0, city.length() - 1);
        }
        return city + "天气";
    }

    @Override
    public void showProgressDialog()
    {
        showProgressDialoghint();
    }

    @Override
    public void hideProgressDialog()
    {
         hideProgressDialogHint();
    }

    @Override
    public void onWeatherFinished(final Weather weather )
    {
        mWeather = weather;
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                //ToastUtils.showCrouton(mActivity, weather.toString(), getContentViewGroup());
                System.out.println("params.toString = " + ParamsUtils.paramsToString(mActivity));
                mTabViewPagerIndicator.setViewPagerAdapter(new WeatherPagerAdapter(mActivity.getSupportFragmentManager()));

            }
        });
    }

    public Weather getWeather()
    {
        return mWeather;
    }
}
