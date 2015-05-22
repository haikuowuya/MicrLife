package com.haikuowuya.microlife.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haikuowuya.microlife.MainActivity;
import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.WeatherActivity;
import com.haikuowuya.microlife.base.BaseAdapter;
import com.haikuowuya.microlife.base.BaseFragment;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by raiyi-suzhou on 2015/5/12 0012.
 */
public class MenuFragment extends BaseFragment
{
    private ListView mListView;
    public static MenuFragment newInstance()
    {
        MenuFragment fragment = new MenuFragment();
        return  fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_menu, null);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        mListView = (ListView) view.findViewById(R.id.lv_listview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mListView.setAdapter(new MenuAdapter());
        mListView.setOnItemClickListener(new OnItemClcikListenerImpl());
    }

    @Override
    public String getFragmentTitle()
    {
        return "左菜单";
    }

    private class MenuAdapter extends  BaseAdapter<ListItem>
    {

        protected MenuAdapter(   )
        {
            super(mActivity,R.layout.list_menu_item, genData());
        }

        @Override
        public void bindDataToView(View convertView, ListItem listItem)
        {
            setTextViewText(convertView,R.id.tv_text,listItem.name);
            setImageViewResId(convertView,R.id.iv_image,listItem.resId);
        }
    }

    private   List<ListItem> genData()
    {
        List<ListItem> data = new LinkedList<>();
        for(int i = 0;i < 10;i++)
        {
            ListItem item = new ListItem();
            item.name = "列表 " +(1+i);
            item.resId = R.mipmap.ic_launcher;
            data.add(item);
        }
        return data;
    }

    private class ListItem
    {
        public String name ;
        public int resId;
    }
    private class OnItemClcikListenerImpl implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            WeatherActivity.actionWeather(mActivity);

        }
    }
}
