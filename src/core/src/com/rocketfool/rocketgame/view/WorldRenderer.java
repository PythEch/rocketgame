
package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.audio.Wav;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.rocketfool.rocketgame.model.*;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Together wht GameScreen, this class draws the view.
 * The main differences are WorldRenderer drawing the objects and GameScreen presenting the UI elements.
 */
public class WorldRenderer {
    //region Constants
    public static final float MAX_ZOOM = 10f;
    public static final float MIN_ZOOM = 0.5f;
    public static final int MAX_ALPHA = 10;  // 1 = opak 255 = saydam
    public static final int MIN_ALPHA = 255;
    public static final float STAR_FREQUENCY = 3f;
    //endregion

    //region Fields
    private Level level;
    private TextureAtlas textureAtlasMeteor;
    private TextureAtlas textureAtlasStar;
    private TextureAtlas textureAtlasObjective1;
    private Animation animationObjective1;
    private Animation animationStar;
    private Animation animationMeteor;
    private float elapsedTime = 0f;
    private Array<VisualMeteor> meteors;
    private TrajectorySimulator trajectorySimulator;
    private OrthographicCamera camera;
    private Sound thrusterGoinger;
    private boolean isGoignerplaying;
    private boolean isBQPlaying;
    private boolean isThrustStopperActive;
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
        textureAtlasObjective1 = new TextureAtlas(Gdx.files.internal("Backgrounds/objectiveSheet/objScreen.atlas"));
        animationObjective1 = new Animation(1f / 80f, textureAtlasObjective1.getRegions());

        trajectorySimulator = new TrajectorySimulator(level);

        //SFX
        thrusterGoinger = AssetManager.THRUSTER_GOINGER;
        isGoignerplaying = false;
        isThrustStopperActive = false;
        isBQPlaying = false;

    }
    //endregion

    //region Methods
    public void draw(SpriteBatch batch) {
        elapsedTime = elapsedTime + Gdx.graphics.getDeltaTime();
        drawMap(batch);
        drawStars(batch);
        drawMeteors(batch);
        drawObjectiveScreen(batch, animationObjective1);
        drawPlanets(batch);
        drawMeteors(batch);
        drawPlayer(batch);
        drawTrajectory(batch);
        drawWarningSign(batch);
        for (VisualMeteor meteor : meteors) {
            meteor.update(Gdx.graphics.getDeltaTime());
        }

        //SFX
        if(level.getPlayable().getCurrentThrust() > 0)
        {
            if (!isGoignerplaying)
            {
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

        if(!isBQPlaying)
        {
            playBackgroundMusic();
            /*isBQPlaying =  true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    isBQPlaying = false;
                }
            },2.0f);*/
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

    private void drawPlanets(SpriteBatch batch) {
        Texture texturePlanet = AssetManager.PLANET1; //Base case

        //Sets texture
        for (Planet planet : level.getPlanets() ) {
            switch (planet.getPlanetType())
            {
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
            }
            texturePlanet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            batch.draw(
                    texturePlanet,
                    planet.getBody().getPosition().x * (toPixel) - (planet.getRadius() * toPixel),
                    planet.getBody().getPosition().y * (toPixel) - (planet.getRadius() * toPixel),
                    planet.getRadius() * toPixel * 2 ,
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

    private void drawObjectiveScreen(SpriteBatch batch, Animation obj) {
        batch.draw(
                obj.getKeyFrame(elapsedTime, true),
                level.getPlayable().getSpawnPoint().x + 200f,
                level.getPlayable().getSpawnPoint().y + 200f
        );
    }

    private void drawTrajectory(SpriteBatch batch) {
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

    private void drawWarningSign(SpriteBatch batch){
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

    public TrajectorySimulator getTrajectorySimulator() {
        return trajectorySimulator;
    }

    //SFX METHODS
    public void playThrustStarter(){
        System.out.println("starter");
        thrusterGoinger.play(Preferences.getInstance().getMasterVolume() / 10f );
    }

    public void playThrusterGoinger(){
        thrusterGoinger.play(Preferences.getInstance().getMasterVolume() / 10f  );
    }

    public void stopThrusterGoinger(){
        System.out.println("stopper");
        thrusterGoinger.stop();
    }

    public void playThrusterStarter(){
        AssetManager.THRUSTER_STARTER.play(Preferences.getInstance().getMasterVolume() / 10f );
    }

    public void playThrusterEnder(){
        if(isThrustStopperActive)
        AssetManager.THRUSTER_ENDER.play(Preferences.getInstance().getMasterVolume() / 10f );
    }

    public void setThrustStopperActive(boolean thrustStopperActive) {
        isThrustStopperActive = thrustStopperActive;
    }

    public void playBackgroundMusic(){
        AssetManager.BQ_MUSIC.setVolume(Preferences.getInstance().getMasterVolume() / 4f);
        AssetManager.BQ_MUSIC.play();
    }
    //endregion
}