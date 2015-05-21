package com.haikuowuya.microlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.haikuowuya.microlife.base.BaseActivity;

/**
 * Created by raiyi-suzhou on 2015/5/21 0021.
 */
public class QRActivity extends BaseActivity
{
    private static final String QR_TITLE="二维码扫描" ;
    public static void actionQR(Activity activity)
    {
        Intent intent = new Intent(activity, QRActivity.class);
        activity.startActivity(intent);

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
    }

    @Override
    public CharSequence getActivityTitle()
    {
        return QR_TITLE;
    }
}
