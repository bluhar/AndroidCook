package cn.auchan.auchandrive.feng;

import java.net.CookieHandler;
import java.net.CookieManager;

import android.app.Application;


public class AuchanApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        
        
        
    }

}
