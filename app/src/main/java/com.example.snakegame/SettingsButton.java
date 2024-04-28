package com.example.snakegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
public class SettingsButton extends GameObject implements Drawable {
    private boolean isShown = false;
    private int yLevel;
    private int xLevel;
    SettingsButton(Context context) {
        super(context);
        yLevel = 20;
        xLevel = 20;
        setBitmap(loadAndScaleResource(context, R.drawable.settingsbutton));
    }
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(getBitmap(), xLevel,yLevel, paint);
    }

    private Bitmap loadAndScaleResource(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, 100, 100, false);
    }


    public boolean isTouched(int x, int y) {
        return x >= xLevel && x <= xLevel + 100 && y >= yLevel && y <= yLevel + 100;
    }

    public void displaySwitch() {
        this.isShown = !isShown;
    }

    public void changeLocation(int x) {
        xLevel = x;
    }

}
