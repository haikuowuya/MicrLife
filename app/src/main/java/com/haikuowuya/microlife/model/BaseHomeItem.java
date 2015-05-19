package com.haikuowuya.microlife.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class BaseHomeItem  implements Parcelable
{
    public String mKey;
    public String id;
    public String name;
    public String url;
    public String title;
    public String param;

    // 以下字段用作统计
    // 序号
    public int sort;
    // 位置编号
    public int positionNo;
    // 日志开关, 1:关， 2：开
    public int status;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mKey);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(param);
        dest.writeInt(sort);
        dest.writeInt(positionNo);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public BaseHomeItem(){

    }

    public BaseHomeItem(Parcel source) {
        id = source.readString();
        mKey = source.readString();
        name = source.readString();
        url = source.readString();
        title = source.readString();
        param = source.readString();
        sort = source.readInt();
        positionNo = source.readInt();
        status = source.readInt();
    }

    public static final Parcelable.Creator<BaseHomeItem> CREATOR = new Creator<BaseHomeItem>() {
        public BaseHomeItem createFromParcel(Parcel source) {
            return new BaseHomeItem(source);
        }

        public BaseHomeItem[] newArray(int size) {
            return new BaseHomeItem[size];
        }
    };
}
