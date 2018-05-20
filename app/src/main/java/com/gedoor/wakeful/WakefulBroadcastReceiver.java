package com.gedoor.wakeful;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WakefulBroadcastReceiver extends BroadcastReceiver {
    private WakefulTileService wakefulTileService;

    public WakefulBroadcastReceiver(WakefulTileService wakefulTileService) {
        this.wakefulTileService = wakefulTileService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        wakefulTileService.tileRelease();
    }
}
