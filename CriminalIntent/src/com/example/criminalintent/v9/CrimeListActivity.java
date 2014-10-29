package com.example.criminalintent.v9;

import android.support.v4.app.Fragment;

import com.example.criminalintent.v2.SingleFragmentActivity;

public class CrimeListActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}

}
