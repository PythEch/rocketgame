
package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.rocketfool.rocketgame.model.*;
import com.badlogic.gdx.utils.Timer;
import com.rocketfool.rocketgame.util.GamePreferences;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Together wht GameScreen, this class draws the view.
 * The main differences are WorldRenderer drawing the objects and GameScreen presenting the UI elements.
 */
public class WorldRenderer implements Disposable {
    //region Constants
    public static final float MAX_ZOOM = 550f;
    public static final float MIN_ZOOM = 1f;
    public static final int MAX_ALPHA = 10;  // 1 = opaque, 255 = transparent
    public static final int MIN_ALPHA = 255;
    public static final float STAR_FREQUENCY = 3f;
    //endregion

    //region Fields
    private Level level;
    private TextureAtlas textureAtlasMeteor;
    private TextureAtlas textureAtlasStar;
    private TextureAtlas textureAtlasLevel3;
    private TextureAtlas textureAtlasLevel4;
    private TextureAtlas textureAtlasLevel5;
    private Animation animationLevel3;
    private Animation animationLevel4;
    private Animation animationLevel5;
    private Animation animationStar;
    private Animation animationMeteor;
    private float elapsedTime = 0f;
    private Array<VisualMeteor> meteors;
    private TrajectorySimulator trajectorySimulator;
    private OrthographicCamera camera;
    private Sound thrusterGoinger;
    private Music warningSound;
    private Music bqMusic;
    private boolean isGoignerplaying;
    private boolean isThrustStopperActive;
    private boolean isBQPlaying;
    //endregion

    //region Constructor
    public WorldRenderer(Level level, OrthographicCamera camera) {
        this.level = level;
        this.camera = camera;

        //Meteors
        textureAtlasMeteor = new TextureAtlas(Gdx.files.internal("Backgrounds/meteorSheets/meteors.atlas"));
        animationMeteor = new Animation(1f / 80f, textureAtlasMeteor.getRegions());
        meteors = new Array<VisualMeteor>();
        meteors.add(new VisualMeteor(0, 0, 10, 10, 180));
        meteors.add(new VisualMeteor(12800, 7200, -3, -4, 0));

        //Stars
        textureAtlasStar = new TextureAtlas(Gdx.files.internal("Backgrounds/starSheets/stars.atlas"));
        for (Texture texture : textureAtlasStar.getTextures()) {
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }
        animationStar = new Animation(1f / 100f, textureAtlasStar.getRegions());

        //ObjectiveScreens
        textureAtlasLevel3 = new TextureAtlas(Gdx.files.internal("Backgrounds/objectiveSheet3/obj.atlas"));
        textureAtlasLevel4 = new TextureAtlas(Gdx.files.internal("Backgrounds/objectiveSheet4/obj.atlas"));
        textureAtlasLevel5 = new TextureAtlas(Gdx.files.internal("Backgrounds/objectiveSheet5/obj.atlas"));
        animationLevel3 = new Animation(1f / 80f, textureAtlasLevel3.getRegions());
        animationLevel4 = new Animation(1f / 80f, textureAtlasLevel4.getRegions());
        animationLevel5 = new Animation(1f / 80f, textureAtlasLevel5.getRegions());


        trajectorySimulator = new TrajectorySimulator(level);

        //SFX
        thrusterGoinger = AssetManager.THRUSTER_GOINGER;
        warningSound = AssetManager.WARNING_SOUND;
        bqMusic = AssetManager.BQ_MUSIC;
        isGoignerplaying = false;
        isThrustStopperActive = false;
        isBQPlaying = false;

        registerCollision();
    }
    //endregion

    //region Methods

    private void registerCollision() {
        level.getWorld().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (contact.getFixtureA().getUserData() == Level.ObjectType.PLAYABLE ||
                        contact.getFixtureB().getUserData() == Level.ObjectType.PLAYABLE) {
                    onCollision();
                }
            }

