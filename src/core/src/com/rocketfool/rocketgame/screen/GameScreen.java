package com.rocketfool.rocketgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.entity.*;
import com.rocketfool.rocketgame.entity.game.EntityManager;
import com.rocketfool.rocketgame.entity.game.Map;
import com.rocketfool.rocketgame.entity.game.Planet;
import com.rocketfool.rocketgame.entity.game.Player;
import com.rocketfool.rocketgame.level.Level;
import com.rocketfool.rocketgame.level.Level1;

import java.text.DecimalFormat;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by pythech on 25/03/16.
 */
public class GameScreen implements Screen {
    //region Fields
    /** This is used to make OpenGL draw objects in one go, for performance reasons. */
    private SpriteBatch batch;
    /** Just like SpriteBatch, but is used to draw strings */
    private BitmapFont font;

    /** Orthographic cameras are used for 2D games, the other one Perspective camera is for 3D games. */
    private OrthographicCamera camera;
    /** Used for drawing the bounds of objects in the physics engine for easier debugging */
    private Box2DDebugRenderer debugRenderer;

    private Level level;

    private Player player;
    //endregion

    //region Constructor
    public GameScreen(SpriteBatch batch, BitmapFont font) {
        // Get these from the Game instance
        this.batch = batch;
        this.font = font;

        // these will be useful later on
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // construct the camera with given width and height
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        // setup debug renderer so we can see what's going on with the physics engine
        debugRenderer = new Box2DDebugRenderer();
        //endregion

        //endregion

        level = new Level1();
        player = level.getPlayer();
    }
    //endregion

    //region Methods
    /**
     * This method is called whenever rendering is needed (once a frame is drawn, i.e 60 fps = 60 calls).
     * We are diving business logic and drawing logic with methods
     * {@link #update(float)} and
     * {@link #draw()}.
     */
    @Override
    public void render(float dt) {
        // Our main method update comes before draw
        update(dt);

        // This cryptic line clears the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Our main draw method
        draw();

        batch.end();

        // Draw boundries of physics objects if debug is enabled
        if (DEBUG)
            debugRenderer.render(level.getWorld(), camera.combined.scl(PPM));
    }

    /**
     * This method is responsible for drawing objects related to our game.
     * Generally it will call bunch of methods of its object.
     */
    private void draw() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // Draw all entities
        level.draw(batch);

        // Draw a debug string which shows the velocity of the spaceship
        if (DEBUG) {
            drawDebugString("  Linear Impulse: " + (int)player.getCurrentImpulse(), 1);
            drawDebugString("Angular Velocity: " + (int)(player.getBody().getAngularVelocity() * 100), 2);
            drawDebugString("  Linear Velocity: " + (int)(player.getBody().getLinearVelocity().len() * 10), 3);
            drawDebugString("X: " + String.format("%.1f", player.getBody().getPosition().x) +
                           " Y: " + String.format("%.1f", player.getBody().getPosition().y), 4);
        }
    }

    private void drawDebugString(String str, int row) {
        font.draw(
                batch,
                str,
                camera.position.x - camera.viewportWidth / 2f,
                camera.position.y - camera.viewportHeight / 2f + font.getLineHeight() * row
        );
    }

    /**
     * Business logic of the game goes here such as physics, camera, UI, statistics etc.
     * @param dt Stands for DeltaTime which is the time passed between two sequential calls of update.
     */
    private void update(float dt) {
        // Update all entities
        level.update(dt);

        // Make the camera focus on the player
        camera.position.set(player.getBody().getPosition().x * toPixel, player.getBody().getPosition().y * toPixel, 0);
        camera.update();
    }

    /**
     * This is used to release (aka get rid of) objects we no longer need to hold in memory
     * when the Game class is used destroyed.
     * Used for performance reasons, so we generally don't need to care about this much.
     */
    @Override
    public void dispose() {
        debugRenderer.dispose();
    }
    //endregion

    //region Methods just to override, ignore...
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
    //endregion
}
