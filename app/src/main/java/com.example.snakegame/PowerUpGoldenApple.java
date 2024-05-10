package com.example.snakegame;

import android.content.Context;
import android.graphics.Point;

public class PowerUpGoldenApple extends PowerUp {
    // Constructor
    PowerUpGoldenApple(SnakeGame snakeGame, Context context, Point spawnRange, int size){
        super("Apple Generator", snakeGame, context, spawnRange, size, R.drawable.golden_apple);
    }

    @Override
    public void activate() {
        snakeGame.setAppleCount(6);
        snakeGame.setAppleBuffTimer(6);
    }
}