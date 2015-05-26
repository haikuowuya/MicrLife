package org.fs.net.evoke;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.fs.net.evoke.data.RequestObject;
import org.fs.net.evoke.database.DatabaseHelper;
import org.fs.net.evoke.listener.HeadCallback;
import org.fs.net.evoke.request.HeadRequest;
import org.fs.net.evoke.util.Util;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fatih on 30/01/15.
 * as org.fs.net.evoke.DownloadManager
 */
public final class DownloadManager {
    
    //public final static String           ACTION_PROGRESS             = "org.fs.net.evoke.action.PROGRESS";
    //public final static String           ACTION_COMPLETE             = "org.fs.net.evoke.action.COMPLETE";
    //public final static String           ACTION_ERROR                = "org.fs.net.evoke.action.ERROR";
    
    public final static int              ERROR_SERVER_CODE           = 0 << 1;
    public final static int              ERROR_PARTIAL_NOT_SUPPORTED = 1 << 1;
    
    public final static int              ERROR_PART_SERVER_CODE      = 2 << 1;
    public final static int              ERROR_PART_CANCELED         = 3 << 1;
    public final static int              ERROR_PART_UNKNOWN          = 4 << 1;
    
    private final WeakReference<Context> contextRef;  
    private final static String          DOWNLOAD_FOLDER_NAME = "evoke";
    
    private static DownloadManager       instance = null;
    
    private final File                      downloadFolder;
    private final ExecutorService           executorService;
    private final Map<Integer, HeadRequest> taskPool;
    
    private final List<HeadRequest>         waitingPool;
    private final Map<Integer, HeadRequest> resumePool; 
    
    private final DatabaseHelper            databaseHelper;
    
    private RequestCallback callback;
    
    private DownloadManager(Context context) {
        if(context == null) {
            throw new IllegalArgumentException("Context can not be null.");
        }
        databaseHelper = DatabaseHelper.getInstance(context);
        downloadFolder = new File(context.getFilesDir(), DOWNLOAD_FOLDER_NAME);
        if(!downloadFolder.exists()) {
            downloadFolder.mkdirs();
        }
        //async task class def has initial 5 and max 128 so we provide this
        // initial of 5 ofc but 64 queue system for these background thread in our pool for performance concern
        //max alive time of its threads is 60 secs.
        executorService = new ThreadPoolExecutor(5, 64, 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    Util.threadFactory("(evoke/1.0) --HEAD threads", false));
        taskPool = new HashMap<>();
        resumePool = new HashMap<>();
        waitingPool = new ArrayList<>();
        contextRef = new WeakReference<>(context);
    }

    /**
     * Only way to create or get instance of download manager. 
     * @param context
     * @return
     */
    public static DownloadManager getInstance(Context context) {
        if(instance == null) {
            instance = new DownloadManager(context);
        }
        return instance;
    }

    /**
     * Stops if task is on the run mode and deletes all downloaded data so far. 
     * @param id
     */
    public void stop(int id) {
        if(taskPool.containsKey(id)) {
            HeadRequest request = taskPool.remove(id);
            request.destroy(true);
        }
    }
    
    public void registerCallback(RequestCallback callback) {
        this.callback = callback;        
    }
    

    /**
     * Start request
     * if pool is ok then start running
     * else 
     * @param requestObject
     */
    public long enqueue(RequestObject requestObject) {
        if(taskPool.size() > 5) {
            HeadRequest request = new HeadRequest(downloadFolder, headCallback, requestObject);
            waitingPool.add(request);
            return request.hashCode();
        }
        else {
            HeadRequest request = new HeadRequest(downloadFolder, headCallback, requestObject);
            long id = request.hashCode();
            taskPool.put((int)id, request);
            executorService.execute(request);
            return id;
        }
    }

    /**
     * pause running task and keep downloaded data 
     * @param id
     */
    public void pause(long id) {
        if(taskPool.containsKey(id)) {
            HeadRequest request = taskPool.remove(id);            
            request.destroy();
            resumePool.put((int)id, request);
        }        
    }

    /**
     * resumes paused task only
     * @param id
     */
    public void resume(long id) {
        if(resumePool.containsKey(id)) {
            HeadRequest request = resumePool.remove(id);
            taskPool.put((int)id, request);
            executorService.execute(request);
        }
    }
    
    private void removeAndEnqueue(int id) {
        taskPool.remove(id);
        if(waitingPool.size() > 0 && !(taskPool.size() > 5)) {
            HeadRequest request = waitingPool.remove(0);
            int newId = request.hashCode();
            taskPool.put(newId, request);
            executorService.execute(request);
        }        
    }
    
    private Uri toUri(File file) {
        if(file == null) {
            return null;
        }
        String urlString = file.toURI().toString();
        return Uri.parse(urlString);
    }

    public final static String EXTRA_ID                = "id";
    public final static String EXTRA_DOWNLOADED_SO_FAR = "downloaded.so.far";
    public final static String EXTRA_TOTAL             = "total";
    public final static String EXTRA_FILE_URL          = "uri";
    public final static String EXTRA_REMOTE_URL        = "remote";
    public final static String EXTRA_ERROR             = "error";

    /**
     * all callback will be remain here.
     */
    private final HeadCallback headCallback = new HeadCallback() {
        
        @Override
        public void onProgress(int id, long soFarDownloaded, long total) {
            //Bundle data = new Bundle();
            //data.putInt(EXTRA_ID, id);
            //data.putLong(EXTRA_DOWNLOADED_SO_FAR, soFarDownloaded);
            //data.putLong(EXTRA_TOTAL, total);

            if(callback != null) {
                //Intent d = new Intent(ACTION_PROGRESS);
                //d.putExtras(data);
                callback.onProgress(id, soFarDownloaded, total);
            }
        }

        @Override
        public void onComplete(int id, File file) {
            removeAndEnqueue(id);
            //Bundle data = new Bundle();
            //data.putInt(EXTRA_ID, id);
            //data.putString(EXTRA_FILE_URL, toUri(file).toString());

            if(callback != null) {
                //Intent d = new Intent(ACTION_COMPLETE);
                //d.putExtras(data);
                callback.onComplete(id, toUri(file).toString());
            }
        }

        @Override
        public void onError(int id, String urlString, int reason) {
            removeAndEnqueue(id);
            //Bundle data = new Bundle();
            //data.putInt(EXTRA_ID, id);
            //data.putString(EXTRA_REMOTE_URL, urlString);
            //data.putInt(EXTRA_ERROR, reason);
            
            if(callback != null) {
                //Intent d = new Intent(ACTION_ERROR);
                //d.putExtras(data);
                callback.onError(id, urlString, reason);
            }
        }
    };


    /**
     * Advised to use this, and you can put this into Bundle too.
     * * *
     * Have to add this because on some device excessive 
     * broadcast cause error. actually not error but I believe it might cause OutOfMemory Exception
     * because Memory consumption goes off the chart. 
     * */
    public static class RequestCallback implements Parcelable {

        public void onError(long id, String urlString, int reason) { }
        public void onProgress(long id, long sofar, long total) { }
        public void onComplete(long id, String toUri) { }

        /*These parts essential if you want to pass callback via intent*/
        @Override
        public int describeContents() { return 0; }
        @Override
        public void writeToParcel(Parcel dest, int flags) { }        
        public final static Creator<RequestCallback> CREATOR = new Creator<RequestCallback>() {
            @Override
            public RequestCallback createFromParcel(Parcel source) { return new RequestCallback(); }
            @Override
            public RequestCallback[] newArray(int size) { return new RequestCallback[size];  }
        };
    }
    
}
