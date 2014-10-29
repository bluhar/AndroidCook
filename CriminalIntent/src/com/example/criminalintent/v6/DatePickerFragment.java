package com.example.criminalintent.v6;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.example.criminalintent.R;

public class DatePickerFragment extends DialogFragment {
    
    public static final String EXTRA_DATE= "com.example.criminalintent.v4.date";
    
    private Date mDate;

    /**
     * ����Ļ����ʾDialogFragmentʱ���й�activity���������ô˷���
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        mDate = (Date) getArguments().get(EXTRA_DATE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new OnDateChangedListener() {
            
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                //����arguments�е����ݣ������豸ͻȻ�����ˣ���ô�����µ���onCreateDialog()����ʱ���Ϳ��Դ�arguments���ٴλ�ȡ����ʱ������
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title).setView(view).setPositiveButton(android.R.string.ok, new OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK);
            }
        }).create();

    }
    
    private void sendResult(int resultCode) {
        if(getTargetFragment() == null) {
            return;
        }
        
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);
        
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
    
    public static DatePickerFragment newInstance(Date date) {
        Bundle data = new Bundle();
        data.putSerializable(EXTRA_DATE, date);
        
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(data);
        return fragment;
    }

}
