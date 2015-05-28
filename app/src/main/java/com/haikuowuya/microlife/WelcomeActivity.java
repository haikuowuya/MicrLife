package com.haikuowuya.microlife;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.haikuowuya.microlife.base.BaseActivity;
import com.haikuowuya.microlife.util.DensityUtils;
import com.haikuowuya.microlife.view.VerticalViewPager;
import com.haikuowuya.microlife.view.common.DepthPageTransformer;


public class WelcomeActivity extends BaseActivity
{
    public static  final String PREF_FIRST_ENTER="first_enter";
    private VerticalViewPager mVerticalViewPager;
    public static void  actionWelcome(Activity activity)
    {
        Intent intent = new Intent(activity,WelcomeActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        mVerticalViewPager.setAdapter(dummyPagerAdapter());
        mVerticalViewPager.setPageTransformer(true, new DepthPageTransformer());

    }

    private void initView()
    {
        mVerticalViewPager = (VerticalViewPager) findViewById(R.id.vertical_vp_viewpager);

    }

    private PagerAdapter dummyPagerAdapter()
    {
        final int[] images = {R.mipmap.ic_image1, R.mipmap.ic_image2, R.mipmap.ic_image3};
        PagerAdapter pagerAdapter = new PagerAdapter()
        {
            @Override
            public int getCount()
            {
                return images.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object o)
            {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                FrameLayout frameLayout = new FrameLayout(mActivity);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ImageView imageView = new ImageView(mActivity);
                imageView.setImageResource(images[position]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(layoutParams);
                frameLayout.addView(imageView);
                container.addView(frameLayout);
                if (position == images.length - 1) {
                    Button button = new Button(mActivity);
                    button.setGravity(Gravity.CENTER);
                    button.setText("进入");
                    ColorStateList colors = (ColorStateList)getResources().getColorStateList(R.drawable.default_text_color_selector);
                        button.setTextColor(colors);
                    button.setBackground(getResources().getDrawable(R.drawable.list_item_selector));
                    int height = DensityUtils.dpToPx(mActivity, 40.f);
                    int width = height * 3;
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                    params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                    params.bottomMargin = DensityUtils.dpToPx(mActivity, 20.f);
                    FrameLayout childFrameLayout = new FrameLayout(mActivity);
                    childFrameLayout.setBackgroundColor(getResources().getColor(R.color.color_indigo_colorPrimaryDark));
                    childFrameLayout.addView(button);
                    frameLayout.addView(childFrameLayout, params);
                    button.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            mPreferences.edit().putBoolean(PREF_FIRST_ENTER, false).commit();
                            MainActivity.actionMain(mActivity);
                            finish();
                        }
                    });
                }
                return frameLayout;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object)
            {
                container.removeView((View) object);
            }
        };
        return pagerAdapter;
    }

    @Override
    public CharSequence getActivityTitle()
    {
        return null;
    }
}
