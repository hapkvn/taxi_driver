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

    public Enemy(Bitmap[] availableCars, int screenWidth, int screenHeight) {
        this.availableCars = availableCars;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }


    public void resetPosition(int baseSpeed, int extraYOffset) {

        image = availableCars[random.nextInt(availableCars.length)];


        float marginRatio = 40f / 360f;
        int margin = (int) (screenWidth * marginRatio);


        int playableWidth = screenWidth - (2 * margin);
        int laneWidth = playableWidth / 3;


        int lane1Center = margin + (laneWidth / 2);
        int lane2Center = margin + laneWidth + (laneWidth / 2);
        int lane3Center = margin + (2 * laneWidth) + (laneWidth / 2);


        int[] lanes = {lane1Center, lane2Center, lane3Center};
        int chosenLaneCenter = lanes[random.nextInt(lanes.length)];


        x = chosenLaneCenter - (image.getWidth() / 2);


        y = -image.getHeight() - extraYOffset - random.nextInt(400);
        speed = baseSpeed + random.nextInt(4);
    }
}