package com.rocketfool.rocketgame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.rocketfool.rocketgame.model.Playable;
import com.rocketfool.rocketgame.model.Level;
import com.rocketfool.rocketgame.view.GameScreen;
import com.rocketfool.rocketgame.view.WorldRenderer;

import static com.rocketfool.rocketgame.util.Constants.DEBUG;

/**
 * Handles user input and signals view {@link GameScreen} and model {@link Level} accordingly
 */
public class WorldController {
    //region Fields
    private Level level;
    private GameScreen screen;
    private WorldRenderer renderer;
    //endregion

    //region Constructor
    public WorldController(Level level, GameScreen screen, WorldRenderer renderer) {
        this.level = level;
        this.screen = screen;
        this.renderer = renderer;
    }
    //endregion

    //region Methods
    public void update(float deltaTime) {
        Playable playable = level.getPlayable();
        Body body = playable.getBody();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playable.turnLeft(deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playable.turnRight(deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (playable.getCurrentThrust() == 0) {
                renderer.playThrusterStarter();
                renderer.setThrustStopperActive( true);
            }
            playable.increaseThrust(deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (playable.getCurrentThrust() == 0) {
                renderer.stopThrusterGoinger();
                renderer.playThrusterEnder();
                renderer.setThrustStopperActive( false);
            }
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

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playable.runSAS(deltaTime);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && level.getState() == Level.State.RUNNING) {
            screen.showPauseScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            playable.toggleMinimizeThrust();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            playable.toggleMaximizeThrust();
        }

        if (DEBUG) {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                playable.setCurrentThrust(0);
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
    //endregion
}
