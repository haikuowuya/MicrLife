package org.fs.net.evoke.data;

import android.net.Uri;

import org.fs.net.evoke.DownloadManager;
import org.fs.net.evoke.util.StringUtility;
import java.io.File;

/**
 * Created by Fatih on 31/01/15.
 * as org.fs.net.evoke.data.RequestObject
 */
public final class RequestObject {
    
    private final String urlString;
    private final File   moveTo;
    private final long   limit;
    private final DownloadManager.RequestCallback callback;

    private RequestObject(final String urlString, final File moveTo, final long limit, final DownloadManager.RequestCallback callback) {
        this.urlString = urlString;
        this.moveTo = moveTo;
        this.limit = limit;
        this.callback = callback;
    }

    public String getUrlString() {
        return urlString;
    }

    public File getMoveTo() {
        return moveTo;
    }

    public long getLimit() {
        return limit;
    }

    public Builder newBuilder() {
        return new Builder()
                    .urlString(urlString)
                    .moveTo(moveTo)
                    .limit(limit)
                    .requestCallback(callback);                        
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                urlString                 + ","  +
                hashCode()                + ","  +
                (moveTo == null ? "" : moveTo.toString()) + ","  +
                (callback == null ? "" : callback.toString()) + "," +
                limit                     + " }";
    }

    public static class Builder {
        String urlString = null;
        File   moveTo    = null;
        long   limit     = 0;
        DownloadManager.RequestCallback callback = null;
        
        public Builder() { }
        
        public Builder urlString(String urlString) {
            if(StringUtility.isNullOrEmpty(urlString)) {
               throw new IllegalArgumentException("urlString can not null"); 
            }
            
            Uri uri = Uri.parse(urlString);
            String scheme = uri.getScheme();
            if(!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) {
                throw new IllegalArgumentException("only http or https scheme acceptable.");
            }
            this.urlString = urlString;
            return this;
        }
        
        public Builder requestCallback(DownloadManager.RequestCallback callback) {
            this.callback = callback;
            return this;
        }
        
        public Builder moveTo(File moveTo) {
            this.moveTo = moveTo;
            return this;
        }
        
        public Builder limit(long limit) {
            this.limit = limit;
            return this;
        }
        
        public RequestObject build() {
            return new RequestObject(urlString, moveTo, limit, callback);            
        }
    }
}
