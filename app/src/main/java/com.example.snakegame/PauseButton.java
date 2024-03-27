package com.example.snakegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
public class PauseButton extends GameObject implements Drawable {
    // Constructor: Called when the PauseButton class is first created
    PauseButton(Context context, int ss) {
        super(context);
        bitmap = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.pausebutton);

        bitmap = Bitmap
                .createScaledBitmap(bitmap,
                        ss, ss, false);
    }
    // Function: Draw the pause button
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, 100, 980, paint);
    }
}
