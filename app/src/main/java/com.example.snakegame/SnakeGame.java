package com.example.snakegame;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import com.example.snakegame.Leaderboard;

// Importing multiple classes from the same package
import com.example.snakegame.ControlButton;
import com.example.snakegame.ArrowButtons;



import java.io.IOException;
import java.util.Random;

class SnakeGame extends SurfaceView implements Runnable, ControlListener {
    private Thread mThread = null; // Thread to run the game
    private long mNextFrameTime; // Time of the next frame
    private volatile boolean mPlaying = false; // Whether the game is playing
    private volatile boolean mPaused = true; // Whether the game is paused
    private volatile boolean usrPause = false; // Whether the user paused the game
    private SoundPool mSP; // SoundPool to play sounds
    private int mEat_ID = -1, mCrashID = -1; // Sound IDs
    protected final int NUM_BLOCKS_WIDE = 40; // Number of blocks wide
    protected final int mNumBlocksHigh;
    protected int mScore; // Number of blocks high and the score
    private Canvas mCanvas; // Canvas to draw on
    private final SurfaceHolder mSurfaceHolder; // SurfaceHolder to hold the canvas
    private final Paint mPaint; // Paint to draw with
    private final Snake mSnake; // Snake object
    private final Apple mApple; // Apple object
    static PauseButton pause;
    private final BadApple mBadApple;
    private final Background background;
    private final Paint mCustomTextPaint; // Paint for custom font text
    private final TextPrint pauseText;
    private final TextPrint author1;
    private final TextPrint author2;
    private TextPrint score;
    private final GameOver gameOver;
    private boolean gameOverFlag = false;
    private int blockSize;
    private Bitmap dirtBlockBitmap;
    private List<Point> dirtBlocks = new ArrayList<>();
    private int gameMode = 0;
    private int halfwayPoint;
    private Leaderboard leaderboard;
    private final TouchControlManager touchManager;
    private boolean displayedFlag;
    static ArrowButtons arrowButtons;
    List<PowerUp> powerUps;
    private int scoreMultiplier = 1;
    private int scoreMultiplierCounter = 0;

    private TitleScreen titleScreen;
    private PauseScreen pauseScreen;
    private SettingScreen settingScreen;
    boolean settingTurnedOff = false;
    boolean titleTurnedOff = false;
    private SettingsButton settingsButton;
    private MazeGame mazeGame;
    Maze maze;
    boolean mazeGameActive = false;
    private volatile int[][] mazeLayout;
    private int level = 0;

    // Constructor: Called when the SnakeGame class is first created
    public SnakeGame(Context context, Point size) {
        super(context);
        blockSize = size.x / NUM_BLOCKS_WIDE; // Size of a block
        mNumBlocksHigh = size.y / blockSize; // Number of blocks high

        // Initialize the SoundPool and load the sounds
        initializeSoundPool(context);
        loadSounds(context);

        leaderboard = new Leaderboard(); // initialize leaderboard

        // Initialize custom text Paint
        mCustomTextPaint = new Paint();
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "PixelOperator.ttf");
        mCustomTextPaint.setTypeface(customFont);
        mCustomTextPaint.setColor(Color.BLACK); // Set text color
        mCustomTextPaint.setTextSize(50); // Set text size

        // Initialize the SurfaceHolder and Paint
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        // Initialize all static objects to be drawn
        background = new Background(context);
        titleScreen = new TitleScreen(context, background.getWidth(), background.getHeight(), mPaint);
        pauseScreen = new PauseScreen(context, background.getWidth(), background.getHeight(), mPaint);
        settingScreen = new SettingScreen(context, background.getWidth(), background.getHeight(), mPaint);
        pause = new PauseButton(context);
        arrowButtons = new ArrowButtons(context);
        pauseText = new TextPrint(context, "Tap To Play!", 250, 200, 700, Color.BLACK);
        score = new TextPrint(context, "0", 120, 20, 120, Color.WHITE);
        author1 = new TextPrint(context, "Kevin Cendana", 50, 1690 , 50, Color.BLACK);
        author2 = new TextPrint(context, "Anthony Vitro", 50, 1690, 110, Color.BLACK);

