package com.rocketfool.rocketgame.model.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.rocketfool.rocketgame.util.Constants.DEBUG;
import static com.rocketfool.rocketgame.util.Constants.toMeter;
import static com.rocketfool.rocketgame.util.Constants.toPixel;

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

    public void left(float deltaTime) {
        body.applyAngularImpulse(rotateImpulse * deltaTime, true);
    }

    public void right(float deltaTime) {
        body.applyAngularImpulse(-rotateImpulse * deltaTime, true);
    }

    public void up(float deltaTime) {
        // FIXME: Use Math.min with some max speed
        currentImpulse = Math.max(0, currentImpulse + deltaTime * impulse);
    }

    public void down(float deltaTime) {
        currentImpulse = Math.max(0, currentImpulse - deltaTime * impulse);
    }
}
