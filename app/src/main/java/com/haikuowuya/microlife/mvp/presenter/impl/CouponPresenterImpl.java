package com.haikuowuya.microlife.mvp.presenter.impl;

import com.haikuowuya.microlife.mvp.presenter.CouponPresenter;
import com.haikuowuya.microlife.mvp.view.CouponBrandView;
import com.haikuowuya.microlife.util.CouponUtils;
import com.haikuowuya.microlife.util.OkHttpUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

/**
 * Created by Administrator on 2015/5/26 0026.
 */
public class CouponPresenterImpl implements CouponPresenter
{
    private CouponBrandView mCouponBrandView;
    private Request mRequest;

    public CouponPresenterImpl(CouponBrandView mCouponBrandView)
    {
        this.mCouponBrandView = mCouponBrandView;
        mRequest = new Request.Builder().url(CouponUtils.COUPON_BRAND_URL).build();
    }

    @Override
    public void doGetCouponBrand()
    {
        mCouponBrandView.showProgressDialog();
        OkHttpUtils.asyncExecute(mRequest, (Callback) mCouponBrandView);
    }
}
