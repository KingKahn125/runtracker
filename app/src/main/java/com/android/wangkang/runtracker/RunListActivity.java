package com.android.wangkang.runtracker;

import android.support.v4.app.Fragment;

/**
 * Created by WangKang on 2015/10/26.
 */
public class RunListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RunListFragment();
    }
}
