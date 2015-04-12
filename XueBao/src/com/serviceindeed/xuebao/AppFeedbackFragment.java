package com.serviceindeed.xuebao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class AppFeedbackFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_feedback, container, false);
        
        final TextView contentView = (TextView) view.findViewById(R.id.app_feedback_content);
        
        
        View submitBtn = view.findViewById(R.id.setting_app_feedback_submit);
        submitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.setting_app_feedback_success, Toast.LENGTH_SHORT).show();
                contentView.setText(null);
            }
        });

        return view;
    }

    
    
}
