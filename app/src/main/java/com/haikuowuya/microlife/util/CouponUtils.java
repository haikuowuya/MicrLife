package com.haikuowuya.microlife.util;

import android.text.TextUtils;
import android.view.TextureView;

import com.haikuowuya.microlife.mvp.model.CouponBrand;

/**
 * Created by raiyi-suzhou on 2015/5/26 0026.
 */
public class CouponUtils
{
    public static final String COUPON_BRAND_URL="http://mk.2000tuan.com/coupon4/view/getshoplist.php?imei=358094050288555&imsi=460015072618804&mac=e0%3A63%3Ae5%3A9c%3Acc%3A8f&platform=Android&platform_model=C6902&version=4100&system_version=4.4.4&app_name=cn.buding.coupon&channel=huawei&device_token=&latitude=0.0&longitude=0.0&city_id=19&username=U47152310&pwd=f21ba1b845fcb48e28868c6d0f67d673&sign=a5be7367cc318a66a4644422492deb5f";
    public static CouponBrand parseCouponBrandJson(String json)
    {
        CouponBrand couponBrand = null;
        if(!TextUtils.isEmpty(json))
        {

        }
        return  couponBrand;

    }
}
