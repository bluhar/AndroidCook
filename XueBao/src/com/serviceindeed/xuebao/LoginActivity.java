package com.serviceindeed.xuebao;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends SingleFragmentActivity {
    
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        //使用自定义的action bar
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        getActionBar().setCustomView(R.layout.sub_activity_actionbar);
        View backBtn = getActionBar().getCustomView().findViewById(R.id.subActivityActionBarBackBtn);
        TextView title = (TextView) getActionBar().getCustomView().findViewById(R.id.subActivityActionBarTitle);
        backBtn.setVisibility(View.GONE);
        title.setText(R.string.login_text);
        
        super.onCreate(savedInstanceState);
    }
    
    

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();
        }
    }*/

}
