package com.example.snakegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class arrowButtons extends GameObject implements Drawable {
    arrowButtons(Context context) {
        super(context);
        setBitmap(loadAndScaleResource(context, R.drawable.arrow));

    }

    // Function: Draw the pause button
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(getBitmap(), 10,890, paint);
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, 100, 100, false);
    }


    public boolean isTouched(int x, int y) {
        return x >= 10 && x <= 110 && y >= 890 && y <= 990;
    }
}
