package com.elad.chatimeapp;

import android.app.Application;

import com.elad.chatimeapp.utils.SharedPrefsUtil;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefsUtil.init(this);
    }
}
