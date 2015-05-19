package com.haikuowuya.microlife.adapter.city;

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
import com.haikuowuya.microlife.model.CityItem;

import java.util.List;

/**
 * Created by raiyi-suzhou on 2015/5/14 0014.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>
{
    private static final int VIEW_TYPE_LOCATION = 1;
    private static final int VIEW_TYPE_DEFAULT = 2;
    private List<CityItem> mData;
    private CityActivity mActivity;

    public CityAdapter(CityActivity activity, List<CityItem> data)
    {
        mActivity = activity;
        mData = data;
        setHasStableIds(true);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        //TODO
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_city_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i)
    {
        viewHolder.frameLocationContainer.setVisibility(View.GONE);
        viewHolder.textView.setText(mData.get(i).getsName());
        if (getItemViewType(i) == VIEW_TYPE_LOCATION)
        {
            viewHolder.textView.setText(getLocationCity());
            viewHolder.frameLocationContainer.setVisibility(View.VISIBLE);
            viewHolder.tvLocation.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    viewHolder.frameLocationContainer.getChildAt(1).setVisibility(View.VISIBLE);
                    viewHolder.tvLocation.setVisibility(View.GONE);
                    mActivity.startLocation();
                }
            });
        }
        viewHolder.frameLocationContainer.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0)
        {
            return VIEW_TYPE_LOCATION;
        }
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public long getItemId(int position)
    {
        if (getItemViewType(position) == VIEW_TYPE_LOCATION)
        {
            return -1;
        }
        return position;
    }

    public String getHeaderText(int position)
    {
        String text = mData.get(position).getKey();
        if(text.length() > 2)
        {
            text = text.substring(0,2);
        }
        return text;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;
        private TextView tvLocation;
        private ImageView ivImageView;
        private FrameLayout frameLocationContainer;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_text);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            frameLocationContainer = (FrameLayout) itemView.findViewById(R.id.frame_location_container);
            ivImageView = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    private String getLocationCity()
    {
        String locationCity = Constants.DEFAULT_CITY;

        if (mActivity.getPreferences().contains(Constants.PREF_LOCATION_CITY))
        {
            locationCity = mActivity.getPreferences().getString(Constants.PREF_LOCATION_CITY, locationCity);
        }
        return locationCity;
    }
}
