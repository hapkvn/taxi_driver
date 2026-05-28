package com.example.game;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

// Lớp này kế thừa từ Application
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Chuyển "Người gác cổng" từ MainActivity sang đây
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onStart(@NonNull LifecycleOwner owner) {
                // Tiếp tục phát nhạc khi mở app
                audioMain.getInstance(MyApplication.this).resumebg();
            }

            @Override
            public void onStop(@NonNull LifecycleOwner owner) {
                // Tạm dừng nhạc khi ẩn app
                audioMain.getInstance(MyApplication.this).pausebg();
            }
        });
    }
}