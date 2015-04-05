package com.serviceindeed.xuebao;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.serviceindeed.xuebao.values.Leave;


public class LeaveFragment2 extends Fragment{

    private Callbacks mCallbacks;
    ListAdapter mAdapter;
    ListView mList;

    //任何打算托管FeedBackFragment的Activity都要实现此接口
    public interface Callbacks {

        void onLeaveSelected(Leave leave);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
        //getActivity().setTitle(R.string.leave_title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_leave, container, false);

        mList = (ListView) view.findViewById(R.id.leave_list_view);    
        mAdapter = new LeaveAdapter(mockLeaves()); //获取请假的list
        mList.setAdapter(mAdapter);
        
        mList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Leave item = (Leave) mAdapter.getItem(position);
                mCallbacks.onLeaveSelected(item);                
            }
        });
        
        ImageView createLeaveBtn = (ImageView) view.findViewById(R.id.createLeaveBtn);
        createLeaveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动leave detail activity
                mCallbacks.onLeaveSelected(null);
            }
        });
        
        
        return view;
    }
    
    private ArrayList<Leave> mockLeaves(){
        ArrayList<Leave> list = new ArrayList<Leave>();
        Leave leave = new Leave();
        leave.setType("年假");
        leave.setStart(new Date());
        leave.setEnd(new Date());
        leave.setId(1);
        leave.setCreate(new Date());
        list.add(leave);
        return list;
    }

    

    /**
     * Leave list view adapter
     * @author anelm
     *
     */
    private class LeaveAdapter extends ArrayAdapter<Leave> {

        public LeaveAdapter(ArrayList<Leave> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_leave, null);
            }

            Leave leave = getItem(position);

            TextView typeView = (TextView) convertView.findViewById(R.id.leave_list_item_leaveType);
            TextView dateView = (TextView) convertView.findViewById(R.id.leave_list_item_leaveStartDate);
            TextView reasonView = (TextView) convertView.findViewById(R.id.leave_list_item_leaveReason);

            typeView.setText(leave.getType());
            dateView.setText(DateFormat.format("yyyy-MM-dd", leave.getStart()));
            reasonView.setText(leave.getReason());

            return convertView;
        }

    }
    
    
}
