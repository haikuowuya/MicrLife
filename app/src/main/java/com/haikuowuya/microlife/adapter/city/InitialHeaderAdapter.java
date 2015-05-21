package com.haikuowuya.microlife.adapter.city;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;
import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.mvp.model.CityItem;

import java.util.List;

/**
 * Created by raiyi-suzhou on 2015/5/14 0014.
 */
public class InitialHeaderAdapter  implements StickyHeadersAdapter<InitialHeaderAdapter.ViewHolder>
{
     private List<CityItem> mData;
    public InitialHeaderAdapter(List<CityItem> data)
    {
        mData = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_letter_header,viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
         viewHolder.tvLetter.setText(mData.get(i).getKey());
    }

    @Override
    public long getHeaderId(int i)
    {
        return  mData.get(i).getKey().charAt(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
         private TextView tvLetter;
        public ViewHolder(View itemView)
        {
            super(itemView);
            tvLetter = (TextView) itemView.findViewById(R.id.tv_letter_text);
        }
    }
}
