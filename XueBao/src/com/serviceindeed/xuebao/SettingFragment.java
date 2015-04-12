package com.serviceindeed.xuebao;

import com.serviceindeed.xuebao.util.CommonUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class SettingFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        TextView version = (TextView) view.findViewById(R.id.setting_version);
        String versionName = CommonUtil.getVersionName(getActivity());
        if(versionName!=null){
            version.setText("V " + versionName);
        }
        
        int[] ids = {R.id.setting_about, R.id.setting_feedback, R.id.setting_grade, R.id.setting_share, R.id.setting_logout_btn};
        this.bindClickListener(view, ids);

        return view;
    }
    
    private void bindClickListener(View view, int[] ids){
        for (int i = 0 ; i < ids.length ; i++) {
            View v = view.findViewById(ids[i]);
            if(v!=null){
                v.setOnClickListener(clickListener);
            }
        }
    }
    
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.setting_share:
                    Intent shareIntent=new Intent(Intent.ACTION_SEND);   
                    shareIntent.setType("text/plain");  
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "学宝应用，欢迎下载！");    
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
                    startActivity(Intent.createChooser(shareIntent, "分享"));  
                    break;
                case R.id.setting_grade:
                    Intent gradeIntent = new Intent(Intent.ACTION_VIEW); 
                    gradeIntent.setData(Uri.parse("market://details?id=" + getActivity().getApplication().getPackageName())); 
                    startActivity(gradeIntent);
                    break;
                case R.id.setting_feedback:
                    Intent feedbackIntent = new Intent(getActivity(), AppFeedbackActivity.class); 
                    startActivity(feedbackIntent);
                    break;
                case R.id.setting_about:
                    Toast.makeText(getActivity(), "当前为最新版本！", Toast.LENGTH_SHORT).show();;
                    break;
                case R.id.setting_logout_btn:
                    //清除用户登录缓存，跳转到登录界面
                    Intent logoutIntent = new Intent(getActivity(), LoginActivity.class); 
                    //http://stackoverflow.com/questions/3473168/clear-the-entire-history-stack-and-start-a-new-activity-on-android
                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logoutIntent);
                    break;
                default:
                    break;
            }
            
        }
    };
    
    
}
