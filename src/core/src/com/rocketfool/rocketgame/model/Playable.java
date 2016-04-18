package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;

import static com.rocketfool.rocketgame.util.Constants.toMeter;

/**
 * Created by pythech on 02/04/16.
 */
public class Playable extends SolidObject {
    //region Fields
    protected float currentImpulse;
    protected float rotateImpulse;
    protected float impulse;
    protected float fuel;
    protected float width;
    protected float height;
    //endregion


    public Playable(float width, float height) {
        this.width = width;
        this.height = height;
        this.currentImpulse = 0;
    }

    //region Methods
    @Override
    public void update(float dt) {
        float angle = body.getAngle();

        Vector2 bottomVector = new Vector2(0, -height / 2f * toMeter).rotateRad(angle);
        Vector2 bottomPosition = bottomVector.add(body.getPosition());

        Vector2 impulseVector = new Vector2(0, dt * currentImpulse).rotateRad(body.getAngle());

        body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, false);
    }
    //endregion

    //region Getters & Setters
    public float getCurrentImpulse() {
        return currentImpulse;
    }

    public float getRotateImpulse() {
        return rotateImpulse;
    }

    public float getImpulse() {
        return impulse;
    }

    public float getFuel() {
        return fuel;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setCurrentImpulse(float currentImpulse) {
        this.currentImpulse = currentImpulse;
    }
    //endregion

    public void turnLeft(float deltaTime) {
        body.applyAngularImpulse(rotateImpulse * deltaTime, true);
    }

    public void turnRight(float deltaTime) {
        body.applyAngularImpulse(-rotateImpulse * deltaTime, true);
    }

    public void increaseThrust(float deltaTime) {
        // FIXME: Use Math.min with some max speed
        currentImpulse = Math.max(0, currentImpulse + deltaTime * impulse);
    }

    public void decreaseThrust(float deltaTime) {
        currentImpulse = Math.max(0, currentImpulse - deltaTime * impulse);
    }

    //If SAS is enabled by the player (shortcut key?), this method is run to automatically reduce angular impulse
    //(didnt add the boolean yet)
    public void applySAS(float deltaTime) {
        body.applyAngularImpulse( -1 / 10 * rotateImpulse * deltaTime, true);
    }

}