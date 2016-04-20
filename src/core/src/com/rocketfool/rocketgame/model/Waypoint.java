package com.rocketfool.rocketgame.model;

/**
 * Non-physical bodies as visual indicators for game objectives.
 */
public class Waypoint extends GameObject {
    private int x;
    private int y;
    private int radius;
    private boolean onScreen;

    public Waypoint(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.onScreen = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isOnScreen() {
        return onScreen;
    }

    public void setOnScreen(boolean onScreen) {
        this.onScreen = onScreen;
    }

    @Override
    public void update(float deltaTime) {

    }
}
