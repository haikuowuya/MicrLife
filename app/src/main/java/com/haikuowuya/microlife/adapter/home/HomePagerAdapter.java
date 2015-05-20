package com.haikuowuya.microlife.adapter.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haikuowuya.microlife.model.HomeItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class HomePagerAdapter extends PagerAdapter
{
    private Context mContext;
    private LinkedList<HomeItem> mData;

    public HomePagerAdapter(Context context, LinkedList<HomeItem> data)
    {
        mContext = context;
        mData = data;


    }

    @Override
    public int getCount()
    {
        return null == mData ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {

        final ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
         String img = mData.get(position).img;
         ImageLoader.getInstance().loadImage( img, new ImageLoadingListener()
        {
            @Override
            public void onLoadingStarted(String imageUri, View view)
            {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason)
            {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view)
            {

            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

}
