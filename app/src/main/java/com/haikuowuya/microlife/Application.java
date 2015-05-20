package com.haikuowuya.microlife;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class Application extends android.app.Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        DisplayImageOptions options = new DisplayImageOptions.Builder().delayBeforeLoading(50).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).threadPriority(Thread.NORM_PRIORITY).threadPoolSize(4).denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache()).discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).imageDownloader(new BaseImageDownloader(this, 30 * 1000, 30 * 1000)).build();
        ImageLoader.getInstance().init(configuration);

    }

}
