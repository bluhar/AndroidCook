package com.serviceindeed.xuebao;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class FeedbackDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        
        //updateBackButtonAndHomeLogo(true, false);
        //设置自定义的action bar
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        getActionBar().setCustomView(R.layout.sub_activity_actionbar);
        getActionBar().getCustomView().findViewById(R.id.subActivityActionBarBackBtn).setOnClickListener(new OnClickListener() {//监听事件  
            @Override  
            public void onClick(View item) {
                finish();
            }
        });  
        

        int id = getIntent().getIntExtra(FeedbackDetailFragment.EXTRA_FEEDBACK_ID, -1);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = FeedbackDetailFragment.newInstance(id);
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                this.finish();
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void updateBackButtonAndHomeLogo(boolean showBackButton, boolean showHomeLogo){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(this) != null) {
                //显示返回按钮
                getActionBar().setDisplayHomeAsUpEnabled(showBackButton);
                //去掉返回按钮旁的LOGO
                getActionBar().setDisplayShowHomeEnabled(showHomeLogo);  
            }
        }
    }

}
