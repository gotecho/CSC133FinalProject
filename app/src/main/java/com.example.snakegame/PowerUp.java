package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class PowerUp extends GameObject implements Drawable, Collidable {
    private final Point location = new Point(); // Location of power up in grid (Not in pixels)
    private final Point mSpawnRange;            // Range of values we can choose from to spawn a power up
    private final int mSize;                    // Size of the power up
    private int score;                          // Score the player gets when they eat the power up

    // Constructor: Called when the PowerUp class is first created
    PowerUp(Context context, Point spawnRange, int size, int drawableId){
        super(context);             // Call the constructor of the GameObject class
        mSpawnRange = spawnRange;   // Initialize the spawn range
        mSize = size;               // Initialize the size of the power up
        location.x = -10;           // Initialize the location of the power up
        score = 0;                  // Initialize the score (Default is 0)

        // Initialize and resize the bitmap
        setBitmap(BitmapFactory.decodeResource(context.getResources(), drawableId));
        setBitmap(Bitmap.createScaledBitmap(getBitmap(), size, size, false));
    }

    // Function: Spawn a PowerUp
    void spawn(){
        // Choose two random values and place the power up
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Function: Draw the power up
    @Override
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(getBitmap(), location.x * mSize, location.y * mSize, paint);
    }

    // Function: Check if the power up is at a specific location
    @Override
    public boolean isColliding(Point location) {
        return this.location.equals(location);
    }

    // Getters and Setters
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}