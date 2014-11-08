package com.example.runtracker;

import android.content.Context;
import android.location.Location;

public class LocationLoader extends DataLoader<Location> {

    private long mRunId;

    public LocationLoader(Context context, long runId) {
        super(context);
        mRunId = runId;
    }

    @Override
    public Location loadInBackground() {
        return RunManager.getInstance(getContext()).getLstLocationForRun(mRunId);
    }

}
