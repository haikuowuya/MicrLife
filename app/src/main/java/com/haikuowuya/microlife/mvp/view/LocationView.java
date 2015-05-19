package com.haikuowuya.microlife.mvp.view;

import com.baidu.location.BDLocation;

/**
 * Created by raiyi-suzhou on 2015/5/15 0015.
 */
public interface LocationView
{
    public void startLocation();
    public void finishLocation(BDLocation bdLocation);
}
