package com.haikuowuya.microlife.mvp.view;

/**
 * Created by raiyi-suzhou on 2015/5/14 0014.
 */
public interface  CityView    extends  BaseView
{

    public void onInsertSuccess();
    public void showCityChangedDialog(String locationCityName, String currentCityName);


}
