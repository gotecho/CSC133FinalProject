package com.example.snakegame;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
class SnakeGame extends SurfaceView implements Runnable {
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
    private final Bitmap pause;
    private final Bitmap background; // Bitmaps for the pause button and background

    // Constructor: Called when the SnakeGame class is first created
    public SnakeGame(Context context, Point size) {
        super(context);
        int blockSize = size.x / NUM_BLOCKS_WIDE; // Size of a block
        mNumBlocksHigh = size.y / blockSize; // Number of blocks high

        // Initialize the SoundPool and load the sounds
        initializeSoundPool(context);
        loadSounds(context);

        // Initialize the SurfaceHolder and Paint
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        // Load the bitmaps
        pause = loadAndScaleResource(context, R.drawable.pausebutton, 100, 100);
        background = loadAndScaleResource(context, R.drawable.flag, 2050, 1080);

        // Create the Snake and Apple objects
        mApple = new Apple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = new Snake(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
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

    // Function: Load and scale a resource
    private Bitmap loadAndScaleResource(Context context, int resourceId, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
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
        if (mSnake.checkDinner(mApple.getLocation())) {
            mApple.spawn();
            mScore++;
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }
        if (mSnake.detectDeath()) {
            mSP.play(mCrashID, 1, 1, 0, 0, 1);
            mPaused = true;
            usrPause = false;
        }
    }

    // Function: Draw the game
    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawBitmap(background, 0, 0, mPaint);
            mCanvas.drawBitmap(pause, 10, 980, mPaint);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(120);
            mCanvas.drawText(String.valueOf(mScore), 20, 120, mPaint);
            if (mPaused) {
                mPaint.setTextSize(250);
                mCanvas.drawText(getResources().getString(R.string.tap_to_play), 200, 700, mPaint);
            }
            mApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    // Function: Handle touch events
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // If the user touches the screen..
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            // If the user did not pause the game..
            if (!usrPause) {
                // If the game is paused, start a new game
                if (mPaused) {
                    mPaused = false;
                    newGame();
                    return true;
                }
                // If the user touches the pause button, pause the game
                if (motionEvent.getX() < 100 && motionEvent.getY() > 980) {
                    mPaused = true;
                    usrPause = true;
                }
                // Else, if the user touches the screen, switch the snake's heading
                else {
                    mSnake.switchHeading(motionEvent);
                }
            }
            // If the user paused the game, resume the game
            else {
                mPaused = false;
                usrPause = false;
                mNextFrameTime = System.currentTimeMillis();
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
