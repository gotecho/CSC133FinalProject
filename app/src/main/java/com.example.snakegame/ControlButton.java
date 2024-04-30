package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.w3c.dom.Text;

public class ControlButton extends GameObject implements Drawable {
    private int xLocation;
    private int yLocation;
    private int xSize;
    private int ySize;
    private final TextPrint buttonText;
    private Paint customPaint;

    // Constructor for Control Switching button with text for the modes initialized as well
    ControlButton(Context context, Paint customPaint, int xLoc, int yLoc, int xSize, int ySize, String buttonText, int textSize) {
        super(context);
        this.customPaint = customPaint;
        xLocation = xLoc;
        yLocation = yLoc;
        this.xSize = xSize;
        this.ySize = ySize;
        setBitmap(loadAndScaleResource(context, R.drawable.controlbutton));
        this.buttonText = new TextPrint(context, buttonText, textSize, xLocation, yLocation, Color.BLACK);
    }

    // Function: Draw the pause button
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(getBitmap(), xLocation, yLocation, paint);
        buttonText.drawCenteredOnBitmap(canvas, customPaint, getBitmap());
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, ySize, xSize, false);
    }

    public boolean isTouched(int x, int y) {
        if(x >= xLocation && x <= xLocation + ySize && y >= yLocation && y <= yLocation + xSize) {
            return true;
        }
        return false;
    }
}

