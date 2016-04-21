package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.rocketfool.rocketgame.model.Level;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Together wht GameScreen, this class draws the view.
 * The main differences are WorldRenderer drawing the objects and GameScreen presenting the UI elements.
 */
public class WorldRenderer {

    //Variable
    private Level level;
    TextureAtlas textureAtlasStar;
    Animation animationStar;
    float elapsedTime = 0f;
    int meteorX;
    int meteorY;

    public WorldRenderer(Level level) {
        this.level = level;

        textureAtlasStar = new TextureAtlas(Gdx.files.internal("Backgrounds/meteorSheets/meteors.atlas"));
        animationStar = new Animation(1f/80f, textureAtlasStar.getRegions() );
        meteorX = level.getMap().getHeight();
        meteorY = level.getMap().getWidth();
    }

    public void draw(SpriteBatch batch) {
        elapsedTime = elapsedTime + Gdx.graphics.getDeltaTime();
        drawMap(batch);
        drawMeteors(batch);
        drawPlayer(batch);
        drawPlanets(batch);

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
        System.out.println("meteorX = " + meteorX);
        System.out.println("meteorY = " + meteorY);
        if(meteorX > 0)
        {
            batch.draw(animationStar.getKeyFrame(elapsedTime, true), meteorX--, meteorY--);
            batch.draw(new Texture(Gdx.files.internal("Backgrounds/halkalıŞeker.png")), 0, 0, meteorX, meteorY, 100, 100);
        }
        //batch.draw(animationStar.getKeyFrame(elapsedTime, true), -200, -200);
    }
}
