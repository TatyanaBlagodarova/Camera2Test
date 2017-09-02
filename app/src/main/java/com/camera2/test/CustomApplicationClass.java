package com.camera2.test;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Tatyana Blagodarova on 5/13/17.
 */

public class CustomApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}