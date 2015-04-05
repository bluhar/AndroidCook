package com.serviceindeed.xuebao;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class PunchDetailActivity extends FragmentActivity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //在activity的action bar上添加返回按钮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(this) != null) {
                //显示返回按钮
                getActionBar().setDisplayHomeAsUpEnabled(true);
                //去掉返回按钮旁的LOGO
                getActionBar().setDisplayShowHomeEnabled(false);
            }
        }

        int id = getIntent().getIntExtra(PunchDetailFragment.EXTRA_PUNCH_ID, -1);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = PunchDetailFragment.newInstance(id);
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
//                if (NavUtils.getParentActivityName(this) != null) {
//                    NavUtils.navigateUpFromSameTask(this);
//                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
