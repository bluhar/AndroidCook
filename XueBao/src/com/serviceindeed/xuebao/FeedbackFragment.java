package com.serviceindeed.xuebao;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.serviceindeed.xuebao.values.Feedback;

public class FeedbackFragment extends ListFragment {

    private Callbacks mCallbacks;

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
        //        setHasOptionsMenu(true);

        setRetainInstance(true); //保留Fragment

        //        mCrimes = CrimeLab.getInstance(getActivity()).getCrimes();

        FeedbackAdapter adapter = new FeedbackAdapter(getFeedbacks());
        setListAdapter(adapter);
    }

    private ArrayList<Feedback> getFeedbacks() {
        //TODO Get the feedback from remote service
        ArrayList<Feedback> feedbacks = new ArrayList<Feedback>();
        Feedback feedback = new Feedback();
        feedback.setContent("这道题好难哦。。。。");
        feedback.setCreateDate(new Date());
        feedbacks.add(feedback);
        return feedbacks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ListView listView = (ListView) v.findViewById(android.R.id.list);
        return v;
    }

    public void updateUI() {
        ((FeedbackAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((FeedbackAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Feedback item = (Feedback) getListAdapter().getItem(position);
        mCallbacks.onFeedbackSelected(item);
    }

    private class FeedbackAdapter extends ArrayAdapter<Feedback> {

        public FeedbackAdapter(ArrayList<Feedback> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_feedback, null);
            }

            Feedback feedback = getItem(position);

            TextView titleView = (TextView) convertView.findViewById(R.id.feedback_header);
            TextView contentView = (TextView) convertView.findViewById(R.id.feedback_content);

            titleView.setText(DateFormat.format("yyyy-MM-dd", feedback.getCreateDate()));
            contentView.setText(feedback.getContent());

            return convertView;
        }

    }
}
