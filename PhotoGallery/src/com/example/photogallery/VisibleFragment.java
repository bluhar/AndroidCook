package com.example.photogallery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * 继承此类的fragment，显示后就表示收到了broadcast?还是notification?
 */
public abstract class VisibleFragment extends Fragment {

    public static final String TAG                 = "VisibleFragment";

    //创建动态broadcast receiver
    private BroadcastReceiver  mOnShowNotification = new BroadcastReceiver() {

                                                       @Override
                                                       public void onReceive(Context context, Intent intent) {
                                                           Toast.makeText(getActivity(), "Got a broadcast:" + intent.getAction(), Toast.LENGTH_LONG).show();
                                                           
                                                           //如果此时收到了broadcast，说明这个fragment显示了，那么这个时候cancel掉notification
                                                           Log.i(TAG, "Canceling notification.");
                                                           setResultCode(Activity.RESULT_CANCELED);
                                                       }
                                                   };

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(mOnShowNotification);
    }

    @Override
    public void onResume() {
        super.onResume();

        //以代码的方式创建intent filter.
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null);
    }

}
