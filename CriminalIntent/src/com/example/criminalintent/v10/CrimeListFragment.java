package com.example.criminalintent.v10;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
    private Callbacks           mCallbacks;
    
    //任何打算托管CrimeListFragment的Activity都要实现此接口
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    //在fragment附加给activity时调用此方法
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //从下面这行代码也可以看出，任何打算托管CrimeListFragment的Activity都要实现此接口
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

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
        
        ListView listView = (ListView) v.findViewById(android.R.id.list);
        
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //显示浮动上下文菜单(floating context menus)，必须调用下面的方法注册视图
            registerForContextMenu(listView);
        }
        else {
            //使用上下文操作栏(contextual action bar)，开启多选模式
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
                
                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }
                
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub
                    
                }
                
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }
                
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
                            CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
                            for (int i = 0 ; i < adapter.getCount() ; i++) {
                                if(getListView().isItemChecked(i)) {
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish(); //销毁action mode
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }

                }
                
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    // TODO Auto-generated method stub
                    
                }
            });
        }
        
        
        return v;
    }

    
    public void updateUI(){
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
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

        mCallbacks.onCrimeSelected(item);
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
                ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
                mCallbacks.onCrimeSelected(crime);
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
    
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
        Crime crime = adapter.getItem(position);
        
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
        
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
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
