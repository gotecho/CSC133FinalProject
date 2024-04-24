package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.w3c.dom.Text;

public class ControlButton extends GameObject implements Drawable {
    private final TextPrint[] controlModes = new TextPrint[3];
    private int currentControl;
    private Paint customPaint;

    // Constructor for Control Switching button with text for the modes initialized as well
    ControlButton(Context context, Paint customPaint) {
        super(context);
        this.customPaint = customPaint;
        setBitmap(loadAndScaleResource(context, R.drawable.controlbutton));
        controlModes[0] = new TextPrint(context, "Arrow Keys", 50, 1400, 850,Color.BLACK);
        controlModes[1] = new TextPrint(context, "Tap Left or Right Side", 30, 1400, 850, Color.BLACK);
        controlModes[2] = new TextPrint(context, "Directional Swipe", 35, 750, 850, Color.BLACK);
        currentControl = 0;
    }

    // Function: Draw the pause button
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(getBitmap(), 1400,790, paint);
        controlModes[currentControl].drawCenteredOnBitmap(canvas, customPaint, getBitmap(), 1400, 800);
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, 400, 300, false);
    }

    public boolean isTouched(int x, int y) {
        if(x >= 1400 && x <= 1800 && y >= 790 && y <= 1090) {
            currentControl = (currentControl + 1) % 3;
            return true;
        }
        return false;
    }

    public int getCurrentControl() {
        return currentControl;
    }
}