        halfwayPoint = background.getWidth() / 2;

        // Create the Snake and Apple objects
        mApple = new Apple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = new Snake(context, this, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = new Snake(context, this, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        touchManager = new TouchControlManager(this);
        mBadApple = new BadApple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mBadApple.setGame(this);

        dirtBlockBitmap = loadAndScaleResource(context, R.drawable.dirtblock, blockSize, blockSize);

        //Initialize gameOver
        Paint paint = new Paint();
        gameOver = new GameOver(context, size.x, size.y, paint);
        settingsButton = new SettingsButton(context);

        //Initialize PowerUps
        powerUps = new ArrayList<>();
        initializePowerUps(context);
        Log.d("print-log", "PowerUps: " + powerUps);

        //Initialize Maze + SnakeGame
        maze = new Maze(getContext(), blockSize);
        maze.setSnakeGame(this);
        maze.setMSnake(mSnake);
        mSnake.setSnake(mSnake);
    }

    // Function: Initialize the SoundPool
    private void initializeSoundPool(Context context) {
        // If the device is running Android 5.0 or higher..
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Create a new AudioAttributes object
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            // Create a new SoundPool object with the AudioAttributes
            mSP = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        // Else, if the device is running Android 4.4 or lower..
        } else {
            // Create a new SoundPool object
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
    }

    // Function: Initialize Power Ups by adding them to the powerUps array
    private void initializePowerUps(Context context) {
        PowerUpScoreDoubler scoreDoubler = new PowerUpScoreDoubler(this, context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        powerUps.add(scoreDoubler);

        // Loop through all PowerUps and print their names
        for (PowerUp powerUp : powerUps) {
            Log.d("print-log", "PowerUp Name: " + powerUp.getName());
        }
    }

    // Function: Load and scale a resource
    private Bitmap loadAndScaleResource(Context context, int resourceId, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    // Function: Load the sounds
    private void loadSounds(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            mEat_ID = mSP.load(assetManager.openFd("get_apple.ogg"), 0);
            mCrashID = mSP.load(assetManager.openFd("snake_death.ogg"), 0);
        } catch (IOException e) {
            // Error handling
        }
    }

    // Function: Start a new game
    public void newGame() {
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh); // Reset the snake
        mApple.spawn(); // Spawn the apple
        mBadApple.setGame(this);
        clearBadApple();
        mScore = 0; // Reset the score
        level = 0;
        mNextFrameTime = System.currentTimeMillis(); // Reset the frame time
        dirtBlocks.clear(); //Reset the list of dirt blocks
        scoreMultiplier = 1; // Reset the score multiplier
        scoreMultiplierCounter = 0; // Reset the score multiplier counter
        displayedFlag = false;
    }

    // Function: Run the game
    @Override
    public void run() {
        // While the game is playing..
        while (mPlaying) {
            // If the game is not paused and an update is required, update the game
            if (!mPaused && updateRequired()) {
                mBadApple.update();
                update();
            }
            draw();
        }
    }

    // Function: Check if an update is required
    public boolean updateRequired() {
        final long TARGET_FPS = 10; // Target frames per second
        final long MILLIS_PER_SECOND = 1000; // Milliseconds per second

        // If the current time is greater than the next frame time, return true; update required
        if (mNextFrameTime <= System.currentTimeMillis()) {
            mNextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / TARGET_FPS;
            return true;
        }
        return false;
    }

    // Function: Update the game
    public void update() {
        if (mazeGameActive) {
            // Update logic for the mazeGame
            startMazeMiniGame();

            mNextFrameTime = System.currentTimeMillis() + 250;

            mSnake.move();
            Point snakeHeadPosition = mSnake.getHeadPosition();
            int snakeX = snakeHeadPosition.x;
            int snakeY = snakeHeadPosition.y;

            maze.setSnakeGame(this);
            maze.setMaze(maze);
            maze.setMSnake(mSnake);
            boolean wallCollision = maze.detectWallCollision(snakeX, snakeY);
            if (wallCollision) {
                // Set mazeGameActive to false to end the maze game
                mazeGameActive = false;
                mSnake.restoreSnakeState(NUM_BLOCKS_WIDE, mNumBlocksHigh);
                mNextFrameTime = System.currentTimeMillis() + 500; // A delay (0.5 seconds)
                return;
            }

            // Check if the snake has collided with the border of the maze
            if (snakeX < 0 || snakeX >= NUM_BLOCKS_WIDE || snakeY < 0 || snakeY >= mNumBlocksHigh) {
                // Set mazeGameActive to false to end the maze game
                mazeGameActive = false;
                mSnake.restoreSnakeState(NUM_BLOCKS_WIDE, mNumBlocksHigh);
                mNextFrameTime = System.currentTimeMillis() + 500; // A delay (0.5 seconds)
                return;
            }
            // Ensure maze layout is initialized before checking if snake reached exit
            if(mazeLayout == null) {
                maze.initializeMazeLayoutIfNeeded();
            }

            // Check if the snake has reached the exit
            boolean reachedExit = maze.checkSnakeReachedExit(snakeX, snakeY);
            System.out.println("Reached Exit: " + reachedExit);

            if (reachedExit) {
                // Set mazeGameActive to false to end the maze game
                mScore += 10; // Increase score for reaching maze exit
                mazeGameActive = false;
                mSnake.restoreSnakeState(NUM_BLOCKS_WIDE, mNumBlocksHigh);
                mNextFrameTime = System.currentTimeMillis() + 500; // Delay before resuming game
                return;
            }
        } else {
            mSnake.move();
            // If the snake eats an apple..
            if (mSnake.checkCollide(mApple)) {
                mApple.spawn(); // Spawn a new apple
                mScore += scoreMultiplier;       // Increase the score
                mSP.play(mEat_ID, 1, 1, 0, 0, 1); // Play the eat sound
                removeDirtBlocksForExplodedBadApple();  // Remove dirt blocks associated with exploded bad apple

                // Decrease the score multiplier counter if it is greater than 0
                if (scoreMultiplierCounter > 0)
                    scoreMultiplierCounter--;
                // If the score multiplier counter is 0, reset the score multiplier to 1
                if (scoreMultiplierCounter <= 0)
                    scoreMultiplier = 1;
            mSnake.checkColor();       // Change snake color (based on score multiplier)
            
                // Spawn a random PowerUp in the array
                if (!powerUps.isEmpty()) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(powerUps.size());
                    powerUps.get(randomIndex).spawn();
                }
                // Check if the score or level has reached the threshold for Maze mini-game
                if (level > 0 && level % 4 == 0) {
                    System.out.println("Starting Maze Mini-game...");
                    mazeGameActive = true;
                }
                level++;
                // Remove dirt blocks when snake eats an apple
                if (mScore > 0) {
                    if (!dirtBlocks.isEmpty()) {
                        dirtBlocks.clear();
                    }
                }
                if (mScore >= 5 && !mBadApple.isSpawned()) {
                    mBadApple.spawn();
                }
            }
            if (mSnake.detectDeath()) {
                mSP.play(mCrashID, 1, 1, 0, 0, 1);
                mPaused = true;
                usrPause = false;
                gameOverFlag = true;
            }
            if (mSnake.checkCollide(mBadApple)) {
                mSP.play(mCrashID, 1, 1, 0, 0, 1);
                mPaused = true;
                usrPause = false;
                gameOverFlag = true;
            }

            // Check for collision with Power Ups
            for (Iterator<PowerUp> iterator = powerUps.iterator(); iterator.hasNext(); ) {
                PowerUp powerUp = iterator.next();
                if (mSnake.checkCollide(powerUp)) {
                    powerUp.setVisible(false); // Hide the power up
                    powerUp.activate();        // Activate the power up
                    mSnake.checkColor();       // Check and change the snake color if needed
            }
            }
            if (checkSnakeDirtBlockCollision()) {
                mSP.play(mCrashID, 1, 1, 0, 0, 1);
                mPaused = true;
                usrPause = false;
                gameOverFlag = true;
            }
            if (gameOverFlag) {
                Player currentPlayer = new Player("Current Player", mScore);
                leaderboard.addPlayer(currentPlayer);
                displayedFlag = true;
    
            leaderboard.isShown(displayedFlag);
                showLeaderboard(); // Display the leaderboard
                level = 0;
            }
        }
    }

