package com.example.criminalintent.v6;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.criminalintent.R;

/**
 * ListFragment中内置了ListView, ListView向adapter申请视图
 * 
 * 如何定制ListView的视图：
 * 		创建自定义列表项视图的XML布局文件
 * 		创建ArrayAdapter<T>的子类，用来创建、填充并返回定义在新布局中的视图
 * @author anelm
 *
 */
public class CrimeListFragment extends ListFragment {

    private static final String TAG           = CrimeListFragment.class.getSimpleName();

    private static final int    REQUEST_CRIME = 1;

    private ArrayList<Crime>    mCrimes;

    private boolean             mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //通知FragmentManager:CrimeListFragment需要接收选项菜单方法回调，即调用 onCreateOptionsMenu()方法
        setHasOptionsMenu(true);

        mSubtitleVisible = false;
        setRetainInstance(true); //保留Fragment

        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.getInstance(getActivity()).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);

    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
            
        }
        return v;
    }



    @Override
    public void onResume() {
        super.onResume();

        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime item = (Crime) getListAdapter().getItem(position);
        Log.d(TAG, item.getTitle() + " was clicked.");

        //Start CrimeActivity
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, item.getId());
        //startActivity(i);
        startActivityForResult(i, REQUEST_CRIME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME) {
            //handle result
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_list, menu);
        
        MenuItem item = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible && item!=null) {
            item.setTitle(R.string.hide_subtitle);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);
                return true;
            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle() == null) {
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    mSubtitleVisible = true;
                }
                else {
                    getActivity().getActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    mSubtitleVisible = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {

        public CrimeAdapter(ArrayList<Crime> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }

            Crime crime = getItem(position);

            TextView titleView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            TextView dateView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            CheckBox solvedView = (CheckBox) convertView.findViewById(R.id.crime_lsit_item_solveCheckBox);

            titleView.setText(crime.getTitle());
            dateView.setText(DateFormat.format("yyyy-MM-dd", crime.getDate()));
            solvedView.setChecked(crime.isSolved());

            return convertView;
        }

    }

}
