package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Snake extends GameObject implements Drawable {
    final ArrayList<Point> segmentLocations;
    private final int mSegmentSize;
    private final Point mMoveRange;
    public enum Heading { UP, RIGHT, DOWN, LEFT }
    private Heading heading = Heading.RIGHT;
    private final Map<Heading, Bitmap> bitmapForHeading = new EnumMap<>(Heading.class);
    private SnakeGame snakeGame;
    private Bitmap originalBitmap;    private Maze maze;
    private Snake snake;


    // Constructor: Called when the Snake class is first created
    Snake(Context context, SnakeGame snakeGame, Point moveRange, int segmentSize) {
        super(context);
        this.snakeGame = snakeGame;
        this.snakeGame = snakeGame;
        segmentLocations = new ArrayList<>();
        mSegmentSize = segmentSize;
        mMoveRange = moveRange;
        initializeBitmaps(context, segmentSize);
    }
    Snake(Context context, Point moveRange, int segmentSize, Maze maze) {
        super(context);
        segmentLocations = new ArrayList<>();
        mSegmentSize = segmentSize;
        mMoveRange = moveRange;
        this.maze = maze; // Assign the Maze reference
        initializeBitmaps(context, segmentSize);
    }

    // Function: Initialize the bitmaps
    private void initializeBitmaps(Context context, int size) {
        Bitmap originalHead = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.body);

        bitmapForHeading.put(Heading.RIGHT, Bitmap.createScaledBitmap(originalHead, size, size, false));
        bitmapForHeading.put(Heading.LEFT, rotateBitmap(bitmapForHeading.get(Heading.RIGHT), 180));
        bitmapForHeading.put(Heading.UP, rotateBitmap(bitmapForHeading.get(Heading.RIGHT), 270));
        bitmapForHeading.put(Heading.DOWN, rotateBitmap(bitmapForHeading.get(Heading.RIGHT), 90));

        bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
        originalBitmap = bitmap;
    }

    private Bitmap addGlow(Bitmap src, int glowRadius, int glowColor) {
        // Create an empty bitmap with the same size as the source bitmap
        Bitmap alpha = src.extractAlpha();
        Bitmap bmp = Bitmap.createBitmap(src.getWidth() + glowRadius, src.getHeight() + glowRadius, Bitmap.Config.ARGB_8888);
    
        // Create a canvas to draw on the new bitmap
        Canvas c = new Canvas(bmp);
    
        // Create a paint object with the specified glow color
        Paint paint = new Paint();
        paint.setColor(glowColor);
    
        // Draw the source bitmap onto the canvas, offset by half the glow radius
        // This centers the source bitmap in the new bitmap
        c.drawBitmap(alpha, glowRadius / 2, glowRadius / 2, paint);
    
        // Use a blur mask filter to create the glow effect
        paint.setMaskFilter(new BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER));
    
        // Draw the source bitmap onto the canvas again, using the paint object with the glow effect
        // This creates the glow effect around the source bitmap
        c.drawBitmap(alpha, glowRadius / 2, glowRadius / 2, paint);

        originalBitmap = bitmap;
        return bmp;

    }

    // Function: Check if the snake is the correct color and change it if not
    public void checkColor() {
        if (snakeGame.getAppleBuffTimer() > 0) {
            bitmap = originalBitmap;
            bitmap = addGlow(bitmap, 10, Color.YELLOW);
        }
        else if (snakeGame.getScoreMultiplier() > 1) {
            bitmap = originalBitmap;
            bitmap = addGlow(bitmap, 10, Color.BLUE);
        } else {
            // Remove the glow effect by resetting the bitmap
            bitmap = originalBitmap;
        }
    }
    
    // Function: Change the glow of a bitmap
    private Bitmap changeBitmapColor(Bitmap sourceBitmap, Paint paint) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
    
        return resultBitmap;
    }

    // Function: Rotate a bitmap
    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    // Function: Reset the snake
    void reset(int width, int height) {
        heading = Heading.RIGHT;
        segmentLocations.clear();

        segmentLocations.add(new Point(width / 2, height / 2));
    }

    // Function: Move the snake
    void move() {
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            segmentLocations.get(i).set(segmentLocations.get(i - 1).x, segmentLocations.get(i - 1).y);
        }

        Point position = segmentLocations.get(0);
        switch (heading) {
            case UP: position.y--; break;
            case RIGHT: position.x++; break;
            case DOWN: position.y++; break;
            case LEFT: position.x--; break;
        }
    }

    // Function: Detect if the snake is dead
    boolean detectDeath() {
        boolean dead = false;
        Point head = segmentLocations.get(0);
        if (head.x == -1 || head.x > mMoveRange.x || head.y == -1 || head.y > mMoveRange.y) dead = true;
        for (int i = 1; i < segmentLocations.size() && !dead; i++) {
            if (head.equals(segmentLocations.get(i))) dead = true;
        }
        return dead;
    }

    // Function: Check if the snake has eaten a collidable object (e.g., an apple)
    boolean checkCollide(Collidable collidable) {
        Point headLocation = segmentLocations.get(0);
        if (collidable.isColliding(headLocation)) {
            segmentLocations.add(new Point(-10, -10)); // Add a new segment to the snake
            return true;
        }
        return false;
    }
    // Function: Check if the snake has eaten a collidable Point object
    public boolean checkCollide(Point point) {
        Point head = segmentLocations.get(0);
        return head.equals(point);
    }

    // Function: Draw the snake
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (!segmentLocations.isEmpty()) {
            canvas.drawBitmap(bitmapForHeading.get(heading), segmentLocations.get(0).x * mSegmentSize, segmentLocations.get(0).y * mSegmentSize, paint);
            for (int i = 1; i < segmentLocations.size(); i++) {
                canvas.drawBitmap(bitmap, segmentLocations.get(i).x * mSegmentSize, segmentLocations.get(i).y * mSegmentSize, paint);
            }
        }
    }

    // Function: Handle the touch event & switch the heading
    void switchHeading(Heading direction) {
        if((direction == Heading.LEFT) || (direction == Heading.RIGHT)) {
            if(direction != heading && direction != Heading.values()[(heading.ordinal() + 2) % Heading.values().length]) {
                heading = direction;
            }
        }
        else {
            if(direction != heading && direction != Heading.values()[(heading.ordinal() + 2) % Heading.values().length]) {
                heading = direction;
            }
        }
    }

    public Heading getLeft() {
        return (Heading.values()[(heading.ordinal() + 1) % Heading.values().length]);
    }
    public Heading getRight() {
        return (Heading.values()[(heading.ordinal() + 3) % Heading.values().length]);
    }
    public Point getHeadPosition() {
        return new Point(segmentLocations.get(0).x, segmentLocations.get(0).y);
    }

    // Function: Restore the saved state of the snake
    void restoreSnakeState(int width, int height) {
        heading = Heading.RIGHT;

        // Store the tail segments temporarily
        List<Point> tailSegments = new ArrayList<>();
        for (int i = 1; i < segmentLocations.size(); i++) {
            tailSegments.add(segmentLocations.get(i));
        }

        segmentLocations.clear();
        // New head position
        segmentLocations.add(new Point(width / 2, height / 2));
        // Re-add the tail segments
        segmentLocations.addAll(tailSegments);
    }

    // Getter & Setter for the Snake object
    public Snake getSnake() {
        return snake;
    }

    // Setter for the Snake object
    public void setSnake(Snake snake) {
        this.snake = snake;
    }
}
