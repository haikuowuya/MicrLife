package com.haikuowuya.microlife.adapter.city;

import android.support.v7.widget.RecyclerView;

import com.haikuowuya.microlife.mvp.model.CityItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by raiyi-suzhou on 2015/5/22 0022.
 */
public class CityAdapterDataObserver extends android.support.v7.widget.RecyclerView.AdapterDataObserver
{
    private List<CityItem> mData;
    private CityAdapter mCityAdapter;

    public CityAdapterDataObserver(List<CityItem> mData, CityAdapter mCityAdapter)
    {
        this.mData = mData;
        this.mCityAdapter = mCityAdapter;
    }

    @Override
    public void onChanged()
    {
        List<CityItem> tmp = new LinkedList<>();
        tmp.addAll(mData);
        mData = tmp;
        System.out.println("数据发生变化");
    }
}
