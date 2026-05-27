package com.example.game.MainMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.game.R;

public class settingLayout extends AppCompatActivity {
    Spinner spnChooseCar;
    RadioGroup rgDifficulty;
    SwitchCompat switchSound;
    Button btnChangePass, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);

        spnChooseCar = findViewById(R.id.spnChooseCar);
        rgDifficulty = findViewById(R.id.rgDifficultyBasic);
        switchSound = findViewById(R.id.switchSound);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnBack = findViewById(R.id.btnBack);

        String[] cars = {"Xe Đỏ (Mặc định)", "Xe Thể thao"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cars);
        spnChooseCar.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);

        String savedCar = prefs.getString("selected_car", "red");
        int carPositionIndex = 0;
        if (savedCar.equals("sport")) {
            carPositionIndex = 1;
        }
        spnChooseCar.setSelection(carPositionIndex);

        int diff = prefs.getInt("difficulty", 1);
        if (diff == 0) rgDifficulty.check(R.id.rbEasy);
        else if (diff == 2) rgDifficulty.check(R.id.rbHard);
        else rgDifficulty.check(R.id.rbMedium);

        boolean soundOn = prefs.getBoolean("sound_on", true);
        switchSound.setChecked(soundOn);

        // Nút Quay lại - giữ nguyên như cũ
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();

                int carPosition = spnChooseCar.getSelectedItemPosition();
                String carCode = "red";
                if (carPosition == 1) {
                    carCode = "sport";
                }
                editor.putString("selected_car", carCode);

                int selectedDiff = rgDifficulty.getCheckedRadioButtonId();
                if (selectedDiff == R.id.rbEasy) editor.putInt("difficulty", 0);
                else if (selectedDiff == R.id.rbHard) editor.putInt("difficulty", 2);
                else editor.putInt("difficulty", 1);

                editor.putBoolean("sound_on", switchSound.isChecked());
                editor.apply();

                Toast.makeText(settingLayout.this, "Đã lưu cài đặt!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Nút đổi mật khẩu - đã sửa bỏ lồng OnClickListener
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(settingLayout.this, changePasswordLayout.class);
                startActivity(intent);
            }
        });
    }
}