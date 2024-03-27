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
}
