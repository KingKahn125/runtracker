package com.android.wangkang.runtracker;

import android.content.Context;
import android.location.Location;

/**
 * Created by WangKang on 2015/10/26.
 */
public class TrackingLocationReceiver extends LocationReceiver {
    @Override
    protected void onLocationReceived(Context context, Location loc) {
        RunManager.get(context).insertLocation(loc);
    }
}
