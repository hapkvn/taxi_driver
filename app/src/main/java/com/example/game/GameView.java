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
    // Đối tượng gọi API cập nhật điểm
    private updatePoint apiUpdatePoint;

    private Bitmap playerCar, flyCar;
    private Bitmap[] enemyCarsArray;
    private ArrayList<Enemy> enemies;
    private final int ENEMY_COUNT = 3;

    private int playerX, playerY;
    private int score = 0;

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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

    // Quản lý dữ liệu cục bộ (Lưu tên user và cài đặt xe/độ khó)
    private SharedPreferences preferencesRole;
    private SharedPreferences preferencesLogin;

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
<<<<<<< Updated upstream
        SharedPreferences prefs = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String selectedCar = prefs.getString("selected_car", "sport");
=======

        // 1. KHỞI TẠO CÁC ĐỐI TƯỢNG (Chống lỗi văng app NullPointerException)
        apiUpdatePoint = new updatePoint();
        // Lấy tên User đã đăng nhập từ file "role"
        preferencesRole = context.getSharedPreferences("role", Context.MODE_PRIVATE);
        // Lấy cài đặt xe và độ khó từ file "login"
        preferencesLogin = context.getSharedPreferences("login", Context.MODE_PRIVATE);

        // 2. TẢI HÌNH ẢNH XE NGƯỜI CHƠI
        String selectedCar = preferencesLogin.getString("selected_car", "blue");
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream


        int difficulty = prefs.getInt("difficulty", 0);
=======
        // 3. THIẾT LẬP ĐỘ KHÓ
        int difficulty = preferencesLogin.getInt("difficulty", 0);
>>>>>>> Stashed changes
        switch (difficulty) {
            case 1:  baseSpeed = 13; break;
            case 2:  baseSpeed = 17; break;
            default: baseSpeed = 8;  break;
        }
<<<<<<< Updated upstream
        flyCar = BitmapFactory.decodeResource(getResources(), flyCarRes);
=======

        flyCar = BitmapFactory.decodeResource(getResources(), R.drawable.flycar);
