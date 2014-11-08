package com.example.runtracker;

import com.example.runtracker.RunDatabaseHelper.LocationCursor;
import com.example.runtracker.RunDatabaseHelper.RunCursor;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.util.Log;

public class RunManager {

    private static final String TAG                 = "RunManager";

    private static final String PREFS_FILE          = "runs";
    private static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";

    public static final String  ACTION_LOCATION     = "com.example.runtracker.ACTION_LOCATION";

    private static RunManager   sRunManager;

    private Context             mAppContext;

    private LocationManager     mLocationManager;

    private RunDatabaseHelper   mHelper;
    private SharedPreferences   mPrefs;
    private long                mCurrentRunId;

    private RunManager(Context ctx) {
        mAppContext = ctx;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);

        mHelper = new RunDatabaseHelper(ctx);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentRunId = mPrefs.getLong(PREF_CURRENT_RUN_ID, -1);
    }

    public static RunManager getInstance(Context ctx) {
        if (sRunManager == null) {
            sRunManager = new RunManager(ctx.getApplicationContext());
        }
        return sRunManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;

        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);

    }

    /**
     * 模拟LocationManager发送location broadcast 
     */
    private void broadcastLaction(Location loc) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, loc);
        mAppContext.sendBroadcast(broadcast);
    }

    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;

        Location lastKnownLocation = mLocationManager.getLastKnownLocation(provider);
        if (lastKnownLocation != null) {
            lastKnownLocation.setTime(System.currentTimeMillis());
            broadcastLaction(lastKnownLocation);
        }

        PendingIntent pi = getLocationPendingIntent(true);
        //获取位置信息
        mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }
    
    public boolean isTrackingRun(Run run) {
        return run != null && run.getId() == mCurrentRunId;
    }

    private Run insertRun() {
        Run run = new Run();
        run.setId(mHelper.insertRun(run));
        return run;
    }

    public void startTrackingRun(Run run) {
        mCurrentRunId = run.getId();
        mPrefs.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunId).commit();
        startLocationUpdates();
    }

    public Run startNewRun() {
        Run run = insertRun();
        startTrackingRun(run);
        return run;
    }

    public void stopRun() {
        stopLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove(PREF_CURRENT_RUN_ID).commit();
    }

    public void inserLocation(Location loc) {
        if (mCurrentRunId != -1) {
            mHelper.inserLocation(mCurrentRunId, loc);
        }
        else {
            Log.e(TAG, "Location received with no tracking run; ignoring.");
        }
    }

    public RunCursor queryRuns() {
        return mHelper.queryRuns();
    }

    public Run getRun(long id) {
        Run run = null;
        RunCursor runCursor = mHelper.queryRun(id);

        runCursor.moveToFirst();
        if (!runCursor.isAfterLast()) {
            run = runCursor.getRun();
        }
        runCursor.close();
        return run;
    }
    
    public Location getLstLocationForRun(long runId) {
        
        Location location = null;
        LocationCursor cursor = mHelper.queryLastLocationForRun(runId);
        
        cursor.moveToFirst();
        
        if(!cursor.isAfterLast()) {
            location = cursor.getLocation();
        }
        cursor.close();
        return location;
        
    }

}
