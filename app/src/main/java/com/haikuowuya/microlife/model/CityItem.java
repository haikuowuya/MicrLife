package com.haikuowuya.microlife.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by raiyi-suzhou on 2015/5/14 0014.
 */
public class CityItem extends RealmObject
{
    @PrimaryKey
    private int id_id;
    private int allletorder;
    private int allorder;
    private String areaCode;
    private int  cdmaId;
    private String ddId;
    private int id;
    private int level ;
    private String lngLat;
    private String name;
    private String sName;
    private String weatherId;
    private String wlId;
    private String key;

    public int getId_id()
    {
        return id_id;
    }

    public void setId_id(int id_id)
    {
        this.id_id = id_id;
    }

    public int getAllletorder()
    {
        return allletorder;
    }

    public int getAllorder()
    {
        return allorder;
    }

    public String getAreaCode()
    {
        return areaCode;
    }

    public int  getCdmaId()
    {
        return cdmaId;
    }

    public String getDdId()
    {
        return ddId;
    }

    public int getId()
    {
        return id;
    }

    public int getLevel()
    {
        return level;
    }

    public String getLngLat()
    {
        return lngLat;
    }

    public String getName()
    {
        return name;
    }

    public String getsName()
    {
        return sName;
    }

    public String getWeatherId()
    {
        return weatherId;
    }

    public String getWlId()
    {
        return wlId;
    }

    public String getKey()
    {
        return key;
    }

    public void setAllletorder(int allletorder)
    {
        this.allletorder = allletorder;
    }

    public void setAllorder(int allorder)
    {
        this.allorder = allorder;
    }

    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }

    public void setCdmaId(int  cdmaId)
    {
        this.cdmaId = cdmaId;
    }

    public void setDdId(String ddId)
    {
        this.ddId = ddId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setLngLat(String lngLat)
    {
        this.lngLat = lngLat;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setsName(String sName)
    {
        this.sName = sName;
    }



    public void setWeatherId(String weatherId)
    {
        this.weatherId = weatherId;
    }

    public void setWlId(String wlId)
    {
        this.wlId = wlId;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
