package com.serviceindeed.xuebao;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
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

import com.serviceindeed.xuebao.values.Punch;

public class PunchFragment extends Fragment {
    
    private Callbacks mCallbacks;
    ListAdapter mAdapter;
    ListView mList;

    public interface Callbacks {
        void onPunchSelected(Punch punch);
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
        
        

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_punch, container, false);

        mList = (ListView) view.findViewById(R.id.punch_list_view);    
        mAdapter = new PunchAdapter(mockPunchs()); //获取请假的list
        mList.setAdapter(mAdapter);
        
        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Punch item = (Punch) mAdapter.getItem(position);
                mCallbacks.onPunchSelected(item);                
            }
        });
        
        ImageView createLeaveBtn = (ImageView) view.findViewById(R.id.createPunchBtn);
        createLeaveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动leave detail activity
                mCallbacks.onPunchSelected(null);
            }
        });
        
        return view;
    }
    
    private ArrayList<Punch> mockPunchs(){
        ArrayList<Punch> list = new ArrayList<Punch>();
        Punch punch = new Punch();
        punch.setType("上班");
        punch.setId(1);
        punch.setCreate(new Date());
        list.add(punch);
        return list;
    }

    

    /**
     * Punch list view adapter
     * @author anelm
     *
     */
    private class PunchAdapter extends ArrayAdapter<Punch> {

        public PunchAdapter(ArrayList<Punch> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_punch, null);
            }

            Punch punch = getItem(position);

            TextView typeView = (TextView) convertView.findViewById(R.id.punch_list_item_type);
            TextView dateView = (TextView) convertView.findViewById(R.id.punch_list_item_createDate);
            TextView reasonView = (TextView) convertView.findViewById(R.id.punch_list_item_punchRemark);

            typeView.setText(punch.getType());
            dateView.setText(DateFormat.format("yyyy-MM-dd", punch.getCreate()));
            reasonView.setText(punch.getRemark());

            return convertView;
        }

    }
    
}
