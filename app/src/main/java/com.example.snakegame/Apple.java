package com.example.snakegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;
class Apple {
    private Point location = new Point(); // Location of apple in grid (Not in pixels
    private Point mSpawnRange; // The range of values we can choose from to spawn an apple
    private int mSize; // The size of the apple
    private Bitmap mBitmapApple; // The bitmap to draw the apple

    // Constructor: Called when the Apple class is first created
    Apple(Context context, Point sr, int s){
        mSpawnRange = sr; // Initialize the spawn range
        mSize = s;        // Initialize the size of the apple
        location.x = -10; // Initialize the location of the apple

        // Initialize and resize the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, s, s, false);
    }

    // Function: Spawn an Apple
    void spawn(){
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Function: Draw the apple
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapApple,
                location.x * mSize, location.y * mSize, paint);

    }

    // Getters
    Point getLocation(){
        return location;
    }
}