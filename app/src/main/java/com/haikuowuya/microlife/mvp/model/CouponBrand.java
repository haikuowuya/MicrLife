package com.haikuowuya.microlife.mvp.model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by raiyi-suzhou on 2015/5/26 0026.
 */
public class CouponBrand  implements Serializable
{
    public  MDL mdl;
    public KFC kfc;
    public ZGF zgf;
    public YHDW yhdw;
    public LinkedList<BaseItem> toList()
    {
        LinkedList<BaseItem> data = new LinkedList<>();
        data.add(mdl);
        data.add(kfc);
        data.add(zgf);
        data.add(yhdw);
        return  data;
                
    }

    @Override
    public String toString()
    {
        return  "MDL = " + mdl.shop_name + " KFC = " + kfc.shop_name + " ZGF = " + zgf.shop_name + " YHDW = " +yhdw.shop_name;
    }

    public static class BaseItem implements  Serializable
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
