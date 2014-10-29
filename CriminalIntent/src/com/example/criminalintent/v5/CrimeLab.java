package com.example.criminalintent.v5;

import java.util.ArrayList;
import java.util.UUID;

import com.example.criminalintent.v1.Crime;

import android.content.Context;

public class CrimeLab {
	
	private static CrimeLab sCrimeLab;
	//Ϊʲô��ҪContext? ��Android�У�����Context�������������Activity����ȡ��Ŀ��Դ������Ӧ�õ�˽�д洢�ռ������
	private Context mAppContext;
	
	private ArrayList<Crime> mCrimes;
	
	
	private CrimeLab(Context appContext){
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
	}
	
	public static CrimeLab getInstance(Context c) {
		if (sCrimeLab == null) {
			//����Ϊʲôû��ֱ��ʹ��context c�أ�
			//��Ϊ���c������activity��Ҳ������service����APP����������������޷���֤activity��serviceһֱ����
			//����Ϊ��֤contextһֱ���ã���Ҫ��ȡApplication Context.
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
