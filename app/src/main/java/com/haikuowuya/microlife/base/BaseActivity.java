package com.haikuowuya.microlife.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.WelcomeActivity;
import com.haikuowuya.microlife.util.ViewUtils;
import com.haikuowuya.microlife.view.common.DrawerArrowDrawable;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by raiyi-suzhou on 2015/5/5 0005.
 */
public class BaseActivity extends AppCompatActivity implements Callback
{
    protected BaseActivity mActivity;
    protected SharedPreferences mPreferences;
    private FrameLayout mFrameContainer;
    protected Toolbar mToolbar;
    /**
     * 中间的标题
     */
    private TextView mTvTitle;
    /**
     * 右边的菜单按钮
     */
    private ImageView mIvMenu;

    public SharedPreferences getPreferences()
    {
        return mPreferences;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivity = this;

        mPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int statusColor = getResources().getColor(R.color.color_indigo_colorPrimaryDark);
        int navColor = getResources().getColor(R.color.color_indigo_nav_color);
        if (!(this instanceof WelcomeActivity))
        {
            ViewUtils.alphaStatusBarAndNavBar(mActivity, statusColor, navColor);
        }

    }

    @Override
    public void setContentView(int layoutResID)
    {
        //特殊处理 {@link WelcomeActivity}
        if (this instanceof WelcomeActivity)
        {
            super.setContentView(layoutResID);
        }
        else
        {
            super.setContentView(R.layout.activity_base);
            mFrameContainer = (FrameLayout) findViewById(R.id.frame_container);
            View contentView = LayoutInflater.from(mActivity).inflate(layoutResID, null);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            mFrameContainer.addView(contentView, layoutParams);
            initToolbar();
        }
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvTitle = (TextView) findViewById(R.id.tv_center_title);
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        if (mToolbar != null)
        {
            setSupportActionBar(mToolbar);
            DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(mActivity)
            {
                public boolean isLayoutRtl()
                {
                    return false;
                }
            };
            getSupportActionBar().setHomeAsUpIndicator(arrowDrawable);
            arrowDrawable.setProgress(1.f);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setTitle(String title)
    {
        mTvTitle.setText(title);
    }

    protected void setMenuResId(int resId)
    {
        mIvMenu.setImageResource(resId);
    }

    protected void onMenuClickListener(View.OnClickListener onClickListener)
    {
        mIvMenu.setOnClickListener(onClickListener);
    }

    protected void onTitleClickListener(View.OnClickListener onClickListener)
    {
        mTvTitle.setOnClickListener(onClickListener);
    }

    public ViewGroup getContentViewGroup()
    {
        return mFrameContainer;
    }

    @Override
    public void onFailure(Request request, IOException e)
    {

    }

    @Override
    public void onResponse(Response response) throws IOException
    {

    }
}
