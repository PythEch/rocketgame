package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by alpino-64 on 21.04.2016.
 */
public class VisualMeteor extends GameObject {
    private Vector2 location;
    private Vector2 speed;
    private int rotateDegree;

    public VisualMeteor(int locationX, int locationY, int speedX, int speedY, int rotateDegree) {
        this.location = new Vector2(locationX, locationY);
        this.speed = new Vector2(speedX, speedY);
        this.rotateDegree = rotateDegree;
    }

    @Override
    public void update(float deltaTime) {
        Vector2 mySpeed = speed.cpy();
        mySpeed.x *= deltaTime * 10;
        mySpeed.y *= deltaTime * 10;
        location = location.add(mySpeed);
    }

    public Vector2 getLocation() {
        return location;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public int getRotateDegree() {
        return rotateDegree;
    }
}
