package com.haikuowuya.microlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.WeatherActivity;
import com.haikuowuya.microlife.base.BaseAdapter;
import com.haikuowuya.microlife.base.BaseFragment;
import com.haikuowuya.microlife.mvp.model.Weather;
import com.haikuowuya.microlife.util.DateUtils;

import java.util.LinkedList;
import java.util.List;

public class TodayFragment extends BaseFragment
{
	private static final String DU = "°C";
	private static final String ZHUAN = "转";

	public static TodayFragment newInstance()
	{
		TodayFragment todayFragment = new TodayFragment();
		return todayFragment;
	}

	private WeatherActivity mWeatherActivity;

	private TextView mTvUpdateTime;
	private TextView mTvWeather;
	private TextView mTvTemperature;
	private View mHeaderView;
	private ListView mListView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_today, null);//TODO
		initView(view);
		return view;
	}

	private void initView(View view)
	{
		mListView = (ListView) view.findViewById(R.id.lv_listview);
		mHeaderView = LayoutInflater.from(mActivity).inflate(R.layout.layout_weather_today_headview,null);
		mTvTemperature = (TextView) mHeaderView.findViewById(R.id.tv_temperature);
		mTvWeather = (TextView) mHeaderView.findViewById(R.id.tv_weather);
		mTvUpdateTime = (TextView) mHeaderView.findViewById(R.id.tv_update_time);

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mWeatherActivity = (WeatherActivity) mActivity;
		if (null != mWeatherActivity.getWeather()
			&& null != mWeatherActivity.getWeather().weatherItems && null != mWeatherActivity.getWeather().realtimeItem )
		{
			 Weather.WeatherItem weatherItem = mWeatherActivity.getWeather().weatherItems.get(0);
			 Weather.RealtimeItem realtimeItem = mWeatherActivity.getWeather().realtimeItem;
			 mTvWeather.setText(realtimeItem.info);
			 mTvTemperature.setText(realtimeItem.feelslike_c);
			 mTvUpdateTime.setText("更新时间:"+ DateUtils.getUpdateTime(realtimeItem.dataUptime));

			LinkedList<TextIconItem> data = new LinkedList<>();
			data.add(new TextIconItem(R.mipmap.ic_sun_rise,"日出时间:"+weatherItem.dayItem.time));
			data.add(new TextIconItem(R.mipmap.ic_sun_down,"日落时间:"+weatherItem.nightItem.time));
			data.add(new TextIconItem(R.mipmap.ic_wind_direct,"当前风向:"+realtimeItem.direct));
			data.add(new TextIconItem(R.mipmap.ic_wind_speed,"当前风速:"+ realtimeItem.windspeed+"km/h"));
			data.add(new TextIconItem(R.mipmap.ic_feelslike_c,"体感温度:"+ realtimeItem.feelslike_c+DU));
			data.add(new TextIconItem(R.mipmap.ic_humidity,"空气湿度:"+realtimeItem.humidity+"%"));
			mListView.addHeaderView(mHeaderView);
			mListView.setAdapter(new TodayListAdapter(data));
		}
	}

	class TodayListAdapter extends BaseAdapter<TextIconItem>
	{

		protected TodayListAdapter(List<TextIconItem> data)
		{
			super(mWeatherActivity, R.layout.list_today_item, data);
		}

		@Override
		public void bindDataToView(View convertView, TextIconItem t)
		{
			setImageViewResId(convertView, R.id.iv_image, t.imgResId);
			setTextViewText(convertView, R.id.tv_text, t.desc);
		}

	}

	class TextIconItem
	{
		public int imgResId;
		public String desc;
		public TextIconItem(int imgResId, String desc)
		{
			this.imgResId = imgResId;
			this.desc = desc;
		}
	}

	@Override
	public String getFragmentTitle()
	{
		return "今日天气";
	}
}
