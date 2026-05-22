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

        // 2. Bơm danh sách các loại xe vào Spinner (Chỉ còn 2 xe)
        String[] cars = {"Xe Đỏ (Mặc định)", "Xe Thể thao"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cars);
        spnChooseCar.setAdapter(adapter);

        // 3. Đọc dữ liệu cài đặt cũ để hiển thị lại lên màn hình
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);

        // Tự động tải lại chiếc xe người chơi đã chọn trước đó
        String savedCar = prefs.getString("selected_car", "red");
        int carPositionIndex = 0; // Vị trí 0: Xe Đỏ (Mặc định)
        if (savedCar.equals("sport")) {
            carPositionIndex = 1; // Vị trí 1: Xe Thể thao
        }
        spnChooseCar.setSelection(carPositionIndex);

        // Load Độ khó (Trong GameView: 0 = Dễ, 1 = Vừa, 2 = Khó)
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

                // Lưu loại xe đã chọn (Chỉ kiểm tra 2 vị trí)
                int carPosition = spnChooseCar.getSelectedItemPosition();
                String carCode = "red"; // Mặc định là xe đỏ

                if (carPosition == 1) {
                    carCode = "sport"; // Nếu chọn vị trí 1 thì lưu là xe thể thao
                }

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
                // TODO: Dùng Intent để gọi màn hình activity_change_password.xml khi cấu hình xong
                btnChangePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lệnh chuyển sang file changePasswordLayout
                        android.content.Intent intent = new android.content.Intent(settingLayout.this, changePasswordLayout.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}