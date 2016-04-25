package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import com.rocketfool.rocketgame.controller.WorldController;
import com.rocketfool.rocketgame.model.LevelManager;
import com.rocketfool.rocketgame.model.Playable;
import com.rocketfool.rocketgame.model.Level;

import com.badlogic.gdx.video.VideoPlayer;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Together wht WorldRenderer, this class draws the view.
 * The main differences are WorldRenderer drawing the objects and GameScreen presenting the UI elements.
 * TODO: Add UI panels
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


    private WorldRenderer renderer;

    private WorldController controller;

    private ParticleEffect particleEffect;

    private Stage stage;

    private Skin skin;


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

        //This part is for the particles coming out of rocket
        if ( cameraTarget.getCurrentThrust() > 0 ){
            this.igniteRocketTrail();
        } else {
            this.stopRocketTrail();
        }

        particleEffect.draw(batch);
        particleEffect.update(dt);
        particleEffect.setPosition(level.getPlayable().getBottomPosition().x * toPixel, level.getPlayable().getBottomPosition().y * toPixel);
        float angle = level.getPlayable().getBody().getAngle() * MathUtils.radiansToDegrees + 270;
        for (int i = 0; i < particleEffect.getEmitters().size; i++) {
            particleEffect.getEmitters().get(i).getAngle().setHigh(angle, angle);
            particleEffect.getEmitters().get(i).getAngle().setLow(angle);
        }
        draw();
        batch.end();

        // Draw boundries of physics objects if debug is enabled
        if (DEBUG)
            debugRenderer.render(level.getWorld(), camera.combined.scl(PPM));

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
            drawDebugString("  Thrust: " + (int) cameraTarget.getCurrentThrust(), 1); //**
            drawDebugString("Angular Velocity: " + (int) (cameraTarget.getBody().getAngularVelocity() * 100), 2);
            drawDebugString("  Linear Velocity: " + (int) (cameraTarget.getBody().getLinearVelocity().len() * 10), 3);
            drawDebugString("X: " + String.format("%.1f", cameraTarget.getBody().getPosition().x) +
                    " Y: " + String.format("%.1f", cameraTarget.getBody().getPosition().y), 4);
            drawDebugString(" Period: " + "( N/A )"  , 6 ); //Writing the method for it...
            drawDebugString("FPS: " + (int)(1f/Gdx.graphics.getDeltaTime()), 7 );
            drawDebugString("GravForce: " + (int) level.getCurrentGravForce(), 9 );
            drawDebugString("Fuel left: " + (int) cameraTarget.getFuelLeft(), 8 );
            drawDebugString("SAS: " + level.getPlayable().getSASEnabled(), 35 );
            drawDebugString("Mass1: " + cameraTarget.getBody().getMassData().mass, 10 );

        }
    }

    private void drawDebugString(String str, int row) {
        font.draw(
                batch,
                str,
                camera.position.x - camera.viewportWidth / 2f * camera.zoom,
                camera.position.y - camera.viewportHeight / 2f * camera.zoom + font.getLineHeight() * row
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
        level = LevelManager.createLevel4();
        cameraTarget = level.getPlayable();
        renderer = new WorldRenderer(level);
        controller = new WorldController(level, this);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
    }

    public void zoomIn() {

        camera.zoom = Math.max(0.5f, camera.zoom / 1.04f);
        if (camera.zoom > 0.5) {
            font.setScale(font.getScaleX() / 1.04f);
        }

    } //**

    public void zoomOut() {
        camera.zoom = Math.min(camera.zoom * 1.04f, 150f);
        if (camera.zoom < 150)
            font.setScale(font.getScaleX() * 1.04f);
    }

    public void igniteRocketTrail() {

        if (particleEffect.isComplete()) {
            particleEffect.reset();

        }
        for (int i = 0; i < particleEffect.getEmitters().size; i++) {
            particleEffect.getEmitters().get(i).setContinuous(true);
        }
    }

    public void stopRocketTrail() {

        for (int i = 0; i < particleEffect.getEmitters().size; i++) {
            particleEffect.getEmitters().get(i).setContinuous(false);
            particleEffect.getEmitters().get(i).duration = 0;
        }
    }

    public void showPauseScreen() {
        Dialog dialog = new Dialog("Paused", skin, "dialog") {
            public void result(Object obj) {
                switch ((Integer) obj) {
                    case 0:
                        DEBUG = !DEBUG;
                        break;
                    case 1:
                        saveCheckpoint();
                        break;
                    case 2:
                        loadCheckpoint();
                        break;
                    case 3:
                        restartLevel();
                        break;
                    case 4:
                        showOptions();
                        break;
                    case 5:
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public float getPrefWidth() {
                return super.getPrefWidth() * 1.5f;
            }

            @Override
            public void hide() {
                super.hide();

                new Timer().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        level.setState(Level.State.RUNNING);
                        System.out.println("called");
                    }
                }, 0.150f);
            }
        };

        dialog.button("Toggle Debug", 0);
        dialog.getButtonTable().row();
        dialog.button("Save Checkpoint", 1);
        dialog.getButtonTable().row();
        dialog.button("Load Checkpoint", 2);
        dialog.getButtonTable().row();
        dialog.button("Restart Level", 3);
        dialog.getButtonTable().row();
        dialog.button("Options", 4);
        dialog.getButtonTable().row();
        dialog.button("Exit", 5);
        dialog.getButtonTable().row().padTop(
                dialog.getButtonTable().getCells().first().getPrefHeight()
        );
        dialog.button("Continue", -1);

        Cell secondCell = dialog.getButtonTable().getCells().get(1);
        secondCell.width(secondCell.getPrefWidth() * 1.5f).height(secondCell.getPrefHeight() * 1.5f);
        for (Cell cell : dialog.getButtonTable().getCells()) {
            cell.uniform().fill();
        }
        dialog.getCells().first().padTop(dialog.getPrefHeight() * 0.05f);
        dialog.padBottom(dialog.getPrefHeight() * 0.05f);


        dialog.key(Input.Keys.ESCAPE, -1);
        //dialog.key(Input.Keys.ENTER, 5);

        level.setState(Level.State.PAUSED);
        dialog.show(stage);
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

    private void saveCheckpoint() {

    }

    private void loadCheckpoint() {

    }

    private void restartLevel() {

    }

    private void showOptions() {

    }
}
