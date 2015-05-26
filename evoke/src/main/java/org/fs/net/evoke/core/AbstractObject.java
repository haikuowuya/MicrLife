package org.fs.net.evoke.core;

import android.util.Log;

import org.fs.net.evoke.util.LogUtil;

/**
 * Created by Fatih on 27/01/15.
 * as org.fs.net.evoke.core.AbstractObject
 */
public abstract class AbstractObject {

    /**
     * Debug level of log 
     * @param message
     */
    protected void log(String message) {
        log(Log.DEBUG, message);
    }

    /**
     * More detailed log for Error, Warn etc... 
     * @param priority
     * @param message
     */
    protected void log(int priority, String message) {
        if(isLogEnabled()) {
            String tag = getClassTag();
            if(null == tag) {
                LogUtil.log(priority, message);
            } else {
                LogUtil.log(priority, tag, message);
            }
        }
    }

    /**
     * Log is on or off 
     * @return
     */
    protected abstract boolean isLogEnabled();

    /**
     * String representation of the specific log
     * @return
     */
    protected abstract String  getClassTag();
}
