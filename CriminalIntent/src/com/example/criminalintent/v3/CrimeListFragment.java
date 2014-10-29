package com.example.criminalintent.v3;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.criminalintent.R;
import com.example.criminalintent.v1.Crime;
import com.example.criminalintent.v1.CrimeFragment;
import com.example.criminalintent.v2.CrimeLab;

/**
 * ListFragment中内置了ListView, ListView向adapter申请视图
 * 
 * 如何定制ListView的视图：
 * 		创建自定义列表项视图的XML布局文件
 * 		创建ArrayAdapter<T>的子类，用来创建、填充并返回定义在新布局中的视图
 * @author anelm
 *
 */
public class CrimeListFragment extends ListFragment{
	
	private static final String TAG = CrimeListFragment.class.getSimpleName();
	
	private static final int REQUEST_CRIME = 1;
	
	private ArrayList<Crime> mCrimes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.getInstance(getActivity()).getCrimes();
		
//		ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
		
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		
		setListAdapter(adapter);
		
		
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime item = (Crime) getListAdapter().getItem(position);
		Log.d(TAG, item.getTitle() + " was clicked.");
		
		//Start CrimeActivity
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, item.getId());
		//startActivity(i);
		startActivityForResult(i, REQUEST_CRIME);
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CRIME) {
			//handle result
		}
	}



	private class CrimeAdapter extends ArrayAdapter<Crime>{

		public CrimeAdapter(ArrayList<Crime> objects) {
			super(getActivity(), 0, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
			}
			
			Crime crime = getItem(position);
			
			TextView titleView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
			TextView dateView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
			CheckBox solvedView = (CheckBox) convertView.findViewById(R.id.crime_lsit_item_solveCheckBox);
			
			titleView.setText(crime.getTitle());
			dateView.setText(crime.getDate().toString());
			solvedView.setChecked(crime.isSolved());
			
			return convertView;
		}

		
	}
	
	
	

	
	
}
