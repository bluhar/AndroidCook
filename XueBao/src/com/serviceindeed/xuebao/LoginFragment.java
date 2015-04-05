package com.serviceindeed.xuebao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Read the login name from preference file.
    }
    
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        
        Button loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                //TODO Verify the login name and password
                //Pass the verify, then show the main page else shot a shot toast
                //Now, mock the result is success, show the main page.
                /*Intent i = new Intent(getActivity(), MainActivity.class);
                startActivityForResult(i, 1);*/
                
                Intent startIntent = new Intent(getActivity(), MainActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);        
                startActivity(startIntent);
            }
        });
        
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
    }

}
