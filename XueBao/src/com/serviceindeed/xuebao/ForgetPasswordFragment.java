package com.serviceindeed.xuebao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class ForgetPasswordFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
                
        Button submitBtn = (Button) view.findViewById(R.id.forget_pwd_submit);
        
        submitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "邮件发送成功！", Toast.LENGTH_SHORT).show();
                
            }
        });

        
                
        return view;
    }

    
    
}
