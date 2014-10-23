package com.example.criminalintent.v2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.criminalintent.R;

public abstract class SingleFragmentActivity extends FragmentActivity{

	protected abstract Fragment createFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		
		//如果是API 11及以上版本，调用getFragmentManager();
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainerV2);
		if (fragment == null) {
			fragment = createFragment();
			//FragmentManager在Activity中，负责管理Activity的Fragment
			fm.beginTransaction().add(R.id.fragmentContainerV2, fragment).commit();
		}
	}
	
	
	
}
