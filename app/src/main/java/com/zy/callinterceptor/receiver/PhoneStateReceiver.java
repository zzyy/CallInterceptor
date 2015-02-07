package com.zy.callinterceptor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import com.zy.callinterceptor.AppApplication;
import com.zy.callinterceptor.dao.InterceptorContactDao;
import com.zy.callinterceptor.pojo.Constants;
import com.zy.callinterceptor.pojo.InterceptorContact;
import com.zy.callinterceptor.util.ContactsUtil;
import com.zy.callinterceptor.util.DBUtils;
import com.zy.callinterceptor.util.LogUtil;
import com.zy.callinterceptor.util.SPUtil;
import com.zy.callinterceptor.util.TelephoneUtil;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        //after boot completed, we need register the receiver to
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)){

            if ((boolean)SPUtil.get(context, Constants.IS_START_INTERCEPTOR, false)){
                TelephoneUtil.getInstance().startCallInterceptor();
            }

        }else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
            LogUtil.d("ACTION_PHONE_STATE_CHANGED");
            //来电时的处理
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE);

            switch (telephonyManager.getCallState()){
                //来电
                case TelephonyManager.CALL_STATE_RINGING:
                    String incomingNumber = intent.getStringExtra( TelephonyManager.EXTRA_INCOMING_NUMBER);
                    LogUtil.d("incomingNumber=" + incomingNumber);
                    //处理具体的来电
                        if ( isNeedInterceptor(incomingNumber) ){
                            TelephoneUtil.getInstance().blockCall();
                        }

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }

        }

    }

    /*boolean isNeedInterceptor(String incomingNumber){
        List<InterceptorContact> blackList = DBUtils.getAllBlackList();
        for (InterceptorContact interceptorContact : blackList){
            if ( PhoneNumberUtils.compare(incomingNumber, interceptorContact.phoneNumber) ){
                LogUtil.d("电话号码匹配, 需拦截");
                return true;
            }
        }
        List<InterceptorContact> whiteList = DBUtils.getAllWhiteList();
        if (!whiteList.isEmpty()) {
            for (InterceptorContact interceptorContact : whiteList) {
                if (PhoneNumberUtils.compare(incomingNumber, interceptorContact.phoneNumber)) {
                    return false;
                }
            }
            LogUtil.d("电话号码匹配, 需拦截");
            return true;
        }
        return false;
    }*/

    boolean isNeedInterceptor(String incomingNumber){
        int inteceptorMode = TelephoneUtil.getInstance().getInterceptorMode();
        if ((inteceptorMode & 0x0010) == 0x0010){
            if (ContactsUtil.isInContacts(incomingNumber)){
                return false;
            }
        }

        if (isInBlackList(incomingNumber)){
            return true;
        }

        if (isInWhiteList(incomingNumber)){
            return false;
        }


        return true;
    }

    boolean isInBlackList(String num){
        List<InterceptorContact> blackList = DBUtils.getAllBlackList();

        for (InterceptorContact interceptorContact : blackList){
            if ( PhoneNumberUtils.compare(num, interceptorContact.phoneNumber) ){
                LogUtil.d("match blackList");
                return true;
            }
        }

        return false;
    }

    boolean isInWhiteList(String num) {
        List<InterceptorContact> whiteList = DBUtils.getAllWhiteList();
        for (InterceptorContact interceptorContact : whiteList) {
            if (PhoneNumberUtils.compare(num, interceptorContact.phoneNumber)) {
                LogUtil.d("match whiteList");
                return true;
            }
        }
        return false;
    }
}
