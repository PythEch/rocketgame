package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.model.Level;
import com.rocketfool.rocketgame.model.TrajectorySimulator;
import com.rocketfool.rocketgame.model.VisualMeteor;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Together wht GameScreen, this class draws the view.
 * The main differences are WorldRenderer drawing the objects and GameScreen presenting the UI elements.
 */
public class WorldRenderer {

    //Variable
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


    public WorldRenderer(Level level) {
        this.level = level;

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
    }

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
        for (VisualMeteor meteor : meteors) {
            meteor.update(Gdx.graphics.getDeltaTime());
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

        batch.draw(
                texture,
                0,
                0,
                0,
                0,
                level.getMap().getWidth(),
                level.getMap().getHeight()
        );
    }

    private void drawPlanets(SpriteBatch batch) {
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
        Texture tr = animationStar.getKeyFrame(elapsedTime, true).getTexture();
        batch.draw(
                tr,
                0,
                0,
                0,
                0,
                level.getMap().getWidth(),
                level.getMap().getHeight());
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
        for (Vector2 pos : trajectorySimulator.getCurrentEstimationPath()) {

            batch.draw(
                    texture,
                    pos.x * toPixel,
                    pos.y * toPixel
            );
        }
    }
}
