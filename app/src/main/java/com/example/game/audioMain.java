package com.example.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Random;

public class audioMain {

    // 1. Singleton Instance
    private static audioMain instance;

    private SoundPool soundPool;
    private HashMap<String, Integer> soundMap;
    private Context context;
    private MediaPlayer bgSound;
    private Random random = new Random();

    // THÊM BIẾN TRẠNG THÁI Ở ĐÂY ĐỂ TRÁNH RESET NHẠC MENU
    private static final int TYPE_NONE = 0;
    private static final int TYPE_MENU = 1;
    private static final int TYPE_RACE = 2;
    private int currentMusicType = TYPE_NONE;

    // Mảng nhạc Menu
    private int[] bg = {
            R.raw.asphat9,
            R.raw.asphat92,
            R.raw.asphat93,
            R.raw.asphat94,
            R.raw.asphat95,
            R.raw.asphat96,
            R.raw.asphat9_1
    };

    // Mảng nhạc Đua xe
    private int[] musicArray = {
            R.raw.a,
            R.raw.b,
            R.raw.c,
            R.raw.d,
            R.raw.e,
            R.raw.f,
            R.raw.g,
            R.raw.h,
            R.raw.i,
            R.raw.k,
            R.raw.l
    };

    // 2. Private Constructor (Cho Singleton)
    private audioMain(Context context) {
        this.context = context.getApplicationContext();
        this.soundMap = new HashMap<>();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        loadAllSounds();
    }

    // 3. Hàm gọi Instance duy nhất
    public static audioMain getInstance(Context context) {
        if (instance == null) {
            instance = new audioMain(context);
        }
        return instance;
    }

    private void loadAllSounds() {
        soundMap.put("fly", soundPool.load(context, R.raw.fly, 1));
        soundMap.put("crash", soundPool.load(context, R.raw.crash, 1));
    }

    public void playSound(String soundKey) {
        if (!isSoundAllowed()) return;

        if (soundMap.containsKey(soundKey)) {
            Integer soundId = soundMap.get(soundKey);
            if (soundId != null) {
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    }

    // Kiểm tra SharedPreferences
    private boolean isSoundAllowed() {
//        SharedPreferences prefs = context.getSharedPreferences("login", Context.MODE_PRIVATE);
//        return prefs.getBoolean("sound_on", true);
        return true;
    }

    public void playMenuMusic() {
        if (!isSoundAllowed()) return;

        // KIỂM TRA TRẠNG THÁI: Nếu đang phát nhạc Menu rồi thì bỏ qua
        if (currentMusicType == TYPE_MENU && bgSound != null && bgSound.isPlaying()) {
            return;
        }

        stopbg(); // Dừng nhạc cũ
        int randomId = bg[random.nextInt(bg.length)];
        startMediaPlayer(randomId, 0.8f);

        currentMusicType = TYPE_MENU; // Cập nhật trạng thái
    }

    // Hàm phát nhạc Race (random một bài trong mảng musicArray)
    public void playRaceMusic() {
        if (!isSoundAllowed()) return;

        stopbg(); // Dừng nhạc cũ
        int randomId = musicArray[random.nextInt(musicArray.length)];
        startMediaPlayer(randomId, 0.5f);

        currentMusicType = TYPE_RACE; // Cập nhật trạng thái
    }

    // Hàm dùng chung để khởi tạo và phát MediaPlayer
    // Hàm dùng chung để khởi tạo và phát MediaPlayer
    private void startMediaPlayer(int soundId, float v) {
        bgSound = MediaPlayer.create(context, soundId);
        if (bgSound != null) {
            // 1. TẮT chế độ lặp lại 1 bài
            bgSound.setLooping(false);
            bgSound.setVolume(v, v);

            // 2. Lắng nghe sự kiện khi bài hát kết thúc
            bgSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Lưu lại trạng thái hiện tại vì hàm stopbg() sẽ reset nó về 0
                    int savedType = currentMusicType;

                    stopbg(); // Dọn dẹp bài hát cũ vừa phát xong

                    // Kiểm tra xem người chơi đang ở đâu để tự động random bài mới tương ứng
                    if (savedType == TYPE_MENU) {
                        int randomId = bg[random.nextInt(bg.length)];
                        startMediaPlayer(randomId, 0.8f);
                        currentMusicType = TYPE_MENU; // Gán lại trạng thái

                    } else if (savedType == TYPE_RACE) {
                        int randomId = musicArray[random.nextInt(musicArray.length)];
                        startMediaPlayer(randomId, 0.5f);
                        currentMusicType = TYPE_RACE; // Gán lại trạng thái
                    }
                }
            });

            bgSound.start();
        }
    }

    // Hàm tạm dừng khi ẩn App
    public void pausebg() {
        if (bgSound != null && bgSound.isPlaying()) {
            bgSound.pause();
        }
    }

    // Hàm tiếp tục khi mở lại App
    public void resumebg() {
        if (bgSound != null && !bgSound.isPlaying() && isSoundAllowed()) {
            bgSound.start();
        }
    }

    public void stopbg() {
        if (bgSound != null) {
            if (bgSound.isPlaying()) {
                bgSound.stop();
            }
            bgSound.release();
            bgSound = null;
        }
        currentMusicType = TYPE_NONE; // Reset trạng thái khi dừng nhạc
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            soundMap.clear();
        }
        stopbg();
    }
}