//package com.example.snakegame;
//
//import android.content.Context;
//import android.graphics.Point;
//
//public class PowerUpAppleGenerator extends PowerUp {
//    // Constructor: Called when the PowerUpAppleGenerator class is first created
//    PowerUpAppleGenerator(SnakeGame snakeGame, Context context, Point spawnRange, int size){
//        super("Apple Generator", snakeGame, context, spawnRange, size, R.drawable.applegenerator);
//    }
//
//    @Override
//    public void activate() {
//        // Spawn an apple
//        Apple apple = new Apple(snakeGame.getContext(), mSpawnRange, mSize);
//        apple.spawn();
//        snakeGame.setAppleCount(snakeGame.getAppleCount() + 1);
//    }
//}