package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;

public class TextPrint extends GameObject implements Drawable{

    private final Paint mCustomTextPaint = new Paint();
    private final Typeface customFont = Typeface.createFromAsset(context.getAssets(), "PixelOperator.ttf");
    private final int quarterTextSize;
    private String text;
    private final int xLoc;
    private final int yLoc;

    public TextPrint(Context context, String input, int size, int xLoc, int yLoc, int color) {
        super(context);
        text = input;
        mCustomTextPaint.setTextSize(size);
        mCustomTextPaint.setTypeface(customFont);
        mCustomTextPaint.setColor(color);
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        quarterTextSize = size / 4;
    }
    public void setString(String newInput) {
        text = newInput;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawText(text, xLoc, yLoc, mCustomTextPaint);
    }

    public void drawCenteredOnBitmap(Canvas canvas, Paint paint, Bitmap bitmap) {
        int halfX = bitmap.getWidth()/2;
        int halfY = bitmap.getHeight()/2;
        mCustomTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, xLoc + halfX, yLoc + halfY + quarterTextSize, mCustomTextPaint);
    }

    public void drawRightAligned(Canvas canvas, Paint paint) {
        mCustomTextPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(text, xLoc, yLoc, mCustomTextPaint);
    }

    public void drawCenterAligned(Canvas canvas, Paint paint) {
        mCustomTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, xLoc, yLoc, mCustomTextPaint);
    }
}
