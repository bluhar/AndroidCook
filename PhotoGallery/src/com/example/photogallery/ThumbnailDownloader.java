package com.example.photogallery;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG              = "ThumbnailDownloader";
    private static final int    MESSAGE_DOWNLOAD = 0;
    private Handler             mHandler;
    private Handler             mResponseHandler;
    Listener<T>                 mListener;
    Map<T, String>              requestMap       = Collections.synchronizedMap(new HashMap<T, String>());

    public interface Listener<T> {

        void onThumbnailDownloaded(T t, Bitmap thumbnail);
    }

    public void setListener(Listener<T> listener) {
        this.mListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        this.mResponseHandler = responseHandler;
    }

    private void handleRequest(final T t) {
        final String url = requestMap.get(t);
        if (url == null)
            return;

        try {
            byte[] bitmapBytes = new FlickrFetcher().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created.");

            //TODO ��ͼƬͨ�����߳����õ�UI�ϣ��������ֻ�����߳�ȥ����
            //�鿴POST������Դ�룬handler����һ��message��Ȼ������message��callbackΪ��ǰ��runnable����
            /*
             *  Message m = Message.obtain();
             *  m.callback = r;
             */
            mResponseHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (requestMap.get(t) != url) {
                        return;
                    }

                    requestMap.remove(t);
                    mListener.onThumbnailDownloaded(t, bitmap);

                }
            });
        }
        catch (IOException e) {
            Log.e(TAG, "Error downloading image", e);
        }
    }

    /*
     * onLooperPrepared����������Looper��һ�μ����Ϣ����֮ǰ
     */
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    T t = (T) msg.obj;
                    Log.i(TAG, "Got a request for url: " + requestMap.get(t));
                    handleRequest(t);
                }
            }

        };
    }

    public void queueThumbnail(T t, String url) {
        Log.i(TAG, "Got an URL: " + url);
        requestMap.put(t, url);

        //��Handler������Message�������͵�message queue��
        mHandler.obtainMessage(MESSAGE_DOWNLOAD, t).sendToTarget();
    }
    
    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
    
    

}
