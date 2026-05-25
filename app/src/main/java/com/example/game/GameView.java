package com.example.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.game.ListView.updatePoint;

import java.util.ArrayList;

public class GameView extends View {
    updatePoint updatePoint = new updatePoint();
    private Bitmap playerCar, flyCar;
    private Bitmap[] enemyCarsArray;
    private ArrayList<Enemy> enemies;
    private final int ENEMY_COUNT = 3;

    private int playerX, playerY;
    private int score = 0;


    private int baseSpeed;
    private int screenWidth, screenHeight;
    private boolean isGameOver = false;
    private Paint scorePaint;

    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private int playerSpeedX = 20;
    private int playerSpeedY = 15;

    private boolean isFlying = false;
    private int flyTimer = 0;
    private final int MAX_FLY_TIME = 50;

    public interface GameOverListener { void onGameOver(); }
    private GameOverListener gameOverListener;
    private boolean isGameOverNotified = false;
    SharedPreferences upPointPre;

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SharedPreferences prefs = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        upPointPre = context.getSharedPreferences("role", Context.MODE_PRIVATE);
        String selectedCar = prefs.getString("selected_car", "sport");
        int carRes;
        int flyCarRes;
        switch (selectedCar) {

            case "fly":
                // Trường hợp 'selected_car' là "fly", ta gán nó về một mẫu xe mặc định.
                carRes = R.drawable.redcar; // Ví dụ: xe đỏ
                flyCarRes = R.drawable.flycar; // Ảnh bay của xe đỏ
                break;
            case "sport":
                carRes = R.drawable.sportcar;
                flyCarRes = R.drawable.sportcarfly; // Ảnh bay của xe thể thao
                break;
            case "sportfly":
                carRes = R.drawable.sportcarfly; // Xe thể thao ở trạng thái bay
                flyCarRes = R.drawable.sportcarfly; // Ảnh bay giữ nguyên
                break;
            default: // Mặc định là xe đỏ
                carRes = R.drawable.redcar;
                flyCarRes = R.drawable.flycar; // Ảnh bay mặc định
                break;
        }
        playerCar = BitmapFactory.decodeResource(getResources(), carRes);



        int difficulty = prefs.getInt("difficulty", 0);
        switch (difficulty) {
            case 1:  baseSpeed = 13; break;
            case 2:  baseSpeed = 17; break;
            default: baseSpeed = 8;  break;
        }
        flyCar = BitmapFactory.decodeResource(getResources(), flyCarRes);

