package com.haikuowuya.microlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.adapter.home.HomePagerAdapter;
import com.haikuowuya.microlife.base.BaseFragment;
import com.haikuowuya.microlife.model.HomeItemHolder;
import com.haikuowuya.microlife.util.HomeItemUtils;
import com.haikuowuya.microlife.util.ToastUtils;
import com.haikuowuya.microlife.view.InfiniteViewPagerIndicatorView;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class FindFragment extends BaseFragment
{



    public static FindFragment newInstance()
    {
        FindFragment homeFragment = new FindFragment();
        return  homeFragment;
    }
    private LinearLayout mLinearHomeContainer;
    private HomeItemHolder mHomeItemHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         View view = inflater.inflate(R.layout.fragment_home, null);
        initView(view);
        mIsInit = true;

        return  view;
    }

    private void initView(View view)
    {
        mLinearHomeContainer = (LinearLayout) view.findViewById(R.id.linear_home_container);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        if (getUserVisibleHint())
        {
            if (mIsInit)
            {
                mIsInit = false;
                 handleHomeItem();
            }
        }
    }
    private void handleHomeItem()
    {
        ToastUtils.showCrouton(mActivity,"加载数据中……", mActivity.getContentViewGroup());
        mHomeItemHolder = HomeItemUtils.getHomeItemHolder(mActivity);
        if(null != mHomeItemHolder )
        {
           if(null != mHomeItemHolder.data5100)
           {
               View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_5100,mLinearHomeContainer);
               InfiniteViewPagerIndicatorView infiniteViewPagerIndicatorView = (InfiniteViewPagerIndicatorView) view.findViewById(R.id.infinite_viewpager);
               infiniteViewPagerIndicatorView.setViewPagerAdapter(new HomePagerAdapter(mActivity, mHomeItemHolder.data5100));
               infiniteViewPagerIndicatorView.hideIndicator();;
           }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            if (mIsInit)
            {
                mIsInit = false;
                handleHomeItem();
            }
        }
    }

    /**添加一个进度圈表示获取数据中*/
    private ProgressBar addProgressBar(FrameLayout frameLayout)
    {
          ProgressBar   mProgressBar = new ProgressBar(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        frameLayout.addView(mProgressBar, params);
        mProgressBar.setVisibility(View.GONE);
        return mProgressBar;
    }
}
