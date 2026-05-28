package com.example.game.admin;

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

import com.example.game.ListView.users_list;
import com.example.game.MainMenu.settingLayout;
import com.example.game.MainScene;
import com.example.game.R;
import com.example.game.audioMain;
import com.example.game.login.MainActivity;

public class adminMemu extends AppCompatActivity {
    Button btnNewGame, btnUsers, btnSetting, btnLogoutAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_memu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        audioMain.getInstance(this).playMenuMusic();

        btnUsers = findViewById(R.id.btnChange);
        btnNewGame = findViewById(R.id.btnPlayAdmin);
        btnSetting = findViewById(R.id.btnSettingAdmin);
        btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(adminMemu.this, MainScene.class);
                startActivity(it);
            }
        });
        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(adminMemu.this, users_list.class);
                startActivity(it);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(adminMemu.this, settingLayout.class);
                startActivity(it);
            }
        });
        btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefRole = getSharedPreferences("role", MODE_PRIVATE);
                prefRole.edit().clear().apply();
                SharedPreferences prefLogin = getSharedPreferences("login", MODE_PRIVATE);
                prefLogin.edit().clear().apply();
                Intent intent = new Intent(adminMemu.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}