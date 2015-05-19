package com.haikuowuya.microlife.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by raiyi-suzhou on 2015/5/11 0011.
 */
public class BaseFragment   extends Fragment
{
    protected  BaseActivity mActivity;
    protected boolean mIsInit = false;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }


}
