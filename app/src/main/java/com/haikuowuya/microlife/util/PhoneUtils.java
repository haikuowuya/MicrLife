package com.haikuowuya.microlife.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class PhoneUtils
{
    public static final String REGEX_PHONENUMBER = "^1[3|4|5|6|7|8|9][0-9]{9}$";
        /**
         * 判断手机号码是够是中国联通号码
         *
         * @param phoneNumber
         * @return
         */
        public static boolean isCUTC(String phoneNumber) {
            if (TextUtils.isEmpty(phoneNumber))
                return false;
            if (phoneNumber.startsWith("130") || phoneNumber.startsWith("131") || phoneNumber.startsWith("132") || phoneNumber.startsWith("155") || phoneNumber.startsWith("156")
                    || phoneNumber.startsWith("185") || phoneNumber.startsWith("186")) {
                return true;
            }
            return false;

        }

        /**
         *
         * 判断运营商
         *
         */
        public static String getOperatorName(Context ct) {
            TelephonyManager telephonyManager = (TelephonyManager) ct.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String operator = telephonyManager.getSimOperator();
                if (operator != null) {
                    operator = operator.trim();
                    return operator;
                /*
                 * if (operator.equals("46000") || operator.equals("46002")) {
                 * //"中国移动" return 1; } else if (operator.equals("46001"))
                 * {//"中国联通" return 2; } else if (operator.equals("46003"))
                 * {//"中国电信" return 3;
                 *
                 * }
                 */
                }
            }
            return null;
        }


        /**
         * 判断当前网络是否是3G网络
         *
         * @param context
         * @return boolean
         */
        public boolean is3G(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
            return false;
        }

        // 判断是否有网络连接
        public static boolean isNetworkConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        // 判断WIFI网络是否可用
        public static boolean isWifiConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWiFiNetworkInfo != null) {
                    return mWiFiNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        // 判断MOBILE网络是否可用
        public static boolean isMobileConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mMobileNetworkInfo != null) {
                    return mMobileNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        //判断是否3G网络
        public static boolean is3GNetwork(Context context) {
            ConnectivityManager cManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cManager.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState().name().equalsIgnoreCase("connected") && info.getTypeName().equalsIgnoreCase("mobile")) {
                    return true;
                }
            }
            return false;
        }

        public static boolean isChinaNet(Context context) {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Log.e("china_net", "" + tm.getNetworkOperator());
            return "46003".equals(tm.getNetworkOperator());
        }

    // 得到硬件的DeviceID
    public static String getDeviceID(Context context) {
        return  getMobileUUID(context);
    }
    public static String getMobileUUID(Context context) {
        return "v2_" +getDeviceUUID(context);
    }

    // 得到硬件的UUID，全球唯一
    public static String getDeviceUUID(Context context) {
        SharedPreferences settings = context.getSharedPreferences("APP_SETTING", Context.MODE_PRIVATE);
        if (settings != null && settings.contains("UNION_DEVICEID_W")) {
            String dvid = settings.getString("UNION_DEVICEID_W", "");
            if (dvid != null && dvid.length() > 0) {
                return dvid;
            }
        }
        // String uniqueId = ("" + getMacAddress(context)).trim();
        String uniqueId = getDeviceInfo() + getSerialno();
        String did = MD5Utils.EncoderByMd5(uniqueId);
        settings.edit().putString("UNION_DEVICEID_W", did).commit();
        return did;
    }

    private static String getSerialno() {
        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
            // Log.d("ZZZ", "serial : " + serialnum);
        } catch (Exception ignored) {
            serialnum = null;
        }
        if (TextUtils.isEmpty(serialnum) || "unknown".equalsIgnoreCase(serialnum)) {
            serialnum = "";
        }
        String serialnum2 = null;
        try {
            @SuppressWarnings("rawtypes")
            Class myclass = Class.forName("android.os.SystemProperties");
            Method[] methods = myclass.getMethods();
            Object[] params = new Object[] { new String("ro.serialno"), new String("Unknown") };
            serialnum2 = (String) (methods[2].invoke(myclass, params));
            // Log.d("ZZZ", "serial2 : " + serialnum2);
        } catch (Exception ignored) {
            serialnum2 = null;
        }
        if (TextUtils.isEmpty(serialnum2) || "unknown".equalsIgnoreCase(serialnum2) || serialnum2.equalsIgnoreCase(serialnum)) {
            serialnum2 = "";
        }
        String data = serialnum + serialnum2;
        return data;
    }

    public static String getDeviceInfo() {
        String m_szDevIDShort = android.os.Build.BOARD + android.os.Build.BRAND + android.os.Build.CPU_ABI + android.os.Build.DEVICE + android.os.Build.DISPLAY + android.os.Build.HOST
                + android.os.Build.MANUFACTURER + android.os.Build.MODEL + android.os.Build.PRODUCT + android.os.Build.TYPE + android.os.Build.USER; // 13
        // digits
        return m_szDevIDShort;
    }

    // 获得基站信息
    public String getCdmaLocation(Context context) {
        TelephonyManager teleMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CellLocation cellLocation = teleMgr.getCellLocation();
        String retu = "";
        if (cellLocation instanceof CdmaCellLocation) {
            CdmaCellLocation cdmaLoc = (CdmaCellLocation) cellLocation;
            retu = cdmaLoc.getBaseStationId() + ":" + cdmaLoc.getSystemId() + ":" + cdmaLoc.getNetworkId();
        } else {
            GsmCellLocation gsmLocation = (GsmCellLocation) cellLocation;
            if (gsmLocation != null) {
            }
        }
        return retu;
    }

    // 获取物理地址信息
    public static String getMacAddress(Context context) {
        WifiManager wManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wManager.getConnectionInfo();
        return "MAC:" + wInfo.getMacAddress();
    }

    // 得到物理系统的imsi
    public static String getSubscriberID(Context context) {
        TelephonyManager teleMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = teleMgr.getSubscriberId();
        return imsi;
    }

    // 得到手机型号
    public static String getMtype(Context context) {
        String mtype = android.os.Build.MODEL;
        return mtype;
    }

    // 得到IMEI
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    // 手机号码，有的可得，有的不可得
    public static String getLine1Number(Context context) {
        TelephonyManager teleMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String numer = teleMgr.getLine1Number();
        if (TextUtils.isEmpty(numer))
            return null;
        if (numer.matches(REGEX_PHONENUMBER))
            return numer;
        if (numer.startsWith("+86"))
            return numer.substring(3);
        if (numer.length() > 11) {
            String numerTemp = numer.substring(numer.length() - 11, numer.length());
            if (numerTemp.matches(REGEX_PHONENUMBER))
                return numerTemp;
        }
        return null;
    }

    public static String getPhoneModel(Context c) {
        Build bd = new Build();
        return bd.MODEL;
    }

}
