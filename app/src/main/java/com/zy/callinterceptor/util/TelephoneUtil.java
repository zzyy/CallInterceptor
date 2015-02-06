package com.zy.callinterceptor.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.zy.callinterceptor.AppApplication;
import com.zy.callinterceptor.pojo.Constants;
import com.zy.callinterceptor.receiver.PhoneStateReceiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Simon on 2015/1/26.
 */
public class TelephoneUtil{
    private PhoneStateReceiver receiver;
    Context context;
    TelephonyManager telephonyManager;
    AudioManager audioManager;
    private int lastAudioMode = -1;

    //挂断电话
    public void endCall(){
        //用反射 获取ITelephony
        try {
            Class<TelephonyManager> clazz = TelephonyManager.class;
//            Method getITelephonyMethod = clazz.getMethod("getITelephony");
            Method getITelephonyMethod = clazz.getDeclaredMethod("getITelephony");
            getITelephonyMethod.setAccessible(true);
            ITelephony mITelephony = (ITelephony)getITelephonyMethod.invoke(telephonyManager);
            mITelephony.endCall();
            LogUtil.d("end call success");
        } catch (Exception e) {
            LogUtil.d("endCall fail");
            LogUtil.e(e.toString());
        }
    }

    public void notification(){
        Notification.Builder builder = new Notification.Builder(context);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentTitle("notification");
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    //静音处理
    public void setSilent(){
        lastAudioMode = audioManager.getMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        LogUtil.d("setSilent; lastAudioMode=" + lastAudioMode);
    }

    public void recoverAudio(){
        LogUtil.d("recoverAudio; lastAudioMode=" + lastAudioMode);
        if (lastAudioMode != -1){
            audioManager.setMode(lastAudioMode);
            LogUtil.d("re");
            lastAudioMode = -1;
        }
    }

    public void blockCall(){
        LogUtil.d("blockCall");
//        this.setSilent();
            this.endCall();
//            this.notification();
//            this.recoverAudio();
    }


    /**
     * 开启电话拦截
     */
    public void startCallInterceptor(){
        LogUtil.d("startCallInterceptor");
        Toast.makeText(context ,"Start call interceptor", Toast.LENGTH_SHORT).show();
        IntentFilter intentFilter = new IntentFilter( TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intentFilter.setPriority(1000);
        receiver = new PhoneStateReceiver();
        context.registerReceiver(receiver, intentFilter);

        SPUtil.put(context, Constants.IS_START_INTERCEPTOR, true);
    }

    /**
     * 关闭电话拦截
     */
    public void endCallInterceptor(){
        LogUtil.d("endCallInterceptor");
        if (receiver != null){
            context.unregisterReceiver(receiver);
            receiver = null;
        }

        SPUtil.put(context, Constants.IS_START_INTERCEPTOR, false);
    }

    public boolean isStartInterceptor(){
        return (boolean)SPUtil.get(context, Constants.IS_START_INTERCEPTOR, false);
    }

    private static TelephoneUtil mInstance;
    public static TelephoneUtil getInstance(){
//        synchronized (mInstance) {
            if (mInstance == null) {
                mInstance = new TelephoneUtil();
            }
//        }
        return mInstance;
    }

    private TelephoneUtil() {
        context = AppApplication.getContext();
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }
}
