package com.example.photogallery;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.photogallery.ThumbnailDownloader.Listener;

public class PhotoGalleryFragment extends VisibleFragment {

    private static final String            TAG = "PhotoGalleryFragment";

    private GridView                       mGridView;
    private ArrayList<GalleryItem>         mItems;
    private ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        updateItmes();

        //启动service
        //        Intent i = new Intent(getActivity(), PollService.class);
        //        getActivity().startService(i);
        //PollService.setServiceAlarm(getActivity(), true);

        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailThread.setListener(new Listener<ImageView>() {

            @Override
            public void onThumbnailDownloaded(ImageView t, Bitmap thumbnail) {
                if (isVisible()) {
                    t.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailThread.start(); //启动handlerThread,会去创建Looper(查看源代码)
        mThumbnailThread.getLooper();
        Log.i(TAG, "Background thread started.");
    }

    public void updateItmes() {
        //后台执行AsyncTask
        new FetchItemsTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit(); //结束线程
        Log.i(TAG, "Background thread destroyed.");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridView);

        setupAdapter();

        return view;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            MenuItem searchItem = menu.findItem(R.id.menu_item_search);
            SearchView searchView = (SearchView) searchItem.getActionView();

            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            ComponentName name = getActivity().getComponentName();

            SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
            searchView.setSearchableInfo(searchInfo);
        }
    }
    
    /**
     * 在3.0以前版本中，每次显示菜单时都会调用此方法
     * 在3.0版本之后版本，需要通过调用invalidateOptionsMenu()方法来通知才会调用此方法
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        
        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if(PollService.isServiceAlarmOn(getActivity())) {
            toggleItem.setTitle(R.string.stop_polling);
        }
        else {
            
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_search:
                getActivity().onSearchRequested(); //触发search动作
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(FlickrFetcher.PREF_SEARCH_QUERY, null).commit();
                updateItmes();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                    getActivity().invalidateOptionsMenu();
                }
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setupAdapter() {
        if (getActivity() == null || mGridView == null)
            return;

        if (mItems != null) {
            //mGridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(), android.R.layout.simple_gallery_item, mItems));
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        }
        else {
            mGridView.setAdapter(null);
        }
    }

    /**
     * 
     * 以下是内部类
     */

    //AsyncTask的第三个泛型参数，是doInBackground()方法的返回类型，也是onPostExecute()的形参类型。
    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>> {

        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {
            /*String urlSpec = "http://www.google.com";
            try {
                String result = new FlickrFetcher().getUrl(urlSpec);
                Log.i(TAG, "Fetched contents of URL: " + result);
            }
            catch (IOException e) {
                Log.e(TAG, "Failed to fetch URL: " + urlSpec, e);
            }*/

            Activity activity = getActivity();
            if (activity == null) {
                return new ArrayList<GalleryItem>();
            }

            String query = PreferenceManager.getDefaultSharedPreferences(activity).getString(FlickrFetcher.PREF_SEARCH_QUERY, null);
            if (query != null) {
                return new FlickrFetcher().search(query);
            }
            else {
                return new FlickrFetcher().fetchItems();
            }

        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> result) {
            mItems = result;
            setupAdapter();
        }

    }

    /**
     * 自定义GridView的adapter
     */
    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem> {

        public GalleryItemAdapter(ArrayList<GalleryItem> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.gallery_default);

            GalleryItem item = getItem(position);
            mThumbnailThread.queueThumbnail(imageView, item.getUrl());

            return convertView;
        }

    }

}
