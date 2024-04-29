package com.example.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class PauseScreen extends GameObject implements Drawable {
    private ControlButton quitButton;
    private TextPrint pauseTextTop;
    private TextPrint pauseTextBottom;
    private int halfScreenWidth;
    private int halfScreenHeight;
    private boolean showing = true;
    private SettingsButton setButton;

    PauseScreen(Context context, int width, int height, Paint paint) {
        super(context);
        halfScreenHeight = height/2;
        halfScreenWidth = width/2;
        setButton = new SettingsButton(context);
        setButton.changeLocation(width - 120);
        pauseTextTop = new TextPrint(context, "tap anywhere to resume", 80, halfScreenWidth, (halfScreenHeight*2) - 75, Color.BLACK);
        pauseTextBottom = new TextPrint(context, "paused", 300, halfScreenWidth, 300, Color.BLACK);
        quitButton = new ControlButton(context, paint, width - 400, height - 200, 125, 250, "quit", 100);

    }
    @Override
    public void draw(Canvas canvas, Paint paint) {
        pauseTextTop.drawCenterAligned(canvas, paint);
        pauseTextBottom.drawCenterAligned(canvas, paint);
        quitButton.draw(canvas, paint);
        setButton.draw(canvas, paint);
    }

    public boolean settingsIsTouched(int x, int y) { return setButton.isTouched(x, y); }

    public boolean quitIsTouched(int x, int y) { return quitButton.isTouched(x, y); }
}
