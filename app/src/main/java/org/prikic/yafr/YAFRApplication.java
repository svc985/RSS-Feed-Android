package org.prikic.yafr;

import android.app.Application;

import timber.log.Timber;

public class YAFRApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
