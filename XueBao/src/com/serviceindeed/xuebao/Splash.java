package com.serviceindeed.xuebao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1500 * 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);

        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
                Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
                startActivity(mainIntent);
                /* Create an Intent that will start the Menu-Activity. */
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}