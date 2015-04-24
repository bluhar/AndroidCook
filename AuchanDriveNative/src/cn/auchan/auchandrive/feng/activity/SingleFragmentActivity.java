package cn.auchan.auchandrive.feng.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import cn.auchan.auchandrive.feng.R;

public abstract class SingleFragmentActivity extends Activity{

	protected abstract Fragment createFragment();
	
	protected int getLayoutResId() {
	    return R.layout.activity_fragment;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        
        //如果是API 11及以上版本，调用getFragmentManager();
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            //FragmentManager在Activity中，负责管理Activity的Fragment
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }
    
	
}
