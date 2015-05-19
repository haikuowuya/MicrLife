package com.haikuowuya.microlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.location.BDLocation;
import com.eowise.recyclerview.stickyheaders.OnHeaderClickListener;
import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.haikuowuya.microlife.adapter.city.CityAdapter;
import com.haikuowuya.microlife.adapter.city.InitialHeaderAdapter;
import com.haikuowuya.microlife.base.BaseActivity;

import com.haikuowuya.microlife.model.CityItem;
import com.haikuowuya.microlife.mvp.presenter.CityPresenter;
import com.haikuowuya.microlife.mvp.presenter.LocationPresenter;
import com.haikuowuya.microlife.mvp.presenter.impl.CityPresenterImpl;
import com.haikuowuya.microlife.mvp.presenter.impl.LocationPresenterImpl;
import com.haikuowuya.microlife.mvp.view.CityView;
import com.haikuowuya.microlife.mvp.view.LocationView;
import com.haikuowuya.microlife.util.ToastUtils;
import com.haikuowuya.microlife.view.FastScrollerLinearLayout;
import com.haikuowuya.microlife.view.common.ScrollingLinearLayoutManager;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by raiyi-suzhou on 2015/5/13 0013.
 */
public class CityActivity extends BaseActivity implements CityView, LocationView
{
    private static final int DURATION = 1000;
    private static final String TITLE = "城市选择";
    private RecyclerView mRecyclerView;

    private SpotsDialog mSpotsDialog;
    private CityPresenter mCityPresenter;
    private LocationPresenter mLocationPresenter;
    private FastScrollerLinearLayout mFastScroller;


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
        setTitle(TITLE);
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
        mSpotsDialog = new SpotsDialog(mActivity, R.style.Custom);
        mFastScroller = (FastScrollerLinearLayout) findViewById(R.id.fast_scroller);
        mFastScroller.setRecyclerView(mRecyclerView);
    }

    @Override
    public void showProgressDialog()
    {
        mSpotsDialog.show();
    }

    @Override
    public void hidProgressDialog()
    {
        if (mSpotsDialog.isShowing())
        {
            mSpotsDialog.dismiss();
        }
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
            CityAdapter mCityAdapter = new CityAdapter(this, mCityItems);

            mRecyclerView.setAdapter(mCityAdapter);

            boolean isOverlay = false;
            StickyHeadersItemDecoration decoration = new StickyHeadersBuilder().setAdapter(mCityAdapter).setOnHeaderClickListener(new OnHeaderClickListenerImpl()).setRecyclerView(mRecyclerView).setStickyHeadersAdapter(new InitialHeaderAdapter(mCityItems), isOverlay).build();
            mRecyclerView.addItemDecoration(decoration);

        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSpotsDialog.dismiss();
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
            mPreferences.edit().putString(Constants.PREF_LOCATION_CITY, bdLocation.getCity()).commit();
            mRecyclerView.getAdapter().notifyDataSetChanged();
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
