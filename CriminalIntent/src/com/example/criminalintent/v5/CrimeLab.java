package com.example.criminalintent.v5;

import java.util.ArrayList;
import java.util.UUID;

import com.example.criminalintent.v1.Crime;

import android.content.Context;

public class CrimeLab {
	
	private static CrimeLab sCrimeLab;
	//为什么需要Context? 在Android中，有了Context，可以完成启动Activity、获取项目资源，查找应用的私有存储空间等任务
	private Context mAppContext;
	
	private ArrayList<Crime> mCrimes;
	
	
	private CrimeLab(Context appContext){
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
	}
	
	public static CrimeLab getInstance(Context c) {
		if (sCrimeLab == null) {
			//这里为什么没有直接使用context c呢？
			//因为这个c可以是activity，也可能是service，在APP的整个生命周期里，无法保证activity或service一直存在
			//所以为保证context一直可用，需要获取Application Context.
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}
	
	public Crime getCrime(UUID id){
		for (Crime crime : mCrimes) {
			if(crime.getId().equals(id)) {
				return crime;
			}
		}
		return null;
	}
	
	public void addCrime(Crime c) {
	    mCrimes.add(c);
	}

}
