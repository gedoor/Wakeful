package com.gedoor.wakeful;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Objects;

public class WakefulTileService extends TileService {
    private final String TAG = "WakefulTileService";
    private final int NOTIFICATION_ID = 101;
    private final String RELEASE = "release";

    private int tileState = Tile.STATE_INACTIVE;
    private int wakeTimeDefault = 60*1000;
    private int wakeTime;

    private WakefulBroadcastReceiver wakefulBroadcastReceiver = new WakefulBroadcastReceiver(this);
    private IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
    private boolean registerReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Objects.equals(intent.getAction(), RELEASE)) {
            tileRelease();
        } else {
            upNotification();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.d(TAG, "onTileAdded");
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.d(TAG, "onTileRemoved");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(TAG, "onStartListening");
        upTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.d(TAG, "onStopListening");
    }

    @Override
    public void onClick() {
        super.onClick();
        Log.d(TAG, "onClick");
        if (!Settings.System.canWrite(this)) {
            collapseStatusBar(this);
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        } else {
            switch (wakeTime) {
                case 0:
                    wakeTime = 5;
                    break;
                case 5:
                    wakeTime = 10;
                    break;
                case 10:
                    wakeTime = 30;
                    break;
                default:
                    wakeTime = 0;
            }
            if (wakeTime != 0) {
                tileActive();
            } else {
                tileRelease();
            }
        }
    }

    private void tileActive() {
        tileState = Tile.STATE_ACTIVE;
        startService(new Intent(this, WakefulTileService.class));
        upTile();
        upScreenOffTimeout();
        if (!registerReceiver) {
            registerReceiver = true;
            registerReceiver(wakefulBroadcastReceiver, intentFilter);
        }
    }

    public void tileRelease() {
        if (registerReceiver) {
            registerReceiver = false;
            unregisterReceiver(wakefulBroadcastReceiver);
        }
        wakeTime = 0;
        tileState = Tile.STATE_INACTIVE;
        upTile();
        stopForeground(true);
        stopSelf();
        upScreenOffTimeout();
    }

    private void upTile() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(tileState);
            if (wakeTime != 0) {
                tile.setLabel(String.format(getString(R.string.active_label), wakeTime));
            } else {
                tile.setLabel(getString(R.string.default_label));
            }
            tile.updateTile();
        }
    }

    private void upScreenOffTimeout() {
        if (wakeTime != 0) {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, wakeTime * 60 * 1000);
        } else {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, wakeTimeDefault);
        }
    }

    private PendingIntent getThisServicePendingIntent(String actionStr) {
        Intent intent = new Intent(this, this.getClass());
        intent.setAction(actionStr);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void upNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MApplication.CHANNEL_WAKEFUL)
                .setSmallIcon(R.drawable.ic_whatshot_24dp)
                .setOngoing(true)
                .setContentTitle(getString(R.string.default_label))
                .setContentText(String.format(getString(R.string.notification_content), wakeTime))
                .setContentIntent(getThisServicePendingIntent(RELEASE));
        Notification notification = builder.build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void collapseStatusBar(Context context) {
        try {
            @SuppressLint("WrongConstant")
            Object service = context.getSystemService ("statusbar");
            Class <?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("collapsePanels");
            expand.invoke(service);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
