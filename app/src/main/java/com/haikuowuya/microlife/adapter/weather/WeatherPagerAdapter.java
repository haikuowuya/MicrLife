package com.haikuowuya.microlife.adapter.weather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haikuowuya.microlife.base.BaseFragment;
import com.haikuowuya.microlife.fragment.ForecastFragment;
import com.haikuowuya.microlife.fragment.HomeFragment;
import com.haikuowuya.microlife.fragment.HourFragment;
import com.haikuowuya.microlife.fragment.LifeFragment;
import com.haikuowuya.microlife.fragment.TodayFragment;

/**
 * Created by raiyi-suzhou on 2015/5/21 0021.
 */
public class WeatherPagerAdapter extends FragmentPagerAdapter
{
    private     BaseFragment[] mFragments = new BaseFragment[]{TodayFragment.newInstance(), ForecastFragment.newInstance(), LifeFragment.newInstance(), HourFragment.newInstance()};
    public WeatherPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragments[position];
    }

    @Override
    public int getCount()
    {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
         return  mFragments[position].getFragmentTitle();
    }
}
