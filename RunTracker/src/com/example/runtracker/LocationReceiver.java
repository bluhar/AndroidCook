package com.example.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {

    private static final String TAG = LocationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Location loc = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);

        if (loc != null) {
            onLocationReceived(context, loc);
            return;
        }

        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(enabled);
        }

    }

    protected void onLocationReceived(Context ctx, Location loc) {
        Log.i(TAG, this + " Got location from " + loc.getProvider() + ": " + loc.getLatitude() + ", " + loc.getLongitude());
    }

    protected void onProviderEnabledChanged(boolean enabled) {
        Log.i(TAG, "Provider " + (enabled ? "enabled" : "disabled"));
    }

}
