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
import com.haikuowuya.microlife.adapter.coupon.CouponBrandAdapter;
import com.haikuowuya.microlife.base.BaseActivity;
import com.haikuowuya.microlife.mvp.model.CityItem;
import com.haikuowuya.microlife.mvp.model.CouponBrand;
import com.haikuowuya.microlife.mvp.presenter.CityPresenter;
import com.haikuowuya.microlife.mvp.presenter.CouponPresenter;
import com.haikuowuya.microlife.mvp.presenter.LocationPresenter;
import com.haikuowuya.microlife.mvp.presenter.impl.CityPresenterImpl;
import com.haikuowuya.microlife.mvp.presenter.impl.CouponPresenterImpl;
import com.haikuowuya.microlife.mvp.presenter.impl.LocationPresenterImpl;
import com.haikuowuya.microlife.mvp.view.CityView;
import com.haikuowuya.microlife.mvp.view.CouponBrandView;
import com.haikuowuya.microlife.mvp.view.LocationView;
import com.haikuowuya.microlife.util.CityUtils;
import com.haikuowuya.microlife.util.CouponUtils;
import com.haikuowuya.microlife.util.ToastUtils;
import com.haikuowuya.microlife.view.FastScrollerLinearLayout;
import com.haikuowuya.microlife.view.common.ScrollingLinearLayoutManager;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by raiyi-suzhou on 2015/5/13 0013.
 */
public class CouponActivity extends BaseActivity  implements CouponBrandView
{
    private static final int DURATION = 1000;
    private static final String TITLE = "优惠券列表";
    private RecyclerView mRecyclerView;
    private CouponPresenter mCouponPresenter;
    

    public static void actionCoupon(Activity activity)
    {
        Intent intent = new Intent(activity, CouponActivity.class);
        activity.startActivity(intent);
    }

    private CouponBrand mCouponBrand;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);   //TODO
        initView();
        setMenuResId(R.mipmap.ic_refresh);
        setListener();
        if(mPreferences.contains(Constants.PREF_COUPON_BRAND))
        {
            mCouponBrand = CouponUtils.parseCouponBrandJson(mPreferences.getString(Constants.PREF_COUPON_BRAND, null));
        }
        if(null != mCouponBrand)
        {
            onCouponBrandFinish(mCouponBrand);
        }
        else
        {
         mCouponPresenter = new CouponPresenterImpl(this);
         mCouponPresenter.doGetCouponBrand();
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
    public void onResponse(Response response) throws IOException
    {
        hideProgressDialog();
        String json = response.body().string();
         mCouponBrand  = CouponUtils.parseCouponBrandJson(json);
        if(null != mCouponBrand)
        {
            mPreferences.edit().putString(Constants.PREF_COUPON_BRAND, json).commit();
            onCouponBrandFinish(mCouponBrand);
          //  ToastUtils.showCrouton(mActivity, mCouponBrand.toString(), getContentViewGroup());
        }
    }

    @Override
    public void onFailure(Request request, IOException e)
    {
        hideProgressDialog();
        super.onFailure(request, e);
    }

    @Override
    public CharSequence getActivityTitle()
    {
        return TITLE;
    }

    @Override
    public void onCouponBrandFinish(final CouponBrand couponBrand)
    {
         runOnUiThread(new Runnable()
         {
             @Override
             public void run()
             {
                 mRecyclerView.setAdapter(new CouponBrandAdapter(couponBrand.toList()));
             }
         });
        
    }

    private class OnMenuClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            mCouponPresenter = new CouponPresenterImpl(CouponActivity.this);
            mCouponPresenter.doGetCouponBrand();
        }
    }

   

}
