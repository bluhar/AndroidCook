package com.example.criminalintent.v10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.criminalintent.R;

public abstract class SingleFragmentActivity extends FragmentActivity{

	protected abstract Fragment createFragment();
	
	protected int getLayoutResId() {
	    return R.layout.activity_fragment;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResId());
		
		//�����API 11�����ϰ汾������getFragmentManager();
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if (fragment == null) {
			fragment = createFragment();
			//FragmentManager��Activity�У��������Activity��Fragment
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}
	
	
	
}
