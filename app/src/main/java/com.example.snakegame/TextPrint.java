package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

public class TextPrint extends GameObject implements Drawable{

    private String text;
    private int xloc;
    private int yloc;
    private int textSize;
    private int color;
    public TextPrint(Context context, String input, int size, int xloc, int yloc, int color) {
        super(context);
        text = input;
        this.textSize = size;
        this.xloc = xloc;
        this.yloc = yloc;
        this.color = color;
    }
    public void setString(String newInput) {
        text = newInput;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(textSize);
        paint.setColor(color);
        canvas.drawText(text, xloc, yloc, paint);
    }

    public void drawCenteredOnBitmap(Canvas canvas, Paint paint, Bitmap bitmap, int bitX, int bitY) {
        int halfX = bitmap.getWidth()/2;
        int halfY = bitmap.getHeight()/2;
        paint.setTextSize(textSize);
        paint.setColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, bitX + halfX, bitY + halfY, paint);
    }
}
