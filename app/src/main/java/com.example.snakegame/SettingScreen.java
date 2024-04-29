package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SettingScreen extends GameObject implements Drawable {
    private TextPrint title;
    private int halfScreenWidth;
    private int halfScreenHeight;
    private boolean showing = false;
    private SettingsButton setButton;
    private int currentControl;
    SettingScreen(Context context, int width, int height, Paint paint) {
        super(context);
        halfScreenHeight = height/2;
        halfScreenWidth = width/2;
        currentControl = 0;
        setButton = new SettingsButton(context);
        setBitmap(loadAndScaleResource(context, R.drawable.settingscreen));
        title = new TextPrint(context, "Settings", 100, halfScreenWidth, 60, Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        canvas.drawBitmap(getBitmap(), halfScreenWidth - (halfScreenWidth/2), 75, paint);
        title.drawCenterAligned(canvas, paint);
        //swipeControl.draw(canvas, paint);
        //arrowControl.draw(canvas, paint);
        //tapControl.draw(canvas, paint);
        setButton.draw(canvas, paint);
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, halfScreenWidth, (halfScreenHeight*2) - 100, false);
    }

    public boolean isShowing() { return showing; }
    public void setShowing(boolean input) { this.showing = input; }
    public boolean backIsTouched(int x, int y) { return setButton.isTouched(x, y); }

    public int getCurrentControl() { return currentControl; }
}
