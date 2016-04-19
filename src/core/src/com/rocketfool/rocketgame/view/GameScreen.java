package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.rocketfool.rocketgame.controller.WorldController;
import com.rocketfool.rocketgame.model.Map;
import com.rocketfool.rocketgame.model.Playable;
import com.rocketfool.rocketgame.model.Level;
import com.rocketfool.rocketgame.model.ExampleLevel;
import com.rocketfool.rocketgame.model.*;

import com.badlogic.gdx.video.VideoPlayer;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by pythech on 25/03/16.
 */
public class GameScreen implements Screen {
    //region Fields
    /**
     * This is used to make OpenGL draw objects in one go, for performance reasons.
     */
    private SpriteBatch batch;

    private VideoPlayer player;

    /**
     * Just like SpriteBatch, but is used to draw strings
     */
    private BitmapFont font;

    /**
     * Orthographic cameras are used for 2D games, the other one Perspective camera is for 3D games.
     */
    private OrthographicCamera camera;
    /**
     * Used for drawing the bounds of objects in the physics engine for easier debugging
     */
    private Box2DDebugRenderer debugRenderer;

    private Level level;

    private Playable cameraTarget;

    private Map map;

    private WorldRenderer renderer;

    private WorldController controller;

    private ParticleEffect particleEffect;


    //endregion

    //region Constructor
    public GameScreen(SpriteBatch batch, BitmapFont font) {
        // Get these from the Game instance
        this.batch = batch;
        this.font = font;
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
        controller.update(dt);
        update(dt);

        // This cryptic line clears the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);


        batch.begin();
        // Our main draw method
        renderer.draw(batch);
        particleEffect.draw(batch);
        particleEffect.update(dt);
        particleEffect.setPosition(level.getPlayable().getBottomPosition().x * toPixel, level.getPlayable().getBottomPosition().y * toPixel);
        float angle = level.getPlayable().getBody().getAngle() * MathUtils.radiansToDegrees + 270;
        for(int i = 0; i < particleEffect.getEmitters().size; i++)
        {
            particleEffect.getEmitters().get(i).getAngle().setHigh(angle, angle);
            particleEffect.getEmitters().get(i).getAngle().setLow(angle);
        }


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


        // Draw a debug string which shows the velocity of the spaceship
        if (DEBUG) {
            drawDebugString("  Linear Impulse: " + (int) cameraTarget.getCurrentImpulse(), 1);
            drawDebugString("Angular Velocity: " + (int) (cameraTarget.getBody().getAngularVelocity() * 100), 2);
            drawDebugString("  Linear Velocity: " + (int) (cameraTarget.getBody().getLinearVelocity().len() * 10), 3);
            drawDebugString("X: " + String.format("%.1f", cameraTarget.getBody().getPosition().x) +
                    " Y: " + String.format("%.1f", cameraTarget.getBody().getPosition().y), 4);
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
     *
     * @param dt Stands for DeltaTime which is the time passed between two sequential calls of update.
     */
    private void update(float dt) {
        // Update all entities
        level.update(dt);

        // Make the camera focus on the player
        camera.position.set(cameraTarget.getBody().getPosition().x * toPixel, cameraTarget.getBody().getPosition().y * toPixel, 0);
        camera.update();
    }

    public void lookAt(Playable target) {
        // TODO: make animation
        cameraTarget = target;
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
        // these will be useful later on
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // construct the camera with given width and height
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        // setup debug renderer so we can see what's going on with the physics engine
        debugRenderer = new Box2DDebugRenderer();
        //endregion

        //For Particles
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("effects/trail.p"), Gdx.files.internal("PNG"));



        //endregion
        level = new ExampleLevel();
        cameraTarget = level.getPlayable();
        renderer = new WorldRenderer(level);
        controller = new WorldController(level, this);

    }

    public void zoomIn() {
        camera.zoom = (float) Math.max(0, camera.zoom - 0.01);
    }

    public void zoomOut() {
        camera.zoom += 0.01;
    }

    public void igniteRocketTrail() {

        System.out.println("ignite");
        if (particleEffect.isComplete() )
        {
            particleEffect.reset();

        }
        for(int i = 0; i < particleEffect.getEmitters().size; i++){
            particleEffect.getEmitters().get(i).setContinuous(true);
        }
    }

    public void stopRocketTrail() {
        System.out.println("stop");

        for(int i = 0; i < particleEffect.getEmitters().size; i++)
        {
            particleEffect.getEmitters().get(i).setContinuous(false);
            particleEffect.getEmitters().get(i).duration = 0;
        }
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
