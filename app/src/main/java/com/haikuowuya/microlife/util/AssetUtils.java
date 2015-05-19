package com.haikuowuya.microlife.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.text.TextUtils;

import org.apache.http.protocol.HTTP;

public class AssetUtils
{
    /**
     * 二级目录
     */
    private static final String SECOND_DIR = "data";
    /**
     * 默认文件名称后缀
     */
    private static final String DEFAULT_FILENAME_SUFFIXES = ".json";
    private static final String FILE_CITY_NAME="city";

    public static String readAssetData(Context context, String fileName)
    {
        return readAssetData(context, fileName, DEFAULT_FILENAME_SUFFIXES);
    }
    public  static String getCityJson(Context context)
    {
        return  readAssetData(context, FILE_CITY_NAME);
    }

    private static String readAssetData(Context context, String fileName, String suffixes)
    {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        try
        {
            inputStream = context.getAssets().open(SECOND_DIR + File.separator + fileName + suffixes);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) != -1)
            {
                stringBuffer.append(new String(buff, 0, len, HTTP.UTF_8));
            }
            return stringBuffer.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }
}


