package com.example.snakegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.EnumMap;
import java.util.Map;

public class ArrowButtons extends GameObject implements Drawable {
    Bitmap[] directions = new Bitmap[4];
    enum Heading { UP, RIGHT, DOWN, LEFT }
    private final Map<Snake.Heading, Bitmap> bitmapForHeading = new EnumMap<>(Snake.Heading.class);
    public ArrowButtons(Context context) {
        super(context);
        Bitmap originalArrow = loadAndScaleResource(context, R.drawable.arrow);

        bitmapForHeading.put(Snake.Heading.RIGHT, Bitmap.createScaledBitmap(originalArrow, 100, 100, false));
        bitmapForHeading.put(Snake.Heading.LEFT, rotateBitmap(bitmapForHeading.get(Snake.Heading.RIGHT), 180));
        bitmapForHeading.put(Snake.Heading.DOWN, rotateBitmap(bitmapForHeading.get(Snake.Heading.RIGHT), 90));
        bitmapForHeading.put(Snake.Heading.UP, rotateBitmap(bitmapForHeading.get(Snake.Heading.RIGHT), 270));
    }

    // Function: Draw the pause button
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmapForHeading.get(Snake.Heading.RIGHT), 2000,790, paint);
        canvas.drawBitmap(bitmapForHeading.get(Snake.Heading.LEFT), 1800,790, paint);
        canvas.drawBitmap(bitmapForHeading.get(Snake.Heading.DOWN), 1900,890, paint);
        canvas.drawBitmap(bitmapForHeading.get(Snake.Heading.UP), 1900,690, paint);
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, 100, 100, false);
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public boolean isTouched(int x, int y) {
        // Touch Right
        if(x >= 2000 && x <= 2100 && y >= 790 && y <= 890) {
            return true;
        }
        // Touch Left
        else if(x >= 1800 && x <= 1900 && y >= 790 && y <= 890) {
            return true;
        }
        // Touch Up
        else if(x >= 1900 && x <= 2000 && y >= 690 && y <= 790) {
            return true;
        }
        // Touch Down
        else return x >= 1900 && x <= 2000 && y >= 890 && y <= 990;
    }
    public Snake.Heading getDirection(int x, int y) {
        if(x >= 2000 && x <= 2100 && y >= 790 && y <= 890) {
            return Snake.Heading.RIGHT;
        }
        // Touch Left
        else if(x >= 1800 && x <= 1900 && y >= 790 && y <= 890) {
            return Snake.Heading.LEFT;
        }
        // Touch Up
        else if(x >= 1900 && x <= 2000 && y >= 690 && y <= 790) {
            return Snake.Heading.UP;
        }
        // Touch Down
        else if(x >= 1900 && x <= 2000 && y >= 890 && y <= 990) {
            return Snake.Heading.DOWN;
        }
        return null;
    }
}
