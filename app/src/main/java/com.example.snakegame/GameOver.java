package com.example.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameOver implements Drawable {
    private final Paint mPaint;
    private final String gameOverText;
    private final String replayButtonText;
    private final Rect replayButtonRect;

    public GameOver(Context context){
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(400);
        mPaint.setTextAlign(Paint.Align.CENTER);

        // Load custom font from assets
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "PixelOperator.ttf");
        // Set custom font to Paint object
        mPaint.setTypeface(customFont);

        gameOverText = "Game Over!";
        replayButtonText = "Replay";
        replayButtonRect = new Rect();
    }
    public void draw(Canvas canvas, Paint paint){
        canvas.drawColor(Color.RED);
        canvas.drawText(gameOverText, canvas.getWidth() / 2f, canvas.getHeight() / 2f, mPaint);

    }

    // Method to check if the replay button is touched
    public boolean isGameOverTouched(int x, int y) {
        return replayButtonRect.contains(x, y);
    }
}
