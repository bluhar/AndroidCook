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
        
        //getIntent() �˷�����õ�intent��������ǰactivity��intent�������ǽ��յ�����intent.
        
        PhotoGalleryFragment fragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "Received a new search query: " + query);
            
            //��ȡshared preference���󲢽�query��������
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(FlickrFetcher.PREF_SEARCH_QUERY, query).commit();
        }
        fragment.updateItmes();
        
    }
    
    

}
