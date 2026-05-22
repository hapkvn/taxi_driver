package com.example.game.admin;

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

public class adminMemu extends AppCompatActivity {

    Button btnNewGame, btnUsers, btnSetting;

    // ĐÃ XÓA bỏ dòng @SuppressLint nguy hiểm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_memu);

        // BẢO VỆ CHỐNG CRASH: Kiểm tra xem id "main" có tồn tại không trước khi bo viền màn hình
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Ánh xạ ID (Lưu ý: Bạn phải mở file activity_admin_memu.xml ra và
        // đảm bảo các nút bấm đang được đặt đúng id là btnPlayM, btnRank, btnSetting nhé)
        btnNewGame = findViewById(R.id.btnNewGame);
        btnUsers = findViewById(R.id.btnRank);
        btnSetting = findViewById(R.id.btnSetting);

        // Xử lý sự kiện khi nút đã được ánh xạ thành công
        if (btnNewGame != null) {
            btnNewGame.setOnClickListener(v -> {
                Intent it = new Intent(adminMemu.this, MainScene.class);
                startActivity(it);
            });
        }

        if (btnUsers != null) {
            btnUsers.setOnClickListener(v -> {
                Intent it = new Intent(adminMemu.this, users_list.class);
                startActivity(it);
            });
        }

        if (btnSetting != null) {
            btnSetting.setOnClickListener(v -> {
                // TODO: Chuyển sang màn hình cài đặt
            });
        }
    }
}