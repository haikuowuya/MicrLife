package com.haikuowuya.microlife.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.haikuowuya.microlife.util.DensityUtils;

public class TabViewPagerIndicator extends LinearLayout
{
    private static final int VP_VIEWPAGER = 122;
    private ViewPagerEx mViewPager;
    private CirclePageIndicator mIndicator;
    private PagerSlidingTabStrip mTabStrip;
    private LayoutParams mParams1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    private LayoutParams mParams2 = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
    private LayoutParams mParams3 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    private OnPageChangeListener mOnPageChangeListener;
    private int mMinHeight = 40;// dp
    private boolean mHasTab = false;
    private OnPageChangeListener mDefaultPageChangeListener = new OnPageChangeListener()
    {
        @Override
        public void onPageSelected(int position)
        {
            mIndicator.setCurrentItem(position);
            if (mOnPageChangeListener != null)
            {
                mOnPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            if (mOnPageChangeListener != null)
            {
                mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {
            if (mOnPageChangeListener != null)
            {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    public TabViewPagerIndicator(Context context)
    {
        this(context, false);
    }

    public TabViewPagerIndicator(Context context, boolean hasTab)
    {
        super(context);
        mHasTab = hasTab;
        init();
    }

    /**
     * 通过XML创建时，显示Tab
     */
    public TabViewPagerIndicator(Context context, AttributeSet attrs)
    {
        this(context, attrs, true);
    }

    public TabViewPagerIndicator(Context context, AttributeSet attrs, boolean hasTab)
    {
        super(context, attrs);
        mHasTab = hasTab;
        init();
    }

    private void init()
    {
        mMinHeight = DensityUtils.dpToPx(getContext(), mMinHeight);
        mViewPager = new ViewPagerEx(getContext());
        mViewPager.setId(VP_VIEWPAGER);
        mIndicator = new CirclePageIndicator(getContext());
        mTabStrip = new PagerSlidingTabStrip(getContext());
        mParams1.height = mMinHeight;
        mParams3.height = mMinHeight / 4;
        setOrientation(VERTICAL);
        addView(mTabStrip, mParams1);
        addView(mViewPager, mParams2);
        addView(mIndicator, mParams3);
        if (!mHasTab)
        {
            mTabStrip.setVisibility(View.GONE);
        }
        customIndicatior();
    }

    /**
     * 自定义Indicator的样式
     */
    private void customIndicatior()
    {

    }

    public void setViewPagerAdapter(PagerAdapter adapter)
    {
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        mIndicator.setViewPager(mViewPager);
        mTabStrip.setViewPager(mViewPager);
        mTabStrip.setOnPageChangeListener(mDefaultPageChangeListener);
    }

    public void setViewPagerChangedListener(OnPageChangeListener listener)
    {
        mOnPageChangeListener = listener;
    }

    public ViewPagerEx getViewPager()
    {
        return mViewPager;
    }

    public CirclePageIndicator getIndicator()
    {
        return mIndicator;
    }

    public PagerSlidingTabStrip getTabStrip()
    {
        return mTabStrip;
    }
}
