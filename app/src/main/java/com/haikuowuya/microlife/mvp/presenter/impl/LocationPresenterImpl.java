package com.haikuowuya.microlife.mvp.presenter.impl;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.haikuowuya.microlife.mvp.presenter.LocationPresenter;
import com.haikuowuya.microlife.mvp.view.LocationView;

/**
 * Created by raiyi-suzhou on 2015/5/15 0015.
 */
public class LocationPresenterImpl implements LocationPresenter
{
    private static final String LOCATION_TYPE="gcj02";    //返回的定位结果是百度经纬度，默认值gcj02
    private LocationView mLocationView;
    private LocationClient mLocationClient;
    private BDLocationListenerImpl mBDLocationListenerImpl;
    public LocationPresenterImpl(LocationView locationView)
    {
        mLocationView = locationView;
        mBDLocationListenerImpl = new BDLocationListenerImpl();
    }
    @Override
    public void executeLocation()
    {
        initLocation();
        mLocationClient.start();
    }

    @Override
    public void stopLocation()
    {
            mLocationClient.unRegisterLocationListener(mBDLocationListenerImpl);
            mLocationClient.stop();
    }

    private void initLocation()
    {
        mLocationClient = new LocationClient((Context) mLocationView);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType(LOCATION_TYPE);//返回的定位结果是百度经纬度，默认值gcj02
        int span=900;     //小于1000只会定位一次
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(mBDLocationListenerImpl);
    }

    private class BDLocationListenerImpl implements BDLocationListener
    {
        public void onReceiveLocation(BDLocation bdLocation)
        {
            mLocationClient.stop();
            mLocationView.finishLocation(bdLocation);
        }
    }
}
