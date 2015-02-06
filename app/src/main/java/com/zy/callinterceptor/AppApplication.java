package com.zy.callinterceptor;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.zy.callinterceptor.dao.DaoMaster;
import com.zy.callinterceptor.dao.DaoSession;
import com.zy.callinterceptor.pojo.Constants;

/**
 * Created by Simon on 2015/1/27.
 */
public class AppApplication extends Application {
    private static Context context;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    //全局的Context对象
    public static Context getContext(){
        return context;
    }

    /**
     * 获取DaoMaster对象, DaoMaster持有数据库对象, 用于获取DaoSession和数据库的建立与删除
     * 未写同步, 可能存在线程安全问题
     * @return
     */
    public static DaoMaster getDaoMaster(){
        if (daoMaster == null){
            SQLiteOpenHelper openHelper = new DaoMaster.DevOpenHelper(context, Constants.INTERCEPTOR_CONTACT_DB_NAME, null);
            daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 获取DaoSession对象, DaoSession用于获取所有具体的dao对象, 用于对数据库的CRUD
     * 未写同步, 可能存在线程安全问题
     * @return
     */
    public static DaoSession getDaoSession(){
        if (daoSession == null){
            if (daoMaster == null){
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }

        return daoSession;
    }
}
