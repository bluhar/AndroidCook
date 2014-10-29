package com.example.criminalintent.v9;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.example.criminalintent.R;

public class CrimeFragment extends Fragment {

    private static final String TAG             = "CrimeFragment";
    public static final String  EXTRA_CRIME_ID  = "com.example.criminalintent.v1.crime_id";
    public static final String  DIALOG_DATE     = "date";
    public static final String  DIALOG_IMAGE    = "image";
    public static final int     REQUEST_DATE    = 0;
    public static final int     REQUEST_PHOTO   = 1;
    public static final int     REQUEST_CONTACT = 2;
    public static final String  DATE_FORMATE    = "yyyy-MM-dd";

    private Crime               mCrime;
    private EditText            mTitleField;
    private Button              mDateButton;
    private Button              mSuspectButton;
    private Button              mReportButton;
    private CheckBox            mSolvedCheckBox;
    private ImageButton         mImageButton;
    private ImageView           mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //通知FragmentManager: CrimeFragment将代表其托管activity实现选项菜单相关的回调方法
        setHasOptionsMenu(true);

        //UUID crimeID = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        //尽量避免fragment与托管activity的直接引用，保持fragment的通用性
        UUID crimeID = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeID);
    }

    /**
     * 创建和配置Fragment视图是通过onCreateView()完成，而不是onCreate().
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //container是Fragment的父视图，第三个参数告知布局生成器是否将生成的视图添加的父视图
        View view = inflater.inflate(R.layout.fragment_crime_v9, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDateButton = (Button) view.findViewById(R.id.crime_date);
        mDateButton.setText(DateFormat.format(DATE_FORMATE, mCrime.getDate()));
        mDateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                //DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                //显示一个自定义的dialog fragment
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mImageButton = (ImageButton) view.findViewById(R.id.crime_imageButton);
        mImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        PackageManager pm = getActivity().getPackageManager();
        boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) || Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD || Camera.getNumberOfCameras() > 0;
        if (!hasCamera) {
            mImageButton.setEnabled(false);
        }

        mImageView = (ImageView) view.findViewById(R.id.crime_imageView);
        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Photo photo = mCrime.getPhoto();
                if (photo == null) {
                    return;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                String fileName = photo.getFileName();
                String path = getActivity().getFileStreamPath(fileName).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
            }
        });

        mSuspectButton = (Button) view.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //隐式的获取联系人intent
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);

            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        mReportButton = (Button) view.findViewById(R.id.crime_reportButton);
        mReportButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //创建一个隐式Intent，action是send，数据类型是text/plain
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });
        
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        //如果没有一个activity能够处理当前的隐式intent，将此功能禁用。
        List<ResolveInfo> activities = pm.queryIntentActivities(i, 0);
        boolean isSendIntentSafe = activities.size() > 0;
        if(!isSendIntentSafe) {
            mReportButton.setEnabled(false);
        }
        

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(DateFormat.format(DATE_FORMATE, date));
        }
        else if (requestCode == REQUEST_PHOTO) {
            String fileName = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (fileName != null) {
                Log.i(TAG, "File name: " + fileName);
                Photo p = new Photo(fileName);
                mCrime.setPhoto(p);
                Log.i(TAG, "Crime: " + mCrime.getTitle() + " has a photo.");
                showPhoto();
            }
        }
        else if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();

            String[] queryFields = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
            Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            if (cursor.getCount() == 0) {
                cursor.close();
                return;
            }

            cursor.moveToFirst();
            String suspect = cursor.getString(0);
            mSuspectButton.setText(suspect);
            cursor.close();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mImageView);
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;

    }

    public void returnResult() {
        //注意这里需要调用托管Activity来设置返回结果
        //fragment自己无法设置 返回结果，只有activity可以设置
        getActivity().setResult(Activity.RESULT_OK, null);
    }

    private void showPhoto() {
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mImageView.setImageDrawable(b);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        }
        else {
            solvedString = getString(R.string.crime_report_solved);
        }

        String dateStr = DateFormat.format(DATE_FORMATE, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else {
            suspect = getString(R.string.crime_report_suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateStr, solvedString, suspect);
        return report;
    }

}