            //region Methods just to override
            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
            //endregion
        });
    }

    private void onCollision() {
        stopWarningSound();
        stopBackgroundMusic();
        stopThrusterGoinger();

        //Plays collusionSound
        AssetManager.EXPLOSION.play(GamePreferences.getInstance().getMasterVolume() / 5f);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

            }
        }, 3.0f);
        AssetManager.DEATH_SIGN.play(GamePreferences.getInstance().getMasterVolume());

        level.setState(Level.State.HEALTH_LOST);
        if (level.getState() == Level.State.GAME_OVER) {
            // TODO: DO GAME OVER ANIMATION
        } else {
            // TODO: DO HEALTH OVER ANIMATION
        }
        registerCollision();
    }

    public void draw(final SpriteBatch batch) {
        elapsedTime = elapsedTime + Gdx.graphics.getDeltaTime();
        drawMap(batch);
        drawStars(batch);
        drawMeteors(batch);
        drawObjectiveScreen(batch);
        drawPlanets(batch);
        drawPlayer(batch);
        drawTrajectory(batch);
        drawWarningSign(batch);
        drawLevel2MoonAsteroid(batch);
        drawLevel3Objects(batch);
        drawLevel4Textures(batch);
        drawMapBorder(batch);


        for (VisualMeteor meteor : meteors) {
            meteor.update(Gdx.graphics.getDeltaTime());
        }


        //SFX
        //Rocket thrust sound
        if (level.getPlayable().getCurrentThrust() > 0) {
            if (!isGoignerplaying) {
                playThrusterGoinger();
                isGoignerplaying = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isGoignerplaying = false;
                    }
                }, 4.0f);
            }
        }

        //BQ
        playBackgroundMusic();

        //MiniMap Warning Sound
        if (trajectorySimulator.isCollided()) {
            playWarningSound();
        } else {
            stopWarningSound();
        }
    }

    private void drawPlayer(SpriteBatch batch) {
        Texture texture = AssetManager.PLAYER_TEXTURE;

        //Increase rendering quality
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Body body = level.getPlayable().getBody();

        batch.draw(
                texture,
                body.getPosition().x * toPixel - texture.getWidth() / 2f,
                body.getPosition().y * toPixel - texture.getHeight() / 2f,
                texture.getWidth() / 2f,
                texture.getHeight() / 2f,
                texture.getWidth(),
                texture.getHeight(),
                1,
                1,
                body.getAngle() * MathUtils.radiansToDegrees,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false
        );
    }

    private void drawMap(SpriteBatch batch) {
        Texture texture = AssetManager.MAP_TEXTURE;
        // this makes background tessellate
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.setFilter(Texture.TextureFilter.MipMapNearestLinear, Texture.TextureFilter.MipMapNearestLinear);

        int alpha = (int) ((MIN_ALPHA - MAX_ALPHA) * (Math.min(MAX_ZOOM, camera.zoom) - MIN_ZOOM) / (MAX_ZOOM - MIN_ZOOM)) + MAX_ALPHA;
        //sabatch.setColor(1, 1, 1, alpha);

        float minX = -camera.viewportWidth * 660 / 2f;
        float minY = -camera.viewportHeight * 660 / 2f;
        float maxX = level.getMap().getWidth() - minX;
        float maxY = level.getMap().getHeight() - minY;

        batch.draw(
                texture,
                minX,
                minY,
                0,
                0,
                (int) (maxX - minX),
                (int) (maxY - minY)
        );

        batch.setColor(1, 1, 1, 1);
    }

    private void drawPlanets(SpriteBatch batch) {
        Texture texturePlanet = AssetManager.PLANET1; //Base case

        //Sets texture
        for (Planet planet : level.getPlanets()) {
            switch (planet.getPlanetType()) {
                case 1:
                    texturePlanet = AssetManager.PLANET1;
                    break;
                case 2:
                    texturePlanet = AssetManager.PLANET2;
                    break;
                case 3:
                    texturePlanet = AssetManager.PLANET3;
                    break;
                case 4:
                    texturePlanet = AssetManager.PLANET4;
                    break;
                case 5:
                    texturePlanet = AssetManager.PLANET5;
                    break;
                case 6:
                    texturePlanet = AssetManager.PLANET6;
                    break;
                case 7:
                    texturePlanet = AssetManager.PLANET7;
                    break;
                case 8:
                    texturePlanet = AssetManager.EARTH;
                    break;
                case 9:
                    texturePlanet = AssetManager.MOON;
                    break;
                case 10:
                    texturePlanet = AssetManager.MARS;
            }
            texturePlanet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            batch.draw(
                    texturePlanet,
                    planet.getBody().getPosition().x * (toPixel) - (planet.getRadius() * toPixel),
                    planet.getBody().getPosition().y * (toPixel) - (planet.getRadius() * toPixel),
                    planet.getRadius() * toPixel * 2,
                    planet.getRadius() * toPixel * 2
            );
        }
    }


    private void drawMeteors(SpriteBatch batch) {
        for (VisualMeteor meteor : meteors) {
            if (meteor.getLocation().x > 0 &&
                    meteor.getLocation().y > 0 &&
                    meteor.getLocation().x < level.getMap().getWidth() &&
                    meteor.getLocation().y < level.getMap().getHeight()) {
                TextureRegion tr = animationMeteor.getKeyFrame(elapsedTime, true);
                batch.draw(
                        tr,
                        meteor.getLocation().x,
                        meteor.getLocation().y,
                        (float) tr.getRegionWidth(),
                        (float) tr.getRegionHeight(),
                        tr.getRegionWidth(),
                        tr.getRegionHeight(),
                        0.6f,
                        0.6f,
                        meteor.getRotateDegree()
                );
            }
        }
    }

    private void drawStars(SpriteBatch batch) {
        Texture texture = animationStar.getKeyFrame(elapsedTime, true).getTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int alpha = (int) ((MIN_ALPHA - MAX_ALPHA) * (Math.min(MAX_ZOOM, camera.zoom) - MIN_ZOOM) / (MAX_ZOOM - MIN_ZOOM)) + MAX_ALPHA;
        batch.setColor(1, 1, 1, alpha);

        batch.draw(
                texture,
                0,
                0,
                0,
                0,
                level.getMap().getWidth(),
                level.getMap().getHeight()
        );


        batch.setColor(1, 1, 1, 1);
    }

    private void drawObjectiveScreen(SpriteBatch batch) {
        boolean shouldDraw = false;
        Animation obj = null;

        if (level.getLevelNo() == 3) {
            obj = animationLevel3;
            shouldDraw = true;
        } else if (level.getLevelNo() == 4) {
            obj = animationLevel4;
            shouldDraw = true;
        } else if (level.getLevelNo() == 5) {
            obj = animationLevel5;
            shouldDraw = true;
        }

        if (shouldDraw) {
            batch.draw(
                    obj.getKeyFrame(elapsedTime, true),
                    level.getPlayable().getSpawnPoint().x * toPixel + 150,
                    level.getPlayable().getSpawnPoint().y * toPixel + 150
            );
        }
    }

    private void drawTrajectory(SpriteBatch batch) {
        if (TrajectorySimulator.enabled) {
            trajectorySimulator.update(Gdx.graphics.getDeltaTime());

            Texture texture = AssetManager.GHOST;
            texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
            for (Vector2 pos : trajectorySimulator.getEstimationPath()) {

                batch.draw(
                        texture,
                        pos.x * toPixel,
                        pos.y * toPixel
                );
            }
        }
    }

    private void drawWarningSign(SpriteBatch batch) {
        //Draws warning sign
        if (trajectorySimulator.isCollided()) {
            float randMultiplier = MathUtils.random(0.7f, 1.0f);
            float myWidth = AssetManager.WARNING.getWidth() * randMultiplier;
            float myHeight = AssetManager.WARNING.getHeight() * randMultiplier;

            batch.draw(
                    AssetManager.WARNING,
                    trajectorySimulator.getCollisionPoint().x * toPixel - myWidth / 2f,
                    trajectorySimulator.getCollisionPoint().y * toPixel - myHeight / 2f,
                    myWidth,
                    myHeight
            );
        }
    }

    private void drawLevel2MoonAsteroid(SpriteBatch batch) {

        for (int i = 0; i < level.getSolidObjects().size; i++) {
            if (level.getSolidObjects().get(i) instanceof MoonAsteroid) {
                MoonAsteroid obj = (MoonAsteroid) level.getSolidObjects().get(i);
                batch.draw(
                        AssetManager.TOXIC_METEOR,
                        obj.getPosition().x * (toPixel) - (obj.getRadius() * toPixel),
                        obj.getPosition().y * (toPixel) - (obj.getRadius() * toPixel),
                        obj.getRadius() * toPixel * 2,
                        obj.getRadius() * toPixel * 2
                );
            }
        }
    }

    private void drawLevel3Objects(SpriteBatch batch)
    {
        if(level.getLevelNo() == 3)
        {
            Texture textureMeteor;
            Texture textureRock;
            for (int i = 0; i < level.getSolidObjects().size; i++) {
                if (level.getSolidObjects().get(i) instanceof RoundObstacle) {
                    if (i % 4 == 0) {
                        textureMeteor = AssetManager.METEOR_NORMAL;
                    } else {
                        textureMeteor = AssetManager.METEOR_NORMAL2;
                    }

                    RoundObstacle obj = (RoundObstacle) level.getSolidObjects().get(i);
                    batch.draw(
                            textureMeteor,
                            obj.getBody().getPosition().x * (toPixel) - (obj.getRadius() * toPixel),
                            obj.getBody().getPosition().y * (toPixel) - (obj.getRadius() * toPixel),
                            obj.getRadius() * toPixel * 2,
                            obj.getRadius() * toPixel * 2f
                    );
                } else if (level.getSolidObjects().get(i) instanceof RectangleObstacle) {
                    RectangleObstacle obj = (RectangleObstacle) level.getSolidObjects().get(i);
                    if (i % 4 == 0) {
                        textureRock = AssetManager.ALIEN_ROCK1;
                    } else {
                        textureRock = AssetManager.ALIEN_ROCK2;
                    }
                    batch.draw(
                            textureRock,
                            obj.getBody().getPosition().x * (toPixel) - (obj.getWidth() * toPixel),
                            obj.getBody().getPosition().y * (toPixel) - (obj.getHeight() * toPixel),
                            obj.getWidth() * toPixel * 2,
                            obj.getHeight() * toPixel * 2
                    );
                }
            }

            batch.draw(
                    AssetManager.CROSS_HERE,
                    16300 * toPixel,
                    9700 * toPixel,
                    AssetManager.CROSS_HERE.getWidth() * 50,
                    AssetManager.CROSS_HERE.getHeight() * 50
            );
        }
    }

    private void drawLevel4Textures(SpriteBatch batch)
    {
        boolean shouldDrawPlayer = true;
        if(level.getLevelNo() == 4)
        {
            if(shouldDrawPlayer) {
                batch.draw(
                        AssetManager.LEVEL4PLAYER2,
                        (level.getSolidObjects().get(0).getBody().getPosition().x * toPixel) - 430,
                        (level.getSolidObjects().get(0).getBody().getPosition().y * toPixel) - 500,
                        AssetManager.LEVEL4PLAYER2.getWidth() * 7,
                        AssetManager.LEVEL4PLAYER2.getHeight() * 7
                );
            }
            if(level.getTriggers().get(2).isTriggeredBefore() ) {
                shouldDrawPlayer = false;
                batch.draw(
                        AssetManager.CROSS_HERE,
                        4300 * toPixel,
                        9100 * toPixel,
                        AssetManager.CROSS_HERE.getWidth() * 50,
                        AssetManager.CROSS_HERE.getHeight() * 50
                );
            }
        }
    }

    private void drawMapBorder(SpriteBatch batch) {
        float radius = level.getMap().getRadius();
        float scale = 10;

        Vector2 mapCenter = level.getMap().getCenter();
        Texture texture = AssetManager.MAPBORDER_DOT;

        for (float angle = 0; angle < 360; angle += 1.5f) {
            Vector2 dotPos = new Vector2(radius, 0).rotate(angle).add(mapCenter);

            batch.draw(
                    texture,
                    dotPos.x - texture.getWidth() * scale / 2f,
                    dotPos.y - texture.getHeight() * scale / 2f,
                    texture.getWidth() * scale,
                    texture.getHeight() * scale
            );
        }
    }

    public static float getMaxZoom() {
        return MAX_ZOOM;
    }

    public static float getMinZoom() {
        return MIN_ZOOM;
    }

    public TrajectorySimulator getTrajectorySimulator() {
        return trajectorySimulator;
    }

    @Override
    public void dispose() {
        textureAtlasMeteor.dispose();
        textureAtlasLevel3.dispose();
        textureAtlasStar.dispose();
    }

    //SFX METHODS
    public void playThrustStarter() {
        System.out.println("starter");
        thrusterGoinger.play(GamePreferences.getInstance().getMasterVolume() / 10f);
    }

    public void playThrusterGoinger() {
        thrusterGoinger.play(GamePreferences.getInstance().getMasterVolume() / 10f);
    }

    public void stopThrusterGoinger() {
        thrusterGoinger.stop();
    }

    public void playThrusterStarter() {
        AssetManager.THRUSTER_STARTER.play(GamePreferences.getInstance().getMasterVolume() / 10f);
    }

    public void playThrusterEnder() {
        if (isThrustStopperActive)
            AssetManager.THRUSTER_ENDER.play(GamePreferences.getInstance().getMasterVolume() / 10f);
    }

    public void setThrustStopperActive(boolean thrustStopperActive) {
        isThrustStopperActive = thrustStopperActive;
    }

    public void playBackgroundMusic() {
        bqMusic.setVolume(GamePreferences.getInstance().getMasterVolume() / 4f);
        bqMusic.play();
    }

    public void stopBackgroundMusic() {
        bqMusic.stop();
    }

    public void playWarningSound() {
        warningSound.setVolume(GamePreferences.getInstance().getMasterVolume() / 3f);
        warningSound.play();
    }

    public void stopWarningSound() {
        warningSound.stop();
    }
    //endregion
}