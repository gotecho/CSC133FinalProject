package com.example.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class MazeGame {
    private static MazeGame instance;
    private Maze maze;
    private boolean inMazeGame;
    private SnakeGame mSnakeGame;


    private MazeGame(Context context, int blockSize) {
        maze = new Maze(context, blockSize);
        inMazeGame = false;
    }

    public static MazeGame getInstance(Context context, int blockSize) {
        if (instance == null) {
            instance = new MazeGame(context, blockSize);
        }
        return instance;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (inMazeGame && maze != null && canvas != null) {
            Log.d("MazeGame", "Drawing maze...");
            // Ensure that mazeLayout is initialized before drawing
            if (maze != null) {
                // Pass mazeGame object to Maze draw() method
                maze.setMazeGame(this);
                maze.draw(canvas, paint);
            } else {
                Log.e("MazeGame", "Maze object is null.");
            }
        } else {
            Log.e("MazeGame", "Canvas or maze object is null, or not in maze game mode.");
        }
    }
    public void startMaze() {
        inMazeGame = true;
    }
    // Getter & Setters of MazeGame, Maze, & SnakeGame
    public void setSnakeGame(SnakeGame snakeGame){
        this.mSnakeGame = snakeGame;
    }
    public void setMazeGame(Maze maze) {
        this.maze = maze;
    }
    public Maze getMaze() {
        return maze;
    }
    public void setMaze(Maze maze) {
        this.maze = maze;
    }
}


