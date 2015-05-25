/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.novoda.downloadmanager.lib;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.novoda.notils.logger.simple.Log;

class RealSystemFacade implements SystemFacade {
    private Context mContext;

    public RealSystemFacade(Context context) {
        mContext = context;
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public NetworkInfo getActiveNetworkInfo(int uid) {
        // https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/net/ConnectivityManager.java#579
        throw new IllegalStateException("We can't implement this :-(");
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    @Override
    public boolean isActiveNetworkMetered() {
        ConnectivityManager conn = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return NetworkMeter.Factory.get(conn).isActiveNetworkMetered();
    }

    @Override
    public boolean isNetworkRoaming() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w("couldn't get connectivity manager");
            return false;
        }

        NetworkInfo info = connectivity.getActiveNetworkInfo();
        boolean isMobile = (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE);
        TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        boolean isRoaming = isMobile && telephony.isNetworkRoaming();
        if (isRoaming) {
            Log.v("network is roaming");
        }
        return isRoaming;
    }

    @Override
    public Long getMaxBytesOverMobile() {
        return DownloadManager.getMaxBytesOverMobile(mContext);
    }

    @Override
    public Long getRecommendedMaxBytesOverMobile() {
        return DownloadManager.getRecommendedMaxBytesOverMobile(mContext);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        mContext.sendBroadcast(intent);
    }

    @Override
    public boolean userOwnsPackage(int uid, String packageName) throws NameNotFoundException {
        return mContext.getPackageManager().getApplicationInfo(packageName, 0).uid == uid;
    }
}
