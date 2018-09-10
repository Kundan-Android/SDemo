package com.caliber.shwaasdemo.Utils;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Caliber on 29-01-2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       // LeakCanary.install(this);
    }
}
