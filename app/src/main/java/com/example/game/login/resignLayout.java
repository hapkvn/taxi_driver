package com.example.game.login;

import android.content.Intent;
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

import com.example.game.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class resignLayout extends AppCompatActivity {
    private  static final String API_REGISTER_URL = "http://10.0.2.2/android_user_api/register.php";

    EditText txtuser, txtname, txtpass, txtvePass;
    Button btnResign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resign_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtuser = findViewById(R.id.txtUserDetail);
        txtname = findViewById(R.id.txtNameDetail);
        txtpass = findViewById(R.id.txtPointDetail);
        txtvePass = findViewById(R.id.txtVePass);
        btnResign = findViewById(R.id.btnChange);

        btnResign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String user = txtuser.getText().toString();
                 String name = txtname.getText().toString();
                 String pass = txtpass.getText().toString();
                 String vePass = txtvePass.getText().toString();

                if(user.isEmpty() || name.isEmpty() || pass.isEmpty() || vePass.isEmpty() ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(resignLayout.this);
                    builder.setTitle("Thông báo!");
                    builder.setMessage("Hãy nhập đủ thông tin!");
                    builder.show();
                    txtname.requestFocus();
                    txtpass.requestFocus();
                    txtuser.requestFocus();
                    txtvePass.requestFocus();
                    return;
                }else if( !pass.equals(vePass)){
                    Toast.makeText(resignLayout.this, "mật khẩu không khớp", Toast.LENGTH_LONG).show();
                }
                else{
                    registerUser(user, name, pass);
                }
            }
        });
    }

    private void registerUser(String user_name, String full_name, String password){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->{
            OkHttpClient client =new OkHttpClient();
            RequestBody formbody = new FormBody.Builder()
                    .add("user_name", user_name)
                    .add("full_name", full_name)
                    .add("password", password)
                    .build();
            Request request = new Request.Builder()
                .url(API_REGISTER_URL)
                .post(formbody)
                .build();
            try(Response response = client.newCall(request).execute()){
                if(response.isSuccessful() && response.body() != null){
                    String jsonReponse = response.body().string();
                    Log.d("API_RESULT", jsonReponse);
                    runOnUiThread(()->{
                        Intent it = new Intent(resignLayout.this, loginLayout.class);
                        startActivity(it);
                        finish();
                    });

                }
            } catch (IOException e) {
                Log.e("API_ERROR", "Lỗi mạng" + e.getMessage());
                runOnUiThread(()-> Toast.makeText(resignLayout.this, "Lỗi kết nối máy chủ" + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

}