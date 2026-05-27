package com.example.game.MainMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.game.ListView.users_list;
import com.example.game.MainScene;
import com.example.game.R;
import com.example.game.login.MainActivity;

public class menuLayout extends AppCompatActivity {

    Button btnPlayGame, btnSetting, btnLogout, btnLeaderboard;
    TextView tvHighScoreValue;

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

        // Ánh xạ View
        tvHighScoreValue = findViewById(R.id.tvHighScoreValue);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnPlayGame = findViewById(R.id.btnPlayGame);
        btnSetting = findViewById(R.id.btnSetting);
        btnLogout = findViewById(R.id.btnLogout);

        // Các sự kiện click nút
        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuLayout.this, MainScene.class);
                startActivity(intent);
            }
        });

        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuLayout.this, users_list.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuLayout.this, settingLayout.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefRole = getSharedPreferences("role", MODE_PRIVATE);
                prefRole.edit().clear().apply();

                SharedPreferences prefLogin = getSharedPreferences("login", MODE_PRIVATE);
                prefLogin.edit().clear().apply();

                Intent intent = new Intent(menuLayout.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    // --- HÀM ONRESUME ĐỂ TỰ ĐỘNG CẬP NHẬT ĐIỂM ---
    @Override
    protected void onResume() {
        super.onResume();

        // Mỗi lần màn hình Menu hiện lên, nó sẽ tự động mở két sắt lấy điểm mới nhất
        SharedPreferences prefRole = getSharedPreferences("role", MODE_PRIVATE);
        int myPoint = prefRole.getInt("point", 0);
        tvHighScoreValue.setText(String.valueOf(myPoint));
    }
}