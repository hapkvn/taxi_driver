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
                // 1. Xóa dữ liệu role
                SharedPreferences prefRole = getSharedPreferences("role", MODE_PRIVATE);
                prefRole.edit().clear().apply();

                // 2. QUAN TRỌNG: Xóa trạng thái đăng nhập trong file "login"
                SharedPreferences prefLogin = getSharedPreferences("login", MODE_PRIVATE);
                prefLogin.edit().clear().apply();

                // 3. Điều hướng về màn hình chính
                Intent intent = new Intent(menuLayout.this, MainActivity.class);
                // Xóa sạch ngăn xếp Activity để người dùng không thể nhấn Back quay lại menu
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}