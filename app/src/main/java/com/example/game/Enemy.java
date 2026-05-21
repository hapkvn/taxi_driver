package com.example.game;

import android.graphics.Bitmap;
import java.util.Random;

public class Enemy {
    public Bitmap image;
    public int x, y;
    public int speed;
    private int screenWidth, screenHeight;
    private Random random = new Random();
    private Bitmap[] availableCars;

    // Hàm khởi tạo không gọi reset ngay mà để GameView tự sắp xếp khoảng cách
    public Enemy(Bitmap[] availableCars, int screenWidth, int screenHeight) {
        this.availableCars = availableCars;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    // extraYOffset giúp bắt buộc xe phải xuất hiện cao hơn mức bình thường để không đè lên xe trước
    public void resetPosition(int baseSpeed, int extraYOffset) {
        // Random loại xe
        image = availableCars[random.nextInt(availableCars.length)];

        // Random lề trái phải
        int minX = 50;
        int maxX = screenWidth - image.getWidth() - 50;
        x = random.nextInt((maxX - minX) + 1) + minX;

        // GIÃN CÁCH TRỤC Y: Đẩy xe lên tít phía trên màn hình cộng thêm khoảng cách ép buộc
        // random.nextInt(400) tạo thêm một chút tự nhiên để khoảng cách không bị cứng nhắc
        y = -image.getHeight() - extraYOffset - random.nextInt(400);

        // GIẢM ĐỘ LỆCH TỐC ĐỘ: Các xe chỉ chênh nhau tối đa 3 đơn vị tốc độ
        // Tránh tình trạng xe sau phóng quá nhanh đâm xuyên qua xe trước
        speed = baseSpeed + random.nextInt(4);
    }
}