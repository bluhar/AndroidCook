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
 * 1. ΪʲôҪ�̳�android.support.v4.app.FragmentActivity��������Activity ?
 * Fragment����API 11���вű����룬�˰汾��֮��汾��Activity֪������Fragment�������ǵ�MIN SDK VERSION��API 8
 * ������Ҫ����android.support.v4�е�Fragment��Ϊ������Щfragment��Ҳ����ҪFragmentActivity��
 * 
 * 2. Fragment���������ڷ��������й�Activity���õģ���Activity���������ڷ�������ϵͳ���õġ�
 * 
 * @author anelm
 *
 */
public class CrimeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cime);
		
		//�����API 11�����ϰ汾������getFragmentManager();
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if (fragment == null) {
			fragment = new CrimeFragment();
			//FragmentManager��Activity�У��������Activity��Fragment
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}

}