    // Function: Draw the game
    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Always draw the background
            background.draw(mCanvas, mPaint);

            // Draw the Maze mini-game if active
            if (mazeGame != null && mazeGameActive) {
                synchronized (mazeGame) {
                    if (mazeGame.getMaze() == null) {
                        mazeGame.setMaze(new Maze(getContext(), blockSize));
                        mazeGame.startMaze();
                    }
                    mazeGame.draw(mCanvas, mPaint);
                    drawPauseBackground(); // Draw the colored background for pause button if mazeGame is active
                    pause.draw(mCanvas, mPaint);
                    mSnake.draw(mCanvas, mPaint);
                    if (settingScreen.getCurrentControl() == 0) {
                        arrowButtons.draw(mCanvas, mPaint);
                    }
                    // Handle paused state
                    if (pause.isPaused() && !titleScreen.isShowing()) {
                        pauseScreen.draw(mCanvas, mPaint);
                    }
                    if (titleScreen.isShowing()) {
                        titleScreen.draw(mCanvas, mPaint);
                    }
                    if(settingScreen.isShowing()) {
                        settingScreen.draw(mCanvas, mPaint);
                    }
                }
            }
            else if(settingScreen.isShowing()) {
                settingScreen.draw(mCanvas, mPaint);
            }
            else if (titleScreen.isShowing()) {
                titleScreen.draw(mCanvas, mPaint);
            } else {
                // Draw game elements only when the title screen is not showing
                mApple.draw(mCanvas, mPaint);
                mSnake.draw(mCanvas, mPaint);
                mBadApple.draw(mCanvas, mPaint);
                score.setString(String.valueOf(mScore));
                score.draw(mCanvas, mPaint);
                pause.draw(mCanvas, mPaint);

                // Draw control buttons based on the current control setting
                if (!mPaused && settingScreen.getCurrentControl() == 0) {
                    arrowButtons.draw(mCanvas, mPaint);
                }

                // Draw power-ups
                for (PowerUp powerUp : powerUps) {
                    powerUp.draw(mCanvas, mPaint);
                }

                // Draw dirt blocks
                for (Point dirtBlock : dirtBlocks) {
                    mCanvas.drawBitmap(dirtBlockBitmap, dirtBlock.x * blockSize, dirtBlock.y * blockSize, mPaint);
                }

                // Handle paused state
                if (pause.isPaused() && !titleScreen.isShowing()) {
                    pauseScreen.draw(mCanvas, mPaint);
                }

                // Handle game over state
                if (gameOverFlag) {
                    gameOver.draw(mCanvas, mPaint);
                }
            }
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public void onDirectionChanged(Snake.Heading direction) {
        mSnake.switchHeading(direction);
    }
    @Override
    public void rotate(boolean trig) {
        if(trig) {
            mSnake.switchHeading(mSnake.getLeft());
        }
        else {
            mSnake.switchHeading(mSnake.getRight());
        }
    }
    @Override
    public void setPause(boolean input) {
        pause.setPauseStatus(input);
    }

