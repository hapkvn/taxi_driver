package com.example.game.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.game.ListView.listUser;
import com.example.game.R;
import com.example.game.audioMain;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class userDetail extends AppCompatActivity {
    Button btnChange, btnDelete;
    EditText txtUserName, txtFullName, txtPoint;
    CheckBox checkBox;
    int role = 0;
    String ten = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        audioMain.getInstance(this).playMenuMusic();


        txtUserName = findViewById(R.id.txtUserDetail);
        txtFullName = findViewById(R.id.txtNameDetail);
        txtPoint = findViewById(R.id.txtPointDetail);
        btnChange = findViewById(R.id.btnChange);
        btnDelete = findViewById(R.id.btnDeleteUser);
        checkBox = findViewById(R.id.checkBox);


        Intent it = getIntent();
        listUser user = (listUser) it.getSerializableExtra("detail_user");

        if (user != null) {

            ten = user.getUserName();
            txtUserName.setText(ten);


            fetchUserDetails(ten);
        } else {
            Toast.makeText(this, "Không nhận được thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            role = isChecked ? 1 : 0;
        });

        btnChange.setOnClickListener(v -> changeUser(role, ten));


        btnDelete.setOnClickListener(v -> deleteUser(ten));
    }


    private void fetchUserDetails(String userName) {
        String sql_url = "http://racing-api.atwebpages.com/query_user.php";
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

                                String fetchedFullName = jsonObject.getString("full_name");
                                int fetchedPoint = jsonObject.getInt("point");
                                int fetchedRole = jsonObject.getInt("role");


                                txtFullName.setText(fetchedFullName);
                                txtPoint.setText(String.valueOf(fetchedPoint));


                                checkBox.setChecked(fetchedRole == 1);
                                role = fetchedRole;

                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(userDetail.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(userDetail.this, "Lỗi kết nối với máy chủ", Toast.LENGTH_LONG).show());
            }
        });
    }


    private void changeUser(int role, String user_name) {
        String sql_url = "http://racing-api.atwebpages.com/change_user.php";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            RequestBody formbody = new FormBody.Builder()
                    .add("user_name", user_name)
                    .add("role", String.valueOf(role))
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
                            String message = jsonObject.getString("message");
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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


    private void deleteUser(String name) {
        String sql_url = "http://racing-api.atwebpages.com/delete_user.php";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            RequestBody formbody = new FormBody.Builder()
                    .add("user_name", name)
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
                            String message = jsonObject.getString("message");
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                            if (status.equals("success")) {

                                finish();
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