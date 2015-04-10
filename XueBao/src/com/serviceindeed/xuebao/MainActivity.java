package com.serviceindeed.xuebao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.serviceindeed.xuebao.values.Feedback;
import com.serviceindeed.xuebao.values.Leave;
import com.serviceindeed.xuebao.values.Punch;

public class MainActivity extends FragmentActivity implements FeedbackFragment.Callbacks, PunchFragment.Callbacks, LeaveFragment2.Callbacks{
    
    public static final String EXTRA_LOGIN_STATUS = "com.serviceindeed.xuebao.login_status";

	/**
	 * 课堂反馈Fragment
	 */
	private FeedbackFragment feedFragment;

	/**
	 * 上下班打卡Fragment
	 */
	private PunchFragment punchFragment;

	/**
	 * 请假Fragment
	 */
	private LeaveFragment2 leaveFragment;

	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//setOverflowShowingAlways();
		
		dm = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

		
		String[] tabNames = this.getResources().getStringArray(R.array.tab_names);
		
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), tabNames));
		tabs.setViewPager(pager);
		setTabsValue();
		
		
		boolean loginSuccess = getIntent().getBooleanExtra(EXTRA_LOGIN_STATUS, false);
		
		if(!loginSuccess) {
		    /**
		     * 如果用户没有登录，跳转到登录page
		     */
		    Intent loginIntent = new Intent(this, LoginActivity.class);
		    startActivity(loginIntent);
		}
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
	    tabs.setBackgroundColor(Color.WHITE);
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#5AC8FD"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#5AC8FD"));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
		tabs.setTabPaddingLeftRight(20);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {
	    
//		private final String[] titles = { "课堂反馈", "打卡", "请假" };
	    private String[] titles;

		public MyPagerAdapter(FragmentManager fm, String[] tabNames) {
			super(fm);
			this.titles = tabNames;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (feedFragment == null) {
					feedFragment = new FeedbackFragment();
				}
				return feedFragment;
			case 1:
				if (punchFragment == null) {
					punchFragment = new PunchFragment();
				}
				return punchFragment;
			case 2:
				if (leaveFragment == null) {
					leaveFragment = new LeaveFragment2();
				}
				return leaveFragment;
			default:
				return null;
			}
		}

	}

	/**
	 * 在如何通过menu再创建popup menu, block了很久
	 * Reference:http://stackoverflow.com/questions/24634136/menu-popup-helper-cannot-be-used-without-anchor
	 * @param item
	 */
	public void showPopup(MenuItem item) {

        final View menuItemView = this.findViewById(item.getItemId());
	    PopupMenu popup = new PopupMenu(this, menuItemView);
	    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_ask_leave:
                        MainActivity.this.onLeaveSelected(null);
                        return true;
                    case R.id.action_ask_punch:
                        MainActivity.this.onPunchSelected(null);
                        return true;
                    case R.id.action_settings:
                        return true;
                    case R.id.action_about:
                        return true;
                    default:
                        return false;
                }            
            }
        });
	    
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.main_popup_menu, popup.getMenu());
	    popup.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_popup_menu:
                this.showPopup(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	

//	@Override
//	public boolean onMenuOpened(int featureId, Menu menu) {
//		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
//			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
//				try {
//					Method m = menu.getClass().getDeclaredMethod(
//							"setOptionalIconsVisible", Boolean.TYPE);
//					m.setAccessible(true);
//					m.invoke(menu, true);
//				} catch (Exception e) {
//				}
//			}
//		}
//		return super.onMenuOpened(featureId, menu);
//	}

//	private void setOverflowShowingAlways() {
//		try {
//		    //这里利用反射找到sHasPermanentMenuKey属性是要做什么？
//			ViewConfiguration config = ViewConfiguration.get(this);
//			Field menuKeyField = ViewConfiguration.class
//					.getDeclaredField("sHasPermanentMenuKey");
//			menuKeyField.setAccessible(true);
//			menuKeyField.setBoolean(config, false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    @Override
    public void onFeedbackSelected(Feedback feedback) {
        //show the feedback detail fragment
       /* Intent i = new Intent(this, FeedbackDetailActivity.class);
        i.putExtra(FeedbackDetailFragment.EXTRA_FEEDBACK_ID, feedback.getId());
        startActivity(i);*/
    }

    @Override
    public void onLeaveSelected(Leave leave) {
        //当请假item被点击后，
        Intent i = new Intent(this, LeaveDetailActivity.class);
        if (leave != null) {
            i.putExtra(LeaveDetailFragment.EXTRA_LEAVE_ID, leave.getId());
        }
        startActivity(i);
    }

    @Override
    public void onPunchSelected(Punch punch) {
        Intent i = new Intent(this, PunchDetailActivity.class);
        if (punch != null) {
            i.putExtra(PunchDetailFragment.EXTRA_PUNCH_ID, punch.getId());
        }
        startActivity(i);
    }

}