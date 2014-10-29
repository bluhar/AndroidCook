package com.example.criminalintent.v10;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.example.criminalintent.R;

public class CrimePagerActivity extends FragmentActivity implements CrimeFragment.Callbacks{
	
	private ViewPager mViewPager;
	private ArrayList<Crime> mCrimes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UUID id = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		
		//通过代码来定义布局，但ID还是要在XML文件中定义
		//ViewPager 只存在于支持库中，没有标准的ViewPager类...
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mCrimes = CrimeLab.getInstance(this).getCrimes();
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				return mCrimes.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Crime crime = mCrimes.get(pos);
				return CrimeFragment.newInstance(crime.getId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Crime crime = mCrimes.get(arg0);
				if(crime.getTitle() != null) {
					setTitle(crime.getTitle());
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		for (int i = 0; i < mCrimes.size(); i++) {
			if(mCrimes.get(i).getId().equals(id)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
		
	}

    @Override
    public void onCrimeUpdated(Crime crime) {
        
    }

	
	

}
