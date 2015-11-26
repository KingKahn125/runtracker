package com.android.wangkang.runtracker;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by WangKang on 2015/11/15.
 */
public abstract  class DataLoader<D> extends AsyncTaskLoader<D> {
    private D mData;

    public DataLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mData!=null){
            deliverResult(mData);
        }else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(D data) {
        mData=data;
        if (isStarted()) super.deliverResult(data);
    }
}
