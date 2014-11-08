package com.example.runtracker;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class RunDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME                   = "runs.sqlite";
    private static final int    VERSION                   = 1;

    private static final String TABLE_RUN                 = "run";
    private static final String COLUMN_RUN_ID             = "_id";
    private static final String COLUMN_RUN_START_DATE     = "start_date";

    private static final String TABLE_LOCATION            = "location";
    private static final String COLUMN_LOCATION_LATITUDE  = "latitude";
    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_ALTITUDE  = "altitude";
    private static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
    private static final String COLUMN_LOCATION_PROVIDER  = "provider";
    private static final String COLUMN_LOCATION_RUN_ID    = "run_id";

    public RunDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create the 'run' table
        db.execSQL("create table run ( _id integer primary key autoincrement, start_date integer)");
        //Create the 'location' table
        db.execSQL("create table location ( timestamp integer, latitude real, longitude real, altitude real, provider varchar(100), run_id integer references run(_id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Implement schema changes and data message here when upgrading
    }

    public long insertRun(Run run) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RUN_START_DATE, run.getStartDate().getTime());
        return getWritableDatabase().insert(TABLE_RUN, null, cv);
    }

    public long inserLocation(long runId, Location loc) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_LATITUDE, loc.getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE, loc.getLongitude());
        cv.put(COLUMN_LOCATION_ALTITUDE, loc.getAltitude());
        cv.put(COLUMN_LOCATION_TIMESTAMP, loc.getTime());
        cv.put(COLUMN_LOCATION_PROVIDER, loc.getProvider());
        cv.put(COLUMN_LOCATION_RUN_ID, runId);
        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }

    public RunCursor queryRuns() {
        Cursor wrapped = getReadableDatabase().query(TABLE_RUN, null, null, null, null, null, COLUMN_RUN_START_DATE + " asc");
        return new RunCursor(wrapped);
    }

    public RunCursor queryRun(long id) {
        Cursor wrapped = getReadableDatabase().query(TABLE_RUN, null, COLUMN_RUN_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, "1");
        return new RunCursor(wrapped);
    }

    public LocationCursor queryLastLocationForRun(long runId) {

        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION, null, COLUMN_LOCATION_RUN_ID + " = ?", new String[] { String.valueOf(runId) }, null, null, COLUMN_LOCATION_TIMESTAMP + " desc", "1");

        return new LocationCursor(wrapped);

    }

    public static class RunCursor extends CursorWrapper {

        public RunCursor(Cursor cursor) {
            super(cursor);
        }

        public Run getRun() {

            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }

            Run run = new Run();
            long runId = getLong(getColumnIndex(COLUMN_RUN_ID));
            run.setId(runId);

            long startDate = getLong(getColumnIndex(COLUMN_RUN_START_DATE));
            run.setStartDate(new Date(startDate));

            return run;

        }

    }

    public static class LocationCursor extends CursorWrapper {

        public LocationCursor(Cursor cursor) {
            super(cursor);
        }

        public Location getLocation() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }

            String provider = getString(getColumnIndex(COLUMN_LOCATION_PROVIDER));
            Location loc = new Location(provider);
            loc.setLongitude(getDouble(getColumnIndex(COLUMN_LOCATION_LONGITUDE)));
            loc.setLatitude(getDouble(getColumnIndex(COLUMN_LOCATION_LATITUDE)));
            loc.setAltitude(getDouble(getColumnIndex(COLUMN_LOCATION_ALTITUDE)));
            loc.setTime(getLong(getColumnIndex(COLUMN_LOCATION_TIMESTAMP)));
            return loc;
        }
    }

}
