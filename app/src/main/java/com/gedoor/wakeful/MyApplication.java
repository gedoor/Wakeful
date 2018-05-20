package com.gedoor.wakeful;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class MyApplication extends Application {
    public final static String CHANNEL_WAKEFUL = "channel_wakeful";

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIdWakeful();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannelIdWakeful() {
        //用唯一的ID创建渠道对象
        NotificationChannel firstChannel = new NotificationChannel(CHANNEL_WAKEFUL,
                getString(R.string.wake_time),
                NotificationManager.IMPORTANCE_LOW);
        //初始化channel
        firstChannel.enableLights(false);
        firstChannel.enableVibration(false);
        firstChannel.setSound(null, null);
        //向notification manager 提交channel
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(firstChannel);
        }
    }
}
