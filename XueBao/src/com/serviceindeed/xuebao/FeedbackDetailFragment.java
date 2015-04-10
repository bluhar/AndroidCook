package com.serviceindeed.xuebao;

import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.serviceindeed.xuebao.values.Feedback;

public class FeedbackDetailFragment extends Fragment {

    public static final String EXTRA_FEEDBACK_ID = "com.serviceindeed.xuebao.feedback_id";

    private Feedback           mFeedback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int feedbackID = (Integer) getArguments().getInt(EXTRA_FEEDBACK_ID);
        mFeedback = getFeedbackById(feedbackID);
        
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_detail, container, false);
        
        TextView title = (TextView) getActivity().findViewById(R.id.subActivityActionBarTitle);
        title.setText(mFeedback.getName());
        
        
        TextView feedbackItem = (TextView) view.findViewById(R.id.feedback_item);
        feedbackItem.setText(mFeedback.getContent());
        return view;
    }
    
    
    /**
     * TODO Get feedback by feedback id.
     * @param id
     * @return
     */
    private Feedback getFeedbackById(int id) {
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setContent("aaaaaaaaaaaaa");
        feedback.setName("TEST");
        feedback.setCreateDate(new Date());
        return feedback;
    }

    public static FeedbackDetailFragment newInstance(int id) {
        FeedbackDetailFragment fragment = new FeedbackDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_FEEDBACK_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

}
