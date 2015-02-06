package com.zy.callinterceptor.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zy.callinterceptor.AppApplication;
import com.zy.callinterceptor.dao.DaoSession;
import com.zy.callinterceptor.dao.InterceptorContactDao;
import com.zy.callinterceptor.pojo.InterceptorContact;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Simon on 2015/1/27.
 */
public class DBUtils {
    private DaoSession daoSession = AppApplication.getDaoSession();
    private static InterceptorContactDao interceptorContactDao = AppApplication.getDaoSession().getInterceptorContactDao();

    public static List<InterceptorContact> getAllInterceptorContacts(){
        return interceptorContactDao.loadAll();
    }

    public static List<InterceptorContact> getAllWhiteList(){
        List<InterceptorContact> result;
        QueryBuilder<InterceptorContact> qb = interceptorContactDao.queryBuilder();
        qb.where(InterceptorContactDao.Properties.Type.eq(InterceptorContact.Type.WHITE_LIST));
        result = qb.list();
        LogUtil.d("DBUtils.getAllWhiteList(); size=" + result.size());
        return result;
    }

    public static List<InterceptorContact> getAllBlackList(){
        List<InterceptorContact> result;
        QueryBuilder<InterceptorContact> qb = interceptorContactDao.queryBuilder();
        qb.where(InterceptorContactDao.Properties.Type.eq(InterceptorContact.Type.BLACK_LIST));
        result = qb.list();
        LogUtil.d("DBUtils.getAllBlackList(); size=" + result.size());
        return result;
    }

    public static void deleteInterceptorContact(List<InterceptorContact> data){
        interceptorContactDao.deleteInTx(data);
    }

    public static void deleteAllData(){
        interceptorContactDao.deleteAll();
    }
}
