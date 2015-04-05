package com.serviceindeed.xuebao;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class LoginActivity extends SingleFragmentActivity {
    
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();
        }
    }*/

}
