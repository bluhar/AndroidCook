package com.example.mylauncher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyLauncherFragment extends ListFragment {

    private static final String TAG = "MyLauncherFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent hideStartupIntent = new Intent(Intent.ACTION_MAIN);
        hideStartupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        //查询能够过滤hideStartupIntent的应用 
        List<ResolveInfo> activities = pm.queryIntentActivities(hideStartupIntent, 0);
        Log.i(TAG, "I've found " + activities.size());

        Collections.sort(activities, new Comparator<ResolveInfo>() {

            @Override
            public int compare(ResolveInfo r1, ResolveInfo r2) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(r1.loadLabel(pm).toString(), r2.loadLabel(pm).toString());
            }
        });

        ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(getActivity(), android.R.layout.simple_list_item_1, activities) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                PackageManager pm = getActivity().getPackageManager();
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                ResolveInfo ri = getItem(position);
                textView.setText(ri.loadLabel(pm));
                return textView;
            }

        };
        
        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo ri = (ResolveInfo) l.getAdapter().getItem(position);
        ActivityInfo info = ri.activityInfo;
        if(info == null) return;
        
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(info.applicationInfo.packageName, info.name);
        //在新的任务中启动能够过滤此intent的activity，而不是在当前任务中启动。
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        startActivity(i);
    }

    
    
}
