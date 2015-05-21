package com.haikuowuya.microlife.mvp.presenter.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.haikuowuya.microlife.mvp.model.CityItem;
import com.haikuowuya.microlife.mvp.presenter.CityPresenter;
import com.haikuowuya.microlife.mvp.view.CityView;
import com.haikuowuya.microlife.util.CityUtils;

import java.util.List;

import io.realm.Realm;

/**
 * Created by raiyi-suzhou on 2015/5/14 0014.
 */
public class CityPresenterImpl implements CityPresenter
{
    private static final int What_INSERT_SUCCESS = 11111;

    private CityView mCityView;
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case What_INSERT_SUCCESS:
                    mCityView.hidProgressDialog();
                    mCityView.onInsertSuccess();
                    break;
            }
        }
    };

    public CityPresenterImpl(CityView cityView)
    {
        mCityView = cityView;
    }

    @Override
    public void insertCity()
    {
        mCityView.showProgressDialog();
        new InsertThread().start();

    }

    class InsertThread extends Thread
    {
        public void run()
        {
            List<CityItem> items = CityUtils.getCityList((Context) mCityView);
            if(null != items)
            {
                Realm realm = Realm.getInstance((Context) mCityView);
                realm.beginTransaction();
                for (int i = 0; i < items.size(); i++)
                {
                    CityItem item = items.get(i);
                  //  System.out.println("城市名称 = " + item.getsName());
                     realm.copyToRealm(item);
                        System.out.println("插入第" + (1 + i) + "条数据");
                }
                realm.commitTransaction();
            }
                mHandler.sendEmptyMessage(What_INSERT_SUCCESS);
        }
    }
}
