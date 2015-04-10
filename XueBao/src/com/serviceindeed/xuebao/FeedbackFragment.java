package com.serviceindeed.xuebao;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.serviceindeed.xuebao.values.Feedback;

public class FeedbackFragment extends Fragment {

    private Callbacks mCallbacks;
    ArrayAdapter<Feedback> mAdapter;
    PullToRefreshListView mPullToRefreshView;

    //任何打算托管FeedBackFragment的Activity都要实现此接口
    public interface Callbacks {
        void onFeedbackSelected(Feedback feedback);
    }

    //在fragment附加给activity时调用此方法
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //从下面这行代码也可以看出，任何打算托管CrimeListFragment的Activity都要实现此接口
        mCallbacks = (Callbacks) activity;
        //getActivity().setTitle(R.string.feedback_title);
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
        //setHasOptionsMenu(true);

        setRetainInstance(true); //保留Fragment
    }

    private ArrayList<Feedback> getFeedbacks() {
        //TODO Get the feedback from remote service
        ArrayList<Feedback> feedbacks = new ArrayList<Feedback>();
        for (int i = 0 ; i < 20 ; i++) {
            Feedback feedback = new Feedback();
            feedback.setContent("Reported to work on a gingerbread device. Note that you'll need to relate android:padding of the LinearLayout to the android:width shape/stroke's value. Please, do not use @android:color/white in your final application but rather a project defined color.");
            feedback.setCreateDate(new Date());
            feedback.setName("小明" + i);
            feedbacks.add(feedback);
        }
        return feedbacks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        mPullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.feedback_list_view);    
        
        mAdapter = new FeedbackAdapter(getFeedbacks()); 
        final ListView actualView = mPullToRefreshView.getRefreshableView();
        
        mPullToRefreshView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //这里写下拉刷新的任务  
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //这里写上拉加载更多的任务  
                new GetDataTask().execute();
            }
        });
        
        actualView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里可能是因为pull to refresh为list view添加了header, 所以index不对了
                //http://stackoverflow.com/questions/11106397/listview-addheaderview-causes-position-to-increase-by-one
                //https://github.com/naver/android-pull-to-refresh/issues/6
                Feedback item = (Feedback) actualView.getItemAtPosition(position);
                //Feedback item = (Feedback) mAdapter.getItem(position);
                mCallbacks.onFeedbackSelected(item);
            }
        });
        actualView.setAdapter(mAdapter);
        
        return view;
    }
    
    /**
     * 加载数据
     * @author anelm
     */
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            updateUI();
            mPullToRefreshView.onRefreshComplete();
            super.onPostExecute(result);
        }

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void updateUI() {
        ((FeedbackAdapter) mAdapter).notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FeedbackAdapter) mAdapter).notifyDataSetChanged();
    }

    private class FeedbackAdapter extends ArrayAdapter<Feedback> {

        public FeedbackAdapter(ArrayList<Feedback> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_feedback, null);
            }

            Feedback feedback = getItem(position);

            TextView titleView = (TextView) convertView.findViewById(R.id.feedback_name);
            TextView dateView = (TextView) convertView.findViewById(R.id.feedback_datetime);
            TextView contentView = (TextView) convertView.findViewById(R.id.feedback_content);

            titleView.setText(feedback.getName());
            dateView.setText(DateFormat.format("yyyy-MM-dd", feedback.getCreateDate()));
            contentView.setText(feedback.getContent());

            return convertView;
        }

    }
}
