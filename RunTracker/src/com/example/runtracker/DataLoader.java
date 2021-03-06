package com.example.runtracker;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * 更加通用的数据加载Loader
 * @author anelm
 *
 * @param <D>
 */
public abstract class DataLoader<D> extends AsyncTaskLoader<D> {

    private D mData;

    public DataLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(D data) {
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {

        if (mData != null) {
            deliverResult(mData);
        }
        else {
            forceLoad();
        }

    }

}
