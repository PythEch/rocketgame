package com.rocketfool.rocketgame.model;

/**
 * Non-physical bodies as visual indicators for game objectives.
 */
public class Waypoint extends GameObject {
    private float x;
    private float y;
    private float radius;
    private boolean onScreen;
    private PositionTrigger t;

    //constructors
    public Waypoint(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.onScreen = true;
        t = null;

    }

    public Waypoint(PositionTrigger t) {
        this.x = t.getX();
        this.y = t.getY();
        this.radius = t.getRadius();
        this.onScreen = true;
        this.t = t;
    }

    //methods
    public void followTrigger() {
        if (t != null) {
            this.x = t.getX();
            this.y = t.getY();
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
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
