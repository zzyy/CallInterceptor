package com.zy.callinterceptor.pojo;

/**
 * Created by Simon on 2015/1/29.
 */
public class Constants {
    public static final String INTERCEPTOR_CONTACT_DB_NAME = "InterceptorContact";

    //sharedPreferences 文件名称
    public static final String SHARED_PREFERENCES_FILE_NAME = "share_data";
    // 用于在 SharedPreferences 中记录是否开启拦截, 及开机时是否要启动拦截
    public static final String IS_START_INTERCEPTOR = "isStartInterceptor";

    //是否记录log, 及log标签名, 在LogUtil中使用
    public static final boolean IS_ALLOW_LOG = true;
    public static final String LOG_TAG = "zy.CallInterceptor";
}