>>>>>>> Stashed changes

        // 4. TẢI HÌNH ẢNH XE ĐỊCH
        enemyCarsArray = new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.drawable.bluecar),
                BitmapFactory.decodeResource(getResources(), R.drawable.yellowcar),
                BitmapFactory.decodeResource(getResources(), R.drawable.truck)
        };

        // 5. CẤU HÌNH VẼ ĐIỂM SỐ
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

        // THIẾT LẬP KHOẢNG CÁCH XE ĐỊCH LÚC MỚI VÀO GAME
        if (enemies.isEmpty()) {
            for (int i = 0; i < ENEMY_COUNT; i++) {
                Enemy enemy = new Enemy(enemyCarsArray, screenWidth, screenHeight);
                // i * 800 giúp các xe rơi xuống nối đuôi nhau đều đặn
                enemy.resetPosition(baseSpeed, i * 800);
                enemies.add(enemy);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isGameOver) {
            // --- CẬP NHẬT VỊ TRÍ NGƯỜI CHƠI ---
            if (isMovingLeft) { playerX -= playerSpeedX; if (playerX < 0) playerX = 0; }
            if (isMovingRight) { playerX += playerSpeedX; if (playerX > screenWidth - playerCar.getWidth()) playerX = screenWidth - playerCar.getWidth(); }
            if (isMovingUp) { playerY -= playerSpeedY; if (playerY < 0) playerY = 0; }
            if (isMovingDown) { playerY += playerSpeedY; if (playerY > screenHeight - playerCar.getHeight()) playerY = screenHeight - playerCar.getHeight(); }

            // --- XỬ LÝ XE ĐỊCH & VA CHẠM ---
            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                enemy.y += enemy.speed;

                // Nếu xe địch lọt qua khỏi màn hình (người chơi né thành công)
                if (enemy.y > screenHeight) {
                    score++;

                    // Điều chỉnh tăng tốc độ rơi của xe địch để game khó dần
                    int diff = preferencesLogin.getInt("difficulty", 0);
                    int minSpeed = (diff == 2) ? 17 : (diff == 1) ? 13 : 8;
                    baseSpeed = minSpeed + (score / 10);

                    // Đẩy xe địch lên lại trên cùng (cách mép trên 500px)
                    enemy.resetPosition(baseSpeed, 500);
                }

                // Xử lý va chạm (Chỉ bắt va chạm nếu KHÔNG đang dùng kỹ năng nhảy/bay)
                if (!isFlying) {
                    boolean isCrashX = playerX < enemy.x + enemy.image.getWidth() && playerX + playerCar.getWidth() > enemy.x;
                    boolean isCrashY = playerY < enemy.y + enemy.image.getHeight() && playerY + playerCar.getHeight() > enemy.y;

                    // Nếu tông trúng xe địch (GAME OVER)
                    if (isCrashX && isCrashY) {
                        isGameOver = true;

                        if (gameOverListener != null && !isGameOverNotified) {
                            gameOverListener.onGameOver(); // Báo ra MainScene để hiện menu thua cuộc
                            isGameOverNotified = true;

                            // GỬI ĐIỂM LÊN SERVER NGAY KHOẢNH KHẮC CHẾT (Chạy 1 lần duy nhất)
                            String username = preferencesRole.getString("userName", "");
                            if (!username.isEmpty()) {
                                apiUpdatePoint.updatePoint(username, score);
                            }
                        }
                    }
                }

                // Vẽ xe địch ra màn hình
                canvas.drawBitmap(enemy.image, enemy.x, enemy.y, null);
            }
<<<<<<< Updated upstream
            finalSoure = score;
            SharedPreferences prefs = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            String username = prefs.getString("userName", "Guest"); // Thêm giá trị mặc định là "Guest"


            if (this.updatePoint != null && username != null) {
                this.updatePoint.updatePoint(username, finalSoure);
            } else {


            }
=======
>>>>>>> Stashed changes

            // --- VẼ XE NGƯỜI CHƠI ---
            if (isFlying) {
                // Đổi hình ảnh sang xe đang bay
                canvas.drawBitmap(flyCar, playerX, playerY, null);
                flyTimer--;
                if (flyTimer <= 0) isFlying = false; // Hết thời gian bay, rơi xuống lại
            } else {
                canvas.drawBitmap(playerCar, playerX, playerY, null);
            }

            // Vẽ điểm số hiện tại ở góc trái màn hình
            canvas.drawText("SCORE: " + score, 50, 100, scorePaint);

            // Gọi lại onDraw liên tục (để tạo hiệu ứng chuyển động hình ảnh 60 FPS)
            invalidate();

        } else {
<<<<<<< Updated upstream
            // Vẽ lại toàn bộ xe trên màn hình lúc chết để làm nền
=======
            // --- KHI GAME OVER (Dừng khung hình) ---
>>>>>>> Stashed changes
            for (Enemy enemy : enemies) {
                canvas.drawBitmap(enemy.image, enemy.x, enemy.y, null);
            }
            canvas.drawBitmap(playerCar, playerX, playerY, null);

<<<<<<< Updated upstream
            // 1. CĂN GIỮA VÀ ĐẨY CHỮ GAME OVER LÊN TRÊN
            scorePaint.setTextAlign(Paint.Align.CENTER); // Tự động căn giữa trục X
=======
            // In chữ GAME OVER đỏ chót ở giữa màn hình
>>>>>>> Stashed changes
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

    // --- CÁC HÀM ĐIỀU KHIỂN ĐƯỢC GỌI TỪ MAINSCENE ---
    public void setMovingLeft(boolean isMoving) { this.isMovingLeft = isMoving; }
    public void setMovingRight(boolean isMoving) { this.isMovingRight = isMoving; }
    public void setMovingUp(boolean isMoving) { this.isMovingUp = isMoving; }
    public void setMovingDown(boolean isMoving) { this.isMovingDown = isMoving; }

    // Hàm kích hoạt nhảy/bay khi bấm nút
    public void triggerJump() {
        if (!isGameOver && !isFlying) {
            isFlying = true;
            flyTimer = MAX_FLY_TIME;
        }
    }
}