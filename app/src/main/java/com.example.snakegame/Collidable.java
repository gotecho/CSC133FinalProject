package com.example.snakegame;

import android.graphics.Point;

public interface Collidable {
    boolean isColliding(Point location);
}
