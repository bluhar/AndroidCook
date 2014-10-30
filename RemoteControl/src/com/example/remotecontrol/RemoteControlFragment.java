package com.example.remotecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RemoteControlFragment extends Fragment {

    private TextView mSelectedTextView;
    private TextView mWorkingTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_remote_control, container, false);
        mSelectedTextView = (TextView) v.findViewById(R.id.fragment_remote_control_selectedTextView);
        mWorkingTextView = (TextView) v.findViewById(R.id.fragment_remote_control_workingTextView);

        OnClickListener buttonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //这里为什么是转成TextView，因为Button继承自TextView，所以是可以转的。
                TextView textView = (TextView) v;
                String working = mWorkingTextView.getText().toString();
                String text = textView.getText().toString();
                if (working.equals("0")) {
                    mWorkingTextView.setText(text);
                }
                else {
                    mWorkingTextView.setText(working + text);
                }
            }
        };

//        Button zeroBtn = (Button) v.findViewById(R.id.fragment_remote_control_zeroButton);
//        Button oneBtn = (Button) v.findViewById(R.id.fragment_remote_control_oneButton);
//        Button enterBtn = (Button) v.findViewById(R.id.fragment_remote_control_enterButton);
//        zeroBtn.setOnClickListener(buttonClickListener);
//        oneBtn.setOnClickListener(buttonClickListener);
        
        TableLayout tableLayout = (TableLayout) v.findViewById(R.id.fragment_remote_control_tableLayout);
        int number = 1;
        for (int i = 2 ; i < tableLayout.getChildCount() - 1 ; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0 ; j < row.getChildCount() ; j++) {
                Button button = (Button) row.getChildAt(j);
                button.setText("" + number);
                button.setOnClickListener(buttonClickListener);
                number++;
            }
        }
        
        TableRow bottomRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount()-1);
        Button deleteBtn = (Button) bottomRow.getChildAt(0);
        Button zeroBtn = (Button) bottomRow.getChildAt(1);
        Button enterBtn = (Button) bottomRow.getChildAt(2);
        deleteBtn.setText("Delete");
        zeroBtn.setText("0");
        enterBtn.setText("Enter");
        zeroBtn.setOnClickListener(buttonClickListener);
        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkingTextView.setText("0");
            }
        });
        


        enterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence working = mWorkingTextView.getText();
                if (working.length() > 0) {
                    mSelectedTextView.setText(working);
                }
                mWorkingTextView.setText("0");
            }
        });

        return v;
    }

}
