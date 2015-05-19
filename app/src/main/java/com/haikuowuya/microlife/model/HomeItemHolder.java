package com.haikuowuya.microlife.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class HomeItemHolder  extends BaseResponseItem {
    /**
     * 首页广告弹出框数据
     */
    public List<HomeItem> data5003;
    /**
     * 退屏活动
     */
    public List<HomeItem> data5004;

    /**
     * 启动页数据
     */
    public List<HomeItem> data5005;
    /**
     * 掌厅配置
     */
    public List<HomeItem> data5201;

    /**
     * 各模块是否发送服务器统计开关
     */
    public List<HomeItem> data2000;

    /**
     * 数据监听
     */
    public List<HomeItem> data1001;

    public LinkedList<HomeItem> data5100;
    public LinkedList<HomeItem> data5200;
    public LinkedList<HomeItem> data5300;
    public LinkedList<HomeItem> data5400;
    public LinkedList<HomeItem> data5500;
    public LinkedList<HomeItem> data5600;
    public LinkedList<HomeItem> data5700;
    public LinkedList<HomeItem> data5800;
    public LinkedList<HomeItem> data5900;
    public LinkedList<HomeItem> data6000;

    public List<HomeItem> getData1001() {
        return data1001;
    }

    public void setData1001(List<HomeItem> data1001) {
        this.data1001 = data1001;
    }

    public List<HomeItem> getData2000() {
        return data2000;
    }

    public void setData2000(List<HomeItem> data2000) {
        this.data2000 = data2000;
    }

    public List<HomeItem> getData5201() {
        return data5201;
    }

    public void setData5201(List<HomeItem> data5201) {
        this.data5201 = data5201;
    }

    public List<HomeItem> getData5003() {
        return data5003;
    }

    public void setData5003(List<HomeItem> data5003) {
        this.data5003 = data5003;
    }

    public List<HomeItem> getData5004() {
        return data5004;
    }

    public void setData5004(List<HomeItem> data5004) {
        this.data5004 = data5004;
    }

    public List<HomeItem> getData5005() {
        return data5005;
    }

    public void setData5005(List<HomeItem> data5005) {
        this.data5005 = data5005;
    }

    @Override
    public String toString()
    {
        return   " data5100 = "+data5100
                +" \ndata5200 = "+data5200
                +" \ndata5300 = "+data5300
                +" \ndata5400 = "+data5400
                +" \ndata5500 = "+data5500
                +" \ndata5600 = "+data5600
                +" \ndata5700 = "+data5700
                +" \ndata5800 = "+data5800
                +" \ndata5900 = "+data5900
                +" \ndata6000 = "+data6000;
    }
}

