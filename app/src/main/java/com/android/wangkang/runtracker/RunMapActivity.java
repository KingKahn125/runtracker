package com.android.wangkang.runtracker;

import android.support.v4.app.Fragment;

/**
 * Created by WangKang on 2015/11/16.
 */
public class RunMapActivity extends SingleFragmentActivity {
    public static final String EXTRA_RUN_ID="com.android.wangkang.runtracker.run_id";

    @Override
    protected Fragment createFragment() {
        long runId=getIntent().getLongExtra(EXTRA_RUN_ID,-1);
        if (runId!=-1){
            return RunMapFragment.newInstance(runId);
        }else {
            return new RunMapFragment();
        }
    }
}
