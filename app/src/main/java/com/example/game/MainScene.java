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

public class MainScene extends AppCompatActivity {

    private GameView gameView;
    private ImageButton btnLeft, btnRight, btnUp, btnDown, btnJump;

    // Khai báo các đối tượng menu Game Over
    private LinearLayout layoutGameOver;
    private Button btnRestart, btnHome;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scene);

        // Ánh xạ 5 nút điều khiển
        gameView = findViewById(R.id.gameView);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);
        btnJump = findViewById(R.id.btnJump);

        // Ánh xạ bảng Menu thua cuộc
        layoutGameOver = findViewById(R.id.layoutGameOver);
        btnRestart = findViewById(R.id.btnRestart);
        btnHome = findViewById(R.id.btnHome);

        // --- XỬ LÝ ẨN NÚT KHI GAME OVER ---
        gameView.setGameOverListener(new GameView.GameOverListener() {
            @Override
            public void onGameOver() {
                // Giấu đi 5 nút điều khiển
                btnLeft.setVisibility(View.GONE);
                btnRight.setVisibility(View.GONE);
                btnUp.setVisibility(View.GONE);
                btnDown.setVisibility(View.GONE);
                btnJump.setVisibility(View.GONE);

                // Hiện bảng menu Game Over lên
                layoutGameOver.setVisibility(View.VISIBLE);
            }
        });

        // --- XỬ LÝ BẤM NÚT MENU ---
        // Nút Chơi Lại (Restart Game)
        btnRestart.setOnClickListener(v -> {
            // Hàm recreate() sẽ tự động tải lại toàn bộ Activity từ đầu (cực kỳ nhanh)
            recreate();
        });

        // Nút Trở Về (Back to Home)
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainScene.this, MainActivity.class);
            startActivity(intent);
            finish(); // Tắt vĩnh viễn ván game hiện tại để giải phóng RAM
        });

        // --- XỬ LÝ CHẠM NÚT ĐIỀU KHIỂN GIỮ NGUYÊN ---
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

        btnJump.setOnClickListener(v -> gameView.triggerJump());
    }
}