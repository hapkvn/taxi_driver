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

        // 1. Ánh xạ các thành phần từ XML sang Java
        spnChooseCar = findViewById(R.id.spnChooseCar);
        rgDifficulty = findViewById(R.id.rgDifficultyBasic);
        switchSound = findViewById(R.id.switchSound);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnBack = findViewById(R.id.btnBack);

        // 2. Bơm danh sách các loại xe vào Spinner
        // Khớp với các loại xe bạn đã định nghĩa trong GameView: blue, red, yellow, truck, fly
        String[] cars = {"Xe Xanh (Mặc định)", "Xe Đỏ", "Xe Vàng", "Xe Tải", "Xe Bay"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cars);
        spnChooseCar.setAdapter(adapter);

        // 3. Đọc dữ liệu cài đặt cũ để hiển thị lại lên màn hình
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);

        // Load Độ khó (Trong GameView: 0 = Dễ (speed 8), 1 = Vừa (speed 13), 2 = Khó (speed 17))
        int diff = prefs.getInt("difficulty", 1);
        if (diff == 0) rgDifficulty.check(R.id.rbEasy);
        else if (diff == 2) rgDifficulty.check(R.id.rbHard);
        else rgDifficulty.check(R.id.rbMedium);

        // Load Âm thanh
        boolean soundOn = prefs.getBoolean("sound_on", true);
        switchSound.setChecked(soundOn);

        // 4. Bắt sự kiện khi bấm nút QUAY LẠI (Sẽ tự động lưu cài đặt)
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();

                // Lưu loại xe đã chọn
                int carPosition = spnChooseCar.getSelectedItemPosition();
                String carCode = "blue"; // Mặc định
                if(carPosition == 1) carCode = "red";
                else if(carPosition == 2) carCode = "yellow";
                else if(carPosition == 3) carCode = "truck";
                else if(carPosition == 4) carCode = "fly";
                editor.putString("selected_car", carCode);

                // Lưu độ khó đã chọn
                int selectedDiff = rgDifficulty.getCheckedRadioButtonId();
                if (selectedDiff == R.id.rbEasy) editor.putInt("difficulty", 0);
                else if (selectedDiff == R.id.rbHard) editor.putInt("difficulty", 2);
                else editor.putInt("difficulty", 1); // rbMedium

                // Lưu trạng thái âm thanh
                editor.putBoolean("sound_on", switchSound.isChecked());

                // Xác nhận lưu vào bộ nhớ
                editor.apply();

                Toast.makeText(settingLayout.this, "Đã lưu cài đặt!", Toast.LENGTH_SHORT).show();
                finish(); // Đóng màn hình cài đặt, trở về màn hình trước đó
            }
        });

        // 5. Nút đổi mật khẩu
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Dùng Intent để gọi màn hình activity_change_password.xml như đã bàn ở trên
                Toast.makeText(settingLayout.this, "Tính năng Đổi mật khẩu đang được phát triển", Toast.LENGTH_SHORT).show();
            }
        });
    }
}