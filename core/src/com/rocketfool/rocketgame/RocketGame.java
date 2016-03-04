package com.rocketfool.rocketgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.rocketfool.rocketgame.utils.Constants.PPM;

public class RocketGame extends ApplicationAdapter {
    public World world;

    private static RocketGame instance = new RocketGame();

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;
    private SpriteBatch batch;

    private Texture backgroundImage;

    private Player player;

    /** Exists only to defeat instantiation. */
    private RocketGame() { }

    /**
     * This makes sure only one instance can be created of {@link RocketGame} at a time.
     * @return Returns the shared instance of {@link RocketGame}.
     */
    public static RocketGame getInstance() {
        return instance;
    }

    /**
     * This is our initialization method, akin to constructor
     * though used for different purposes.
     * Here we build our objects etc.
     */
    @Override
    public void create() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        world = new World(new Vector2(0, 0), true);
        renderer = new Box2DDebugRenderer();

        backgroundImage = new Texture("Backgrounds/darkPurple.png");
        // makes background repeatable
        backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        FileHandle fh = new FileHandle("PNG/playerShip2_orange.png");
        player = new Player(new Texture(fh, true));
    }

    /**
     * This method is called whenever rendering is needed (once a frame is drawn, i.e 60 fps = 60 calls).
     * We are diving business logic and drawing logic with methods
     * {@link #update(float)} and
     * {@link #draw()}.
     */
    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        draw();

        batch.end();

        renderer.render(world, camera.combined.scl(PPM));
    }

    /**
     * This is used to release (aka get rid of) objects we no longer need to hold in memory
     * when the Game class is used destroyed.
     * Used for performance reasons, so we generally don't need to care about this much.
     */
    @Override
    public void dispose() {
        renderer.dispose();
        world.dispose();
    }

    /**
     * This method is responsible for drawing objects related to our game.
     * Generally it will call bunch of methods of its object.
     */
    private void draw() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        batch.draw(
                backgroundImage,
                0,
                0,
                0,
                0,
                width,
                height
        );

        player.draw(batch);

    }

    /**
     * Business logic of the game goes here such as physics, camera, UI, statistics etc.
     * @param dt Stands for DeltaTime which is the time passed between two sequential calls of update.
     */
    private void update(float dt) {
        player.update(dt);

        // Make the camera focus on the player
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // update physics objects
        world.step(1 / 60f, 6, 2);

    }
}
