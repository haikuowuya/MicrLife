package com.haikuowuya.microlife.adapter.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.model.HomeItem;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.LinkedList;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class HomePagerAdapter    extends PagerAdapter
{
    private Context mContext;
    private LinkedList<HomeItem> mData;
    private ImageLoader mImageLoader;
    public HomePagerAdapter(Context context, LinkedList<HomeItem> data)
    {
        mContext = context;
        mData = data;
        mImageLoader = new ImageLoader(Volley.newRequestQueue(context), new ImageCacheImpl())  ;

    }
    @Override
    public int getCount()
    {
        return null ==mData ?0:mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view ==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        NetworkImageView imageView = new NetworkImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        imageView.setDefaultImageResId(R.mipmap.ic_image1);
//        imageView.setErrorImageResId(R.mipmap.ic_image3);
//        imageView.setImageUrl(mData.get(position).img, mImageLoader);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(mData.get(position).img, new ImageLoadingListener()
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

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view)
            {

            }
        });
        return  imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }
    private class ImageCacheImpl implements  ImageLoader.ImageCache
    {
           private LruCache<String,Bitmap> mLruCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory()/8));
        @Override
        public Bitmap getBitmap(String url)
        {
            return mLruCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap)
        {
               mLruCache.put(url,bitmap);
        }
    }
}
