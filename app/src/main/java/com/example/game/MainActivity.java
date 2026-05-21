package com.example.game;

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

public class MainActivity extends AppCompatActivity {
    Button btnlogin, btnResign;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Intent testGame = new Intent(MainActivity.this, MainScene.class);
        startActivity(testGame);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnlogin = findViewById(R.id.btnLogin);
        btnResign = findViewById(R.id.btnResign);
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        boolean logined = preferences.getBoolean("lged", false);

        if(logined){
            Intent it = new Intent(MainActivity.this, MainScene.class);
            startActivity(it);
            finish();
            return;
        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, loginLayout.class);
                startActivity(it);
            }
        });
        btnResign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, resignLayout.class);
                startActivity(it);
            }
        });
    }
}