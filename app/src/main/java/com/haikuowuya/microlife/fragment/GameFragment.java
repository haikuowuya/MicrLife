package com.haikuowuya.microlife.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.adapter.game.DownloadAdapter;
import com.haikuowuya.microlife.base.BaseFragment;
import com.haikuowuya.microlife.util.ToastUtils;

import org.fs.net.evoke.DownloadManager;
import org.fs.net.evoke.data.RequestObject;

import java.util.HashMap;
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
        return homeFragment;
    }

    private ListView mListView;
    private PtrClassicFrameLayout mPtrContainer;
    private SparseIntArray mDownloadPosition = new SparseIntArray();
    private HashMap<Long, Integer> mDownloadPositionMap = new HashMap<>();

    private LinkedList<DownloadAdapter.DownloadItem> mItems = new LinkedList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_game, null);
        initView(view);
        mIsInit = true;
        return view;
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
        mDownloadManager = DownloadManager.getInstance(mActivity);
        mListView.setOnItemClickListener(new OnItemClickListenerImpl());
        mPtrContainer.setPtrHandler(new PtrHandler()
        {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1)
            {
                return mListView.getFirstVisiblePosition() == 0;
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
        ToastUtils.showCrouton(mActivity, "加载数据中……", mActivity.getContentViewGroup());
        mListView.setAdapter(new DownloadAdapter(mActivity, addDownloadData()));
    }

    private LinkedList<DownloadAdapter.DownloadItem> addDownloadData()
    {
        LinkedList<DownloadAdapter.DownloadItem> items = new LinkedList<>();
        for (int i = 0; i < 5; i++)
        {
            DownloadAdapter.DownloadItem item = new DownloadAdapter.DownloadItem();
            item.title = "网易新闻" + (1 + mItems.size() + i);
            item.fileName = " 网易新闻" + (1 + mItems.size() + i) + ".apk";
            item.url = "http://gdown.baidu.com/data/wisegame/91e040161bec41b1/wangyixinwen_405.apk";
            item.desc = "网易新闻是一个新闻客户端";
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

    /**
     * 添加一个进度圈表示获取数据中
     */
    private ProgressBar addProgressBar(FrameLayout frameLayout)
    {
        ProgressBar mProgressBar = new ProgressBar(mActivity);   /**/
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        frameLayout.addView(mProgressBar, params);
        mProgressBar.setVisibility(View.GONE);
        return mProgressBar;
    }

    @Override
    public String getFragmentTitle()
    {
        return "游戏";
    }

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            startDownload(position);
        }
    }

    private long startDownload(int positionInAdapter)
    {
        mItems.get(positionInAdapter).isBeginDownload = true;
        RequestObject.Builder builder = new RequestObject.Builder().urlString(mItems.get(positionInAdapter).url);
        final long startId = mDownloadManager.enqueue(builder.build());
        mDownloadManager.registerCallback(new SubRequestCallback( ));
        mDownloadPositionMap.put(startId, positionInAdapter);
        return startId;

    }
    class SubRequestCallback extends  DownloadManager.RequestCallback
    {


        public SubRequestCallback( )
        {

        }
        int  progress;

        public void onProgress(long id, long sofar, long total)
        {
            if (total != 0)
            {
                boolean newPercentage = progress != sofar * 100 / total;
                if (newPercentage)
                {
                    progress = (int) (sofar * 100 / total);
                    System.out.println("" + String.format("request : %d\tprogress: %d\t total : %s", id, progress,total+""));
                    int  mPositionInAdapter = mDownloadPositionMap.get(id);
                    mItems.get(mPositionInAdapter).progress = progress;
                    if (mPositionInAdapter >= mListView.getFirstVisiblePosition() && mPositionInAdapter <= mListView.getLastVisiblePosition())
                    {
                        int positionInListView = mPositionInAdapter - mListView.getFirstVisiblePosition();
                       final  ProgressBar progressBar = (ProgressBar) mListView.getChildAt(positionInListView).findViewById(R.id.pb_download_progress);
                        final TextView textView = (TextView) mListView.getChildAt(positionInListView).findViewById(R.id.tv_progress);
                        progressBar.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress(progress);
                            }
                        });
                        textView.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                textView.setText(""+ progress);
                            }
                        });
                    }
                }
            }
        }
    }
}

