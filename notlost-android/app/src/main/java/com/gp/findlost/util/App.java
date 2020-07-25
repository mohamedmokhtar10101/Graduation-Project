package com.gp.findlost.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context = null;
    @SuppressLint("StaticFieldLeak")
    private static App application = null;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity = null;

    public static Context getContext() {
        return context;
    }

    public static App getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
        application = this;

    }

}
