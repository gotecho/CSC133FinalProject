package com.example.snakegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;
public class Apple extends GameObject implements Drawable, Collidable {
    private final Point location = new Point(); // Location of apple in grid (Not in pixels
    private final Point mSpawnRange; // The range of values we can choose from to spawn an apple
    private final int mSize; // The size of the apple

    // Constructor: Called when the Apple class is first created
    Apple(Context context, Point sr, int s){
        super(context);   // Call the constructor of the GameObject class
        mSpawnRange = sr; // Initialize the spawn range
        mSize = s;        // Initialize the size of the apple
        location.x = -10; // Initialize the location of the apple

        // Initialize and resize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
        bitmap = Bitmap.createScaledBitmap(bitmap, s, s, false);
    }

    // Function: Spawn an Apple
    void spawn(){
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Function: Draw the apple
    @Override
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bitmap,
                location.x * mSize, location.y * mSize, paint);

    }

    // Function: Check if the apple is at a specific location
    @Override
    public boolean isColliding(Point location) {
        return this.location.equals(location);
    }
}