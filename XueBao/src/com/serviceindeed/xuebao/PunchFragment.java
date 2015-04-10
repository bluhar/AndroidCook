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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.serviceindeed.xuebao.values.Punch;

public class PunchFragment extends Fragment implements AnimationListener{
    
    private Callbacks mCallbacks;
    ListAdapter mAdapter;
    ListView mList;
    ImageView mImageView;
    boolean hidedAnimation = false;
    private Animation mInAnim, mOutAnim;
    int mLastFirstVisibleItem;

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

    private void bindAnimation(){
        mList.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, 
                int visibleItemCount, int totalItemCount) {
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final ListView lw = mList;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        final View view = inflater.inflate(R.layout.fragment_punch, container, false);

        mList = (ListView) view.findViewById(R.id.punch_list_view);    
        mImageView = (ImageView) view.findViewById(R.id.createPunchBtn);    
        mAdapter = new PunchAdapter(mockPunchs()); //获取请假的list
        mList.setAdapter(mAdapter);
        
        mOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_to_bottom_punch);
        mOutAnim.setAnimationListener(PunchFragment.this);
        
        mInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_bottom_punch);
        mInAnim.setAnimationListener(PunchFragment.this);
        
        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Punch item = (Punch) mAdapter.getItem(position);
                mCallbacks.onPunchSelected(item);                
            }
        });
        
        this.bindAnimation();
        
        //监听当view都被渲染后，只有组件被渲染后才可以获得控件的位置
        // set a global layout listener which will be called when the layout pass is completed and the view is drawn
//        view.getViewTreeObserver().addOnGlobalLayoutListener(
//            new ViewTreeObserver.OnGlobalLayoutListener() {
//                public void onGlobalLayout() {
//                    //Remove the listener before proceeding
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                    else {
//                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    }
//    
//                    // measure your views here
//                    int x = Math.round(mImageView.getX());
//                    int y = Math.round(mImageView.getY());
//                    bindAnimation(x,y);
//                }
//        });

        
        mImageView.setOnClickListener(new OnClickListener() {
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
        for (int i = 0 ; i < 20 ; i++) {
            Punch punch = new Punch();
            punch.setType("上班");
            punch.setId(i);
            punch.setCreate(new Date());
            punch.setLocation("上海市杨浦区国权路525号");
            if(i%2 == 1) {
                punch.setRemark("签到测试....");
            }
            list.add(punch);            
        }
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
            TextView location = (TextView) convertView.findViewById(R.id.punch_list_item_location_text);

            typeView.setText(punch.getType());
            dateView.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", punch.getCreate()));
            location.setText(punch.getLocation());
            if(punch.getRemark() == null || punch.getRemark().isEmpty()) {
                reasonView.setVisibility(View.GONE);
            }
            else {
                reasonView.setText(punch.getRemark());
            }

            return convertView;
        }

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
            hidedAnimation = true;
        } else if (animation == mInAnim) {
            mImageView.setVisibility(View.VISIBLE);
            hidedAnimation = false;
        }
        mImageView.clearAnimation();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
    
}
