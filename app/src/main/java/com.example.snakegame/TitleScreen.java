package com.example.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;


public class TitleScreen extends GameObject implements Drawable {
    private ControlButton startButton;
    private ControlButton leaderboardButton;
    private TextPrint[] authors = new TextPrint[5];
    private TextPrint title;
    private int halfScreenWidth;
    private int halfScreenHeight;
    private boolean showing = true;
    private SettingsButton setButton;
    TitleScreen(Context context, int width, int height, Paint paint) {
        super(context);
        halfScreenHeight = height/2;
        halfScreenWidth = width/2;
        authors[0] = new TextPrint(context, "Kevin Cendana", 30, width - 10, 20, Color.BLACK);
        authors[1] = new TextPrint(context, "Danica Galang", 30, width - 10, 60, Color.BLACK);
        authors[2] = new TextPrint(context, "Isabel Santoyo-Garcia", 30, width - 10, 100, Color.BLACK);
        authors[3] = new TextPrint(context, "Brandon Barragan", 30, width - 10, 140, Color.BLACK);
        authors[4] = new TextPrint(context, "Anthony Vitro", 30, width - 10, 180, Color.BLACK);
        setButton = new SettingsButton(context);

        title = new TextPrint(context, "Snake Game", 200, halfScreenWidth, halfScreenHeight - 200, Color.BLACK);
        leaderboardButton = new ControlButton(context, paint, halfScreenWidth + 300, halfScreenHeight - 50, 700, 500, "Leaderboard", 60);
        startButton = new ControlButton(context, paint, halfScreenWidth - 900, halfScreenHeight - 50, 700, 500, "Start Game", 60);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        for (int i = 0; i < 5; i++) {
            authors[i].drawRightAligned(canvas, paint);
        }
        title.drawCenterAligned(canvas, paint);
        leaderboardButton.draw(canvas, paint);
        startButton.draw(canvas, paint);
        setButton.draw(canvas, paint);
    }

    public boolean isShowing() { return showing; }
    public void setShowing(boolean input) { this.showing = input; }
    public boolean settingsIsTouched(int x, int y) { return setButton.isTouched(x, y); }
    public boolean startIsTouched(int x, int y) { return startButton.isTouched(x, y); }

}
