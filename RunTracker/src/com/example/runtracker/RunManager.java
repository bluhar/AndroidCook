package com.example.runtracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class RunManager {

    private static final String TAG             = "RunManager";

    public static final String  ACTION_LOCATION = "com.example.runtracker.ACTION_LOCATION";

    private static RunManager   sRunManager;

    private Context             mAppContext;

    private LocationManager     mLocationManager;

    private RunManager(Context ctx) {
        mAppContext = ctx;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
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
        if(lastKnownLocation != null) {
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
    

}
