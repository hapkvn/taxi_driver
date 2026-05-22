package com.example.game;

import android.content.Context;
import android.content.SharedPreferences; // Bổ sung thư viện này
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import java.util.HashMap;

public class audioMain {
    private static audioMain instance;
    private SoundPool soundPool;
    private HashMap<String, Integer> soundMap;
    private Context context;
    private MediaPlayer bgSound;

    public audioMain(Context context) {
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

    private void loadAllSounds() {
        soundMap.put("fly", soundPool.load(context, R.raw.fly, 1));
    }

    public void playSound(String soundKey) {
        // 1. KIỂM TRA CÀI ĐẶT TRƯỚC KHI PHÁT ÂM THANH HIỆU ỨNG (TIẾNG BAY)
        SharedPreferences prefs = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean isSoundOn = prefs.getBoolean("sound_on", true);

        if (!isSoundOn) {
            return; // Nếu đang tắt tiếng -> Chặn luôn, thoát hàm
        }

        if (soundMap.containsKey(soundKey)) {
            Integer soundId = soundMap.get(soundKey);
            if (soundId != null) {
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    }

    public void bgSoundStart(){
        // 2. KIỂM TRA CÀI ĐẶT TRƯỚC KHI PHÁT NHẠC NỀN
        SharedPreferences prefs = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean isSoundOn = prefs.getBoolean("sound_on", true);

        if (!isSoundOn) {
            return; // Nếu đang tắt tiếng -> Chặn luôn nhạc nền, thoát hàm
        }

        stopbg();
        bgSound = MediaPlayer.create(context, R.raw.background);
        if(bgSound !=null){
            bgSound.setLooping(true);
            bgSound.setVolume(0.5f, 0.5f);
            bgSound.start();
        }
    }

    public void stopbg(){
        if(bgSound !=null){
            if(bgSound.isPlaying()){
                bgSound.stop();
            }
            bgSound.release();
            bgSound = null;
        }
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