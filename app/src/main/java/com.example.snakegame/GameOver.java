package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    //private final Rect replayButtonRect;
    private Bitmap replayButtonBitmap;
    private Bitmap leaderboardButtonBitmap;

    public GameOver(Context context) {
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
        //replayButtonRect = new Rect();

        replayButtonBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playagainbutton);
        replayButtonBitmap = Bitmap.createScaledBitmap(replayButtonBitmap, 900, 200, false);

        leaderboardButtonBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.leaderboardbutton);
        leaderboardButtonBitmap = Bitmap.createScaledBitmap(leaderboardButtonBitmap, 980, 250, false);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.RED);
        canvas.drawText(gameOverText, canvas.getWidth() / 2f, canvas.getHeight() / 2f, mPaint);

        canvas.drawBitmap(replayButtonBitmap, 610, 580, null);
        canvas.drawBitmap(leaderboardButtonBitmap, 540, 750, null);

    }

    // Method to check if the replay button is touched
}
