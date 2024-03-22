package com.example.snakegame;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class PauseButton {
    private Bitmap pauseButton;

    PauseButton(Context context, int ss) {
        pauseButton = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.pausebutton);

        pauseButton = Bitmap
                .createScaledBitmap(pauseButton,
                        ss, ss, false);
    }
    void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(pauseButton, 100, 980, paint);
    }
}
