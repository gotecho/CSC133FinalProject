package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameObject {
    protected Bitmap bitmap; // The bitmap image of the game object
    protected Context context; // The context for accessing resources

    // Constructor
    public GameObject(Context context) {
        this.context = context;
    }

    // Getters and Setters
    protected void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
