package com.example.criminalintent.v9;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.example.criminalintent.v2.SingleFragmentActivity;


public class CrimeCameraActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        //��������������
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //����ϵͳ״̬��
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        super.onCreate(savedInstanceState);
    }
    
    

}
