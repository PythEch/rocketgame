package com.rocketfool.rocketgame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.rocketfool.rocketgame.model.ForceDiagram;
import com.rocketfool.rocketgame.model.Playable;
import com.rocketfool.rocketgame.model.Level;
import com.rocketfool.rocketgame.model.TrajectorySimulator;
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

    /** This int tracks which controls are disabled/enabled.
     *  Only pausing works at 1, and all controls are enabled at 7*/
    public static byte controlState = 7;
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

        if (controlState == -1 && Gdx.input.isKeyPressed(Input.Keys.A)) {
            screen.zoomIn();
        }
        if (controlState == -1 && Gdx.input.isKeyPressed(Input.Keys.S)) {
            screen.zoomOut();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && level.getState() == Level.State.RUNNING) {
            screen.showPauseScreen();
        }
        if (controlState >= 2 && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playable.turnLeft(deltaTime);
        }
        if (controlState >= 2 && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playable.turnRight(deltaTime);
        }
        if (controlState >= 3 && Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
            playable.toggleSAS();
        }
        if (controlState >= 3 && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playable.runSAS(deltaTime);
        }
        if (controlState >= 4 && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (playable.getCurrentThrust() == 0) {
                renderer.playThrusterStarter();
                renderer.setThrustStopperActive( true);
            }
            playable.increaseThrust(deltaTime);
        }
        if (controlState >= 4 && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (playable.getCurrentThrust() == 0) {
                renderer.stopThrusterGoinger();
                renderer.playThrusterEnder();
                renderer.setThrustStopperActive( false);
            }
            playable.decreaseThrust(deltaTime);
        }
        if (controlState >= 5 && Gdx.input.isKeyPressed(Input.Keys.A)) {
            screen.zoomIn();
        }
        if (controlState >= 5 && Gdx.input.isKeyPressed(Input.Keys.S)) {
            screen.zoomOut();
        }
        if (controlState >= 6 && Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            TrajectorySimulator.enabled = !TrajectorySimulator.enabled;
        }

        if (controlState >= 6 && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            ForceDiagram.setEnabled(!ForceDiagram.isEnabled());
        }

        if (controlState >= 7 && Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            playable.toggleMinimizeThrust();
        }

        if (controlState >= 7 && Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            playable.toggleMaximizeThrust();
            if (playable.getCurrentThrust() == 0) {
                renderer.playThrusterStarter();
                renderer.setThrustStopperActive(true);
            }
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
