package com.haikuowuya.microlife.fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

import static com.haikuowuya.microlife.R.id.image;
import static com.haikuowuya.microlife.R.id.iv_home_image;

/**
 * Created by raiyi-suzhou on 2015/5/12 0012.
 */
public class MainFragment extends BaseFragment
{
    private static final String[] TEXT=new String[]{"首页","游戏","发现","流量","围观"};
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
        mViewPager.setPageTransformer(true, new DepthXPageTransformer());
        mLinearBottomBarContainer = (LinearLayout) view.findViewById(R.id.linear_bottom_bar_container);
        resetTabStatus();
        ((TextView) mLinearBottomBarContainer.getChildAt(mSelectedPosition).findViewById(R.id.tv_home_text)).setTextColor(0xFFFF0000);
        ((ImageView) mLinearBottomBarContainer.getChildAt(mSelectedPosition).findViewById(R.id.iv_home_image)).setImageState(new int[]{android.R.attr.state_pressed},true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
//        mViewPager.setAdapter(newPagerAdapter());
        mViewPager.setAdapter(newFragmentPagerAdapter());
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount() - 1);
        setListener();

    }

    private void setListener()
    {
        mViewPager.setOnPageChangeListener(new OnPagerChanageListenerImpl());
        for (int i = 0; i < mLinearBottomBarContainer.getChildCount(); i++)
        {
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

    private void changePageScrolledTab(int position, float positionOffset, int positionOffsetPixels)
    {
        System.out.println("selectedPosition = " + mSelectedPosition + " position = " + position + " positionOffset = " + positionOffset + "          positionOffsetPixels = " + positionOffsetPixels);
        if (position != mSelectedPosition)
        {
            View previousView = mLinearBottomBarContainer.getChildAt(mSelectedPosition);
            View currentView = mLinearBottomBarContainer.getChildAt(position);
            TextView previousTextView = (TextView) previousView.findViewById(R.id.tv_home_text);
            TextView currentTextView = (TextView) currentView.findViewById(R.id.tv_home_text);
            previousTextView.setTextColor(0xFFFF0000 & ((int) (positionOffset * 0xFF00FFFF)));
            currentTextView.setTextColor(0xFFFFFF00 & ((int) (positionOffset * 0xFF00FFFF)));
        }
    }

    private void changeSelectedTab(int position)
    {
        if (mSelectedPosition != position)
        {
            mSelectedPosition = position;
            resetTabStatus();
            View childView = mLinearBottomBarContainer.getChildAt(mSelectedPosition);
            TextView textView = (TextView) childView.findViewById(R.id.tv_home_text);
            ImageView imageView = (ImageView) childView.findViewById(R.id.iv_home_image);
            imageView.setImageState(new int[]{android.R.attr.state_pressed}, true);
           // textView.setTextColor(mActivity.getResources().getColor(R.color.color_indigo_colorPrimaryDark));
            textView.setTextColor(0xFFFF0000);
        }

    }

    private void resetTabStatus()
    {
        for (int i = 0; i < mLinearBottomBarContainer.getChildCount(); i++)
        {
            TextView textView = (TextView) mLinearBottomBarContainer.getChildAt(i).findViewById(R.id.tv_home_text);
            ImageView  imageView = (ImageView) mLinearBottomBarContainer.getChildAt(i ).findViewById(iv_home_image);
            textView.setTextColor(mActivity.getResources().getColor(R.color.color_indigo_colorPrimaryDark));
            textView.setText(TEXT[i]);
            imageView.setImageState(new int[]{android.R.attr.state_empty}, true);
            switch (i)
            {
                case 0:  imageView.setImageResource(R.drawable.home_home_selector);break;
                case 1: imageView.setImageResource(R.drawable.home_game_selector);break;
                case 2: imageView.setImageResource(R.drawable.home_find_selector);break;
                case 3: imageView.setImageResource(R.drawable.home_flow_selector);break;
                case 4: imageView.setImageResource(R.drawable.home_look_selector);break;
            }
        }
    }

    private FragmentPagerAdapter newFragmentPagerAdapter()
    {
        final BaseFragment[] fragments = new BaseFragment[]{HomeFragment.newInstance(), GameFragment.newInstance(), FindFragment.newInstance(), FlowFragment.newInstance(), LookFragment.newInstance()};
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(mActivity.getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int position)
            {
                return fragments[position];
            }

            @Override
            public int getCount()
            {
                return fragments.length;
            }
        };
        return fragmentPagerAdapter;
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

        OnViewClickListenerImpl(int position)
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
