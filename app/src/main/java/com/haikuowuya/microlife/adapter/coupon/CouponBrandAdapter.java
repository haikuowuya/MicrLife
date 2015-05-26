package com.haikuowuya.microlife.adapter.coupon;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haikuowuya.microlife.CityActivity;
import com.haikuowuya.microlife.Constants;
import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.adapter.city.CityAdapterDataObserver;
import com.haikuowuya.microlife.mvp.model.CityItem;
import com.haikuowuya.microlife.mvp.model.CouponBrand;
import com.haikuowuya.microlife.util.CityUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by raiyi-suzhou on 2015/5/14 0014.
 */
public class CouponBrandAdapter extends RecyclerView.Adapter<CouponBrandAdapter.ViewHolder>
{
    private static final int VIEW_TYPE_LOCATION = 1;
    private static final int VIEW_TYPE_DEFAULT = 2;
    private List<CouponBrand.BaseItem> mData;
    

    public CouponBrandAdapter( List<CouponBrand.BaseItem> data)
    {
        mData = data;
        setHasStableIds(true);
    }
   

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        //TODO
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_coupon_brand_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i)
    {
        
        viewHolder.textView.setText(mData.get(i).shop_name);
        viewHolder.viewContainer.setOnClickListener(new OnClickListenerImpl());
        ImageLoader.getInstance().loadImage(mData.get(i).shop_icon, new ImageLoadingListener()
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
                viewHolder.imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view)
            {

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position)
    {
       
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public long getItemId(int position)
    {
       
        return position;
    }

    
    private class OnClickListenerImpl implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            
            
        }
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;
        private ImageView imageView ;
        private View viewContainer;
       
        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_text);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            viewContainer = itemView;
           
        }
    }
 
}
