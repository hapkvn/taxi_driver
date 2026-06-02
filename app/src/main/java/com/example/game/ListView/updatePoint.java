package com.example.game.ListView;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class updatePoint {
    public void updatePoint(String username, int point) {
        String url = "http://racing-api.atwebpages.com/update_point.php";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            RequestBody frombody = new FormBody.Builder()
                    .add("user_name", username)
                    .add("point", String.valueOf(point))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(frombody)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Log.d("API_UPDATE_POINT", "Kết quả: " + jsonResponse);
                }
            } catch (IOException e) {
                Log.e("API_ERROR", "Không thể kết nối để cập nhật điểm: " + e.getMessage());
            }
        });
    }



}
