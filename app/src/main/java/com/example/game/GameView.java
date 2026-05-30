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
    private audioMain audio;
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
        String selectedCar = prefs.getString("selected_car", "red");
        int carRes;
        int flyCarRes;
        audio = audioMain.getInstance(context);
        switch (selectedCar) {

            case "fly":

                carRes = R.drawable.redcar;
                flyCarRes = R.drawable.flycar;
                break;
            case "sport":
                carRes = R.drawable.sportcar;
                flyCarRes = R.drawable.sportcarfly;
                break;
            case "sportfly":
                carRes = R.drawable.sportcarfly;
                flyCarRes = R.drawable.sportcarfly;
                break;
            default:
                carRes = R.drawable.redcar;
                flyCarRes = R.drawable.flycar;
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


        if (enemies.isEmpty()) {
            for (int i = 0; i < ENEMY_COUNT; i++) {
                Enemy enemy = new Enemy(enemyCarsArray, screenWidth, screenHeight);

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


            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                enemy.y += enemy.speed;

                if (enemy.y > screenHeight) {
                    score++;

                    int diff = getContext().getSharedPreferences("login", Context.MODE_PRIVATE).getInt("difficulty", 0);
                    int minSpeed = (diff == 2) ? 17 : (diff == 1) ? 13 : 8;
                    baseSpeed = minSpeed + (score / 10);


                    enemy.resetPosition(baseSpeed, 500);
                }

                if (!isFlying) {
                    boolean isCrashX = playerX < enemy.x + enemy.image.getWidth() && playerX + playerCar.getWidth() > enemy.x;
                    boolean isCrashY = playerY < enemy.y + enemy.image.getHeight() && playerY + playerCar.getHeight() > enemy.y;

                    if (isCrashX && isCrashY) {
                        isGameOver = true;
                        if (gameOverListener != null && !isGameOverNotified) {


                            SharedPreferences prefRole = getContext().getSharedPreferences("role", Context.MODE_PRIVATE);
                            String username = prefRole.getString("userName", "");
                            int oldHighScore = prefRole.getInt("point", 0);


                            if (score > oldHighScore) {
                                SharedPreferences.Editor editor = prefRole.edit();
                                editor.putInt("point", score);
                                editor.apply();
                            }

                            if (!username.isEmpty()) {
                                updatePoint apiUpdate = new updatePoint();
                                apiUpdate.updatePoint(username, score);
                            }


                            gameOverListener.onGameOver();
                            isGameOverNotified = true;
                        }
                    }
                }

                canvas.drawBitmap(enemy.image, enemy.x, enemy.y, null);
            }



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

            for (Enemy enemy : enemies) {
                canvas.drawBitmap(enemy.image, enemy.x, enemy.y, null);
            }
            canvas.drawBitmap(playerCar, playerX, playerY, null);

            audio.playSound("crash");
            scorePaint.setTextAlign(Paint.Align.CENTER);
            scorePaint.setColor(Color.RED);
            scorePaint.setTextSize(90);
            scorePaint.setFakeBoldText(true);


            float gameOverY = screenHeight / 3.5f;
            canvas.drawText("GAME OVER", screenWidth / 2f, gameOverY, scorePaint);


            scorePaint.setColor(Color.YELLOW);
            scorePaint.setTextSize(65);

            canvas.drawText("FINAL SCORE: " + score, screenWidth / 2f, gameOverY + 120, scorePaint);


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