package com.serviceindeed.xuebao;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.serviceindeed.xuebao.values.Punch;


public class PunchDetailFragment extends Fragment{

    public static final String EXTRA_PUNCH_ID = "com.serviceindeed.xuebao.punch_id";
    
    private SimpleDateFormat      mFormatter             = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Punch                 mPunch;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int leaveId = getArguments().getInt(EXTRA_PUNCH_ID);
        if (leaveId == -1) {
            mPunch = null;
        }
        else {
            //TODO 根据请假ID，查询请假信息
            mPunch = getLeaveById(1);
        }
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_punch_detail, container, false);
        
        //设置spinner\
        Spinner s = (Spinner) view.findViewById(R.id.punchType);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.punch_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        
        Button submitBtn = (Button) view.findViewById(R.id.punchDetailSubmitBtn);
        if(mPunch == null ) {
            //新建
            submitBtn.setVisibility(View.VISIBLE);
        }
        else{
            //修改
            submitBtn.setVisibility(View.GONE);
        }
        
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    /**
     * 当action bar被点击后，触发onOptionsItemSelected()方法
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * TODO 根据请假ID查询请假信息
     * @param id
     * @return
     */
    private Punch getLeaveById(int id) {
        Punch punch = new Punch();
        punch.setType("上班");
        punch.setId(1);
        punch.setCreate(new Date());
        return punch;
    }

    public static PunchDetailFragment newInstance(int id) {
        PunchDetailFragment fragment = new PunchDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_PUNCH_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

}
