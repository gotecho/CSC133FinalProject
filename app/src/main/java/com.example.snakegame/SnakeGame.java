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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
class SnakeGame extends SurfaceView implements Runnable, ControlListener {
    private Thread mThread = null; // Thread to run the game
    private long mNextFrameTime; // Time of the next frame
    private volatile boolean mPlaying = false; // Whether the game is playing
    private volatile boolean mPaused = true; // Whether the game is paused
    private volatile boolean usrPause = false; // Whether the user paused the game
    private SoundPool mSP; // SoundPool to play sounds
    private int mEat_ID = -1, mCrashID = -1; // Sound IDs
    private final int NUM_BLOCKS_WIDE = 40; // Number of blocks wide
    private final int mNumBlocksHigh;
    private int mScore; // Number of blocks high and the score
    private Canvas mCanvas; // Canvas to draw on
    private final SurfaceHolder mSurfaceHolder; // SurfaceHolder to hold the canvas
    private final Paint mPaint; // Paint to draw with
    private final Snake mSnake; // Snake object
    private final Apple mApple; // Apple object
    static PauseButton pause;
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
    private List<Point> dirtBlocks = new ArrayList<Point>();
    private final int halfwayPoint;
    private final TouchControlManager touchManager;
    private final ControlButton controlButton;
    static ArrowButtons arrowButtons;


    // Constructor: Called when the SnakeGame class is first created
    public SnakeGame(Context context, Point size) {
        super(context);
        int blockSize = size.x / NUM_BLOCKS_WIDE; // Size of a block
        mNumBlocksHigh = size.y / blockSize; // Number of blocks high

        // Initialize the SoundPool and load the sounds
        initializeSoundPool(context);
        loadSounds(context);

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
        pause = new PauseButton(context);
        controlButton = new ControlButton(context, mCustomTextPaint);
        arrowButtons = new ArrowButtons(context);
        pauseText = new TextPrint(context, "Tap To Play!", 250, 200, 700, Color.BLACK);
        score = new TextPrint(context, "0", 120, 20, 120, Color.WHITE);
        author1 = new TextPrint(context, "Kevin Cendana", 50, 1690 , 50, Color.BLACK);
        author2 = new TextPrint(context, "Anthony Vitro", 50, 1690, 110, Color.BLACK);

        halfwayPoint = background.getWidth() / 2;

        // Create the Snake and Apple objects
        mApple = new Apple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = new Snake(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        touchManager = new TouchControlManager(this);

        //Initialize gameOver
        gameOver = new GameOver(context);
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
        mScore = 0; // Reset the score
        mNextFrameTime = System.currentTimeMillis(); // Reset the frame time
    }

    // Function: Run the game
    @Override
    public void run() {
        // While the game is playing..
        while (mPlaying) {
            // If the game is not paused and an update is required, update the game
            if (!mPaused && updateRequired()) {
                update();
            }
            // If the game is not paused, draw the game
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
        mSnake.move();
        if (mSnake.checkCollide(mApple)) {
            mApple.spawn();
            mScore++;
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }
        if (mSnake.detectDeath()) {
            mSP.play(mCrashID, 1, 1, 0, 0, 1);
            mPaused = true;
            usrPause = false;
            gameOverFlag = true;
        }
    }

    // Function: Draw the game
    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            background.draw(mCanvas, mPaint);
            pause.draw(mCanvas, mPaint);
            score.setString(String.valueOf(mScore)); // Update the object with current score
            score.draw(mCanvas, mPaint);

            mApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            if (mPaused) {
                controlButton.draw(mCanvas, mPaint);
                pauseText.draw(mCanvas, mCustomTextPaint);
                author1.draw(mCanvas, mCustomTextPaint);
                author2.draw(mCanvas, mCustomTextPaint);
            }
            else {
                if(controlButton.getCurrentControl() == 0) {
                    arrowButtons.draw(mCanvas, mPaint);
                }
            }

            if(gameOverFlag){
                gameOver.draw(mCanvas, mPaint);
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
        // If the user touches the screen..
            // Start a new game if game is paused and gameOverFlag is true
            int mode = controlButton.getCurrentControl();
            if (mPaused && gameOverFlag) {
                mPaused = false;
                usrPause = false;
                newGame();
                mNextFrameTime = System.currentTimeMillis();
                gameOverFlag = false; // Reset gameOverFlag
                return true;
            }
            // If the user did not pause the game..
            if (!pause.isPaused()) {
                // If the game is paused, start a new game
                if (mPaused) {
                    mPaused = false;
                    newGame();
                    return touchManager.handleTouchInput(motionEvent);
                }
                // Swipe controls
                if (mode == 2) {
                    touchManager.handleSwipeEvent(motionEvent);
                    mPaused = pause.isPaused();
                    return true;
                }
                // Touch controls
                else if (mode == 1) {
                    touchManager.handleTouchControl(motionEvent, halfwayPoint);
                    mPaused = pause.isPaused();
                    return true;
                }
                else if (mode == 0) {
                    touchManager.handleArrowControl(motionEvent);
                    mPaused = pause.isPaused();
                    return true;
                }
            }
            // If the user paused the game, resume the game
            else {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(!controlButton.isTouched((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        mPaused = false;
                        pause.setPauseStatus(false);
                        mNextFrameTime = System.currentTimeMillis();
                    }
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
}
