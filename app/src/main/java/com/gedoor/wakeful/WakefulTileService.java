package com.gedoor.wakeful;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class WakefulTileService extends TileService {
    private final String TAG = "WakefulTileService";

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.d(TAG, "onTileAdded");
        getQsTile().setState(Tile.STATE_INACTIVE);
        getQsTile().updateTile();
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
        getQsTile().setState(Tile.STATE_INACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.d(TAG, "onStopListening");
    }

    @Override
    public void onClick() {
        super.onClick();
        //Start main activity
        startActivity(new Intent(this, MainActivity.class));
    }

}
