package com.caliber.shwaasdemo.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.caliber.shwaasdemo.R;
import com.caliber.shwaasdemo.Utils.SessionManager;

public class SplashScreen extends AppCompatActivity {
    private int SPLASH_TIME_OUT = 4000;
    private SharedPreferences prefs = null;
    private Handler handler;
    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        prefs = getSharedPreferences("com.caliber.shwaasdemo", MODE_PRIVATE);
        handler  = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!session.isLoggedIn()) {
                    Intent in = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                }

                else {
                    startActivity(new Intent(SplashScreen.this,HomeActivity.class));
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
