package com.haikuowuya.microlife.fragment;

import java.util.LinkedList;
import java.util.List;

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

public class LifeFragment extends BaseFragment
{
	public static LifeFragment newInstance()
	{
		LifeFragment lifeFragment = new LifeFragment();
		return lifeFragment;
	}

	private WeatherActivity mWeatherActivity;
	private ListView mListView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_life, null);//TODO
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
		if (null != mWeatherActivity.getWeather()
			&& null != mWeatherActivity.getWeather().lifeItem )
		{
			Weather.LifeItem lifeItem = mWeatherActivity.getWeather().lifeItem ;
			LinkedList<TextIconItem> data = new LinkedList<TextIconItem>();
			TextIconItem kongTiaoItem = new TextIconItem();
			kongTiaoItem.exp = lifeItem.kongTiaoExp;
			kongTiaoItem.imgResId = R.mipmap.ic_kongtiao;
			kongTiaoItem.desc = lifeItem.kongTiaoDesc;
			TextIconItem yunDongItem = new TextIconItem();
			yunDongItem.exp = lifeItem.yunDongExp;
			yunDongItem.desc = lifeItem.yunDongDesc;
			yunDongItem.imgResId = R.mipmap.ic_yundong;
			
			TextIconItem ziWaiXianItem = new TextIconItem();
			ziWaiXianItem.exp = lifeItem.ziWaiXianExp;
			ziWaiXianItem.imgResId = R.mipmap.ic_ziwaixian;
			ziWaiXianItem.desc = lifeItem.ziWaiXianDesc;
			TextIconItem ganMaoItem = new TextIconItem();
			ganMaoItem.exp = lifeItem.ganMaoExp;
			ganMaoItem.imgResId = R.mipmap.ic_ganmao;
			ganMaoItem.desc = lifeItem.ganMaoDesc;
			TextIconItem xiCheItem = new TextIconItem();
			xiCheItem.desc = lifeItem.xiCheDesc;
			xiCheItem.imgResId = R.mipmap.ic_xiche;
			xiCheItem.exp = lifeItem.xiCheExp;
			TextIconItem wuRanItem = new TextIconItem();
			wuRanItem.desc = lifeItem.wuRanDesc;
			wuRanItem.imgResId = R.mipmap.ic_img_1;
			wuRanItem.exp = lifeItem.wuRanExp;
			TextIconItem chuanyiItem = new TextIconItem();
			chuanyiItem.exp = lifeItem.chuanYiExp;
			chuanyiItem.desc = lifeItem.chuanyiDesc;
			chuanyiItem.imgResId = R.mipmap.ic_chuanyi;
			
			data.add(chuanyiItem);
			data.add(kongTiaoItem);
			data.add(ganMaoItem);
			data.add(yunDongItem);
			data.add(ziWaiXianItem);
			data.add(xiCheItem);
			data.add(wuRanItem);
			
			mListView.setAdapter(new LifeListAdapter(data));
		}
	}



	@Override
	public String getFragmentTitle()
	{
			return "生活指数";
		}
	class LifeListAdapter extends BaseAdapter<TextIconItem>
	{

		protected LifeListAdapter(List<TextIconItem> data)
		{
			super(mWeatherActivity, R.layout.list_life_item, data);
		}

		@Override
		public void bindDataToView(View convertView, TextIconItem t)
		{
			setImageViewResId(convertView, R.id.iv_image, t.imgResId);
			setTextViewText(convertView, R.id.tv_exp, t.exp);
			setTextViewText(convertView, R.id.tv_text, t.desc);
		}

	}

	class TextIconItem
	{
		public int imgResId;
		public String exp;
		public String desc;
	}
}
