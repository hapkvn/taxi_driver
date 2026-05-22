package com.example.game.MainMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.game.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class changePasswordLayout extends AppCompatActivity {

    EditText txtOldPass, txtNewPass, txtConfirmPass;
    Button btnUpdatePass, btnBackFromChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password); // Liên kết với file XML activity_change_password.xml

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Ánh xạ các View từ giao diện XML sang Java
        txtOldPass = findViewById(R.id.txtOldPass);
        txtNewPass = findViewById(R.id.txtNewPass);
        txtConfirmPass = findViewById(R.id.txtConfirmPass);
        btnUpdatePass = findViewById(R.id.btnUpdatePass);
        btnBackFromChange = findViewById(R.id.btnBackFromChange);

        // 2. Xử lý sự kiện khi bấm nút QUAY LẠI
        btnBackFromChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng màn hình này để quay trở lại màn hình Cài đặt (settingLayout)
            }
        });

        // 3. Xử lý sự kiện khi bấm nút CẬP NHẬT
        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = txtOldPass.getText().toString().trim();
                String newPass = txtNewPass.getText().toString().trim();
                String confirmPass = txtConfirmPass.getText().toString().trim();

                // Kiểm tra xem người dùng có bỏ trống ô nào không
                if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(changePasswordLayout.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem mật khẩu mới và ô nhập lại có trùng khớp không
                if (!newPass.equals(confirmPass)) {
                    Toast.makeText(changePasswordLayout.this, "Mật khẩu mới nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                    txtConfirmPass.requestFocus(); // Nháy con trỏ vào ô nhập lại mật khẩu
                    return;
                }

                // Lấy tên Username đang đăng nhập hệ thống từ SharedPreferences "role" (giống loginLayout lưu)
                SharedPreferences prefs = getSharedPreferences("role", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("userName", "");

                if (currentUser.isEmpty()) {
                    Toast.makeText(changePasswordLayout.this, "Lỗi: Không tìm thấy tài khoản đăng nhập!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tiến hành gọi hàm đẩy dữ liệu lên Server qua API
                changePasswordApi(currentUser, oldPass, newPass);
            }
        });
    }

    // 4. Hàm xử lý kết nối luồng mạng kết nối API cập nhật dữ liệu mật khẩu mới
    private void changePasswordApi(String userName, String oldPassword, String newPassword) {
        String API_CHANGE_PASS_URL = "http://10.0.2.2/android_user_api/change_password.php";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();

            // Đóng gói các tham số để gửi lên file PHP qua phương thức POST
            RequestBody formBody = new FormBody.Builder()
                    .add("user_name", userName)
                    .add("old_password", oldPassword)
                    .add("new_password", newPassword)
                    .build();

            Request request = new Request.Builder()
                    .url(API_CHANGE_PASS_URL)
                    .post(formBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();

                    // Chuyển về luồng UI Thread chính để hiển thị thông báo lên màn hình
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(changePasswordLayout.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                finish(); // Đổi mật khẩu thành công thì đóng luôn màn hình, tự động lùi về cài đặt
                            } else {
                                Toast.makeText(changePasswordLayout.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(changePasswordLayout.this, "Lỗi đọc cấu trúc dữ liệu từ Server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(changePasswordLayout.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}