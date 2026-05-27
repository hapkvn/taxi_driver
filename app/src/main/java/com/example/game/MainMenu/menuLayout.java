package com.example.game.MainMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.game.ListView.users_list;
import com.example.game.MainScene;
import com.example.game.R;
import com.example.game.admin.userDetail;
import com.example.game.login.MainActivity;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        tvHighScoreValue = findViewById(R.id.tvHighScoreValue);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnPlayGame = findViewById(R.id.btnPlayGame);
        btnSetting = findViewById(R.id.btnSetting);
        btnLogout = findViewById(R.id.btnLogout);
        SharedPreferences prefRole = getSharedPreferences("role", MODE_PRIVATE);
        String currentUser = prefRole.getString("userName", "");
        fetchUserDetails(currentUser);

        // 2. Chuyển sang đường đua
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
    private void fetchUserDetails(String userName) {
        String sql_url = "http://10.0.2.2/android_user_api/query_user.php";
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();

            RequestBody formbody = new FormBody.Builder()
                    .add("user_name", userName)
                    .build();

            Request request = new Request.Builder()
                    .url(sql_url)
                    .post(formbody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();

                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                int fetchedPoint = jsonObject.getInt("point");
                                tvHighScoreValue.setText(String.valueOf(fetchedPoint));

                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi kết nối với máy chủ", Toast.LENGTH_LONG).show());
            }
        });
    }
}