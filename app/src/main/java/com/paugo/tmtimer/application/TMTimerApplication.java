package com.paugo.tmtimer.application;

import android.app.Application;

public class TMTimerApplication extends Application {
    public static boolean isSoundOn = false;

    private static TMTimerApplication singleton;

    public static TMTimerApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}
