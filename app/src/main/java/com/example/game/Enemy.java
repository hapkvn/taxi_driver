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
        // 1. Random loại xe ngẫu nhiên
        image = availableCars[random.nextInt(availableCars.length)];

        // 2. TÍNH TOÁN LÀN ĐƯỜNG THEO TỈ LỆ GỐC (40 - 280 - 40)
        // Tổng chiều rộng ảnh là 360. Vỉa hè chiếm tỉ lệ 40/360.
        float marginRatio = 40f / 360f;
        int margin = (int) (screenWidth * marginRatio);

        // Tính lòng đường và chiều rộng 1 làn
        int playableWidth = screenWidth - (2 * margin);
        int laneWidth = playableWidth / 3;

        // Tính vị trí TÂM của 3 làn đường (Trái - Giữa - Phải)
        int lane1Center = margin + (laneWidth / 2);
        int lane2Center = margin + laneWidth + (laneWidth / 2);
        int lane3Center = margin + (2 * laneWidth) + (laneWidth / 2);

        // Gom 3 làn vào mảng để bốc thăm ngẫu nhiên
        int[] lanes = {lane1Center, lane2Center, lane3Center};
        int chosenLaneCenter = lanes[random.nextInt(lanes.length)];

        // Đặt xe vào CHÍNH GIỮA làn đã chọn
        x = chosenLaneCenter - (image.getWidth() / 2);

        // 3. Logic giãn cách trục Y và tốc độ giữ nguyên
        y = -image.getHeight() - extraYOffset - random.nextInt(400);
        speed = baseSpeed + random.nextInt(4);
    }
}