package com.haikuowuya.microlife.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by raiyi-suzhou on 2015/5/18 0018.
 */
public class ConfigUtils
{

        public static boolean DEBUG = true; // 是否是debug模式
        public static boolean TEST_MODE = false;// 是否是测试模式

        /**
         * 首页
         */
        public static final String TAB_KEY_INDEX = "raiyi_index";
        /**
         * 下载
         */
        public static final String TAB_KEY_HFB = "raiyi_hfb";
        /**
         * 流量
         */
        public static final String TAB_KEY_FLOW = "raiyi_flow";
        /**
         * 发现
         */
        public static final String TAB_KEY_DISCOVER = "raiyi_discover";
        /**
         * 围观
         */
        public static final String TAB_KEY_WELOOK = "raiyi_welook";

        public static int PUBLISH = 0;// 0:正式版和各大市场；1:是适配版；2测试版

        public final static String baseNormalUrl = "http://v1.api.wo116114.com";// 正式环境
        public final static String baseTestUrl = "http://v1.api.dev2.wo116114.com:81";// 测试环境
        /** 保存已勾选的模块,以#分开 */
        public static String ATTENTION_USER_DATA = "attention_sp";

        public static String APP_TAG = "mlifeAnd"; // 正式升级版本
        public static String APP_ADAPTER_TAG = "mlifeAdapt";// 预装适配版本
        public static String APP_TEST_TAG = "mlifetest"; // 测试版本

        /** 应用的所有文件输出SD文件路径 */
        public static final String APP_DIR = "microlife/cache/";

        // --------------分享---------------
        // 使用 ShareUtil中的字段
        // public static final String SHARE_TXT = "share_txt";
        // public static final String SHARE_PIC = "share_pic";
        // public static final String SHARE_IMGURL = "share_imgurl";
        // public static final String SHARE_URL = "share_url";
        // public static final String SHARE_TITLE = "share_title";
        //
        // public static final String SHARE_CONTENT = "share_content";
        // public static final String SHARE_CONTENT_NUMCONTROL =
        // "share_numandcontent";

        // ///////////////////////////////////////需要程序动态指定的参数///////////////////////////////

        /** 网络请求来自那种客户端的 */
        public static final String SRC = "android";

        /** 网络请求来自那种客户端的类型 */
        public static final String PROTECT_ID = "101";// "101";// 将来要换成160的 ,内侧版10

        public static boolean LOGIN_ACTION_CHANGED = false;

        /** 关注的用户数据对应的SharedPrefrence Key */
        public static boolean isNeedRefresh = false;

        /** 服务器返回的url */
        public static String BACK_URL = "back_url";

        /** Sqlite数据库名称 */
        public static final String DB_NAME = "116114_db";

        /** 推送相关 */
        // 推送lastid存储路径
        public static String PULL_DIR = "microlife/pull/";
        /** wo116114最后一条pull推送的id */
        public static String PULL_LASTID = "pull_lastid";
        public static final String PUSH_PARAMS = "push_params";
        public static final String PUSH_PARAMS_SUBJECT = "push_subject";
        /** 实时推送指派的appid */
        public static int APPID = 161;
        /** 推送消息到达的标记 */
        public static String MSG_ACHIEVE_FLAG = "msg_achieve_flag";
        public static boolean PUSH_TOUCHUAN = false;// 透传模式
        /** 推送设置的sharedprefrence key */
        public static String SENDSETTING = "sendsetting";

        /** 商家详情 */
        // 微站地址前缀
        public static final String WZ_URL = "http://Ltweb.wwz114.cn/";
        /** 保存已关注商户的id,以#分开 */
        public static String ATTENTION_ATT_COMPANY = "attention_att_company";
        public static final int SHOP_LOGIN_OK = 3011;

        public static String TAG_MARKET = "";// shanxi 91 hiapk 360 raiyi
        public static String VERSION = "";// 当前版本号,小版本号
        public static String MODE_VERSION = "";// 大版本号，主要是根据市场相关的

        /**
         * 获取渠道号
         *
         * @return
         */
        public static final String getMarket(Context context ) {
            if (!TextUtils.isEmpty(TAG_MARKET) && !TAG_MARKET.equals("未知"))
                return TAG_MARKET;
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                TAG_MARKET = applicationInfo.metaData.getString("BaiduMobAd_CHANNEL");
                if (TextUtils.isEmpty(TAG_MARKET)) {
                    TAG_MARKET = applicationInfo.metaData.getInt("BaiduMobAd_CHANNEL") + "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(TAG_MARKET)) {
                return "未知";
            } else {
                return TAG_MARKET;
            }
        }

        /**
         * 获取 当前版本号,小版本号
         *
         * @return
         */
        public static String getVersion(Context context ) {
            if (!TextUtils.isEmpty(VERSION) && !VERSION.equals("未知")) {
                return VERSION;
            }
            try {
                PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                VERSION = pi.versionName + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(VERSION)) {
                return "未知";
            } else {
                return VERSION;
            }

        }

        /**
         * /获取 大版本号，主要是根据市场相关的
         *
         * @return
         */
        public static String getModeVersion(Context context) {
            return getVersion(context);
        }

}
