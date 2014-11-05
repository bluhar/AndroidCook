package com.example.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RunFragment extends Fragment {

    private Button            mStartButton, mStopButton;

    private TextView          mStartedTextView, mLatitudeTextView, mLongitudeTextView, mAltitudeTextView,
            mDurationTextView;

    private RunManager        mRunManager;

    private Run               mRun;
    private Location          mLastLocation;

    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {

                                                    @Override
                                                    protected void onLocationReceived(Context ctx, Location loc) {
                                                        mLastLocation = loc;
                                                        if(isVisible()) {
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
    }

    private void updateUI() {
        boolean stared = mRunManager.isTrackingRun();

        mStartButton.setEnabled(!stared);
        mStopButton.setEnabled(stared);
        
        if(mRun != null) {
            mStartedTextView.setText(mRun.getStartDate().toString());
        }
        
        int durationSeconds = 0;
        
        if(mRun != null && mLastLocation != null) {
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
                mRunManager.startLocationUpdates();
                mRun = new Run();
                updateUI();

            }
        });

        mStopButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mRunManager.stopLocationUpdates();
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

    
    
}
