package com.example.criminalintent.v10;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.criminalintent.R;

public class CrimeCameraFragment extends Fragment {

    private static final String    TAG                  = "CrimeCameraFragment";

    private Camera                 mCamera;
    private SurfaceView            mSurfaceView;
    private View                   mProgressContainer;

    public static final String     EXTRA_PHOTO_FILENAME = "com.example.criminalintent.v8.phote_filename";

    private Camera.ShutterCallback mShutterCallback     = new ShutterCallback() {

                                                            @Override
                                                            public void onShutter() {
                                                                mProgressContainer.setVisibility(View.VISIBLE);
                                                            }
                                                        };

    private Camera.PictureCallback mJpgCallback         = new PictureCallback() {

                                                            @Override
                                                            public void onPictureTaken(byte[] data, Camera camera) {
                                                                String fileName = UUID.randomUUID().toString() + ".jpg";
                                                                boolean success = true;
                                                                FileOutputStream output = null;
                                                                try {
                                                                    output = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                                                                    output.write(data);
                                                                }
                                                                catch (Exception e) {
                                                                    Log.e(TAG, "Error writing to file " + fileName, e);
                                                                    success = false;
                                                                }
                                                                finally {
                                                                    if (output != null) {
                                                                        try {
                                                                            output.close();
                                                                        }
                                                                        catch (IOException e) {
                                                                            Log.e(TAG, "Error closing file " + fileName, e);
                                                                            success = false;
                                                                        }
                                                                    }
                                                                }

                                                                if (success) {
                                                                    Intent i = new Intent();
                                                                    i.putExtra(EXTRA_PHOTO_FILENAME, fileName);
                                                                    getActivity().setResult(Activity.RESULT_OK, i);
                                                                    Log.i(TAG, "JPEG saved at " + fileName);
                                                                }
                                                                else {
                                                                    getActivity().setResult(Activity.RESULT_CANCELED);
                                                                }

                                                                getActivity().finish();
                                                            }
                                                        };

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);
        Button takeBtn = (Button) view.findViewById(R.id.crime_camera_takePictureButton);
        takeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.takePicture(mShutterCallback, null, mJpgCallback);
                }
            }
        });

        mSurfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        //虽然下面的代码已经作废，但是在HONEYCOMB版本之前，相机预览仍然需要下面的代码。
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //Surface对象代表着原始像素数据的缓冲区
        //Surface对象也有生命周期：SurfaceView出现在屏幕上时，会创建Surface;SurfaceView从屏幕上消失时，surface随即被销毁
        //Surface不存在时，必须保证没有任何内容要在上面绘制。
        holder.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(holder);
                    }
                }
                catch (IOException e) {
                    Log.e(TAG, "Error setting up preview display.", e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null)
                    return;

                Parameters parameters = mCamera.getParameters();
                Size s = getBestSupportedsSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(s.width, s.height);
                parameters.setPictureSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                }
                catch (Exception e) {
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }

            }
        });

        mProgressContainer = view.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
        }
        else {
            mCamera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private Size getBestSupportedsSize(List<Size> sizes, int width, int height) {
        Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;

        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }

        return bestSize;
    }

}
