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
		
		//如果是API 11及以上版本，调用getFragmentManager();
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if (fragment == null) {
			fragment = createFragment();
			//FragmentManager在Activity中，负责管理Activity的Fragment
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}
	
	
	
}
