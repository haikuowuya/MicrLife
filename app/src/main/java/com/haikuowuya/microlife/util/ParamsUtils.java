package com.haikuowuya.microlife.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.haikuowuya.microlife.Application;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class ParamsUtils
{
    public static Map<String, String> addPublic(Context context)
    {
        Map<String, String> params= new HashMap<>();
        addPublicParmas(context, params);
        addMd5Parmas(params);
        System.out.println("添加公共参数");
        return params;
    }
    public static String  paramsToString(Context context)
    {
        Map<String,String> params = addPublic(context);
        StringBuffer stringBuffer = new StringBuffer();
        for(String str: params.keySet())
        {
            stringBuffer.append(str);
            stringBuffer.append("=");
            stringBuffer.append(params.get(str));
            stringBuffer.append("&");
        }
        if(stringBuffer.length() > 0)
        {
            stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length()-1);
        }
        return  stringBuffer.toString();
    }

    /**
     * 公共参数
     *
     * @param params
     */
    private static Map<String, String> addPublicParmas(Context context, Map<String, String> params)
    {
        if (!params.containsKey("netType"))
        {
            params.put("netType", getNetType(context));
        }
        if (!params.containsKey("src"))
        {
            params.put("src", "android");// 那种客户端发送的网络请求
        }
        if (!params.containsKey("versonModule"))
        {
            params.put("versonModule", ConfigUtils.getModeVersion(context));
        }
        if (!params.containsKey("version"))
        {
            params.put("version", ConfigUtils.getVersion(context));// // 当前版本号
        }
        if (!params.containsKey("deviceId"))
        {
            String deviceID = PhoneUtils.getDeviceID(context);
            if (deviceID == null || deviceID.length() < 5)
            {
                deviceID = "v2_00000000";
            }
            params.put("deviceId", deviceID);
        }

        if (!params.containsKey("mType"))
        {
            params.put("mType", android.os.Build.MODEL);// 手机型号
        }

        if (!params.containsKey("mtyb"))
        {
            params.put("mtyb", android.os.Build.BRAND);// 手机品牌
        }

        if (!params.containsKey("osVersion"))
        {
            params.put("osVersion", android.os.Build.VERSION.RELEASE);
        }

        if (!params.containsKey("sim_type") && !TextUtils.isEmpty(PhoneUtils.getOperatorName(context)))
        {
            params.put("sim_type", PhoneUtils.getOperatorName(context));
        }

        if (!params.containsKey("imei"))
        {
            params.put("imei", PhoneUtils.getIMEI(context));
        }

        if (!params.containsKey("imsi"))
        {
            params.put("imsi", PhoneUtils.getSubscriberID(context));
        }

        if (!params.containsKey("market"))
        {
            params.put("market", ConfigUtils.getMarket(context));
        }

        if (!params.containsKey("productId"))
        {
            if (ConfigUtils.PUBLISH == 0)
            {
                params.put("productId", "101");
            }
            else if (ConfigUtils.PUBLISH == 1)
            {
                params.put("productId", "10");
            }
            else if (ConfigUtils.PUBLISH == 2)
            {
                params.put("productId", "50");
            }
        }
            /*
        if (UserManager.getInstance().isLogin())
        {// 等待处理
            if (!TextUtils.isEmpty(UserManager.getInstance().getCasId()) && !requestParams.containsKey("casId"))
            {
                requestParams.put("casId", UserManager.getInstance().getCasId());
            }
            if (!TextUtils.isEmpty(UserManager.getInstance().getAccesstoken()) && !requestParams.containsKey("accessTocken"))
            {
                requestParams.put("accessTocken", UserManager.getInstance().getAccesstoken());
            }
            if (!TextUtils.isEmpty(UserManager.getInstance().getSessionId()) && !requestParams.containsKey("sessionId"))
            {
                requestParams.put("sessionId", UserManager.getInstance().getSessionId());
            }
        }
        */
        if (!params.containsKey("No"))
        {
            params.put("No", "11");
        }
        else if (!params.containsKey("cityId"))
        {
            params.put("cityId", "10002");
        }
        return params;
    }

    private static String getNetType(Context context)
    {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null)
        {
            if (info.getType() == 1)
            {
                return TextUtils.isEmpty(info.getTypeName()) ? "未知" : info.getTypeName();
            }
            else
            {
                return TextUtils.isEmpty(info.getSubtypeName()) ? "未知" : info.getSubtypeName();
            }
        }
        return "未知";
    }

    /**
     * 添加md5参数
     *
     * @param params
     */
    private static Map<String, String> addMd5Parmas(Map<String, String> params)
    {
        if (params != null)
        {
            params.put("api_key", KeyUtils.apiKey);
            params.put("api_md5", getSortedParamsString(params));
        }
        return params;
    }

    /**
     * getSortedParamsString:根据服务器端接口规定,生成MD5校验字符串
     */
    private static String getSortedParamsString(Map<String, String> params)
    {
        String result = "";
        String paramsStr = params.toString();
        String[] strings = paramsStr.trim().split("&");
        if (strings.length > 0)
        {
            // 对参数字符串列表排序
            List<String> list = Arrays.asList(strings);
            Collections.sort(list);
            // 累加排序后的参数字符串
            for (String s : list)
            {
                result += s;
            }
        }
        result = MD5Utils.getMD5(result + KeyUtils.apiSecret);
        return result;
    }

}
