package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.WindowManager;
import android.util.DisplayMetrics;

public class Background extends GameObject implements Drawable{

    private int width;
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
        // Get size of any device's screen using the application
        DisplayMetrics disMet = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(disMet);
        int height = disMet.heightPixels;
        width = disMet.widthPixels;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public int getWidth() {
        return width;
    }
}
