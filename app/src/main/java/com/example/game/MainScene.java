package com.example.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.login.MainActivity;

public class MainScene extends AppCompatActivity {

    private GameView gameView;
    private ImageButton btnLeft, btnRight, btnUp, btnDown, btnJump;

    // Khai báo quản lý âm thanh
    private audioMain audio;

    // Khai báo các đối tượng menu Game Over
    private LinearLayout layoutGameOver;
    private Button btnRestart, btnHome;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scene);

        // 1. KHỞI TẠO AUDIO (Phải có dòng này đầu tiên để không bị lỗi Null)
        audio = audioMain.getInstance(this);

        // 2. PHÁT NHẠC ĐUA XE (Sai lầm ở code cũ là gọi playMenuMusic)
        audio.playRaceMusic();

        // Ánh xạ các nút và view
        gameView = findViewById(R.id.gameView);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);
        btnJump = findViewById(R.id.btnJump);

        layoutGameOver = findViewById(R.id.layoutGameOver);
        btnRestart = findViewById(R.id.btnRestart);
        btnHome = findViewById(R.id.btnHome);

        // Sự kiện Game Over
        gameView.setGameOverListener(new GameView.GameOverListener() {
            @Override
            public void onGameOver() {
                // Giấu đi 5 nút điều khiển
                btnLeft.setVisibility(View.GONE);
                btnRight.setVisibility(View.GONE);
                btnUp.setVisibility(View.GONE);
                btnDown.setVisibility(View.GONE);
                btnJump.setVisibility(View.GONE);

                layoutGameOver.setVisibility(View.VISIBLE);
            }
        });


        btnRestart.setOnClickListener(v -> {
            recreate();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainScene.this, MainActivity.class);
            startActivity(intent);

            audio.playMenuMusic();
            finish();
        });

        btnLeft.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) gameView.setMovingLeft(true);
            else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) gameView.setMovingLeft(false);
            return true;
        });

        btnRight.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) gameView.setMovingRight(true);
            else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) gameView.setMovingRight(false);
            return true;
        });

        btnUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) gameView.setMovingUp(true);
            else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) gameView.setMovingUp(false);
            return true;
        });

        btnDown.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) gameView.setMovingDown(true);
            else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) gameView.setMovingDown(false);
            return true;
        });

        btnJump.setOnClickListener(v ->{
            audio.playSound("fly");
            gameView.triggerJump();
        });

    }
}