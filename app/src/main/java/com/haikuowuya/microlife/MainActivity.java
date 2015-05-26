package com.haikuowuya.microlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.haikuowuya.microlife.base.BaseActivity;
import com.haikuowuya.microlife.fragment.MainFragment;
import com.haikuowuya.microlife.fragment.MenuFragment;
import com.haikuowuya.microlife.util.DensityUtils;
import com.haikuowuya.microlife.view.common.DrawerArrowDrawable;

public class MainActivity extends BaseActivity
{
    private static  final String DEFAULT_CITY_TITLE="苏州";
    public static void actionMain(Activity activity)
    {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    private ActionBarDrawerToggle mActionBarDrawerTogger;
    private DrawerLayout mDrawerLayout;

    private DrawerArrowDrawable mArrowDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (mPreferences.getBoolean(WelcomeActivity.PREF_FIRST_ENTER, true))
        {
            WelcomeActivity.actionWelcome(this);
            finish();
        }
        setContentView(R.layout.activity_main); //TODO
        initView();

        getSupportFragmentManager().beginTransaction().add(R.id.frame_left_container, MenuFragment.newInstance()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_center_container, MainFragment.newInstance()).commit();
        installDrawerLayout();
        setListener();
        setMenuResId(R.mipmap.ic_qr);
    }

    private void initView()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

    }

    private void setListener()
    {
        OnClickListenerImpl onClickListenerImpl = new OnClickListenerImpl();
        onTitleClickListener(onClickListenerImpl);
        onMenuClickListener(onClickListenerImpl);
    }

    private void installDrawerLayout()
    {
        mArrowDrawable = new DrawerArrowDrawable(mActivity)
        {
            public boolean isLayoutRtl()
            {
                return false;
            }
        };
        mActionBarDrawerTogger = new ActionBarDrawerToggle(mActivity, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                mArrowDrawable.setProgress(0.f);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                mArrowDrawable.setProgress(1.f);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                mArrowDrawable.setProgress(slideOffset);
            }
        };
        getSupportActionBar().setHomeAsUpIndicator(mArrowDrawable);
        mDrawerLayout.setDrawerListener(mActionBarDrawerTogger);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onPostCreate(savedInstanceState, persistentState);
        mActionBarDrawerTogger.syncState();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.START))
                {
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
                else
                {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if(mDrawerLayout.isDrawerOpen(Gravity.START))
        {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public CharSequence getActivityTitle()
    {
        String title = DEFAULT_CITY_TITLE;
        if(getCurrentCity() != null)
        {
            title = getCurrentCity().getsName();
        }
        return   title;
    }
    public boolean isDrawerOpen()
    {
        return  mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    private class OnClickListenerImpl implements View.OnClickListener
    {
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.tv_center_title:
                    CityActivity.actionCity(mActivity);
                    break;
                case R.id.iv_menu:
                    QRActivity.actionQR(mActivity);
                    break;
            }
        }
    }
}
