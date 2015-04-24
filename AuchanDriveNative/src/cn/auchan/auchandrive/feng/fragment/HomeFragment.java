package cn.auchan.auchandrive.feng.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.auchan.auchandrive.feng.R;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;


public class HomeFragment extends Fragment{
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
    private Callbacks           mCallbacks;

    
    
    //任何打算托管CrimeListFragment的Activity都要实现此接口
    public interface Callbacks {
        void onViewClick(int viewId);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        
        TextView slider = (TextView) v.findViewById(R.id.left_slider);
        slider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onViewClick(v.getId());
            }
        });
        
        mPullRefreshScrollView = (PullToRefreshScrollView) v.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }
        });

        mScrollView = mPullRefreshScrollView.getRefreshableView();
        
        
        return v;
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Do some stuff here

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshScrollView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
    
}
