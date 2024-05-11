package com.example.snakegame;

import android.content.Context;
import android.graphics.Point;

public class PowerUpFactory {
    public static PowerUp createPowerUp(String name, SnakeGame snakeGame, Context context, Point spawnRange, int size) {
        if ("ScoreDoubler".equals(name)) {
            return new PowerUpScoreDoubler(snakeGame, context, spawnRange, size);
        } else if ("GoldenApple".equals(name)) {
            return new PowerUpGoldenApple(snakeGame, context, spawnRange, size);
        }
        return null;  // Return null or throw an exception if type is unknown
    }
}
