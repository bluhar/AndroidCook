package com.serviceindeed.xuebao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class FeedbackDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        int id = getIntent().getIntExtra(FeedbackDetailFragment.EXTRA_FEEDBACK_ID, -1);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = FeedbackDetailFragment.newInstance(id);
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

}
