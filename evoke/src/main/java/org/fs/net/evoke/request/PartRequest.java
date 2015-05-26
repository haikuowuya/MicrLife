package org.fs.net.evoke.request;

import android.util.Log;
import android.util.Pair;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.fs.net.evoke.DownloadManager;
import org.fs.net.evoke.data.PartObject;
import org.fs.net.evoke.listener.PartCallback;
import org.fs.net.evoke.th.AbstractRunnable;
import org.fs.net.evoke.util.LogUtil;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Fatih on 29/01/15.
 * as org.fs.net.evoke.request.PartRequest
 */
public class PartRequest extends AbstractRunnable {

    /**
     *  this should be executed in 60 secs.
     */
    
    private PartObject part;
    private PartCallback callback;

    private final int connectionTimeout = 10000;
    private final int readTimeout       = 25000;
    
    private OkHttpClient client  = new OkHttpClient();
    private OkUrlFactory factory = new OkUrlFactory(client);

    private Pair<String, String> userAgent = new Pair<>("User-Agent", "(evoke/1.0) Android OS");
    
    private final static int MAX_BUFFER = 1024 * 8;//read buffer 8 kb block...
    
    private final static String METHOD = "GET";
    private final static String RANGES = "Range";
    
    public PartRequest(final PartObject part, final PartCallback callback) {
        super("--GET with Range:bytes=%s", part.toString());
        this.part = part;
        this.callback = callback;
        System.setProperty("http.keepAlive", "false");
    }

    /**
     *
     * @return
     */
    protected boolean isLogEnabled() {
        return true;
    }

    /**
     *
     * @return
     */
    protected String getClassTag() {
        return HeadRequest.class.getSimpleName();
    }

    /**
     *
     * @param message
     */
    protected void log(String message) {
        log(Log.DEBUG, message);
    }

    /**
     *
     * @param priority
     * @param message
     */
    protected void log(int priority, String message) {
        if(isLogEnabled()) {
            LogUtil.log(priority, getClassTag(), message);
        }
    }

    /**
     * parse 
     * @return
     */
    private URL parse() {
        try {
            return new URL(part.getUrlString());
        } catch (Exception e) {
            if(isLogEnabled()) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 
     * @return
     */
    private HttpURLConnection open() {
        URL parse = parse();
        if(parse != null) {
            return factory.open(parse());
        }
        return null;
    }

    /**
     * Clear resources. 
     */
    private void clear() {
        part = null;
        callback = null;
        client  = null;
        factory = null;
    }
    
    @Override
    protected void execute() {
        if(Thread.interrupted()) {
            callback.onPartCompleted(DownloadManager.ERROR_PART_CANCELED, 0L, hashCode());
            clear();
            return;
        }
        HttpURLConnection connection = open();
        long total;
        try {
            connection.setRequestMethod(METHOD);
            connection.setRequestProperty(userAgent.first, userAgent.second);
            connection.setRequestProperty(RANGES, part.getRange());
            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setUseCaches(false);
            connection.setChunkedStreamingMode(0);
            connection.connect();
            int code = connection.getResponseCode();
            //only if server partial downloads.
            if(code == 206) {
                total = connection.getContentLength();
                FileOutputStream out = part.getFile().length() == 0
                                            ? new FileOutputStream(part.getFile())
                                            : new FileOutputStream(part.getFile(), true);
                
                InputStream is = new BufferedInputStream(connection.getInputStream());
                byte[] buffer = new byte[MAX_BUFFER];
                int size;
                while((size = is.read(buffer)) >= 0) {
                    out.write(buffer, 0, size);
                    callback.onPartCompleted(size, total, hashCode());
                }
                out.close();
                is.close();
            }
            else {
                callback.onPartCompleted(DownloadManager.ERROR_PART_SERVER_CODE, 0L, hashCode());
            }
            connection.disconnect();
            connection = null;
        } catch (Exception exp) {
            if(isLogEnabled()) {
                exp.printStackTrace();
                connection.disconnect();
            }
            callback.onPartCompleted(DownloadManager.ERROR_PART_UNKNOWN, 0L, hashCode());
        }
        clear();
    }
}
