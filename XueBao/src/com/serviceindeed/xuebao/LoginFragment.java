package com.serviceindeed.xuebao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginFragment extends Fragment {

    private boolean restart = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
//                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                /**
                 * 如果登录成功，传参数
                 */
                startIntent.putExtra(MainActivity.EXTRA_LOGIN_STATUS, true);
                startActivity(startIntent);
            }
        });
        
        TextView forgetPwdView = (TextView) view.findViewById(R.id.forget_pwd);
        forgetPwdView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(i);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
