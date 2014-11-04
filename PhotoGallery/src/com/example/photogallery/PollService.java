package com.example.photogallery;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PollService extends IntentService {

    private static final String TAG           = "PollService";
    private static final int    POLL_INTERVAL = 1000 * 15;    //15 SECONDS

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //检查网络是否可用
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvaiable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;

        if (!isNetworkAvaiable)
            return;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String query = prefs.getString(FlickrFetcher.PREF_SEARCH_QUERY, null);
        String lastResultId = prefs.getString(FlickrFetcher.PREF_LAST_RESULT_ID, null);

        ArrayList<GalleryItem> items;
        if (query != null) {
            items = new FlickrFetcher().search(query);
        }
        else {
            items = new FlickrFetcher().fetchItems();
        }
        if (items.isEmpty())
            return;

        String resultId = items.get(0).getId();

        if (!resultId.equals(lastResultId)) {
            Log.i(TAG, "Got a new result: " + resultId);
            
            //创建notification
            Resources res = getResources();
            PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, PhotoGalleryActivity.class), 0);
            
            Notification notification = new NotificationCompat.Builder(this).setTicker(res.getString(R.string.new_pictures_title))
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(res.getString(R.string.new_pictures_title))
            .setContentText(res.getString(R.string.new_pictures_text))
            .setContentIntent(pi)
            .setAutoCancel(true).build();
            
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //第一个参数是notification的标识ID，如果同一个ID发送两条notification，则第二条notification会替换第一条。
            notificationManager.notify(0, notification);
            
        }
        else {
            Log.i(TAG, "Got an old result: " + resultId);
        }

        prefs.edit().putString(FlickrFetcher.PREF_LAST_RESULT_ID, resultId).commit();
    }

    //启动和停止pending intent.
    public static void setServiceAlarm(Context ctx, boolean isOn) {
        Intent i = new Intent(ctx, PollService.class);
        PendingIntent pi = PendingIntent.getService(ctx, 0, i, 0);
        PendingIntent pi2 = PendingIntent.getService(ctx, 0, i, 0);
        Intent i2 = new Intent(ctx, PollService.class);
        PendingIntent pi3 = PendingIntent.getService(ctx, 0, i2, 0);
        //书上讲同一个intent请求PendingIntent两次，得到的pending intent仍会是同一个。。。
        System.out.println(i == i2);
        System.out.println(pi == pi2);
        Log.i(TAG, "Create two intent(context and class is same), equal ? " + (i==i2)); //false
        Log.i(TAG, "Create two pending intent(intent is sam), equal ? " + (pi==pi2)); //false
        Log.i(TAG, "Create two pending intent(two intent, intent context and class is same), equal ? " + (pi==pi3)); //false
        
        Log.i(TAG, "equeal methon............");
        Log.i(TAG, "Create two intent(context and class is same), equal ? " + (i.equals(i2))); //false
        Log.i(TAG, "Create two pending intent(intent is sam), equal ? " + (pi.equals(pi2))); //true
        Log.i(TAG, "Create two pending intent(two intent, intent context and class is same), equal ? " + (pi.equals(pi3))); //true

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
        }
        else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context ctx) {
        Intent i = new Intent(ctx, PollService.class);
        //FLAG_NO_CREATE,如果pending intent不存在，返回null
        PendingIntent pi = PendingIntent.getService(ctx, 0, i, PendingIntent.FLAG_NO_CREATE);

        return pi != null;
    }

}
