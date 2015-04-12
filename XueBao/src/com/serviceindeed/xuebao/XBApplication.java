package com.serviceindeed.xuebao;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;

/**
 * ��Application
 * http://developer.baidu.com/map/wiki/index.php?title=android-locsdk/guide/v5-0
 */
public class XBApplication extends Application {
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	
	public TextView mLocationResult,logMsg;
	public ImageView mRefreshBtn;
	public Vibrator mVibrator;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
	}

	
	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			logMsg(location.getAddrStr());
		}
	}
	
	
	/**
	 * ��ʾ�����ַ���
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null && str!=null){
			    mLocationResult.setText(str);
			    mLocationClient.stop();
			    //ֹͣ����
			    if(mRefreshBtn != null) {
			        mRefreshBtn.clearAnimation();
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
