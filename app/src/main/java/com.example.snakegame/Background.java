package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Background extends GameObject implements Drawable{
    private final int height = 1100; // Screen height in pixels
    private final int width = 2100; // Screen width in pixels
    Background(Context context) {
        super(context);
        setBitmap(loadAndScaleResource(context, R.drawable.background));
    }
    // Function: Draw the pause button
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(getBitmap(), 0, 0, paint);
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
}
