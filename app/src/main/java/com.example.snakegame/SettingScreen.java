package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.w3c.dom.Text;

public class SettingScreen extends GameObject implements Drawable {
    private TextPrint[] others = new TextPrint[2];
    private int halfScreenWidth;
    private int halfScreenHeight;
    private boolean showing = false;
    private SettingsButton setButton;
    private int currentControl;
    private TextPrint currentControlModeText[] = new TextPrint[3];
    private TextPrint currentMode;
    private ControlButton[] controlModes = new ControlButton[3];
    SettingScreen(Context context, int width, int height, Paint paint) {
        super(context);
        halfScreenHeight = height/2;
        halfScreenWidth = width/2;
        int quarterScreenWidth = width / 2;
        currentControl = 0;
        setButton = new SettingsButton(context);
        setBitmap(loadAndScaleResource(context, R.drawable.settingscreen));
        others[0] = new TextPrint(context, "Settings", 100, halfScreenWidth, 70, Color.BLACK);
        controlModes[0] = new ControlButton(context, paint, halfScreenWidth - ((2*quarterScreenWidth)/5), halfScreenHeight, 150, 400,"Arrow Keys", 70);
        controlModes[1] = new ControlButton(context, paint, halfScreenWidth - 200, halfScreenHeight + (halfScreenHeight/2), 150, 400,"Tap Controls", 70);
        controlModes[2] = new ControlButton(context, paint, halfScreenWidth + 45, halfScreenHeight, 150, 400,"Directional Swipe", 50);
        currentMode = new TextPrint(context, "Current Control Setting:", 60, halfScreenWidth - (quarterScreenWidth/2) + 40, 150, Color.BLACK);
        currentControlModeText[0] = new TextPrint(context, "Arrow Keys", 60, halfScreenWidth + (quarterScreenWidth/2) - 55, 150, Color.BLACK);
        currentControlModeText[1] = new TextPrint(context, "Tap Controls", 60, halfScreenWidth + (quarterScreenWidth/2) - 55, 150, Color.BLACK);
        currentControlModeText[2] = new TextPrint(context, "Directional Swipe", 60, halfScreenWidth + (quarterScreenWidth/2) - 55, 150, Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        canvas.drawBitmap(getBitmap(), halfScreenWidth - (halfScreenWidth/2), 75, paint);
        others[0].drawCenterAligned(canvas, paint);
        currentMode.draw(canvas, paint);
        currentControlModeText[currentControl].drawRightAligned(canvas, paint);
        for(int i = 0; i < controlModes.length; i++) {
            controlModes[i].draw(canvas, paint);
        }
        setButton.draw(canvas, paint);
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, halfScreenWidth, (halfScreenHeight*2) - 100, false);
    }

    public boolean isShowing() { return showing; }
    public void setShowing(boolean input) { this.showing = input; }
    public boolean backIsTouched(int x, int y) { return setButton.isTouched(x, y); }
    public boolean controlChange(int x, int y) {
        for(int i = 0; i < controlModes.length; i++) {
            if(controlModes[i].isTouched(x, y)) {
                currentControl = i;
                return true;
            }
        }
        return false;
    }

    public int getCurrentControl() { return currentControl; }
}
