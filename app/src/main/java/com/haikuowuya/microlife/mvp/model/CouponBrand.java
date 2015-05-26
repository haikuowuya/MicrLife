package com.haikuowuya.microlife.mvp.model;

import java.io.Serializable;

/**
 * Created by raiyi-suzhou on 2015/5/26 0026.
 */
public class CouponBrand  implements Serializable
{
    public  MDL mdl;
    public KFC kfc;
    public ZGF zgf;
    public YHDW yhdw;
    private static class BaseItem implements  Serializable
    {
        public int  shop_id;
        public String shop_name;
        public String shop_icon;
        public int  shop_action;
        public String shop_url;
        public String tag_url;
    }
    public static class  MDL  extends  BaseItem
    {

    }
    public static class KFC extends  BaseItem
    {

    }
    public static class ZGF extends  BaseItem
    {

    }
    public static class  YHDW extends  BaseItem
    {

    }
}
