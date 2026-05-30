package com.example.game;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        ProcessLifecycleOwner.get().getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onStart(@NonNull LifecycleOwner owner) {

                audioMain.getInstance(MyApplication.this).resumebg();
            }

            @Override
            public void onStop(@NonNull LifecycleOwner owner) {

                audioMain.getInstance(MyApplication.this).pausebg();
            }
        });
    }
}