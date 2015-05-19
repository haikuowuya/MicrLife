package com.haikuowuya.microlife.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.haikuowuya.microlife.model.HomeItem;
import com.haikuowuya.microlife.model.HomeItemHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class HomeItemUtils
{
    private static final String DATA_5100="data5100";
    private static final String DATA_5200="data5200";
    private static final String DATA_5300="data5300";
    private static final String DATA_5400="data5400";
    private static final String DATA_5500="data5500";
    private static final String DATA_5600="data5600";
    private static final String DATA_5700="data5700";
    private static final String DATA_5800="data5800";
    private static final String DATA_5900="data5900";
    private static final String DATA_6000="data6000";
    public static HomeItemHolder getHomeItemHolder(Context context)
    {
        HomeItemHolder homeItemHolder =new HomeItemHolder();
        String homeJson = AssetUtils.getHomeJson(context);
        if(!TextUtils.isEmpty(homeJson))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(homeJson)    ;
                if(null != jsonObject)
                {
                     parse5100(homeItemHolder, jsonObject.optString(DATA_5100));
                     parse5200(homeItemHolder, jsonObject.optString(DATA_5200));
                     parse5300(homeItemHolder, jsonObject.optString(DATA_5300));
                     parse5400(homeItemHolder, jsonObject.optString(DATA_5400));
                     parse5500(homeItemHolder, jsonObject.optString(DATA_5500));
                     parse5600(homeItemHolder, jsonObject.optString(DATA_5600));
                     parse5700(homeItemHolder, jsonObject.optString(DATA_5700));
                     parse5800(homeItemHolder, jsonObject.optString(DATA_5800));
                     parse5900(homeItemHolder, jsonObject.optString(DATA_5900));
                     parse6000(homeItemHolder, jsonObject.optString(DATA_6000));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return  homeItemHolder;
    }
    private static  void parse5100(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5100 = parseHomeItem(json);
    }
    private static  void parse5200(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5200 = parseHomeItem(json);
    }
    private static  void parse5300(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5300 = parseHomeItem(json);
    }
    private static  void parse5400(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5400 = parseHomeItem(json);
    }
    private static  void parse5500(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5500 = parseHomeItem(json);
    }
    private static  void parse5600(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5600 = parseHomeItem(json);
    }
    private static  void parse5700(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5700 = parseHomeItem(json);
    }
    private static  void parse5800(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5800 = parseHomeItem(json);
    } private static  void parse5900(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data5900 = parseHomeItem(json);
    } private static  void parse6000(HomeItemHolder homeItemHolder,String json)
    {
        homeItemHolder.data6000 = parseHomeItem(json);
    }

    private static LinkedList<HomeItem> parseHomeItem(String json)
    {
        LinkedList<HomeItem> items = null;
        if(!TextUtils.isEmpty(json))
        {
            try
            {
                JSONArray jsonArray =new JSONArray(json);
                if(null != jsonArray && jsonArray.length() > 0)
                {
                    items = new LinkedList<>();
                    for (int i = 0;i < jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        if(jsonObject != null)
                        {
                             HomeItem item = new HomeItem();
                            item.name = jsonObject.optString("name");
                            item.mKey = jsonObject.optString("mKey");
                            item.positionNo = jsonObject.optInt("positionNo");
                            item.url = jsonObject.optString("url");
                            item.img = jsonObject.optString("img");
                            item.status = jsonObject.optInt("status");
                            items.add(item);
                        }
                    }
                    if(items.isEmpty())
                    {
                        items = null;
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return  items;
    }
}
