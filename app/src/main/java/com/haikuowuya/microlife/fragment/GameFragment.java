package com.haikuowuya.microlife.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.adapter.game.DownloadAdapter;
import com.haikuowuya.microlife.adapter.home.HomePagerAdapter;
import com.haikuowuya.microlife.base.BaseFragment;
import com.haikuowuya.microlife.model.HomeItemHolder;
import com.haikuowuya.microlife.util.HomeItemUtils;
import com.haikuowuya.microlife.util.ToastUtils;
import com.haikuowuya.microlife.view.InfiniteViewPagerIndicatorView;
import com.novoda.downloadmanager.lib.DownloadManager;
import com.novoda.downloadmanager.lib.Request;

import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by raiyi-suzhou on 2015/5/19 0019.
 */
public class GameFragment extends BaseFragment
{

    private DownloadManager mDownloadManager;

    public static GameFragment newInstance()
    {
        GameFragment homeFragment = new GameFragment();
        return  homeFragment;
    }

    private ListView mListView;
    private PtrClassicFrameLayout mPtrContainer;

    private LinkedList<DownloadAdapter.DownloadItem> mItems = new LinkedList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         View view = inflater.inflate(R.layout.fragment_game, null);
        initView(view);
        mIsInit = true;
        return  view;
    }

    private void initView(View view)
    {
        mPtrContainer = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_container);
        mListView = (ListView) view.findViewById(R.id.lv_listview);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mDownloadManager = new DownloadManager(mActivity.getContentResolver());
        mListView.setOnItemClickListener(new OnItemClickListenerImpl());

        mPtrContainer.setPtrHandler(new PtrHandler()
        {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1)
            {
                return  mListView.getFirstVisiblePosition() ==0;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout ptrFrameLayout)
            {

                ptrFrameLayout.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ptrFrameLayout.refreshComplete();
                        handleHomeItem();
                    }
                }, 2000L);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (getUserVisibleHint())
        {
            if (mIsInit)
            {
                mIsInit = false;
                 handleHomeItem();
            }
        }
    }
    private void handleHomeItem()
    {
        ToastUtils.showCrouton(mActivity,"加载数据中……", mActivity.getContentViewGroup());
        mListView.setAdapter(new DownloadAdapter(mActivity, addDownloadData()));
    }

    private LinkedList<DownloadAdapter.DownloadItem> addDownloadData()
    {
        LinkedList<DownloadAdapter.DownloadItem> items = new LinkedList<>();
        for(int i = 0;i < 5;i++)
        {
            DownloadAdapter.DownloadItem item = new DownloadAdapter.DownloadItem();
            item.title = "网易新闻" + (1+mItems.size()+i);
            item.fileName = " 网易新闻"+ (1+mItems.size()+i)+".apk";
            item.url = "http://gdown.baidu.com/data/wisegame/91e040161bec41b1/wangyixinwen_405.apk";
            item.desc ="网易新闻是一个新闻客户端";
            items.add(item);
        }
        mItems.addAll(items);
        return mItems;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            if (mIsInit)
            {
                mIsInit = false;
                handleHomeItem();
            }
        }
    }

    /**添加一个进度圈表示获取数据中*/
    private ProgressBar addProgressBar(FrameLayout frameLayout)
    {
          ProgressBar   mProgressBar = new ProgressBar(mActivity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        frameLayout.addView(mProgressBar, params);
        mProgressBar.setVisibility(View.GONE);
        return mProgressBar;
    }

    @Override
    public String getFragmentTitle()
    {
        return "游戏";
    }
    private class  OnItemClickListenerImpl  implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            startDownload(position);

        }
    }

    private long  startDownload(int positionInAdapter)
    {
        DownloadAdapter.DownloadItem item = mItems.get(positionInAdapter);
        mItems.get(positionInAdapter).isBeginDownload = true;
        if(positionInAdapter >= mListView.getFirstVisiblePosition() &&
                positionInAdapter <= mListView.getLastVisiblePosition()) {
            int positionInListView = positionInAdapter - mListView.getFirstVisiblePosition();
            ProgressBar progressBar = (ProgressBar) mListView.getChildAt(positionInListView)
                    .findViewById(R.id.pb_download_progress);
            progressBar.setVisibility(View.VISIBLE);
        }
        Uri uri = Uri.parse(item.url);
        final Request request = new Request(uri);
        request.setDestinationInInternalFilesDir(mActivity, Environment.DIRECTORY_DOWNLOADS, item.fileName);
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(item.title);
        request.setDescription(item.desc);
        request.setMimeType("application/vnd.android");
        long startId =  mDownloadManager.enqueue(request);
        return  startId;
    }
}
