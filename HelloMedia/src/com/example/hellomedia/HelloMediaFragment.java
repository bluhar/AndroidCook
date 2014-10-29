package com.example.hellomedia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HelloMediaFragment extends Fragment {

    private Button mPlayBtn;
    private Button mStopBtn;
    
    private AudioPalyer mPlayer = new AudioPalyer();
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //可以保留fragment实例：可销毁和重建fragment的视图，但无需销毁fragment自身。
        //保留fragment实例的时间很短，而activity,fragment的onSaveInstanceState()方法保存的时间与【activity记录】的时间一样长。
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_hello_media, container, false);
        mPlayBtn = (Button) view.findViewById(R.id.helloMedia_palyBtn);
        mStopBtn = (Button) view.findViewById(R.id.helloMedia_stopBtn);
        
        mPlayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.play(getActivity());
            }
        });
        mStopBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
            }
        });

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }
    
    

}
