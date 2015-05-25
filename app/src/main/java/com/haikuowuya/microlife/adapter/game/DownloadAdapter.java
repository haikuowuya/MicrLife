package com.haikuowuya.microlife.adapter.game;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.base.BaseAdapter;
import com.novoda.downloadmanager.lib.DownloadManager;
import com.novoda.downloadmanager.lib.Query;
import com.novoda.downloadmanager.lib.Request;

import java.util.List;

/**
 * Created by raiyi-suzhou on 2015/5/25 0025.
 */
public class DownloadAdapter extends BaseAdapter<DownloadAdapter.DownloadItem>
{
    private Activity mActivity;

    public DownloadAdapter(Activity activity, List<DownloadItem> data)
    {
        super(activity, R.layout.list_download_item, data);
        mActivity = activity;

    }

    @Override
    public void bindDataToView(View convertView, final DownloadItem downloadItem)
    {
        setTextViewText(convertView, R.id.tv_download_title, downloadItem.title + "  ::  " + downloadItem.fileName);
        setTextViewText(convertView, R.id.tv_download_status, downloadItem.getDownloadStatusText());
        setViewVisibility(convertView, R.id.pb_download_progress, View.GONE);
        if (downloadItem.isBeginDownload)
        {
            setViewVisibility(convertView, R.id.pb_download_progress, View.VISIBLE);
        }
    }

    public static class DownloadItem
    {
        public String title;
        public String fileName;
        public int downloadStatus;
        public String url;
        public String desc;
        public boolean isBeginDownload;

        public DownloadItem()
        {
        }

        public String getDownloadStatusText()
        {
            if (downloadStatus == DownloadManager.STATUS_RUNNING)
            {
                return "Downloading";
            }
            else if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL)
            {
                return "Complete";
            }
            else if (downloadStatus == DownloadManager.STATUS_FAILED)
            {
                return "Failed";
            }
            else if (downloadStatus == DownloadManager.STATUS_PENDING)
            {
                return "Queued";
            }
            else if (downloadStatus == DownloadManager.STATUS_PAUSED)
            {
                return "Paused";
            }
            else
            {
                return "WTH";
            }
        }
    }
}
