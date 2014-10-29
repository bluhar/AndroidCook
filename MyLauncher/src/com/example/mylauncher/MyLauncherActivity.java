package com.example.mylauncher;

import android.support.v4.app.Fragment;

public class MyLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MyLauncherFragment();
    }

}
