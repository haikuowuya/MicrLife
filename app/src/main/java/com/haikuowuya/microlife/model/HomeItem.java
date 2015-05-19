package com.haikuowuya.microlife.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class HomeItem extends BaseHomeItem
{
    public String dealFlag;
    public String dealInfo;
    public String dealType;
    public String dealValue;
    public String filterUrl;

    public String img;
    public String intro;
    public String isComment;

    public int localImage = -1;

    // 200-工具栏
    public String androidIcon;
    public String iosIcon;
    public String smallIcon;
    public String isApk;// 是否是Apk
    public String apkUrl;// 下载地址
    public String apkPkg;// 包名
    public String apkCls;// 类名
    public String apkScheme;// 隐式启动
    public String isActive;//
    public String apkName;// 中文或英文名称

    // 201 -本地生活
    public String remark;

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getIsActive()
    {
        return isActive;
    }

    public void setIsActive(String isActive)
    {
        this.isActive = isActive;
    }

    public int getLocalImage()
    {
        return localImage;
    }

    public void setLocalImage(int localImage)
    {
        this.localImage = localImage;
    }

    public String getApkPkg()
    {
        return apkPkg;
    }

    public void setApkPkg(String apkPkg)
    {
        this.apkPkg = apkPkg;
    }

    public String getApkCls()
    {
        return apkCls;
    }

    public void setApkCls(String apkCls)
    {
        this.apkCls = apkCls;
    }

    public String getApkScheme()
    {
        return apkScheme;
    }

    public void setApkScheme(String apkScheme)
    {
        this.apkScheme = apkScheme;
    }

    public String getApkName()
    {
        return apkName;
    }

    public void setApkName(String apkName)
    {
        this.apkName = apkName;
    }

    public String getApkUrl()
    {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl)
    {
        this.apkUrl = apkUrl;
    }

    public String getIsApk()
    {
        return isApk;
    }

    public void setIsApk(String isApk)
    {
        this.isApk = isApk;
    }

    public String getDealFlag()
    {
        return dealFlag;
    }

    public void setDealFlag(String dealFlag)
    {
        this.dealFlag = dealFlag;
    }

    public String getDealInfo()
    {
        return dealInfo;
    }

    public void setDealInfo(String dealInfo)
    {
        this.dealInfo = dealInfo;
    }

    public String getDealType()
    {
        return dealType;
    }

    public void setDealType(String dealType)
    {
        this.dealType = dealType;
    }

    public String getDealValue()
    {
        return dealValue;
    }

    public void setDealValue(String dealValue)
    {
        this.dealValue = dealValue;
    }

    public String getFilterUrl()
    {
        return filterUrl;
    }

    public void setFilterUrl(String filterUrl)
    {
        this.filterUrl = filterUrl;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getIntro()
    {
        return intro;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    public String getIsComment()
    {
        return isComment;
    }

    public void setIsComment(String isComment)
    {
        this.isComment = isComment;
    }

    public String getAndroidIcon()
    {
        return androidIcon;
    }

    public void setAndroidIcon(String androidIcon)
    {
        this.androidIcon = androidIcon;
    }

    public String getIosIcon()
    {
        return iosIcon;
    }

    public void setIosIcon(String iosIcon)
    {
        this.iosIcon = iosIcon;
    }

    public String getSmallIcon()
    {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon)
    {
        this.smallIcon = smallIcon;
    }

    public HomeItem()
    {
        super();
    }

    public HomeItem(Parcel source)
    {
        super(source);
        dealInfo = source.readString();
        dealType = source.readString();
        dealValue = source.readString();
        dealFlag = source.readString();
        filterUrl = source.readString();

        img = source.readString();
        intro = source.readString();
        isComment = source.readString();
        localImage = source.readInt();

        androidIcon = source.readString();
        iosIcon = source.readString();
        smallIcon = source.readString();
        isApk = source.readString();
        apkUrl = source.readString();
        apkPkg = source.readString();
        apkCls = source.readString();
        apkScheme = source.readString();
        isActive = source.readString();
        remark = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);

        dest.writeString(dealFlag);
        dest.writeString(dealInfo);
        dest.writeString(dealType);
        dest.writeString(dealValue);
        dest.writeString(filterUrl);

        dest.writeString(img);
        dest.writeString(intro);
        dest.writeString(isComment);

        dest.writeInt(localImage);

        dest.writeString(androidIcon);
        dest.writeString(iosIcon);
        dest.writeString(smallIcon);
        dest.writeString(isApk);
        dest.writeString(apkUrl);
        dest.writeString(apkPkg);
        dest.writeString(apkCls);
        dest.writeString(apkScheme);
        dest.writeString(isActive);
        dest.writeString(remark);

    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public static final Parcelable.Creator<HomeItem> CREATOR = new Creator<HomeItem>()
    {
        public HomeItem createFromParcel(Parcel source)
        {
            return new HomeItem(source);
            // Advertisement model = new Advertisement();
            // // 从Parcelable读出
            // // id = source.readString();
            // // mKey = source.readString();
            // // name = source.readString();
            // // url = source.readString();
            // // title = source.readString();
            // // param = source.readString();
            // return model;
        }

        public HomeItem[] newArray(int size)
        {
            return new HomeItem[size];
        }
    };

    @Override
    public String toString()
    {

        return  "mKey = " + mKey + " img = " + img + " positionNo = " + positionNo + " url = " +url;
    }
}
