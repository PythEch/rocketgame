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


    /** A Box2D world which manages the physics engine. */
    private World world;
    /** Orthographic cameras are used for 2D games, the other one Perspective camera is for 3D games. */
    private OrthographicCamera camera;
    /** Used for drawing the bounds of objects in the physics engine for easier debugging */
    private Box2DDebugRenderer debugRenderer;


    /** Manages the spaceship entity and responds to player input at the same time */
    private Player player;
    /** Draws the map and manages the objects like planets */
    private Map map;


    /** This holds all entities in the game so we can `draw` and `update` them */
    private Array<Entity> entities;
    //endregion

    //region Things which make this screen singleton
    /** Internal property that holds only one instance of {@link GameScreen} at a time. */
    private static final GameScreen instance = new GameScreen();

    /** Exists only to defeat instantiation. */
    private GameScreen() {}

    /** Returns the shared instance of {@link GameScreen} */
    public static GameScreen getInstance() {
        return instance;
    }
    //endregion

    //region Constructor
    /**
     * This is our initialization method, akin to constructor though used for different purposes.
     */
    public void init(SpriteBatch batch, BitmapFont font) {
        // Get these from the Game instance
        this.batch = batch;
        this.font = font;

        // these will be useful later on
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        //region Setup the environment for our game
        // construct the world for Box2D with no gravity
        world = new World(new Vector2(0, 0), true);

        // construct the camera with given width and height
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        // setup debug renderer so we can see what's going on with the physics engine
        debugRenderer = new Box2DDebugRenderer();
        //endregion

        //region Construct game specific objects
        player = new Player(0, 0);
        map = new Map(width * 100, height * 100);
        map.addPlanet(new Planet(75, 75, 1e4f, 50));

        // the order of addition is important because it changes z-order
        entities = new Array<Entity>();
        entities.add(map);
        entities.add(player);
        //endregion
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
            debugRenderer.render(world, camera.combined.scl(PPM));
    }

    /**
     * This method is responsible for drawing objects related to our game.
     * Generally it will call bunch of methods of its object.
     */
    private void draw() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // Draw all entities
        for (Entity entity : entities) {
            entity.draw(batch);
        }

        // Draw a debug string which shows the velocity of the spaceship
        if (DEBUG) {
            font.draw(
                    batch,
                    "  Linear Impulse: " + player.getCurrentImpulse(),
                    camera.position.x - camera.viewportWidth / 2f,
                    camera.position.y - camera.viewportHeight / 2f + font.getLineHeight()
            );

            font.draw(
                    batch,
                    "Angular Velocity: " + player.getBody().getAngularVelocity(),
                    camera.position.x - camera.viewportWidth / 2f,
                    camera.position.y - camera.viewportHeight / 2f + font.getLineHeight() * 2
            );

            font.draw(
                    batch,
                    "  Linear Velocity: " + player.getBody().getLinearVelocity().len(),
                    camera.position.x - camera.viewportWidth / 2f,
                    camera.position.y - camera.viewportHeight / 2f + font.getLineHeight() * 3
            );
        }
    }

    /**
     * Business logic of the game goes here such as physics, camera, UI, statistics etc.
     * @param dt Stands for DeltaTime which is the time passed between two sequential calls of update.
     */
    private void update(float dt) {
        // Update all entities
        for (Entity entity : entities) {
            entity.update(dt);
        }

        // Make the camera focus on the player
        camera.position.set(player.getBody().getPosition().x * toPixel, player.getBody().getPosition().y * toPixel, 0);
        camera.update();

        // update/calculate physics objects
        world.step(1 / 60f, 6, 2);
    }

    /**
     * This is used to release (aka get rid of) objects we no longer need to hold in memory
     * when the Game class is used destroyed.
     * Used for performance reasons, so we generally don't need to care about this much.
     */
    @Override
    public void dispose() {
        debugRenderer.dispose();
        world.dispose();
    }
    //endregion

    //region Getters & Setters
    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
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
