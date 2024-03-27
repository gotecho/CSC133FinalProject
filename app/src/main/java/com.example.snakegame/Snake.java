package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Snake implements Drawable {
    private final ArrayList<Point> segmentLocations;
    private final int mSegmentSize;
    private final Point mMoveRange;
    private final int halfWayPoint;
    private enum Heading { UP, RIGHT, DOWN, LEFT }
    private Heading heading = Heading.RIGHT;

    private final Map<Heading, Bitmap> bitmapForHeading = new EnumMap<>(Heading.class);
    private Bitmap mBitmapBody;

    // Constructor: Called when the Snake class is first created
    Snake(Context context, Point mr, int ss) {
        segmentLocations = new ArrayList<>();
        mSegmentSize = ss;
        mMoveRange = mr;

        initializeBitmaps(context, ss);
        halfWayPoint = mr.x * ss / 2;
    }

    // Function: Initialize the bitmaps
    private void initializeBitmaps(Context context, int size) {
        Bitmap originalHead = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        mBitmapBody = BitmapFactory.decodeResource(context.getResources(), R.drawable.body);

        bitmapForHeading.put(Heading.RIGHT, Bitmap.createScaledBitmap(originalHead, size, size, false));
        bitmapForHeading.put(Heading.LEFT, rotateBitmap(bitmapForHeading.get(Heading.RIGHT), 180));
        bitmapForHeading.put(Heading.UP, rotateBitmap(bitmapForHeading.get(Heading.RIGHT), 90));
        bitmapForHeading.put(Heading.DOWN, rotateBitmap(bitmapForHeading.get(Heading.RIGHT), 270));

        mBitmapBody = Bitmap.createScaledBitmap(mBitmapBody, size, size, false);
    }

    // Function: Rotate a bitmap
    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    // Function: Reset the snake
    void reset(int w, int h) {
        heading = Heading.RIGHT;
        segmentLocations.clear();
        segmentLocations.add(new Point(w / 2, h / 2));
    }

    // Function: Move the snake
    void move() {
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            segmentLocations.get(i).set(segmentLocations.get(i - 1).x, segmentLocations.get(i - 1).y);
        }

        Point p = segmentLocations.get(0);
        switch (heading) {
            case UP: p.y--; break;
            case RIGHT: p.x++; break;
            case DOWN: p.y++; break;
            case LEFT: p.x--; break;
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


    // Function: Draw the snake
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (!segmentLocations.isEmpty()) {
            canvas.drawBitmap(bitmapForHeading.get(heading), segmentLocations.get(0).x * mSegmentSize, segmentLocations.get(0).y * mSegmentSize, paint);
            for (int i = 1; i < segmentLocations.size(); i++) {
                canvas.drawBitmap(mBitmapBody, segmentLocations.get(i).x * mSegmentSize, segmentLocations.get(i).y * mSegmentSize, paint);
            }
        }
    }

    // Function: Handle the touch event & switch the heading
    void switchHeading(MotionEvent motionEvent) {
        if (motionEvent.getX() >= halfWayPoint) {
            heading = Heading.values()[(heading.ordinal() + 1) % Heading.values().length];
        } else {
            heading = Heading.values()[(heading.ordinal() + 3) % Heading.values().length]; // +3 for counterclockwise rotation
        }
    }
}
