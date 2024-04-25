package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class BadApple extends GameObject implements Drawable, Collidable{
    private final Point location = new Point(); // Location of bad apple in grid (Not in pixels)
    private final Point mSpawnRange; // The range of values we can choose from to spawn a bad apple
    private final int mSize; // The size of the bad apple
    private boolean exploded = false; // flag to track if the bad apple has exploded
    private boolean isSpawned = false;
    private long spawnTime; // Tracks the time when bad apple was spawned
    private final long explosionDelay = 4000; // 4 secs delay
    private Bitmap explodedBadAppleBitmap;
    private SnakeGame game;
    private static final int MAX_DIRT_BLOCK_SIZE = 1;


    BadApple(Context context, Point spawnRange, int size){
        super(context);   // Call the constructor of the GameObject class
        mSpawnRange = spawnRange; // Initialize the spawn range
        mSize = size;        // Initialize the size of the bad apple
        location.x = -10; // Initialize the location of the bad apple

        // Initialize and resize the bitmap
        setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.badapple));
        setBitmap(Bitmap.createScaledBitmap(getBitmap(), size, size, false));

        // Initialize and resize the exploded bad apple image
        explodedBadAppleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explodedbadapple);
        explodedBadAppleBitmap = Bitmap.createScaledBitmap(explodedBadAppleBitmap, size, size, false);
    }

    void spawn(){
        // Choose two random values and place the bad apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
        isSpawned = false;
        exploded = false;
        spawnTime = System.currentTimeMillis();
    }

    @Override
    public boolean isColliding(Point location) {
        return this.location.equals(location);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(exploded){
            // Draw the exploded bad apple
            canvas.drawBitmap(explodedBadAppleBitmap, location.x * mSize, location.y * mSize, paint);
        } else{
            // Draw the bad apple
            canvas.drawBitmap(getBitmap(), location.x * mSize, location.y * mSize, paint);
        }
    }

    public void update(){
        if(!exploded && System.currentTimeMillis() - spawnTime >= explosionDelay){
            exploded(); // Triggers explosion if 10 seconds has passed

            //Update sprite
            setBitmap(explodedBadAppleBitmap);
            setBitmap(Bitmap.createScaledBitmap(explodedBadAppleBitmap, mSize, mSize, false));
        }
    }

    // Explodes the bad apple & spawns the dirt blocks
    public void exploded(){
        exploded = true;
        int explosionRadius = 2;
        for(int dx = -explosionRadius; dx <= explosionRadius; dx++){
            for(int dy = -explosionRadius; dy <= explosionRadius; dy++){
                int newX = location.x + dx;
                int newY = location.y + dy;
                // Check if the new position is within the game grid
                if (newX >= 0 && newX < mSpawnRange.x && newY >= 0 && newY < mSpawnRange.y) {
                    // Spawn dirt blocks
                    spawnDirtBlocks(new Point(newX, newY), 1);
                }
            }
        }
    }

    // Draw a dirt block on the canvas
    private void spawnDirtBlocks(Point explosionLocation, float explosionSize){
        Random random = new Random();

        // Calculate random offsets for x and y within the explosion radius
        double dx = random.nextDouble() * (explosionSize / 2) - (explosionSize / 4); // Random value in range [-explosionSize/4, explosionSize/4]
        double dy = random.nextDouble() * (explosionSize / 2) - (explosionSize / 4); // Random value in range [-explosionSize/4, explosionSize/4]
        // Adjust the spawn location based on explosion location & offsets
        double newX = explosionLocation.x + dx;
        double newY = explosionLocation.y + dy;

        // Check if the new position is within the game grid
        if (newX >= 0 && newX < mSpawnRange.x && newY >= 0 && newY < mSpawnRange.y) {
            if(game != null){
                // Calculate the dirt block size based on the current score
                float blockSize = calculateDirtBlockSize(game.mScore);

                game.spawnDirtBlocks(new Point((int)newX, (int)newY), (int) blockSize); // Adjust the size if needed
            }
        }
    }
    private float calculateDirtBlockSize(int score) {
        // Adjust the dirt block radius size based on the level
        switch (score / 5) {
            case 0: // level = 5-9
                return 0.25f; // Minimum radius size
            case 1: // level = 10-14
                return 0.5f;
            case 2: // level = 15-20
                return 0.75f;
            case 3: // level = 20 - onwards
                return 1.0f;
            default:
                return MAX_DIRT_BLOCK_SIZE;
        }
    }

    public boolean isDirtBlockWithinExplosionRadius(Point dirtBlock) {
        double explosionRadius = 0.25;
        double dx = dirtBlock.x - location.x;
        double dy = dirtBlock.y - location.y;
        return Math.abs(dx) <= explosionRadius && Math.abs(dy) <= explosionRadius;
    }

    public boolean isSpawned(){
        return isSpawned;
    }

    // Setter method to set the SnakeGame instance
    public void setGame(SnakeGame game){
        this.game = game;
    }
    public void clear() {
        location.set(-10, -10); // Reset the location of the bad apple to a default value
        isSpawned = false; // Reset the spawned flag
        exploded = false; // Reset the exploded flag
        spawnTime = 0; // Reset the spawn time
    }
}
