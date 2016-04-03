package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.rocketfool.rocketgame.model.level.Level;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by pythech on 02/04/16.
 */
public class WorldRenderer {
    private Level level;

    public WorldRenderer(Level level) {
        this.level = level;
    }

    public void draw(SpriteBatch batch) {
        drawPlayer(batch);
        drawMap(batch);
        drawPlanets(batch);
    }

    private void drawPlayer(SpriteBatch batch) {
        Texture texture = AssetManager.MAP_TEXTURE;
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Body body = level.getPlayer().getBody();

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
        // this makes background repeatable
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
}
