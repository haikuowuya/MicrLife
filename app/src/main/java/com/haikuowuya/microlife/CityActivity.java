package com.haikuowuya.microlife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.location.BDLocation;
import com.eowise.recyclerview.stickyheaders.OnHeaderClickListener;
import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.haikuowuya.microlife.adapter.city.CityAdapter;
import com.haikuowuya.microlife.adapter.city.InitialHeaderAdapter;
import com.haikuowuya.microlife.base.BaseActivity;

import com.haikuowuya.microlife.mvp.model.CityItem;
import com.haikuowuya.microlife.mvp.presenter.CityPresenter;
import com.haikuowuya.microlife.mvp.presenter.LocationPresenter;
import com.haikuowuya.microlife.mvp.presenter.impl.CityPresenterImpl;
import com.haikuowuya.microlife.mvp.presenter.impl.LocationPresenterImpl;
import com.haikuowuya.microlife.mvp.view.CityView;
import com.haikuowuya.microlife.mvp.view.LocationView;
import com.haikuowuya.microlife.util.ACache;
import com.haikuowuya.microlife.util.CityUtils;
import com.haikuowuya.microlife.util.ToastUtils;
import com.haikuowuya.microlife.view.FastScrollerLinearLayout;
import com.haikuowuya.microlife.view.common.ScrollingLinearLayoutManager;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by raiyi-suzhou on 2015/5/13 0013.
 */
public class CityActivity extends BaseActivity implements CityView, LocationView
{
    private static final int DURATION = 1000;
    private static final String TITLE = "城市选择";
    private RecyclerView mRecyclerView;

    private CityPresenter mCityPresenter;
    private LocationPresenter mLocationPresenter;
    private FastScrollerLinearLayout mFastScroller;
    private CityAdapter mCityAdapter;


    public static void actionCity(Activity activity)
    {
        Intent intent = new Intent(activity, CityActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);   //TODO
        initView();

        setMenuResId(R.drawable.location_selector);
        fillData();
        setListener();
    }


    private void fillData()
    {
        if (Realm.getInstance(mActivity).where(CityItem.class).findAll().size() == 0)
        {
            mCityPresenter = new CityPresenterImpl(this);
            mCityPresenter.insertCity();
        }
        else
        {
            onInsertSuccess();
        }
    }

    private void setListener()
    {
        onMenuClickListener(new OnMenuClickListener());
    }

    private void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recyclerview);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setLayoutManager(new ScrollingLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false, DURATION));

        mFastScroller = (FastScrollerLinearLayout) findViewById(R.id.fast_scroller);
        mFastScroller.setRecyclerView(mRecyclerView);
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
    public void onInsertSuccess()
    {
        Realm realm = Realm.getInstance(this);
        RealmResults<CityItem> mCityItems = realm.allObjects(CityItem.class);

        System.out.println("mCityItems.size = " + mCityItems.size());
        if (!mCityItems.isEmpty())
        {
            mRecyclerView.removeAllViews();
              mCityAdapter = new CityAdapter(this, mCityItems);

            mRecyclerView.setAdapter(mCityAdapter);

            boolean isOverlay = false;
            StickyHeadersItemDecoration decoration = new StickyHeadersBuilder().setAdapter(mCityAdapter).setOnHeaderClickListener(new OnHeaderClickListenerImpl()).setRecyclerView(mRecyclerView).setStickyHeadersAdapter(new InitialHeaderAdapter(mCityItems), isOverlay).build();
            mRecyclerView.addItemDecoration(decoration);

        }
    }

    @Override
    public void showCityChangedDialog(final String locationCityName, final String currentCityName)
    {
        AlertDialog dialog = new AlertDialog.Builder(mActivity).
                setTitle("提示")
                .setMessage("定位的城市和当前选择的城市不一致，是否要切换到定位的城市")
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                           CityItem currentCity =  CityUtils.saveSelectedCity(mActivity, locationCityName);
                        mCityAdapter.setSelectedCityItem(currentCity);
                            mCityAdapter.notifyDataSetChanged();
                             dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

    }

    @Override
    public void startLocation()
    {
//        Toast.makeText(mActivity, "正在定位……", Toast.LENGTH_SHORT).show();
        ToastUtils.showCrouton(mActivity, "正在定位", getContentViewGroup());
        mLocationPresenter = new LocationPresenterImpl(this);
        mLocationPresenter.executeLocation();
    }

    @Override
    public void finishLocation(BDLocation bdLocation)
    {
        if (null != bdLocation)
        {
//            Toast.makeText(mActivity, bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
            ToastUtils.showCrouton(mActivity, bdLocation.getAddrStr(), getContentViewGroup());
            String city = bdLocation.getCity();
            if (city.contains(Constants.SHI) && city.substring(city.length() - 1).equals(Constants.SHI))
            {
                city = city.substring(0, city.length() - 1);
            }
//            RealmQuery<CityItem> query = Realm.getInstance(mActivity).where(CityItem.class).equalTo(CityItem.FIELD_S_NAME, city);
//            if (null != query)
//            {
//                CityItem cityItem = query.findFirst();
//                System.out.println("定位城市  = " + cityItem.getsName());
//                ACache aCache = ACache.get(mActivity);
//                aCache.put(Constants.PREF_SELECTED_CITY, cityItem);
//            }
            mPreferences.edit().putString(Constants.PREF_LOCATION_CITY, city).commit();
            CityItem currentCity = Realm.getInstance(mActivity).where(CityItem.class).equalTo(CityItem.FIELD_IS_CURRENT_CITY, true).findFirst();
            if(!currentCity.getsName().equals(city))
            {
                showCityChangedDialog(city, currentCity.getsName());
            }
        }

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (null != mLocationPresenter)   //判断用户是否点击了定位按钮
        {
            mLocationPresenter.stopLocation();
        }
    }

    @Override
    public CharSequence getActivityTitle()
    {
        return TITLE;
    }

    private class OnMenuClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            startLocation();
        }
    }

    private class OnHeaderClickListenerImpl implements OnHeaderClickListener
    {
        public void onHeaderClick(View view, long l)
        {
            //  Toast.makeText(mActivity, "hello", Toast.LENGTH_SHORT).show();
        }
    }

}
