package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by pythech on 02/04/16.
 */
public class Waypoint extends GameObject {
    private int x;
    private int y;
    private int radius;
    private boolean active;

    public Waypoint(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.active = true;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void update(float deltaTime) {

    }
}
