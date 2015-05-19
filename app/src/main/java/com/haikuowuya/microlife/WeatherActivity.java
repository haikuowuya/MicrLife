package com.haikuowuya.microlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.haikuowuya.microlife.base.BaseActivity;
import com.haikuowuya.microlife.util.ParamsUtils;
import com.haikuowuya.microlife.util.WeatherUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class WeatherActivity extends BaseActivity
{

    public static void actionWeather(Activity activity)
    {
        Intent intent = new Intent(activity , WeatherActivity.class);
        activity.startActivity(intent);
    }
    private RecyclerView mRecyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String city = mPreferences.getString(Constants.PREF_LOCATION_CITY, Constants.DEFAULT_CITY);
        if(city.contains(Constants.SHI) && city.substring(city.length()-1).equals(Constants.SHI))
        {
            city = city.substring(0,city.length()-1);
        }
        setTitle(city + "天气");
        initView();
    }

    private void initView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        StringRequest request =  new StringRequest(WeatherUtils.getWeatherUrl("101190401"), this, this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                ParamsUtils.addPublic(mActivity, params);
                return params ;
            }
        };
        System.out.println("requestUrl = " + request.getUrl() );
      mRequestQueue.add(request);
       mRequestQueue.start();
    }

    @Override
    public void onResponse(Object response)
    {
        System.out.println("response = " + response.toString() );
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        super.onErrorResponse(error);
    }
}
