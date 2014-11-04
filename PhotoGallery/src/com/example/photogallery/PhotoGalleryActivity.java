package com.example.photogallery;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PhotoGalleryActivity extends SingleFragmentActivity {
    
    private static final String TAG = "PhotoGalleryActivity";

    @Override
    protected Fragment createFragment() {
        return new PhotoGalleryFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        
        //getIntent() 此方法获得的intent是启动当前activity的intent，而不是接收的最新intent.
        
        PhotoGalleryFragment fragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "Received a new search query: " + query);
            
            //获取shared preference对象并将query保存起来
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(FlickrFetcher.PREF_SEARCH_QUERY, query).commit();
        }
        fragment.updateItmes();
        
    }
    
    

}
