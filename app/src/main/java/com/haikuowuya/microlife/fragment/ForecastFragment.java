package com.haikuowuya.microlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.WeatherActivity;
import com.haikuowuya.microlife.base.BaseAdapter;
import com.haikuowuya.microlife.base.BaseFragment;
import com.haikuowuya.microlife.mvp.model.Weather;

public class ForecastFragment extends BaseFragment
{
    private static final String DU = "°C";
    private static final String ZHUAN = "转";

    public static ForecastFragment newInstance()
    {
        ForecastFragment forecastFragment = new ForecastFragment();
        return forecastFragment;
    }

    private ListView mListView;
    private WeatherActivity mWeatherActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_forecast, null);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        mListView = (ListView) view.findViewById(R.id.lv_listview);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mWeatherActivity = (WeatherActivity) mActivity;
        if (null != mWeatherActivity.getWeather() && null != mWeatherActivity.getWeather().weatherItems)
        {
            mListView.setAdapter(new WeatherListAdapter());
        }
    }

    @Override
    public String getFragmentTitle()
    {

        return "天气预报";
    }

    class WeatherListAdapter extends BaseAdapter<Weather.WeatherItem>
    {
        protected WeatherListAdapter()
        {
            super(mActivity, R.layout.list_weather_item, mWeatherActivity.getWeather().weatherItems);
        }

        @Override
        public void bindDataToView(View convertView, Weather.WeatherItem t)
        {
            setTextViewText(convertView, R.id.tv_week, t.week);
            String weather = t.dayItem.info;
            if (!t.dayItem.info.equals(t.nightItem.info))
            {
                weather = t.dayItem.info + ZHUAN + t.nightItem.info;
            }
            setTextViewText(convertView, R.id.tv_weather, weather);
            setTextViewText(convertView, R.id.tv_temperature, t.nightItem.temperature + "~" + t.dayItem.temperature + DU);
            setTextViewText(convertView, R.id.tv_wind, t.dayItem.direct + " " + t.dayItem.power);
            int resId = mWeatherActivity.getResources().getIdentifier("ic_img_" + t.dayItem.img, "mipmap", mWeatherActivity.getPackageName());
            if (resId == 0)
            {
                resId = R.mipmap.ic_img_0;
            }
            setImageViewResId(convertView, R.id.iv_image, resId);
        }
    }
}
