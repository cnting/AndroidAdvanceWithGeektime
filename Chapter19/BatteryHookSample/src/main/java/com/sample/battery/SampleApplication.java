package com.sample.battery;


import android.app.Application;
import android.content.Context;

public class SampleApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }


    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
    }
}
