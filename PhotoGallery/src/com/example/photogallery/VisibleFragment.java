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
 * �̳д����fragment����ʾ��ͱ�ʾ�յ���broadcast?����notification?
 */
public abstract class VisibleFragment extends Fragment {

    public static final String TAG                 = "VisibleFragment";

    //������̬broadcast receiver
    private BroadcastReceiver  mOnShowNotification = new BroadcastReceiver() {

                                                       @Override
                                                       public void onReceive(Context context, Intent intent) {
                                                           Toast.makeText(getActivity(), "Got a broadcast:" + intent.getAction(), Toast.LENGTH_LONG).show();
                                                           
                                                           //�����ʱ�յ���broadcast��˵�����fragment��ʾ�ˣ���ô���ʱ��cancel��notification
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

        //�Դ���ķ�ʽ����intent filter.
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null);
    }

}
