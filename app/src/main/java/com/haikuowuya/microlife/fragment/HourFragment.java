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

public class HourFragment extends BaseFragment
{
    private static final String DU = "°C";
    private static final String ZHUAN = "转";

    public static HourFragment newInstance()
    {
        HourFragment hourFragment = new HourFragment();
        return hourFragment;
    }

    private WeatherActivity mWeatherActivity;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_hour, null);
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
        if (null != mWeatherActivity.getWeather() && null != mWeatherActivity.getWeather().hourItems)
        {
            mListView.setAdapter(new HourListAdapter());
        }
    }



    @Override
    public String getFragmentTitle()
    {
        return "逐时天气";

    }

    class HourListAdapter extends BaseAdapter<Weather.HourItem>
    {
        protected HourListAdapter()
        {
            super(mWeatherActivity, R.layout.list_hour_item, mWeatherActivity.getWeather().hourItems);
        }

        @Override
        public void bindDataToView(View convertView, Weather.HourItem t)
        {
            setTextViewText(convertView, R.id.tv_hour, t.hour);
            setTextViewText(convertView, R.id.tv_weather, t.info);
            setTextViewText(convertView, R.id.tv_temperature, t.temperature + DU);
            int resId = 0;
            if (t.isNight)
            {
                resId = mWeatherActivity.getResources().getIdentifier("ic_img_" + t.img + "_night", "mipmap", mWeatherActivity.getPackageName());
            }
            if (resId == 0)
            {
                resId = mWeatherActivity.getResources().getIdentifier("ic_img_" + t.img, "mipmap", mWeatherActivity.getPackageName());
            }
            if (resId == 0)
            {
                resId = R.mipmap.ic_img_0;
            }
            setImageViewResId(convertView, R.id.iv_image, resId);
        }
    }
}
