package com.haikuowuya.microlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.base.BaseFragment;
import com.haikuowuya.microlife.util.AssetUtils;
import com.haikuowuya.microlife.view.VerticalViewPager;
import com.haikuowuya.microlife.view.common.DepthXPageTransformer;

/**
 * Created by raiyi-suzhou on 2015/5/12 0012.
 */
public class MainFragment extends BaseFragment
{
    public static MainFragment newInstance()
    {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    private ViewPager mViewPager;
    private LinearLayout mLinearBottomBarContainer;
    private int mSelectedPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, null);   //TODO
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        mViewPager = (ViewPager) view.findViewById(R.id.vp_viewpager);
        mLinearBottomBarContainer = (LinearLayout) view.findViewById(R.id.linear_bottom_bar_container);
        ((TextView) mLinearBottomBarContainer.getChildAt(mSelectedPosition).findViewById(R.id.tv_home_text)).setTextColor(0xFFFF0000);
        mViewPager.setPageTransformer(true, new DepthXPageTransformer());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewPager.setAdapter(newPagerAdapter());
        setListener();

    }

    private void setListener()
    {
        mViewPager.setOnPageChangeListener(new OnPagerChanageListenerImpl());
        for (int i = 0; i < mLinearBottomBarContainer.getChildCount(); i++) {
            mLinearBottomBarContainer.getChildAt(i).setOnClickListener(new OnViewClickListenerImpl(i));
        }
    }
    private class OnPagerChanageListenerImpl implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
               //changePageScrolledTab(position, positionOffset, positionOffsetPixels );
        }

        @Override
        public void onPageSelected(int position)
        {
            changeSelectedTab(position);

        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

    private void changePageScrolledTab(int position,float positionOffset,int positionOffsetPixels)
    {
        System.out.println("selectedPosition = " + mSelectedPosition + " position = " + position + " positionOffset = " + positionOffset
        +"          positionOffsetPixels = " +   positionOffsetPixels);
        if(position !=mSelectedPosition)
        {
            View previousView = mLinearBottomBarContainer.getChildAt(mSelectedPosition);
            View currentView = mLinearBottomBarContainer.getChildAt(position);
            TextView previousTextView = (TextView) previousView.findViewById(R.id.tv_home_text);
            TextView currentTextView = (TextView) currentView.findViewById(R.id.tv_home_text);
            previousTextView.setTextColor(0xFFFF0000 &((int)(positionOffset * 0xFF00FFFF)) );
            currentTextView.setTextColor(0xFFFFFF00 &((int)(positionOffset * 0xFF00FFFF)) );
        }
    }

    private void changeSelectedTab(int position)
    {
        if (mSelectedPosition != position) {
            mSelectedPosition = position;
            resetTabStatus();
            View childView = mLinearBottomBarContainer.getChildAt(mSelectedPosition);
            TextView textView = (TextView) childView.findViewById(R.id.tv_home_text);
            textView.setTextColor(0xFFFF0000);
        }

    }

    private void resetTabStatus()
    {
        for (int i = 0; i < mLinearBottomBarContainer.getChildCount(); i++) {
            ((TextView) mLinearBottomBarContainer.getChildAt(i).findViewById(R.id.tv_home_text)).setTextColor(0xFF000000);
        }
    }

    private PagerAdapter newPagerAdapter()
    {
        final int[] images = new int[]{R.mipmap.ic_image1, R.mipmap.ic_image2, R.mipmap.ic_image3, R.mipmap.ic_image2, R.mipmap.ic_image1};
        PagerAdapter pagerAdapter = new PagerAdapter()
        {
            @Override
            public int getCount()
            {
                return images.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object)
            {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ImageView imageView = new ImageView(mActivity);
                imageView.setImageResource(images[position]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                container.addView(imageView, layoutParams);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object)
            {
                container.removeView((View) object);
            }
        };
        return pagerAdapter;
    }

    private class OnViewClickListenerImpl implements View.OnClickListener
    {
        private int position;
        OnViewClickListenerImpl(int position )
        {
            this.position = position;
        }
        @Override
        public void onClick(View v)
        {
             mViewPager.setCurrentItem(position);
        }
    }

}
