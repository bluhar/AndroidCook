package com.example.criminalintent.v1;

import com.example.criminalintent.R;
import com.example.criminalintent.R.id;
import com.example.criminalintent.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 
 * 1. 为什么要继承android.support.v4.app.FragmentActivity，而不是Activity ?
 * Fragment是在API 11级中才被引入，此版本及之后版本的Activity知道管理Fragment，而我们的MIN SDK VERSION是API 8
 * 所以需要引入android.support.v4中的Fragment，为管理这些fragment，也就需要FragmentActivity了
 * 
 * 2. Fragment的生命周期方法是由托管Activity调用的，而Activity的生命周期方法是由系统调用的。
 * 
 * @author anelm
 *
 */
public class CrimeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cime);
		
		//如果是API 11及以上版本，调用getFragmentManager();
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if (fragment == null) {
			fragment = new CrimeFragment();
			//FragmentManager在Activity中，负责管理Activity的Fragment
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}

}
