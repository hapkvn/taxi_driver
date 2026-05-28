package com.example.game.ListView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.game.R;
import com.example.game.admin.userDetail;
import com.example.game.audioMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class users_list extends AppCompatActivity {
    adapter adapterr;
    ArrayList<listUser> listUsers;
    ListView lsUserView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_users_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        audioMain.getInstance(this).playMenuMusic();

        // 1. Ánh xạ ListView và khởi tạo mảng ngay từ đầu trên luồng chính
        lsUserView = findViewById(R.id.lstUser);
        listUsers = new ArrayList<>();
        preferences = getSharedPreferences("role", MODE_PRIVATE);

        int role = preferences.getInt("checkRole", 0);
        if(role == 1) {
            lsUserView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listUser listu = listUsers.get(position);
                    Intent it = new Intent(users_list.this, userDetail.class);
                    it.putExtra("detail_user", (Serializable) listu);
                    startActivity(it);
                }
            });
        }

        // 3. Tiến hành gọi API
        String url = "http://racing-api.atwebpages.com/get_leaderboard.php";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Log.d("API_LEADERBOARD", "Dữ liệu: " + jsonResponse);

                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String status = jsonObject.getString("status");

                        if (status.equals("success")) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject row = dataArray.getJSONObject(i);
                                String userName = row.getString("user_name"); // Lấy thêm dòng này
                                String fullName = row.getString("full_name");
                                int point = row.getInt("point");
                                if (point > 0 & role !=1 ) {
                                listUsers.add(new listUser(userName, fullName, String.valueOf(point)));
                                }
                            }

                            runOnUiThread(() -> {
                                adapterr = new adapter(users_list.this, R.layout.layout_users, listUsers);
                                lsUserView.setAdapter(adapterr);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                Log.e("API_ERROR", "Lỗi tải bảng xếp hạng: " + e.getMessage());
            }
        });
    }
}