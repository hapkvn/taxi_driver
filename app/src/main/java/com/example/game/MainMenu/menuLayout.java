package com.example.game.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.game.ListView.users_list;
import com.example.game.MainScene;
import com.example.game.R;

public class menuLayout extends AppCompatActivity {
    Button btnNewG, btnRank, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_layout);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ĐÃ SỬA: Ánh xạ đúng ID cho từng nút
        // (Lưu ý: Bạn hãy kiểm tra lại file XML xem id của nút chơi mới có đúng là btnNewGame không nhé)
        btnNewG = findViewById(R.id.btnNewGame);
        btnRank = findViewById(R.id.btnRank);
        btnSetting = findViewById(R.id.btnSetting);

        // Nút Chơi Mới
        btnNewG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(menuLayout.this, MainScene.class);
                startActivity(it);
            }
        });

        // Nút Bảng Xếp Hạng
        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(menuLayout.this, users_list.class);
                startActivity(it);
            }
        });

        // Nút Cài Đặt
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm logic chuyển sang màn hình cài đặt âm thanh ở đây sau
            }
        });
    }
}