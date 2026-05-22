package com.example.game.MainMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.game.MainScene;
import com.example.game.R;
import com.example.game.login.MainActivity;

public class menuLayout extends AppCompatActivity {

    Button btnPlayGame, btnSetting, btnLogout;

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

        // 1. Ánh xạ nút bấm
        btnPlayGame = findViewById(R.id.btnPlayGame);
        btnSetting = findViewById(R.id.btnSetting);
        btnLogout = findViewById(R.id.btnLogout);

        // 2. Chuyển sang đường đua
        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuLayout.this, MainScene.class);
                startActivity(intent);
            }
        });

        // 3. Chuyển sang Cài đặt (để chọn xe, đổi mật khẩu...)
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuLayout.this, settingLayout.class);
                startActivity(intent);
            }
        });

        // 4. Đăng xuất (Xóa phiên đăng nhập và về màn hình chính)
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("role", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear(); // Xóa sạch dữ liệu user hiện tại
                editor.apply();

                // Lùi về màn hình đăng nhập ban đầu
                Intent intent = new Intent(menuLayout.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa lịch sử trang để không bấm Back lùi lại menu được
                startActivity(intent);
                finish();
            }
        });
    }
}