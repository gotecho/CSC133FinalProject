package com.example.snakegame;

public interface ControlListener {
    public void onDirectionChanged(Snake.Heading direction);
    public void setPause(boolean stop);
    public void rotate(boolean trig);
}
