package com.rocketfool.rocketgame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.rocketfool.rocketgame.model.entity.Playable;

import static com.rocketfool.rocketgame.util.Constants.DEBUG;

/**
 * Created by pythech on 02/04/16.
 */
public class WorldController {
    private Playable playable;

    public WorldController(Playable playable) {
        this.playable = playable;
    }

    public void update(float deltaTime) {
        updatePlayable(deltaTime);
    }

    private void updatePlayable(float deltaTime) {
        Body body = playable.getBody();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.applyAngularImpulse(playable.getRotateImpulse() * deltaTime, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.applyAngularImpulse(-playable.getRotateImpulse() * deltaTime, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playable.setCurrentImpulse( Math.max(0, playable.getCurrentImpulse() * (1 + deltaTime)) );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playable.setCurrentImpulse( Math.max(0,  playable.getCurrentImpulse() * (1 - deltaTime)) );
        }

        if (DEBUG) {
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                playable.setCurrentImpulse(0);
                body.setAngularVelocity(0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                body.setLinearVelocity(0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                body.setTransform(0, 0, 0);
            }
        }
    }
}
