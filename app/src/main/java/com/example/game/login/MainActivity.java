    package com.example.game.login;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.example.game.GameView;
    import com.example.game.ListView.users_list;
    import com.example.game.MainScene;
    import com.example.game.MainMenu.settingLayout;
    import com.example.game.MainMenu.menuLayout;
    import com.example.game.R;
    import com.example.game.admin.adminMemu;


    public class MainActivity extends AppCompatActivity {
        Button btnlogin, btnResign;
        SharedPreferences preferences;
        SharedPreferences preferencesRole;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ProcessLifecycleOwner.get().getLifecycle().addObserver(new DefaultLifecycleObserver() {
                @Override
                public void onStart(@NonNull LifecycleOwner owner) {
                    // Khi người dùng mở lại App (Foreground) -> Tiếp tục phát nhạc
                    audioMain.getInstance(MainActivity.this).resumebg();
                }

                @Override
                public void onStop(@NonNull LifecycleOwner owner) {
                    // Khi người dùng ẩn App, ra màn hình chính (Background) -> Tạm dừng nhạc
                    audioMain.getInstance(MainActivity.this).pausebg();
                }
            });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            btnlogin = findViewById(R.id.btnlogin);
            btnResign = findViewById(R.id.btnResign);
            preferences = getSharedPreferences("login", MODE_PRIVATE);
            preferencesRole = getSharedPreferences("role", MODE_PRIVATE);
            boolean logined = preferences.getBoolean("lged", false);
            int role = preferencesRole.getInt("checkRole", 0);
            if (logined){
                if(role ==1){
                    Intent it = new Intent(MainActivity.this, adminMemu.class);
                    startActivity(it);
                    finish();
                    return;
                }else{
                    Intent it = new Intent(MainActivity.this, menuLayout.class);
                    startActivity(it);
                    finish();
                    return;
                }
            }
            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(MainActivity.this, loginLayout.class);
                    startActivity(it);
                }
            });
            btnResign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(MainActivity.this, resignLayout.class);
                    startActivity(it);
                }
            });

        }
    }