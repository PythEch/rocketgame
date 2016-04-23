package com.rocketfool.rocketgame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Body;
import com.rocketfool.rocketgame.model.Playable;
import com.rocketfool.rocketgame.model.Level;
import com.rocketfool.rocketgame.view.GameScreen;
import com.rocketfool.rocketgame.view.WorldRenderer;

import static com.rocketfool.rocketgame.util.Constants.DEBUG;

/**
 * Created by pythech on 02/04/16.
 */
public class WorldController {
    private Level level;
    private GameScreen screen;

    public WorldController(Level level, GameScreen screen) {
        this.level = level;
        this.screen = screen;
    }

    public void update(float deltaTime) {
        updatePlayable(deltaTime);
    }

    private void updatePlayable(float deltaTime) {
        Playable playable = level.getPlayable();
        Body body = playable.getBody();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playable.turnLeft(deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playable.turnRight(deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            playable.increaseThrust(deltaTime);
            screen.igniteRocketTrail();
        }
        else
        {
            screen.stopRocketTrail();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playable.decreaseThrust(deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            screen.zoomIn();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            screen.zoomOut();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
            playable.toggleSAS();
        }

        if ( !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) ) {
            playable.runSAS(deltaTime);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){

        }

        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            playable.minimizeThrust( deltaTime );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            playable.maximizeThrust( deltaTime );
        }

        if (DEBUG) {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                playable.setCurrentImpulse(0);
                body.setAngularVelocity(0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.G)) {
                body.setLinearVelocity(0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                body.setTransform(0, 0, 0);
            }
        }
    }
}
