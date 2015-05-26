package com.haikuowuya.microlife.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haikuowuya.microlife.Constants;
import com.haikuowuya.microlife.CouponActivity;
import com.haikuowuya.microlife.MainActivity;
import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.WeatherActivity;
import com.haikuowuya.microlife.adapter.city.InitialHeaderAdapter;
import com.haikuowuya.microlife.base.BaseAdapter;
import com.haikuowuya.microlife.base.BaseFragment;
import com.haikuowuya.microlife.mvp.model.CouponBrand;
import com.haikuowuya.microlife.mvp.model.Weather;
import com.haikuowuya.microlife.mvp.presenter.impl.WeatherPresenterImpl;
import com.haikuowuya.microlife.mvp.view.WeatherView;
import com.haikuowuya.microlife.util.DateUtils;
import com.haikuowuya.microlife.util.ToastUtils;
import com.haikuowuya.microlife.util.WeatherUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by raiyi-suzhou on 2015/5/12 0012.
 */
public class MenuFragment extends BaseFragment implements WeatherView, Callback
{
    public static MenuFragment newInstance()
    {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }
    private Weather mWeather;
    private TextView mTvUpdateTime;
    private TextView mTvWeather;
    private TextView mTvTemperature;
    private TextView mTvPm;
    private ProgressBar mPbRefreshProgress;
    private RelativeLayout mRelativeWeatherContainer;
    private ImageView mIvWeatherRefresh;
    private TextView mTvPmInfo;
    private CardView mCardCoupon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_menu, null);   //TODO
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        mRelativeWeatherContainer = (RelativeLayout) view.findViewById(R.id.relative_weather_container);
        mTvTemperature = (TextView) mRelativeWeatherContainer.findViewById(R.id.tv_temperature);
        mTvWeather = (TextView) mRelativeWeatherContainer.findViewById(R.id.tv_weather);
        mTvUpdateTime = (TextView) mRelativeWeatherContainer.findViewById(R.id.tv_update_time);
        mTvPm = (TextView) mRelativeWeatherContainer.findViewById(R.id.tv_pm);
        mPbRefreshProgress = (ProgressBar) mRelativeWeatherContainer.findViewById(R.id.pb_refresh_progress);
        mIvWeatherRefresh = (ImageView) mRelativeWeatherContainer.findViewById(R.id.iv_weather_refresh);
        mTvPmInfo = (TextView) mRelativeWeatherContainer.findViewById(R.id.tv_pm_info);
        mCardCoupon = (CardView) view.findViewById(R.id.card_coupon);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initWeatherData();
        setListener();
    }
    public  void initWeatherData()
    {
        Weather weather = WeatherUtils.parseWeatherJson(mActivity.getPreferences().getString(Constants.PREF_WEATHER_JSON, ""));
        if (null != weather && !WeatherUtils.isWeatherExpired(mActivity.getPreferences().getLong(Constants.PREF_WEATHER_UPDATE_TIME, 0))
                && WeatherUtils.isCurrentCityWeather(weather.cityCode, mActivity.getCurrentCity().getWeatherId())
                )
        {
            onWeatherFinished(weather);
        } else
        {
            if (null != mActivity.getCurrentCity())
            {
                new WeatherPresenterImpl(this, mActivity.getCurrentCity().getWeatherId()).doGetWeather();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(null != mWeather &&null != mActivity.getCurrentCity() )
        {
            if(!WeatherUtils.isCurrentCityWeather(mWeather.cityCode, mActivity.getCurrentCity().getWeatherId()))
            {
                new WeatherPresenterImpl(this, mActivity.getCurrentCity().getWeatherId()).doGetWeather();
            }
        }
    }

    private void setListener()
    {
        OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
        mRelativeWeatherContainer.setOnClickListener(onClickListenerImpl);
        mIvWeatherRefresh.setOnClickListener(onClickListenerImpl);
        mCardCoupon.setOnClickListener(onClickListenerImpl);
    }

    @Override
    public String getFragmentTitle()
    {
        return "左菜单";
    }

    @Override
    public void showProgressDialog()
    {
        ToastUtils.showCrouton(mActivity, "正在更新天气", mRelativeWeatherContainer);
        mPbRefreshProgress.setVisibility(View.VISIBLE);
        mIvWeatherRefresh.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressDialog()
    {
        mPbRefreshProgress.setVisibility(View.GONE);
        mIvWeatherRefresh.setVisibility(View.VISIBLE);

    }

    @Override
    public void onWeatherFinished(final Weather weather)
    {
        mWeather = weather;
        mActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                hideProgressDialog();
                Weather.WeatherItem weatherItem = weather.weatherItems.get(0);
                Weather.RealtimeItem realtimeItem = weather.realtimeItem;
                mTvWeather.setText(realtimeItem.info);
                mTvTemperature.setText(realtimeItem.feelslike_c);
                mTvUpdateTime.setText("更新时间:" + DateUtils.getUpdateTime(realtimeItem.dataUptime));
                if (null != weather.pm25)
                {
                    mTvPm.setText("PM值：" + weather.pm25.pm25);
                    mTvPmInfo.setText(weather.pm25.quality);
                    mTvPmInfo.setBackgroundColor(Color.parseColor(weather.pm25.color));
                }
                mRelativeWeatherContainer.setBackgroundResource(R.mipmap.ic_weather_background_sun);
                if (realtimeItem.info.contains(Weather.DUYUN))
                {
                    mRelativeWeatherContainer.setBackgroundResource(R.mipmap.ic_weather_background_cloudy);
                } else if (realtimeItem.info.contains(Weather.YU))
                {
                    mRelativeWeatherContainer.setBackgroundResource(R.mipmap.ic_weather_background_rain);
                }
            }
        });
    }

    @Override
    public void onFailure(Request request, IOException e)
    {
        ToastUtils.showCrouton(mActivity, "更新天气失败", mActivity.getContentViewGroup());
    }

    @Override
    public void onResponse(Response response) throws IOException
    {
        if (response.isSuccessful())
        {
            String json = response.body().string();
            if (!TextUtils.isEmpty(json))
            {
                Weather weather = WeatherUtils.parseWeatherJson(json);
                if (null != weather)
                {
                    mActivity.getPreferences().edit().putString(Constants.PREF_WEATHER_JSON, json).commit();
                    mActivity.getPreferences().edit().putLong(Constants.PREF_WEATHER_UPDATE_TIME, System.currentTimeMillis()).commit();
                    onWeatherFinished(weather);
                }
            }
        }
    }

    private class OnClickListenerImpl implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.relative_weather_container:
                    WeatherActivity.actionWeather(mActivity);
                    break;
                case R.id.iv_weather_refresh:
                    new WeatherPresenterImpl(MenuFragment.this, mActivity.getCurrentCity().getWeatherId()).doGetWeather();
                    break;
                case R.id.card_coupon:
                    CouponActivity.actionCoupon(mActivity);
                    break;

            }
        }
    }
}