        enemyCarsArray = new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.drawable.bluecar),
                BitmapFactory.decodeResource(getResources(), R.drawable.yellowcar),
                BitmapFactory.decodeResource(getResources(), R.drawable.truck)
        };

        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(60);
        scorePaint.setFakeBoldText(true);
        enemies = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;

        playerX = (screenWidth / 2) - (playerCar.getWidth() / 2);
        playerY = screenHeight - playerCar.getHeight() - 100;

        // THIẾT LẬP KHOẢNG CÁCH LÚC MỚI VÀO GAME
        if (enemies.isEmpty()) {
            for (int i = 0; i < ENEMY_COUNT; i++) {
                Enemy enemy = new Enemy(enemyCarsArray, screenWidth, screenHeight);
                // i * 800 giúp: Xe 1 ở 0px, Xe 2 bị đẩy lên 800px, Xe 3 bị đẩy lên 1600px
                // Màn hình sẽ có các xe nối đuôi nhau rơi xuống rất đều
                enemy.resetPosition(baseSpeed, i * 800);
                enemies.add(enemy);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isGameOver) {
            if (isMovingLeft) { playerX -= playerSpeedX; if (playerX < 0) playerX = 0; }
            if (isMovingRight) { playerX += playerSpeedX; if (playerX > screenWidth - playerCar.getWidth()) playerX = screenWidth - playerCar.getWidth(); }
            if (isMovingUp) { playerY -= playerSpeedY; if (playerY < 0) playerY = 0; }
            if (isMovingDown) { playerY += playerSpeedY; if (playerY > screenHeight - playerCar.getHeight()) playerY = screenHeight - playerCar.getHeight(); }

            // XÓA: int finalSoure = 0; (Đã bỏ đi vì không cần thiết)

            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                enemy.y += enemy.speed;

                if (enemy.y > screenHeight) {
                    score++;
                    // Điều chỉnh độ khó
                    int diff = getContext().getSharedPreferences("login", Context.MODE_PRIVATE).getInt("difficulty", 0);
                    int minSpeed = (diff == 2) ? 17 : (diff == 1) ? 13 : 8;
                    baseSpeed = minSpeed + (score / 10);

                    // Khi một xe chạy qua màn hình, đẩy nó lên tít trên đỉnh (cách ít nhất 500px) để tạo khe hở
                    enemy.resetPosition(baseSpeed, 500);
                }

                if (!isFlying) {
                    boolean isCrashX = playerX < enemy.x + enemy.image.getWidth() && playerX + playerCar.getWidth() > enemy.x;
                    boolean isCrashY = playerY < enemy.y + enemy.image.getHeight() && playerY + playerCar.getHeight() > enemy.y;

                    if (isCrashX && isCrashY) {
                        isGameOver = true;
                        if (gameOverListener != null && !isGameOverNotified) {

                            // [ĐOẠN SỬA LỖI ĐIỂM] - Gọi cập nhật điểm ĐÚNG 1 LẦN khi vừa tông xe
                            SharedPreferences prefRole = getContext().getSharedPreferences("role", Context.MODE_PRIVATE);
                            String username = prefRole.getString("userName", "");

                            if (!username.isEmpty()) {
                                updatePoint apiUpdate = new updatePoint(); // Khởi tạo biến
                                apiUpdate.updatePoint(username, score);    // Gửi điểm thật (score)
                            }
                            // -----------------------------------------------------------------

                            gameOverListener.onGameOver();
                            isGameOverNotified = true;
                        }
                    }
                }

                canvas.drawBitmap(enemy.image, enemy.x, enemy.y, null);
            }

            // XÓA: Toàn bộ đoạn gọi API check liên tục ở giữa vòng lặp vẽ

            if (isFlying) {
                canvas.drawBitmap(flyCar, playerX, playerY, null);
                flyTimer--;
                if (flyTimer <= 0) isFlying = false;
            } else {
                canvas.drawBitmap(playerCar, playerX, playerY, null);
            }

            canvas.drawText("SCORE: " + score, 50, 100, scorePaint);
            invalidate();

        } else {
            // Vẽ lại toàn bộ xe trên màn hình lúc chết để làm nền
            for (Enemy enemy : enemies) {
                canvas.drawBitmap(enemy.image, enemy.x, enemy.y, null);
            }
            canvas.drawBitmap(playerCar, playerX, playerY, null);

            // 1. CĂN GIỮA VÀ ĐẨY CHỮ GAME OVER LÊN TRÊN
            scorePaint.setTextAlign(Paint.Align.CENTER); // Tự động căn giữa trục X
            scorePaint.setColor(Color.RED);
            scorePaint.setTextSize(90); // Chữ to và ngầu hơn
            scorePaint.setFakeBoldText(true);

            // Đặt Y ở khoảng 1/3 phía trên màn hình (để nhường chỗ cho nút bấm bên dưới)
            float gameOverY = screenHeight / 3.5f;
            canvas.drawText("GAME OVER", screenWidth / 2f, gameOverY, scorePaint);

            // 2. HIỂN THỊ ĐIỂM SỐ NGAY BÊN DƯỚI CHỮ GAME OVER
            scorePaint.setColor(Color.YELLOW); // Chữ điểm màu vàng cho nổi bật
            scorePaint.setTextSize(65);

            // Đẩy tọa độ Y xuống thêm 100px so với chữ GAME OVER
            canvas.drawText("FINAL SCORE: " + score, screenWidth / 2f, gameOverY + 120, scorePaint);

            // 3. TRẢ LẠI CÀI ĐẶT CŨ CHO LẦN CHƠI SAU
            // Tránh việc điểm số lúc đang chơi ở góc trái trên cùng bị căn giữa sai vị trí
            scorePaint.setTextAlign(Paint.Align.LEFT);
            scorePaint.setTextSize(60);
        }
    }

    public void setMovingLeft(boolean isMoving) { this.isMovingLeft = isMoving; }
    public void setMovingRight(boolean isMoving) { this.isMovingRight = isMoving; }
    public void setMovingUp(boolean isMoving) { this.isMovingUp = isMoving; }
    public void setMovingDown(boolean isMoving) { this.isMovingDown = isMoving; }
    public void triggerJump() { if (!isGameOver && !isFlying) { isFlying = true; flyTimer = MAX_FLY_TIME; } }
}