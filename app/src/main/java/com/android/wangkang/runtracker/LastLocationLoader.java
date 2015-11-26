package com.android.wangkang.runtracker;

import android.content.Context;
import android.location.Location;

/**
 * Created by WangKang on 2015/11/16.
 */
public class LastLocationLoader extends DataLoader<Location> {
    private long mRunId;

    public LastLocationLoader(Context context,long runId){
        super(context);
        mRunId=runId;
    }

    @Override
    public Location loadInBackground() {
        return RunManager.get(getContext()).getLastLocationForRun(mRunId);
    }
}
