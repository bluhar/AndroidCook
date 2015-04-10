package com.serviceindeed.xuebao;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.serviceindeed.xuebao.values.Leave;

/**
 * 请假明细fragment
 * @author anelm
 *
 */
public class LeaveDetailFragment extends Fragment {

    public static final String    EXTRA_LEAVE_ID         = "com.serviceindeed.xuebao.leave_id";
    private SimpleDateFormat      mFormatter             = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Leave                 mLeave;
    private TextView              mLeaveStartDate;
    private TextView              mLeaveEndDate;

    private SlideDateTimeListener mStartDateTimeListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            mLeaveStartDate.setText(mFormatter.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {
        }
    };
    private SlideDateTimeListener mEndDateTimeListener = new SlideDateTimeListener() {
        
        @Override
        public void onDateTimeSet(Date date)
        {
            mLeaveEndDate.setText(mFormatter.format(date));
        }
        
        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {
        }
    };



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int leaveId = getArguments().getInt(EXTRA_LEAVE_ID);
        if (leaveId == -1) {
            mLeave = null;

        }
        else {
            //TODO 根据请假ID，查询请假信息
            mLeave = getLeaveById(1);
        }
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //container是Fragment的父视图，第三个参数告知布局生成器是否将生成的视图添加到父视图
        View view = inflater.inflate(R.layout.fragment_leave_detail, container, false);
        
        Button submitBtn = (Button) view.findViewById(R.id.submitBtn);
        if(mLeave == null) {
            getActivity().setTitle(R.string.leave_fragment_request);
            //新建
            submitBtn.setVisibility(View.VISIBLE);
        }
        else {
            getActivity().setTitle(R.string.leave_fragment_detail);
            //修改
            submitBtn.setVisibility(View.GONE);
        }

        //设置spinner，date picker, submit button

        Spinner s = (Spinner) view.findViewById(R.id.leaveType);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.leave_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        mLeaveStartDate = (TextView) view.findViewById(R.id.leaveStartDate);
        mLeaveEndDate = (TextView) view.findViewById(R.id.leaveEndDate);
        mLeaveStartDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager()).setListener(mStartDateTimeListener).setInitialDate(new Date())
                //.setMinDate(minDate)
                //.setMaxDate(maxDate)
                //.setIs24HourTime(true)
                //.setTheme(SlideDateTimePicker.HOLO_DARK)
                //.setIndicatorColor(Color.parseColor("#990000"))
                .build().show();

            }
        });
        mLeaveEndDate.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager()).setListener(mEndDateTimeListener).setInitialDate(new Date())
                //.setMinDate(minDate)
                //.setMaxDate(maxDate)
                //.setIs24HourTime(true)
                //.setTheme(SlideDateTimePicker.HOLO_DARK)
                //.setIndicatorColor(Color.parseColor("#990000"))
                .build().show();
                
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
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
    private Leave getLeaveById(int id) {
        Leave leave = new Leave();
        leave.setType("年假");
        leave.setStart(new Date());
        leave.setEnd(new Date());
        leave.setId(1);
        leave.setCreate(new Date());
        return leave;
    }

    public static LeaveDetailFragment newInstance(int id) {
        LeaveDetailFragment fragment = new LeaveDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_LEAVE_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

}
