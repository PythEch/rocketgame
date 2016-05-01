package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rocketfool.rocketgame.controller.WorldController;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.model.*;

import com.badlogic.gdx.video.VideoPlayer;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Together wht WorldRenderer, this class draws the view.
 * The main differences are WorldRenderer drawing the objects and GameScreen presenting the UI elements.
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

    private Viewport viewport;

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

    private RocketGame game;
    private float elapsedTime;

    private Minimap minimap;

    private PopupView popupView;

    private BitmapFont timerFont;

    private ShapeRenderer shapeRenderer;

    private Animation waypointAnimation;

    private TextureAtlas waypointAtlas;

    private Texture sasTexture;

    //endregion

    //region Constructor
    public GameScreen(Level level, RocketGame game, SpriteBatch batch, BitmapFont font) {
        // Get these from the Game instance
        this.game = game;
        this.batch = batch;
        this.font = font;
        this.level = level;
        elapsedTime = 0;
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

        sasTexture = AssetManager.SAS_OFF;


        batch.begin();
        // Our main draw method
        renderer.draw(batch);

        //This part is for the particles coming out of rocket
        if (cameraTarget.getCurrentThrust() > 0) {
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
        elapsedTime += dt;
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
            drawDebugString("  Thrust: " + (int) cameraTarget.getCurrentThrust(), 25);
            drawDebugString("Angular Velocity: " + (int) (cameraTarget.getBody().getAngularVelocity() * 100), 26);
            drawDebugString("  Linear Velocity: " + (int) (cameraTarget.getBody().getLinearVelocity().len() * 10), 27);
            drawDebugString("X: " + String.format("%.1f", cameraTarget.getBody().getPosition().x) +
                    " Y: " + String.format("%.1f", cameraTarget.getBody().getPosition().y), 28);
            drawDebugString("FPS: " + (int) (1f / Gdx.graphics.getDeltaTime()), 29);
            drawDebugString("GravForce: " + (int) level.getCurrentGravForce(), 31);
            drawDebugString("Fuel left: " + (int) cameraTarget.getFuelLeft(), 30);
            drawDebugString("SAS: " + level.getPlayable().getSASEnabled(), 35);
            drawDebugString("Mass1: " + cameraTarget.getBody().getMassData().mass, 32);

        }



        //Overlay-static
        //When camera moves or zooms, overlay follows it by below algorithm
        batch.draw(
                AssetManager.OVERLAY,
                camera.position.x - camera.viewportWidth / 2f * camera.zoom,
                camera.position.y - camera.viewportHeight / 2f * camera.zoom,
                0,
                0,
                camera.viewportWidth,
                camera.viewportHeight,
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                AssetManager.OVERLAY.getWidth(),
                AssetManager.OVERLAY.getHeight(),
                false,
                false
        );

        //Overlay-dynamic
        Texture overlayFiller = AssetManager.PROGFILLER;
        overlayFiller.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        //Velocity bar
        float velocityRate;
        if (cameraTarget.getBody().getLinearVelocity().len() * 10 > 1165) {
            velocityRate = 394;
        } else {
            velocityRate = ((cameraTarget.getBody().getLinearVelocity().len() * 10) * 394) / level.getPlayable().getMaxVelocity(); // 394 When bar is full
        }
        batch.draw(
                overlayFiller,
                camera.position.x - (camera.viewportWidth / 2f - 462) * camera.zoom, //462 Velocity bar's starting pointX
                camera.position.y - (camera.viewportHeight / 2f - 677) * camera.zoom, //677 Velocity bar's starting pointY
                0,
                0,
                velocityRate, // 394 When bar is full
                20, //Height of bar
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                overlayFiller.getWidth(),
                overlayFiller.getHeight(),
                false,
                false
        );

        //Fuel bar
        float fuelRate;
        fuelRate = (level.getPlayable().getFuelLeft() * 394) / level.getPlayable().getStartingFuel(); //394 When bar is full
        batch.draw(
                overlayFiller,
                camera.position.x - (camera.viewportWidth / 2f - 462) * camera.zoom, //462 Fuel bar's starting pointX
                camera.position.y - (camera.viewportHeight / 2f - 635.9f) * camera.zoom, //635.9f Fuel bar's starting pointY
                0,
                0,
                fuelRate,
                20, //Height of bar
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                overlayFiller.getWidth(),
                overlayFiller.getHeight(),
                false,
                false
        );

        //Thrust bar
        float thrustRate;
        thrustRate = (level.getPlayable().getCurrentThrust() * 69) / level.getPlayable().getMaxThrust(); //69 When bar is full
        batch.draw(
                overlayFiller,
                camera.position.x - (camera.viewportWidth / 2f - 894) * camera.zoom, //462 Fuel bar's starting pointX
                camera.position.y - (camera.viewportHeight / 2f - 635.9f) * camera.zoom, //635.9f Fuel bar's starting pointY
                0,
                0,
                14,
                thrustRate, //When bar is max
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                overlayFiller.getWidth(),
                overlayFiller.getHeight(),
                false,
                false
        );

        //Timer
        String str = "" + (int) elapsedTime;
        if ((int) elapsedTime >= 60) {
            str = "" + (int) (elapsedTime / 60) + ":" + (int) elapsedTime % 60;
        }
        timerFont.setScale(camera.zoom);
        timerFont.draw(
                batch,
                str,
                camera.position.x + (camera.viewportWidth / 2f - 140f) * camera.zoom - timerFont.getBounds(str).width / 2f,
                camera.position.y + (camera.viewportHeight / 2f + 320f) * camera.zoom - timerFont.getLineHeight() * 12f
        );

        //Health
        Texture rocketTexture = AssetManager.PLAYER_TEXTURE;
        for (int i = 0; i < level.getHealth(); i++) {
            batch.draw(
                    rocketTexture,
                    //-120 = indent pixel from left, (75*i) = gap between textures
                    camera.position.x - (-120 + (camera.viewportWidth / 2f) - (75 * i)) * camera.zoom,
                    camera.position.y - (camera.viewportHeight / 2f - 635) * camera.zoom, //-635 Y axis level
                    0,
                    0,
                    rocketTexture.getWidth() / 1.7f, //1.7f = scaling ratio
                    rocketTexture.getHeight() / 1.7f,
                    camera.zoom,
                    camera.zoom,
                    0,
                    0,
                    0,
                    rocketTexture.getWidth(),
                    rocketTexture.getHeight(),
                    false,
                    false
            );
        }
        font.draw(
                batch,
                level.getObjectiveWindow().getText(),
                camera.position.x - (camera.viewportWidth / 2f - 20) * camera.zoom,
                camera.position.y + (camera.viewportHeight / 2f - 100) * camera.zoom
        );

        //WarningMapSign
        // flash the sign by only showing it for 0.5 seconds
        if (renderer.getTrajectorySimulator().isCollided() && (int) (elapsedTime * 2) % 2 == 0) {
            batch.draw(
                    AssetManager.WARNING,
                    camera.position.x - (camera.viewportWidth / 2f - 1022) * camera.zoom, //1022 hud x pos
                    camera.position.y - (camera.viewportHeight / 2f - 235) * camera.zoom, //235 hud y pos
                    0,
                    0,
                    AssetManager.WARNING.getWidth() / 14f, //14f = scaling ratio
                    AssetManager.WARNING.getHeight() / 14f,
                    camera.zoom,
                    camera.zoom,
                    0,
                    0,
                    0,
                    AssetManager.WARNING.getWidth(),
                    AssetManager.WARNING.getHeight(),
                    false,
                    false
            );
        }

        // draw waypoint
        if (level.getWaypoint() != null) {
            //Texture texture = waypointAnimation.getKeyFrame(elapsedTime, true).getTexture();
            //texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            TextureRegion texture = waypointAnimation.getKeyFrame(elapsedTime, true);
            batch.draw(
                    texture,
                    level.getWaypoint().getPosition().x * toPixel,
                    level.getWaypoint().getPosition().y * toPixel,
                    0,
                    0,
                    texture.getRegionWidth(),
                    texture.getRegionHeight(),
                    0.25f,
                    0.25f,
                    level.getWaypoint().getAngle()
            );
        }

        if (minimap.isEnabled()) {
            minimap.draw(batch);
        }

        //SAS Indicator
        if(level.getPlayable().getSASEnabled() )
            sasTexture = AssetManager.SAS_ON;
        else
            sasTexture = AssetManager.SAS_OFF;

        batch.draw(
                sasTexture,
                camera.position.x - (camera.viewportWidth / 2f - 0 )  * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - 400) * camera.zoom,
                0,
                0,
                sasTexture.getWidth(),
                sasTexture.getHeight(),
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                sasTexture.getWidth() ,
                sasTexture.getHeight() ,
                false,
                false
        );


        popupView.draw(batch);

        //Level end and game end
        if (level.getState() == Level.State.LEVEL_FINISHED) {

            batch.draw(
                    AssetManager.LEVEL_FINISHED,
                    camera.position.x - camera.viewportWidth / 2f * camera.zoom,
                    camera.position.y - camera.viewportHeight / 2f * camera.zoom,
                    AssetManager.LEVEL_FINISHED.getWidth() * camera.zoom,
                    AssetManager.LEVEL_FINISHED.getHeight() * camera.zoom
            );

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {

                    Level newLevel = null;
                    boolean isGameOver = false;
                    switch (level.getLevelNo()) {
                        case 1:
                            newLevel = LevelManager.createLevel2();
                            break;
                        case 2:
                            newLevel = LevelManager.createLevel3();
                            break;
                        case 3:
                            newLevel = LevelManager.createLevel4();
                            break;
                        case 4:
                            newLevel = LevelManager.createLevel5();
                            break;
                        case 5:
                            isGameOver = true;
                            break;
                    }
                    if (!isGameOver) {
                        game.setScreen(new GameScreen(newLevel, game, batch, font));
                       // game.setScreen(new CutsceneScreen(game, batch, font, AssetManager.TAKEOFF_VIDEO, newLevel, "aaa"));
                    } else {
                        level.setState(Level.State.GAME_OVER);
                    }

                }
            }, 5.0f);

        } else if (level.getState() == Level.State.GAME_OVER) {
            renderer.stopThrusterGoinger();
            renderer.stopWarningSound();
            renderer.stopBackgroundMusic();
            popupView.stopPopupShutter();
            game.setScreen(new EndingScreen(game, batch, font));
        }

        // draw trigger bounds for debug
        if (DEBUG) {
            for (Trigger trigger : level.getTriggers()) {
                if (trigger instanceof PositionTrigger) {
                    PositionTrigger positionTrigger = (PositionTrigger) trigger;

                    batch.end();
                    shapeRenderer.setProjectionMatrix(camera.combined);

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                    if (positionTrigger.isTriggeredBefore()) {
                        shapeRenderer.setColor(Color.GREEN);
                    } else {
                        shapeRenderer.setColor(Color.RED);
                    }

                    shapeRenderer.circle(
                            positionTrigger.getPosition().x * toPixel,
                            positionTrigger.getPosition().y * toPixel,
                            positionTrigger.getRadius() * toPixel
                    );

                    shapeRenderer.end();
                    batch.begin();

                }
            }
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
     * @param dt Stands for DeltaTime which is the elapsedTime passed between two sequential calls of update.
     */
    private void update(float dt) {
        // Update all entities
        level.update(dt);

        // Make the camera focus on the player
        camera.position.set(cameraTarget.getBody().getPosition().x * toPixel, cameraTarget.getBody().getPosition().y * toPixel, 0);
        camera.update();

        popupView.update(dt);
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
        renderer.dispose();
        if (player != null)
            player.dispose();
        font.dispose();
        particleEffect.dispose();
        stage.dispose();
        skin.dispose();
        timerFont.dispose();
        shapeRenderer.dispose();
        waypointAtlas.dispose();
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

        //Waypoint
        waypointAtlas = new TextureAtlas(Gdx.files.internal("waypointSheets/waypoint.atlas"));
        waypointAnimation = new Animation(1f / 60f, waypointAtlas.getRegions());


        //level = LevelManager.createLevel2();
        cameraTarget = level.getPlayable();
        renderer = new WorldRenderer(level, camera);
        controller = new WorldController(level, this, renderer);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));

        minimap = new Minimap(1064, 48, 116, level, camera, renderer.getTrajectorySimulator());

        level.setScreenReference(this);

        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        popupView = new PopupView(level.getPopup(), camera);

        timerFont = new BitmapFont(Gdx.files.internal("fonts/contrax.fnt"));

        shapeRenderer = new ShapeRenderer();

        level.setState(Level.State.RUNNING);
    } //endregion

    public void zoomIn() {

        camera.zoom = Math.max(0.5f, camera.zoom / 1.04f);
        font.setScale(camera.zoom);

    }

    public void zoomOut() {
        camera.zoom = Math.min(camera.zoom * 1.04f, 550f);
        font.setScale(camera.zoom);
    }

    public void setZoom(float zoom) {
        camera.zoom = zoom;
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
                        level.resetLevel();
                        level.setHealth(3);
                        break;
                    case 2:
                        game.setScreen(new MainMenuScreen(game, batch, font));
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
                    }
                }, 0.150f);
            }
        };

        dialog.button("Toggle Debug", 0);
        dialog.getButtonTable().row();
        dialog.button("Restart Level", 1);
        dialog.getButtonTable().row();
        dialog.button("Exit", 2);
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
        viewport.update(width, height);
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

    public Minimap getMinimap() {
        return minimap;
    }
}
