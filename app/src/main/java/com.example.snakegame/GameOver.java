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
    private final SettingsButton settingsButton;
    private final SettingScreen settingScreen;
    private Bitmap exitButtonBitmap;

    public GameOver(Context context, int width, int height, Paint paint) {
        settingsButton = new SettingsButton(context);
        settingScreen = new SettingScreen(context, width, height, paint);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(400);
        mPaint.setTextAlign(Paint.Align.CENTER);

        // Load custom font from assets
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "PixelOperator.ttf");
        // Set custom font to Paint object
        mPaint.setTypeface(customFont);

        gameOverText = "Game Over!";

        settingsButton.changeLocation(20);

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

        exitButtonBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.exitleft);
        int exitButtonWidth = exitButtonBitmap.getWidth();
        int exitButtonHeight = exitButtonBitmap.getHeight();
        exitButtonBitmap = Bitmap.createScaledBitmap(exitButtonBitmap, 200, 200, false);



    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.RED);
        canvas.drawText(gameOverText, canvas.getWidth() / 2f, canvas.getHeight() / 2f, mPaint);

        canvas.drawBitmap(replayButtonBitmap, 610, 580, null);
        canvas.drawBitmap(leaderboardButtonBitmap, 540, 750, null);
        canvas.drawBitmap(exitButtonBitmap,10, 800, null );

        settingsButton.draw(canvas, paint);

    }

    public boolean isReplayButtonTouched(int x, int y){
        return x >= 610 && x <= 610 + replayButtonBitmap.getWidth() &&
                y >= 580 && y <= 580 + replayButtonBitmap.getHeight();

    }
    public boolean isSettingsButtonTouched(int x, int y) {
        // Check if the touch event is within the bounds of the settings button
        return settingsButton.isTouched(x, y);
    }
    public boolean isExitButtonTouched(int x, int y){
        return x >= 10 && x <= 10 + replayButtonBitmap.getWidth() &&
                y >= 800 && y <= 800 + replayButtonBitmap.getHeight();
    }


}
