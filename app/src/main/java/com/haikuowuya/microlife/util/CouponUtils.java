package com.haikuowuya.microlife.util;

import android.text.TextUtils;
import android.view.TextureView;

import com.haikuowuya.microlife.mvp.model.CouponBrand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by raiyi-suzhou on 2015/5/26 0026.
 */
public class CouponUtils
{
    private static  final String TAG_SHOP_LIST="shop_list";
    private static final String TAG_SHOP_ID="shop_id";
    private static final String TAG_SHOP_NAME="shop_name";
    private static final String TAG_SHOP_ACTION="shop_action";
    private static final String TAG_SHOP_ICON="shop_icon";
    private static final String TAG_SHOP_URL="shop_url";
    private static final String TAG_TAG_URL="tag_url";
    public static final String COUPON_BRAND_URL="http://mk.2000tuan.com/coupon4/view/getshoplist.php?imei=358094050288555&imsi=460015072618804&mac=e0%3A63%3Ae5%3A9c%3Acc%3A8f&platform=Android&platform_model=C6902&version=4100&system_version=4.4.4&app_name=cn.buding.coupon&channel=huawei&device_token=&latitude=0.0&longitude=0.0&city_id=19&username=U47152310&pwd=f21ba1b845fcb48e28868c6d0f67d673&sign=a5be7367cc318a66a4644422492deb5f";
    public static CouponBrand parseCouponBrandJson(String json)
    {
        CouponBrand couponBrand = null;
        if(!TextUtils.isEmpty(json))
        {
            try
            {
                JSONArray jsonArray = new JSONArray(json);
                if(null != jsonArray)
                {
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    if(null != jsonObject)
                    {
                        JSONArray jArray = jsonObject.optJSONArray(TAG_SHOP_LIST);
                        if(null != jArray)
                        {
                            couponBrand = new CouponBrand();
                              int  shop_id=0, shop_action=0;
                              String shop_name=null, shop_icon=null , shop_url=null, tag_url=null;
                            for(int i = 0;i < jArray.length();i++)
                            {
                                JSONObject jObject = jArray.optJSONObject(i);
                                if(null != jObject)
                                {
                                    shop_id = jObject.optInt(TAG_SHOP_ID);
                                    shop_icon = jObject.optString(TAG_SHOP_ICON);
                                    shop_name = jObject.optString(TAG_SHOP_NAME);
                                    shop_action = jObject.optInt(TAG_SHOP_ACTION);
                                    shop_url = jObject.optString(TAG_SHOP_URL);
                                    tag_url = jObject.optString(TAG_TAG_URL);
                                }
                                if(i ==0)
                                {
                                    CouponBrand.MDL mdl = new CouponBrand.MDL();
                                    mdl.shop_action = shop_action;
                                    mdl.shop_icon = shop_icon;
                                    mdl.shop_id =shop_id;
                                    mdl.shop_name = shop_name;
                                    mdl.shop_url = shop_url;
                                    mdl.tag_url = tag_url;
                                   couponBrand.mdl = mdl; 
                                    
                                }
                                if(i ==1)
                                {
                                    CouponBrand.KFC kfc = new CouponBrand.KFC();
                                    kfc.shop_action = shop_action;
                                    kfc.shop_icon = shop_icon;
                                    kfc.shop_id =shop_id;
                                    kfc.shop_name = shop_name;
                                    kfc.shop_url = shop_url;
                                    kfc.tag_url = tag_url;
                                    couponBrand.kfc = kfc;
                                }
                                if(i ==2)
                                {
                                    CouponBrand.ZGF zgf = new CouponBrand.ZGF();
                                    zgf.shop_action = shop_action;
                                    zgf.shop_icon = shop_icon;
                                    zgf.shop_id =shop_id;
                                    zgf.shop_name = shop_name;
                                    zgf.shop_url = shop_url;
                                    zgf.tag_url = tag_url;
                                    couponBrand.zgf = zgf;
                                }
                                if(i ==3)
                                {
                                    CouponBrand.YHDW yhdw = new CouponBrand.YHDW();
                                    yhdw.shop_action = shop_action;
                                    yhdw.shop_icon = shop_icon;
                                    yhdw.shop_id =shop_id;
                                    yhdw.shop_name = shop_name;
                                    yhdw.shop_url = shop_url;
                                    yhdw.tag_url = tag_url;
                                    couponBrand.yhdw = yhdw;
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return  couponBrand;
    }
}
