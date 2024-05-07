package com.example.snakegame;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;


public class Maze implements Drawable {
    private volatile int[][] mazeLayout;
    private int[][] layout;
    private int mazeWidth;
    private int mazeHeight;
    private final int blockSize;
    private Paint wallPaint;
    private Paint pathPaint;
    private Paint exitPaint;
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}}; // Right, Left, Down, U

    // Constants for cell types
    private static final int PATH = 1;
    private static final int WALL = 0;
    private static final int EXIT = 2;

    // Colors for different cell types (customize as needed)
    private static final int COLOR_WALL = Color.BLACK;
    private static final int COLOR_PATH = Color.WHITE;
    private static final int COLOR_EXIT = Color.GREEN;
    private SnakeGame mSnakeGame;
    private Snake mSnake;
    private Point exitPosition;
    private boolean mazeInitialized;
    protected MazeGame mazeGame;
    protected Maze maze;

    public Maze(Context context, int blockSize) {
        this.blockSize = blockSize;
        mazeInitialized = false;

        initializePaints(); // Initialize paints for different cell types
        calculateMazeSize(context); // Calculate maze size based screen size
        initializeMazeLayoutIfNeeded();
    }

    private void initializePaints() {
        wallPaint = new Paint();
        wallPaint.setColor(COLOR_PATH);
        pathPaint = new Paint();
        pathPaint.setColor(COLOR_WALL);
        exitPaint = new Paint();
        exitPaint.setColor(COLOR_EXIT);
    }

    private void calculateMazeSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Calculate the maximum number of blocks that can fit in both dimensions
        mazeWidth = size.x / blockSize;
        mazeHeight = size.y / blockSize;

        // Adjust mazeWidth and mazeHeight to ensure the entire screen is covered
        if (size.x % blockSize != 0) {
            mazeWidth++;
        }
        if (size.y % blockSize != 0) {
            mazeHeight++;
        }
    }


    void initializeMazeLayoutIfNeeded() {
        if (mazeLayout == null && mSnakeGame != null && mSnakeGame.mazeGameActive) {
            initializeMazeLayout();
            mazeInitialized = true;
            Log.d("Maze", "Maze layout initialized.");
        } else {
            Log.e("Maze", "Maze layout is not initialized or game is not active.");
        }
    }

    private void initializeMazeLayout() {
        mazeLayout = new int[mazeHeight][mazeWidth];

        // Initialize the maze with all walls
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                mazeLayout[y][x] = WALL;
            }
        }

        Random random = new Random();
        int startX = 1;
        int startY = 1;

        // Initialize the stack for backtracking
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(startX, startY));

        while (!stack.isEmpty()) {
            Point current = stack.peek();
            int currentX = current.x;
            int currentY = current.y;

            mazeLayout[currentY][currentX] = PATH;

            // Shuffle the directions to randomize the path generation
            Collections.shuffle(Arrays.asList(DIRECTIONS), random);

            boolean hasValidNeighbor = false;

            for (int[] dir : DIRECTIONS) {
                //int nextX = currentX + dir[0] * 2;
                //int nextY = currentY + dir[1] * 2;
                int nextX = currentX + dir[0] * 4; // Move 4 blocks at a time for a wider path
                int nextY = currentY + dir[1] * 4; // Move 4 blocks at a time for a wider path

                if (isValidCell(nextX, nextY)) {
                    //ONE BLOCK PATH
                    mazeLayout[currentY + dir[1]][currentX + dir[0]] = PATH; // Break the wall
                    stack.push(new Point(nextX, nextY));
                    hasValidNeighbor = true;
                    break;
                }
            }

            if (!hasValidNeighbor) {
                stack.pop(); // Backtrack if no valid neighbor
            }
        }
        exitPosition = generateExitPosition();
        setMazeLayout(mazeLayout);

    }
    private boolean isValidCell(int x, int y) {
        return x > 0 && x < mazeWidth && y > 0 && y < mazeHeight && mazeLayout[y][x] == WALL;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Ensure that mazeGame is not null before using it
        if (mazeGame != null) {
            if (!mazeInitialized) {
                initializeMazeLayoutIfNeeded();
            }

            layout = getMazeLayout();
            System.out.println(mazeLayout);
            System.out.println(layout);

            if (mazeLayout != null) {
                // Use the layout to draw the maze
                for (int y = 0; y < mazeLayout.length && y < mazeHeight; y++) {
                    for (int x = 0; x < mazeLayout[y].length && x < mazeWidth; x++) {
                        int cellValue = mazeLayout[y][x];

                        int left = x * blockSize;
                        int top = y * blockSize;
                        int right = left + blockSize;
                        int bottom = top + blockSize;

                        switch (cellValue) {
                            case PATH: // Path
                                canvas.drawRect(left, top, right, bottom, pathPaint);
                                break;
                            case WALL: // Wall
                                canvas.drawRect(left, top, right, bottom, wallPaint);
                                break;
                            case EXIT: // Exit
                                canvas.drawRect(left, top, right, bottom, exitPaint);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    public boolean checkSnakeReachedExit(int snakeX, int snakeY){
        Point exitPosition = this.exitPosition;

        System.out.println("Snake Position: (" + snakeX + ", " + snakeY + ")");
        System.out.println("Exit Position: (" + exitPosition.x + ", " + exitPosition.y + ")");
        if(exitPosition != null && exitPosition.x == snakeX && exitPosition.y == snakeY){
            if(mSnakeGame != null){
                handleSnakeExit();
                System.out.println("Snake reached the maze exit!");
            }
            return snakeX == exitPosition.x && snakeY == exitPosition.y;
        } else {
            return false;
        }
    }
    public void handleSnakeExit() {
        if (mSnakeGame.mazeGameActive) {
            mSnakeGame.mazeGameActive = false; // Set mazeGameActive to false
            mSnakeGame.mScore += 7; // Increase the score or perform any other action
            mSnake.getSnake();
            mSnake.restoreSnakeState(mSnakeGame.NUM_BLOCKS_WIDE, mSnakeGame.mNumBlocksHigh);
            // Perform any other actions needed when the snake reaches the exit
        }
    }

    private Point generateExitPosition(){
        if(exitPosition == null) {
            Random random = new Random();
            int exitX = random.nextInt(mazeWidth - 2) + 1; // Exclude borders
            int exitY = random.nextInt(mazeHeight - 2) + 1; // Exclude borders
            mazeLayout[exitY][exitX] = EXIT;
            return new Point(exitX, exitY); // Return and set the exit position
        } else{
            return exitPosition;
        }

    }
    public boolean detectWallCollision(int x, int y) {
        maze.setMaze(maze);
        mazeLayout = getMazeLayout();

        if (mazeLayout != null && x >= 0 && x < mazeWidth && y >= 0 && y < mazeHeight) {
            Log.d("Maze", "Cell value at (" + x + ", " + y + "): " + mazeLayout[y][x]);
            return mazeLayout[y][x] == PATH;
            // return mazeLayout[y][x] != PATH;
            // return mazeLayout[y][x] == PATH;
        }
        return false;
    }


    public int[][] getMazeLayout(){
        return layout;
    }

    // Setter for mazeLayout
    public void setMazeLayout(int[][] mazeLayout) {
        this.mazeLayout = mazeLayout;
        this.layout = mazeLayout;
    }
    // Setter method for mSnake & MazeGame
    public void setMazeGame(MazeGame mazeGame){
        this.mazeGame = mazeGame;
    }
    public void setMSnake(Snake snake) {
        mSnake = snake;
    }
    public void setSnakeGame(SnakeGame snakeGame){
        this.mSnakeGame = snakeGame;
    }
    public void setMaze(Maze maze){
        this.maze = maze;
    }
    public Maze getMaze(){
        return maze;
    }


}
