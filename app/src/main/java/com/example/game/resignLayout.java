package com.example.game;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class resignLayout extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
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
        txtuser = findViewById(R.id.txtUser);
        txtname = findViewById(R.id.txtName);
        txtpass = findViewById(R.id.txtPass);
        txtvePass = findViewById(R.id.txtVePass);
        btnResign = findViewById(R.id.btnResign);

        sqLiteDatabase = openOrCreateDatabase("game_user.db", MODE_PRIVATE, null);

        try{
            String sql = "create table if not exists data_user(user text PRIMARY KEY, name text not null, pass text not null)";
            sqLiteDatabase.execSQL(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
                }
                else{
                    if(!pass.equals(vePass)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(resignLayout.this);
                        builder.setTitle("Thông báo!");
                        builder.setMessage("Mật khẩu không khớp!");
                        builder.show();
                        return;
                    }
                    try {
                        String sql = "INSERT INTO data_user(user, name, pass) VALUES(?, ?, ?)";
                        sqLiteDatabase.execSQL(sql, new Object[]{user, name, pass});
                        AlertDialog.Builder builder = new AlertDialog.Builder(resignLayout.this);
                        builder.setTitle("Thông báo!");
                        builder.setMessage("Đăng ký thành công!");
                        builder.show();
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(resignLayout.this);
                        builder.setTitle("Lỗi!");
                        builder.setMessage("Tài khoản đã tồn tại hoặc có lỗi xảy ra!");
                        builder.show();
                    }
                }

            }
        });


    }
}