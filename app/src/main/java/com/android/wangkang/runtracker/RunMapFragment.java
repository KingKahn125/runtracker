package com.android.wangkang.runtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.SupportMapFragment;


/**
 * Created by WangKang on 2015/11/16.
 */
public class RunMapFragment extends SupportMapFragment {
    private static final String ARG_RUN_ID="RUN_ID";
    private AMap mAMap;
    public static RunMapFragment newInstance(long runId){
        Bundle args=new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunMapFragment rf=new RunMapFragment();
        rf.setArguments(args);
        return rf;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View v=super.onCreateView(inflater,parent,savedInstanceState);
        mAMap=getMap();
        //mAMap.setLocationSource(LocationSource);
        mAMap.setMyLocationEnabled(true);

        return v;
    }

    }
