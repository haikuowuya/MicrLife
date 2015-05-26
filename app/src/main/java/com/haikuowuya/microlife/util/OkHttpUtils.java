package com.haikuowuya.microlife.util;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by raiyi-suzhou on 2015/5/20 0020.
 */
public class OkHttpUtils
{
    private static final OkHttpClient sOkHttpClient = new OkHttpClient();
    static
    {
        sOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        sOkHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
    }

    /***
     * 开启异步线程访问网络
     * @param request：请求
     * @param callback:回调
     */
    public static void asyncExecute(Request request, Callback callback)
    {
        sOkHttpClient.newCall(request).enqueue(callback);

    }
    public static  void cancleRequest(Request request)
    {
        sOkHttpClient.cancel(request);
    }
    public static void asynExecute(Request request)
    {
        asyncExecute(request, new Callback(){
            @Override
            public void onFailure(Request request, IOException e)
            {
                     //空实现
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                     //空实现
            }
        });
    }
}
