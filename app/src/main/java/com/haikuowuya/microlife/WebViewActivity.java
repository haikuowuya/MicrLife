package com.haikuowuya.microlife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.haikuowuya.microlife.base.BaseActivity;

/**
 * Created by raiyi-suzhou on 2015/5/21 0021.
 */
public class WebViewActivity extends BaseActivity
{
    public static final String EXTRA_URL = "url";

    public static void actionWebView(Context context, String url)
    {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    private WebView mWebView;
    private String mUrl;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);  //TODO
        initView();
        initWebView();
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mWebView.loadUrl(mUrl);

    }

    private void initWebView()
    {
        mWebView.setWebChromeClient(new SubWebChromeClient());
        mWebView.setWebViewClient(new SubWebViewClient());
    }

    private void initView()
    {
        mWebView = (WebView) findViewById(R.id.wv_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);

    }

    @Override
    public CharSequence getActivityTitle()
    {
        return "网页";
    }

    private class SubWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.VISIBLE);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class SubWebChromeClient extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            super.onProgressChanged(view, newProgress);
            if(mProgressBar.getProgress() < newProgress)
            {
                mProgressBar.setProgress(newProgress);
            }
            if (mProgressBar.getProgress() == mProgressBar.getMax())
            {
                mProgressBar.setVisibility(View.INVISIBLE);

            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title)
        {
            super.onReceivedTitle(view, title);
            setTitleText(title);
        }
    }
}
