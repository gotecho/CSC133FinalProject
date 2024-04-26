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
    private Bitmap replayButtonBitmap;
    private final Rect replayButtonRect;

    private Bitmap leaderboardButtonBitmap;
    private Canvas canvas;

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

        replayButtonBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playagainbutton);
        int buttonWidth = replayButtonBitmap.getWidth();
        int buttonHeight = replayButtonBitmap.getHeight();
        int buttonX = 610; // Adjust as needed
        int buttonY = 580; // Adjust as needed

        // Initialize the bounds of the "Play Again" button
        replayButtonRect = new Rect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight);

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

    public boolean isReplayButtonTouched(int x, int y){
        return x >= 610 && x <= 610 + replayButtonBitmap.getWidth() &&
                y >= 580 && y <= 580 + replayButtonBitmap.getHeight();

    }

}
