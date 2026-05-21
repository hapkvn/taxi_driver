package com.example.game;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class settingLayout extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);

        prefs = getSharedPreferences("login", MODE_PRIVATE);

        Button btnChooseCar  = findViewById(R.id.btnChooseCar);
        Button btnDifficulty = findViewById(R.id.btnDifficulty);
        Button btnSound      = findViewById(R.id.btnSound);
        Button btnChangePass = findViewById(R.id.btnChangePass);
        Button btnBack       = findViewById(R.id.btnBack);

        updateCarButton(btnChooseCar);
        updateSoundButton(btnSound);
        updateDifficultyButton(btnDifficulty);


        btnChooseCar.setOnClickListener(v -> {
            String[] cars    = {"Xe thường", "Xe đỏ", "Xe vàng", "Xe tải", "Xe bay"};
            String[] carKeys = {"blue", "red", "yellow", "truck", "fly"};
            new AlertDialog.Builder(this)
                    .setTitle("Chọn kiểu xe")
                    .setItems(cars, (dialog, which) -> {
                        prefs.edit().putString("selected_car", carKeys[which]).apply();
                        updateCarButton(btnChooseCar);
                        Toast.makeText(this, "Đã chọn: " + cars[which], Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });


        btnDifficulty.setOnClickListener(v -> {
            String[] levels = {"Dễ", "Trung bình", "Khó"};
            new AlertDialog.Builder(this)
                    .setTitle("Chọn độ khó")
                    .setItems(levels, (dialog, which) -> {
                        prefs.edit().putInt("difficulty", which).apply();
                        updateDifficultyButton(btnDifficulty);
                        Toast.makeText(this, "Độ khó: " + levels[which], Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });


        btnSound.setOnClickListener(v -> {
            boolean current = prefs.getBoolean("sound_on", true);
            prefs.edit().putBoolean("sound_on", !current).apply();
            updateSoundButton(btnSound);
        });


        btnBack.setOnClickListener(v -> finish());
    }

    private void updateCarButton(Button btn) {
        String car = prefs.getString("selected_car", "normal");
        switch (car) {
            case "red":    btn.setText("XE ĐỎ");   break;
            case "yellow": btn.setText("XE VÀNG");  break;
            case "truck":  btn.setText("XE TẢI");   break;
            case "fly":    btn.setText("XE BAY");   break;
            default:       btn.setText("XE THƯỜNG"); break;
        }
    }

    private void updateSoundButton(Button btn) {
        boolean on = prefs.getBoolean("sound_on", true);
        btn.setText(on ? "ÂM THANH: BẬT" : "ÂM THANH: TẮT");
    }

    private void updateDifficultyButton(Button btn) {
        int level = prefs.getInt("difficulty", 0);
        String[] labels = {"ĐỘ KHÓ: DỄ", "ĐỘ KHÓ: TRUNG BÌNH", "ĐỘ KHÓ: KHÓ"};
        btn.setText(labels[level]);
    }
}