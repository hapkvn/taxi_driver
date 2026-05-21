package com.example.game;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import java.util.HashMap;

public class audioMain {
    // 1. Sửa lỗi chính tả từ 'intantance' thành 'instance'
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
        // Đảm bảo bạn đã bỏ file fly.mp3 hoặc fly.wav vào thư mục res/raw nhé
        soundMap.put("fly", soundPool.load(context, R.raw.fly, 1));

        // Bạn có thể thêm các âm thanh khác tại đây
        // soundMap.put("click", soundPool.load(context, R.raw.click, 1));
    }

    public void playSound(String soundKey) {
        if (soundMap.containsKey(soundKey)) {
            Integer soundId = soundMap.get(soundKey);
            if (soundId != null) {
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    }
    public void bgSoundStart(){
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


    // 2. Sửa lỗi chính tả từ 'relase' thành 'release'
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            soundMap.clear();
        }
        stopbg();
    }
}