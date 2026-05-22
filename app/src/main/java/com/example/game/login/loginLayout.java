package com.example.game.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.game.MainMenu.menuLayout;
import com.example.game.R;
import com.example.game.admin.adminMemu;

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

public class loginLayout extends AppCompatActivity {
    private static final String API_lOGIN_URL = "http://10.0.2.2/android_user_api/login.php";
    EditText txtuerName, txtPassword;
    Button btnLogin;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        preferences = getSharedPreferences("role", MODE_PRIVATE);
        txtuerName = findViewById(R.id.txtUser);
        txtPassword = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnNewGame);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = txtuerName.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if(user_name.isEmpty() || password.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(loginLayout.this);
                    builder.setTitle("Thông báo!");
                    builder.setMessage("Hãy nhập đủ thông tin");
                    builder.show();
                    txtuerName.requestFocus();
                    txtPassword.requestFocus();
                    return;
                }else{
                    loginUer(user_name, password);

                }
            }
        });
    }
    private void loginUer(String user, String password){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        SharedPreferences.Editor editor = preferences.edit();

        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("user_name", user)
                    .add("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(API_lOGIN_URL)
                    .post(formBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Log.d("API_RESULT", jsonResponse);

                    // ĐÃ SỬA: Đưa toàn bộ logic hiển thị và chuyển màn hình vào trong runOnUiThread
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);

                            // ĐÃ SỬA TYPO: "staus" -> "status" để trùng khớp với API PHP
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {
                                JSONObject userObj = jsonObject.getJSONObject("user");
                                editor.putString("userName", user);
                                int role = userObj.getInt("role");

                                editor.putInt("checkRole", role);
                                editor.apply();

                                Toast.makeText(loginLayout.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                                if (role == 1) {
                                    Intent it = new Intent(loginLayout.this, adminMemu.class);
                                    startActivity(it);
                                } else {
                                    Intent it = new Intent(loginLayout.this, menuLayout.class);
                                    startActivity(it);
                                }
                                finish(); // Đóng hẳn màn hình loginLayout

                            } else {
                                Toast.makeText(loginLayout.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(loginLayout.this, "Lỗi đọc dữ liệu JSON từ Server", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(loginLayout.this, "Không thể kết nối đến Server", Toast.LENGTH_LONG).show());
            }
        });
    }
}