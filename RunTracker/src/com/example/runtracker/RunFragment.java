package com.example.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RunFragment extends Fragment {

    private static final String ARG_RUN_ID        = "RUN_ID";
    private static final int    LOAD_RUN          = 0;
    private static final int    LOAD_LOCATION          = 1;

    private Button              mStartButton, mStopButton;

    private TextView            mStartedTextView, mLatitudeTextView, mLongitudeTextView, mAltitudeTextView,
            mDurationTextView;

    private RunManager          mRunManager;

    private Run                 mRun;
    private Location            mLastLocation;

    private BroadcastReceiver   mLocationReceiver = new LocationReceiver() {

                                                      @Override
                                                      protected void onLocationReceived(Context ctx, Location loc) {

                                                          if (!mRunManager.isTrackingRun(mRun))
                                                              return;

                                                          mLastLocation = loc;
                                                          if (isVisible()) {
                                                              updateUI();
                                                          }

                                                      }

                                                      @Override
                                                      protected void onProviderEnabledChanged(boolean enabled) {
                                                          int text = enabled ? R.string.gps_enabled : R.string.gps_disabled;
                                                          Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
                                                      }

                                                  };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mRunManager = RunManager.getInstance(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            long runId = bundle.getLong(ARG_RUN_ID, -1);
            if (runId != -1) {
//                mRun = mRunManager.getRun(runId);
//                mLastLocation = mRunManager.getLstLocationForRun(runId);
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(LOAD_RUN, bundle, new RunLoaderCallbacks());
                loaderManager.initLoader(LOAD_LOCATION, bundle, new LocationLoaderCallbacks());
            }
        }
    }

    /**
     * 创建fragment，并为其绑定参数
     * @param runId
     * @return
     */
    public static RunFragment newInstance(long runId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);

        RunFragment rf = new RunFragment();
        rf.setArguments(args);
        return rf;
    }

    private void updateUI() {
        boolean stared = mRunManager.isTrackingRun();
        boolean trackingThisRun = mRunManager.isTrackingRun(mRun);

        mStartButton.setEnabled(!stared);
        mStopButton.setEnabled(stared && trackingThisRun);

        if (mRun != null) {
            mStartedTextView.setText(mRun.getStartDate().toString());
        }

        int durationSeconds = 0;

        if (mRun != null && mLastLocation != null) {
            durationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());
            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
        }

        mDurationTextView.setText(mRun.formatDuration(durationSeconds));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragement_run, container, false);

        mStartedTextView = (TextView) v.findViewById(R.id.run_startedTextView);
        mLatitudeTextView = (TextView) v.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView = (TextView) v.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView = (TextView) v.findViewById(R.id.run_altitudeTextView);
        mDurationTextView = (TextView) v.findViewById(R.id.run_durationTextView);

        mStartButton = (Button) v.findViewById(R.id.run_startButton);
        mStopButton = (Button) v.findViewById(R.id.run_stopButton);

        mStartButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mRun == null) {
                    mRun = mRunManager.startNewRun();
                }
                else {
                    mRunManager.startTrackingRun(mRun);
                }

                updateUI();

            }
        });

        mStopButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //                mRunManager.stopLocationUpdates();

                mRunManager.stopRun();

                updateUI();
            }
        });

        updateUI();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(RunManager.ACTION_LOCATION));
    }

    @Override
    public void onStop() {
        //注意下面这行在super之前
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    private class RunLoaderCallbacks implements LoaderCallbacks<Run> {

        @Override
        public Loader<Run> onCreateLoader(int arg0, Bundle arg1) {
            return new Runloader(getActivity(), arg1.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Run> arg0, Run arg1) {
            mRun = arg1;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Run> arg0) {

        }

    }
    
    private class LocationLoaderCallbacks implements LoaderCallbacks<Location> {

        @Override
        public Loader<Location> onCreateLoader(int arg0, Bundle arg1) {
            return new LocationLoader(getActivity(), arg1.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Location> arg0, Location arg1) {
            mLastLocation = arg1;
            updateUI();
            
        }

        @Override
        public void onLoaderReset(Loader<Location> arg0) {
            
        }
        
    }

}