    // Function: Handle touch events
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();
        // If the user touches the screen..
        // Start a new game if game is paused and gameOverFlag is true
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (mPaused && gameOverFlag && gameOver.isReplayButtonTouched(touchX, touchY)) {
                mPaused = false;
                usrPause = false;
                newGame();
                mNextFrameTime = System.currentTimeMillis();
                gameOverFlag = false; // Reset gameOverFlag
                return true;
            }
            // If the game is paused and gameOverFlag is true, show the settings screen
            if(mPaused && gameOverFlag && gameOver.isExitButtonTouched(touchX, touchY)){
                mPaused = true;
                usrPause = true;
                titleScreen.setShowing(true);
                gameOverFlag = false;
                return true;
            }
            if(mPaused && gameOverFlag && gameOver.isLeaderBoardButtonTouched(touchX, touchY)){
                leaderboard.isShown(true);
                leaderboard.display();
                showLeaderboard();
            }

            if (!settingScreen.isShowing() && (pauseScreen.settingsIsTouched(touchX, touchY) || titleScreen.settingsIsTouched(touchX, touchY)) && !gameOverFlag) {
                settingScreen.setShowing(true);
                pause.setPauseStatus(false);
                return true;
            }
            if (mPaused && titleScreen.isShowing() && titleScreen.startIsTouched(touchX, touchY)) {
                titleScreen.setShowing(false);
                pause.setPauseStatus(false);
                titleTurnedOff = true;
                return true;
            }
            if(mPaused && titleScreen.isShowing() && titleScreen.leaderboardIsTouched(touchX, touchY)){
                leaderboard.isShown(true);
                leaderboard.display();
                showLeaderboard();
            }
            // Hide settings if it is showing and back button is touched
            else if (settingScreen.isShowing() && settingScreen.backIsTouched(touchX, touchY)) {
                settingScreen.setShowing(false);
                pause.setPauseStatus(true);
                settingTurnedOff = true;
                return true;
            }
            else if (settingScreen.isShowing() && settingScreen.controlChange(touchX, touchY)) {
                mPaused = true;
                //pause.setPauseStatus(true);
                return true;
            }
            else if (pause.isPaused() && pauseScreen.quitIsTouched(touchX, touchY)) {
                pause.setPauseStatus(false);
                titleScreen.setShowing(true);
                return true;
            }
        }
        // If the game is paused and gameOverFlag is true, do nothing
        if (mPaused && gameOverFlag) {
            return true;
        }
        if(mPaused && titleScreen.isShowing()) {
            return true;
        }
        if(settingScreen.isShowing()) {
            return true;
        }
        // If the user did not pause the game..

        if (pause.isPaused()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && !settingTurnedOff) {
                if (!SnakeGame.pause.isTouched(touchX, touchY) && !titleScreen.settingsIsTouched(touchX, touchY) && !titleTurnedOff) {
                    pause.setPauseStatus(false);
                    mPaused = false;
                    mNextFrameTime = System.currentTimeMillis();
                }
                return true;
            }
            settingTurnedOff = false;
            titleTurnedOff = false;
        }
        if (!pause.isPaused()) {
            // If the game is paused, start a new game
            if (mPaused) {
                mPaused = false;
                newGame();
                return touchManager.handleTouchInput(motionEvent);
            }
            else {
                return handleActiveGameInput(motionEvent, settingScreen.getCurrentControl());
            }
        }
        return true;
    }

    // Function: Pause the game
    public void pause() {
        mPlaying = false; // Stop the game

        // Try to stop the thread
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error handling
        }
    }

    // Function: Resume the game
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }

    public void clearBadApple(){
        mBadApple.clear();
    }

    // Function: Spawn dirt blocks at close proxity of exploded bad apple
    public void spawnDirtBlocks(Point explosionLocation, int explosionSize){
        // Adjust the spawn location based on explosion location & size
        int startX = Math.max(explosionLocation.x - explosionSize, 0);
        int startY = Math.max(explosionLocation.y - explosionSize, 0);
        int endX = Math.min(explosionLocation.x + explosionSize + 1, NUM_BLOCKS_WIDE);
        int endY = Math.min(explosionLocation.y + explosionSize + 1, mNumBlocksHigh);

        for(int x = startX; x < endX; x++){
            for(int y = startY; y < endY; y++){
                dirtBlocks.add(new Point(x, y));
            }
        }

    }
    public void removeDirtBlock(Point location) {
        if (dirtBlocks.contains(location)) {
            dirtBlocks.remove(location);
        }
    }

    private void removeDirtBlocksForExplodedBadApple() {
        // Iterate through the dirt blocks and remove those associated with the exploded bad apple
        List<Point> blocksToRemove = new ArrayList<>();
        for (Point dirtBlock : dirtBlocks) {
            if (isDirtBlockAssociatedWithExplodedBadApple(dirtBlock)) {
                blocksToRemove.add(dirtBlock);
                // Call the removeDirtBlock method to remove the dirt block
                removeDirtBlock(dirtBlock);
            }
        }
        dirtBlocks.removeAll(blocksToRemove);
    }
    private boolean isDirtBlockAssociatedWithExplodedBadApple(Point dirtBlock) {
        return mBadApple.isSpawned() && mBadApple.isDirtBlockWithinExplosionRadius(dirtBlock);
    }

    // Function to check for collisions between the snake and dirt blocks
    private boolean checkSnakeDirtBlockCollision() {
        for (Point dirtBlock : dirtBlocks) {
            if (mSnake.checkCollide(dirtBlock)) {
                return true;
            }
        }
        return false;
    }
    private void showLeaderboard() {
        ((Activity) getContext()).runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View dialogView = inflater.inflate(R.layout.dialog_leaderboard, null);
            builder.setView(dialogView);

            ListView listView = dialogView.findViewById(R.id.leaderboard_list);
            List<String> playerScores = new ArrayList<>();
            for (Player player : leaderboard.getPlayers()) {
                playerScores.add(player.toString());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, playerScores);
            listView.setAdapter(adapter);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private boolean handleActiveGameInput(MotionEvent motionEvent, int mode) {
        // Handle swipe, touch, or arrow controls depending on the mode
        if (mode == 2) {
            touchManager.handleSwipeEvent(motionEvent);
            mPaused = pause.isPaused();
        } else if (mode == 1) {
            touchManager.handleTouchControl(motionEvent, halfwayPoint);
            mPaused = pause.isPaused();
        } else if (mode == 0) {
            touchManager.handleArrowControl(motionEvent);
            mPaused = pause.isPaused();
        }
        return true;
    }
    private void startMazeMiniGame(){
        if(mazeGame == null){
            try{
                // Initialize the mazeGame object if it's null
                mazeGame = MazeGame.getInstance(getContext(), blockSize);
                mazeGame.startMaze();
                mazeGame.setSnakeGame(this);
                mazeGame.setMazeGame(maze);
            } catch(Exception e){
                e.printStackTrace();
            }
        } else{
            // if mazeGame is already initialized, make sure it's started
            mazeGame.startMaze();
        }
    }

    // Function: Draw background square for pause button
    private void drawPauseBackground() {
        if (mazeGame != null) { // Check if mazeGame is not null
            int padding = 10; // Padding around the pause button

            // Coordinates of the pause button
            int left = 10;
            int top = 890;
            int right = 110;
            int bottom = 990;

            // Adjust the background rectangle's coordinates to be at the bottom-left corner of the pause button
            int adjustedTop = bottom - (bottom - top);
            int adjustedBottom = bottom + padding;

            // Set your desired background color
            mPaint.setColor(Color.rgb(173, 216, 230));
            mCanvas.drawRect(left - padding, adjustedTop - padding, right + padding, adjustedBottom + padding, mPaint);
        }
    }


    // Getters and Setters
    public void setScoreMultiplier(int scoreMultiplier) { this.scoreMultiplier = scoreMultiplier; }
    public int getScoreMultiplier() { return scoreMultiplier;}
    public void setScoreMultiplierCounter(int scoreMultiplierCounter) { this.scoreMultiplierCounter = scoreMultiplierCounter; }
    public int getScoreMultiplierCounter() { return scoreMultiplierCounter; }
}
