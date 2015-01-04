package com.serviceindeed.xuebao;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.serviceindeed.xuebao.values.Leave;

public class LeaveFragment extends ListFragment {

    private Callbacks mCallbacks;

    //任何打算托管FeedBackFragment的Activity都要实现此接口
    public interface Callbacks {

        void onLeaveSelected(Leave leave);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

        setRetainInstance(true); //保留Fragment

        getActivity().setTitle(R.string.leave_title);

        LeaveAdapter adapter = new LeaveAdapter(getLeaves());
        setListAdapter(adapter);
    }

    private ArrayList<Leave> getLeaves() {
        //TODO Get the leave from remote service
        ArrayList<Leave> leaves = new ArrayList<Leave>();
        Leave leave = new Leave();
        leave.setCreate(new Date());
        leave.setStart(new Date());
        leave.setType("病假");
        leave.setReason("生病...");
        leave.setStatus(1);
        leaves.add(leave);
        return leaves;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ListView listView = (ListView) v.findViewById(android.R.id.list);

        return v;
    }

    public void updateUI() {
        ((LeaveAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((LeaveAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Leave item = (Leave) getListAdapter().getItem(position);
        mCallbacks.onLeaveSelected(item);
    }

    private class LeaveAdapter extends ArrayAdapter<Leave> {

        public LeaveAdapter(ArrayList<Leave> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_feedback, null);
            }

            Leave leave = getItem(position);

            return convertView;
        }

    }

}
