package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.w3c.dom.Text;

public class ControlButton extends GameObject implements Drawable {
    int xLocation;
    int yLocation;
    int xSize;
    int ySize;
    private final TextPrint[] otherButtonText = new TextPrint[3];
    private final TextPrint[] controlModes = new TextPrint[3];
    private int currentControl;
    private Paint customPaint;

    // Constructor for Control Switching button with text for the modes initialized as well
    ControlButton(Context context, Paint customPaint, int xLoc, int yLoc, int xSize, int ySize) {
        super(context);
        this.customPaint = customPaint;
        xLocation = xLoc;
        yLocation = yLoc;
        this.xSize = xSize;
        this.ySize = ySize;
        setBitmap(loadAndScaleResource(context, R.drawable.controlbutton));
        controlModes[0] = new TextPrint(context, "Arrow Keys", 60, xLocation, yLocation, Color.BLACK);
        controlModes[1] = new TextPrint(context, "Tap Left or Right Side", 50, xLocation, yLocation, Color.BLACK);
        controlModes[2] = new TextPrint(context, "Directional Swipe", 35, xLocation, yLocation, Color.BLACK);
        currentControl = 0;
    }

    // Function: Draw the pause button
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(getBitmap(), xLocation,yLocation, paint);
        controlModes[currentControl].drawCenteredOnBitmap(canvas, customPaint, getBitmap());
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, ySize, xSize, false);
    }

    public boolean isTouched(int x, int y) {
        if(x >= xLocation && x <= xLocation + xSize && y >= yLocation && y <= yLocation + ySize) {
            currentControl = (currentControl + 1) % 3;
            return true;
        }
        return false;
    }

    public int getCurrentControl() {
        return currentControl;
    }
}

