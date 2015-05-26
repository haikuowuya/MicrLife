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
import com.haikuowuya.microlife.mvp.view.CouponBrandView;
import com.haikuowuya.microlife.mvp.view.LocationView;
import com.haikuowuya.microlife.util.CityUtils;
import com.haikuowuya.microlife.util.ToastUtils;
import com.haikuowuya.microlife.view.FastScrollerLinearLayout;
import com.haikuowuya.microlife.view.common.ScrollingLinearLayoutManager;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by raiyi-suzhou on 2015/5/13 0013.
 */
public class CouponActivity extends BaseActivity  implements CouponBrandView
{
    private static final int DURATION = 1000;
    private static final String TITLE = "城市选择";
    private RecyclerView mRecyclerView;

    private CityPresenter mCityPresenter;
    private LocationPresenter mLocationPresenter;
    private FastScrollerLinearLayout mFastScroller;
    private CityAdapter mCityAdapter;


    public static void actionCoupon(Activity activity)
    {
        Intent intent = new Intent(activity, CouponActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);   //TODO
        initView();
        setMenuResId(R.drawable.location_selector);
        setListener();
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
    protected void onPause()
    {
        super.onPause();

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
