package com.rocketfool.rocketgame.entity.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.entity.SolidObject;
import com.rocketfool.rocketgame.util.TextureManager;

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
    //endregion

    //region Methods
    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.applyAngularImpulse(rotateImpulse * dt, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.applyAngularImpulse(-rotateImpulse * dt, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currentImpulse += dt * impulse;
            currentImpulse = Math.max(currentImpulse, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentImpulse -= dt * impulse;
            currentImpulse = Math.max(currentImpulse, 0);
        }

        if (DEBUG) {
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                currentImpulse = 0;
                body.setAngularVelocity(0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                body.setLinearVelocity(0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                body.setTransform(0, 0, 0);
            }
        }

        move(dt);
    }

    private void move(float dt) {
        float angle = body.getAngle();

        Vector2 bottomVector = new Vector2(0, -texture.getHeight() / 2f * toMeter).rotateRad(angle);
        Vector2 bottomPosition = bottomVector.add(body.getPosition());

        Vector2 impulseVector = new Vector2(0, dt * currentImpulse).rotateRad(body.getAngle());

        body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, false);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                texture,
                this.getBody().getPosition().x * toPixel - texture.getWidth() / 2f,
                this.getBody().getPosition().y * toPixel - texture.getHeight() / 2f,
                texture.getWidth() / 2f,
                texture.getHeight() / 2f,
                texture.getWidth(),
                texture.getHeight(),
                1,
                1,
                body.getAngle() * MathUtils.radiansToDegrees,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false
        );
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
    //endregion
}
