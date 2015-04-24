package cn.auchan.auchandrive.feng.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import cn.auchan.auchandrive.feng.R;
import cn.auchan.auchandrive.feng.fragment.HomeFragment;
import cn.auchan.auchandrive.feng.fragment.SlideMenuFragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 
 * @author anelm
 * Reference:
 * 1. 隐藏ActionBar 
 *      http://stackoverflow.com/questions/8500283/how-to-hide-action-bar-before-activity-is-created-and-then-show-it-again
 * 2. 在Android中使用icon font
 *      http://www.iconfont.cn/help/iconuse.html
 * 3. 使用开源的slidingmenu
 *      https://github.com/jfeinstein10/SlidingMenu
 */
public class MainActivity extends SlidingFragmentActivity implements HomeFragment.Callbacks{
    
    protected Fragment mFrag;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * 隐藏action bar logo & title
         * getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);*/
        /**
         * 隐藏action bar
         * 下面的链接有很多种方法，代码或定义style
         * http://stackoverflow.com/questions/8500283/how-to-hide-action-bar-before-activity-is-created-and-then-show-it-again
         */
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        
        /**
         * 设置滑动显示的view
         */
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getFragmentManager().beginTransaction();
            mFrag = new SlideMenuFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        } else {
            mFrag = (Fragment)this.getFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
        //设置当前activity的view
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new HomeFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

    }
    
    /**
     * 显示侧滑view
     */
    public void showSlider(){
        this.toggle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // TODO Auto-generated method stub
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onViewClick(int viewId) {
        if(viewId == R.id.left_slider) {
            this.showSlider();
        }
        
    }

}
