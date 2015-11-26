package com.android.wangkang.runtracker;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by WangKang on 2015/10/15.
 */
public class RunFragment extends Fragment {

    private static final String ARG_RUN_ID="RUN_ID";
    private Run mRun;
    private Location mLastLocation;
    private Button mStartButton,mStopButton,mMapButton;
    private TextView mStartedTextView,mLatitudeTextView,mLongitudeTextView,mAltitudeTextView,mDurationTextView;
    private RunManager mRunManager;
    private static final int LOAD_RUN=0;
    private static final int LOAD_LOCATION=1;

    private BroadcastReceiver mLocationReceiver=new LocationReceiver(){
        @Override
        protected void onLocationReceived(Context context, Location loc) {
            if (!mRunManager.isTrackingRun())
                return;
            mLastLocation=loc;
            if (isVisible()) updateUI();
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            int toastText=enabled?R.string.gps_enabled:R.string.gps_disabled;
            Toast.makeText(getActivity(),toastText,Toast.LENGTH_LONG).show();
        }
    };



    public static RunFragment newInstance(long runId){
        Bundle args=new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunFragment rf=new RunFragment();
        rf.setArguments(args);
        return rf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRunManager=RunManager.get(getActivity());

        Bundle args=getArguments();
        if (args!=null){
            long runId=args.getLong(ARG_RUN_ID,-1);
            if (runId!=-1){
                /*mRun=mRunManager.getRun(runId);
                mLastLocation=mRunManager.getLastLocationForRun(runId);*/
                LoaderManager lm=getLoaderManager();
                lm.initLoader(LOAD_RUN,args,new RunLoaderCallbacks());
                lm.initLoader(LOAD_LOCATION,args,new LocationLoaderCallbacks());
            }
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_run,container,false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mStartedTextView=(TextView)view.findViewById(R.id.run_startedTextView);
        mLatitudeTextView=(TextView)view.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView=(TextView)view.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView=(TextView)view.findViewById(R.id.run_altitudeTextView);
        mDurationTextView=(TextView)view.findViewById(R.id.run_durationTextView);

        mStartButton=(Button)view.findViewById(R.id.run_startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mRunManager.startLocationUpdates();
                mRun=new Run();*/
                //mRun=mRunManager.startNewRun();
                if (mRun==null){
                    mRun=mRunManager.startNewRun();
                }else{
                    mRunManager.startTrackingRun(mRun);
                }
                updateUI();
            }
        });
        mStopButton=(Button)view.findViewById(R.id.run_stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mRunManager.stopLocationUpdates();
                mRunManager.stopRun();
                updateUI();
            }
        });

        mMapButton=(Button)view.findViewById(R.id.run_mapButton);
        mMapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),RunMapActivity.class);
                i.putExtra(RunMapActivity.EXTRA_RUN_ID,mRun.getId());
                startActivity(i);
            }
        });

        updateUI();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(RunManager.ACTION_LOCATION));

    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    private void updateUI(){
        boolean started=mRunManager.isTrackingRun();
        boolean trackingThisRun=mRunManager.isTrackingRun();

        if (mRun!=null)
            mStartedTextView.setText(mRun.getStartDate().toString());
        int durationSeconds=0;
        if (mRun!=null&&mLastLocation!=null){
            durationSeconds=mRun.getDurationSeconds(mLastLocation.getTime());
            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
            mMapButton.setEnabled(true);
        }else {
            mMapButton.setEnabled(false);
        }
        mDurationTextView.setText(Run.formatDuration(durationSeconds));
        mStartButton.setEnabled(!started);
        mStopButton.setEnabled(started&&trackingThisRun);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_run, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class RunLoaderCallbacks implements LoaderCallbacks<Run>{
        @Override
        public Loader<Run> onCreateLoader(int id, Bundle args) {
            return new RunLoader(getActivity(), args.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Run> loader, Run data) {
            mRun=data;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Run> loader) {

        }
    }

    private class LocationLoaderCallbacks implements LoaderCallbacks<Location>{
        @Override
        public Loader<Location> onCreateLoader(int id, Bundle args) {
            return new LastLocationLoader(getActivity(),args.getLong(ARG_RUN_ID));

        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location data) {
            mLastLocation=data;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {

        }
    }

}
