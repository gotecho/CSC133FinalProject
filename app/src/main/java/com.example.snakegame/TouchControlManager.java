package com.example.snakegame;

import android.view.MotionEvent;
public class TouchControlManager {
    private static final int SWIPE_THRESH = 100;
    private float downX, downY;
    private final ControlListener listener;
    private boolean pause = false;

    public TouchControlManager(ControlListener list) {
        listener = list;
    }

    public boolean handleTouchInput(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return true;
        }
        return true;
    }

    public void handleTouchControl(MotionEvent event, int halfWayPoint) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if(SnakeGame.pause.isTouched((int) event.getX(), (int) event.getY())) {
                listener.setPause(true);
            }
            else if (event.getX() < halfWayPoint) {
                listener.rotate(true);
            }
            else {
                listener.rotate(false);
            }
        }
        return true;
    }

    public void handleSwipeEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                if (SnakeGame.pause.isTouched((int) downX,(int) downY)) {
                    pause = true;

                }
                return true;
            case MotionEvent.ACTION_UP:
                float deltaX = downX - event.getX();
                float deltaY = downY - event.getY();
                if(SnakeGame.pause.isTouched((int) event.getX(), (int) event.getY())) {
                    listener.setPause(true);
                }
                else if(Math.abs(deltaX) >= SWIPE_THRESH || Math.abs(deltaY) >= SWIPE_THRESH) {
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (deltaX > 0) {
                            listener.onDirectionChanged(Snake.Heading.LEFT);
                        } else {
                            listener.onDirectionChanged(Snake.Heading.RIGHT);
                        }
                    } else {
                        if (deltaY > 0) {
                            listener.onDirectionChanged(Snake.Heading.UP);
                        } else {
                            listener.onDirectionChanged((Snake.Heading.DOWN));
                        }
                    }
                }
                return true;
        }
        return false;
    }

    public void handleArrowControl(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(SnakeGame.pause.isTouched((int) event.getX(), (int) event.getY())) {
                listener.setPause(true);
            }
            else if(SnakeGame.arrowButtons.isTouched((int) event.getX(), (int) event.getY())) {
                listener.onDirectionChanged(SnakeGame.arrowButtons.getDirection((int) event.getX(), (int) event.getY()));
            }
            return true;
        }
        return false;
    }
}
