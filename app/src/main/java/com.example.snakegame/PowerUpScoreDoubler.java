package com.example.snakegame;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

public class PowerUpScoreDoubler extends PowerUp {
    // Constructor
    PowerUpScoreDoubler(SnakeGame snakeGame, Context context, Point spawnRange, int size){
        super("Score Doubler", snakeGame, context, spawnRange, size, R.drawable.scoredoubler);
    }

    @Override
    public void activate() {
        Log.d("print-log", "PowerUpScoreDoubler.activate(): Activated Score Doubler power up.");
        snakeGame.setScoreMultiplier(2);
    }
}