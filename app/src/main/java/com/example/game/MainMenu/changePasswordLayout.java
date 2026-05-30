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
import com.example.game.admin.userDetail;
import com.example.game.audioMain;

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

    EditText txtOldPass, txtNewPass, txtConfirmPass, txtChangeuser, txtFullName;
    Button btnUpdatePass, btnBackFromChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        audioMain.getInstance(this).playMenuMusic();


        txtFullName = findViewById(R.id.txtChangeFullname);
        txtOldPass = findViewById(R.id.txtOldPass);
        txtNewPass = findViewById(R.id.txtNewPass);
        txtConfirmPass = findViewById(R.id.txtConfirmPass);
        btnUpdatePass = findViewById(R.id.btnUpdatePass);
        btnBackFromChange = findViewById(R.id.btnBackFromChange);
        SharedPreferences prefs = getSharedPreferences("role", Context.MODE_PRIVATE);
        String currentUser = prefs.getString("userName", "");
        query_user(currentUser);


        btnBackFromChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnameChange = txtFullName.getText().toString().trim();
                String newPass = txtNewPass.getText().toString().trim();
                String confirmPass = txtConfirmPass.getText().toString().trim();


                if (fullnameChange.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(changePasswordLayout.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!newPass.equals(confirmPass)) {
                    Toast.makeText(changePasswordLayout.this, "Mật khẩu mới nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                    txtConfirmPass.requestFocus();
                    return;
                }



                if (currentUser.isEmpty()) {
                    Toast.makeText(changePasswordLayout.this, "Lỗi: Không tìm thấy tài khoản đăng nhập!", Toast.LENGTH_SHORT).show();
                    return;
                }


                changePasswordApi(currentUser, fullnameChange, newPass);
            }
        });
    }


    private void changePasswordApi(String userName, String fullname, String newPassword) {
        String API_CHANGE_PASS_URL = "http://racing-api.atwebpages.com/change_information.php";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();


            RequestBody formBody = new FormBody.Builder()
                    .add("user_name", userName)
                    .add("full_name", fullname)
                    .add("password", newPassword)
                    .build();

            Request request = new Request.Builder()
                    .url(API_CHANGE_PASS_URL)
                    .post(formBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();


                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(changePasswordLayout.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                finish();
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
    private void query_user(String UserName){
        String sql_url = "http://racing-api.atwebpages.com/query_user.php";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            OkHttpClient client = new OkHttpClient();

                RequestBody FormBody = new FormBody.Builder()
                        .add("user_name", UserName)
                        .build();
                Request request = new Request.Builder()
                        .url(sql_url)
                        .post(FormBody)
                        .build();

                try(Response response= client.newCall(request).execute()){
                    if(response.isSuccessful() && request.body() != null){
                        String JsonReponce  = response.body().string();
                        runOnUiThread(()->{
                            try{
                                JSONObject jsonObject = new JSONObject(JsonReponce);
                                String status = jsonObject.getString("status");
                                if(status.equals("success")){
                                    String fullName = jsonObject.getString("full_name");
                                    txtFullName.setHint(fullName);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(changePasswordLayout.this, "Lỗi kết nối với máy chủ", Toast.LENGTH_LONG).show());

                }
        });


    }

}