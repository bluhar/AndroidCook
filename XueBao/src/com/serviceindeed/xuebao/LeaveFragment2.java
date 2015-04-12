package com.serviceindeed.xuebao;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.serviceindeed.xuebao.util.ScrollDetectingListView;
import com.serviceindeed.xuebao.values.Leave;
import com.serviceindeed.xuebao.values.Punch;


public class LeaveFragment2 extends Fragment implements AnimationListener{

    private Callbacks mCallbacks;
    ListAdapter mAdapter;
    ScrollDetectingListView mList;
    ImageView mImageView;
    
    String DATE_FORMATE = "yyyy-MM-dd hh:mm:ss";

    
    boolean hidedAnimation = false;
    private Animation mInAnim, mOutAnim;
    int mLastFirstVisibleItem;


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
    
    private void bindAnimation(){
        mList.setOnScrollListener(new OnScrollListener() {
            private int mPosition;
            private int mOffset;
            private int mInitialScroll;
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, 
                int visibleItemCount, int totalItemCount) {
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
               final ListView lw = mList;
                
                
//                final int currentFirstVisibleItem = lw.getFirstVisiblePosition();
//                if (!hidedAnimation && currentFirstVisibleItem > mLastFirstVisibleItem) {
//                    mImageView.startAnimation(mOutAnim);
//                }
//                else if (hidedAnimation && currentFirstVisibleItem < mLastFirstVisibleItem) {
//                    mImageView.startAnimation(mInAnim);
//                }

                if (view.getId() == lw.getId()) {
                    final int currentFirstVisibleItem = lw.getFirstVisiblePosition();
                    if (!hidedAnimation && currentFirstVisibleItem > mLastFirstVisibleItem) {
                        mImageView.startAnimation(mOutAnim);
                    }
                    else if (hidedAnimation && currentFirstVisibleItem < mLastFirstVisibleItem) {
                        mImageView.startAnimation(mInAnim);
                    }
                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_leave, container, false);

        mList = (ScrollDetectingListView) view.findViewById(R.id.leave_list_view);  
        mImageView = (ImageView) view.findViewById(R.id.createLeaveBtn);    
        mAdapter = new LeaveAdapter(mockLeaves()); //获取请假的list
        mList.setAdapter(mAdapter);
        
        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Leave item = (Leave) mAdapter.getItem(position);
                mCallbacks.onLeaveSelected(item);                
            }
        });
        
        mOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_to_bottom_punch);
        mOutAnim.setAnimationListener(LeaveFragment2.this);
        mInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_bottom_punch);
        mInAnim.setAnimationListener(LeaveFragment2.this);
        
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动leave detail activity
                mCallbacks.onLeaveSelected(null);
            }
        });
        
        bindAnimation();
        return view;
    }
    
    private ArrayList<Leave> mockLeaves(){
        ArrayList<Leave> list = new ArrayList<Leave>();
        for (int i = 0 ; i < 15 ; i++) {
            Leave leave = new Leave();
            leave.setType("年假");
            leave.setStart(new Date());
            leave.setEnd(new Date());
            leave.setId(1);
            leave.setCreate(new Date());
            leave.setReason("请假原因说明.....请假原因说明.....请假原因说明.....");
            list.add(leave);
        }
        return list;
    }
    
    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == mOutAnim) {
            hidedAnimation = true;
        } else if (animation == mInAnim) {
            hidedAnimation = false;
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == mOutAnim) {
            mImageView.setVisibility(View.GONE);
        } else if (animation == mInAnim) {
            mImageView.setVisibility(View.VISIBLE);
        }
        mImageView.clearAnimation();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
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
            TextView dateView = (TextView) convertView.findViewById(R.id.leave_list_item_leaveStartDateEndDate);
            TextView reasonView = (TextView) convertView.findViewById(R.id.leave_list_item_leaveReason);
            TextView createDateView = (TextView) convertView.findViewById(R.id.leave_list_item_leaveCreateDate);

            typeView.setText(leave.getType());
            CharSequence dateRange = DateFormat.format(DATE_FORMATE, leave.getStart()) + " 至 " + DateFormat.format(DATE_FORMATE, leave.getEnd());
            dateView.setText(dateRange);
            reasonView.setText(leave.getReason());
            createDateView.setText(DateFormat.format(DATE_FORMATE, leave.getCreate()));

            return convertView;
        }

    }
    
    
}
