package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.rocketfool.rocketgame.model.Level;
import com.rocketfool.rocketgame.model.Planet;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by pythech on 27/04/16.
 */
public class Minimap {

    public static final float SCALE = 1;

    private int originX;
    private int originY;
    private int radius;
    private Level level;
    private OrthographicCamera camera;

    public Minimap(int originX, int originY, int radius, Level level, OrthographicCamera camera) {
        this.originX = originX;
        this.originY = originY;
        this.radius = radius;
        this.level = level;
        this.camera = camera;
    }

    public void draw(SpriteBatch batch) {
        float playableScale = 0.25f;

        Vector2 playablePos = level.getPlayable().getBody().getPosition();

        drawAt(batch, AssetManager.MINIMAP_PLAYER, playablePos.x, playablePos.y, playableScale);

        for (Planet planet : level.getPlanets()) {
            Vector2 planetPos = planet.getBody().getPosition();
            float playableArea = level.getPlayable().getWidth() * level.getPlayable().getHeight();
            float planetArea = (float)(Math.PI * planet.getRadius() * planet.getRadius());
            float planetScale = planetArea / (float)Math.pow(playableArea, 1.5f) * playableScale;

            drawAt(batch, AssetManager.MINIMAP_PLANET, planetPos.x, planetPos.y, planetScale);
        }
    }

    private void drawAt(SpriteBatch batch, Texture texture, float x, float y, float scale) {
        x = 2 * radius * (x * toPixel / level.getMap().getWidth());
        y = 2 * radius * (y * toPixel / level.getMap().getHeight());

        batch.draw(
                texture,
                camera.position.x + (-camera.viewportWidth / 2f + x + originX - texture.getWidth() * scale / 2f) * camera.zoom,
                camera.position.y + (-camera.viewportHeight / 2f + y + originY - texture.getHeight() * scale / 2f) * camera.zoom,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                camera.zoom * scale,
                camera.zoom * scale,
                0,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false
        );
    }

    private void testDraw(SpriteBatch batch) {
        drawAt(batch, AssetManager.MINIMAP_PLAYER, 0, 0, 1);
        drawAt(batch, AssetManager.MINIMAP_PLAYER, 0, radius * 2, 1);
        drawAt(batch, AssetManager.MINIMAP_PLAYER, radius * 2, 0, 1);
        drawAt(batch, AssetManager.MINIMAP_PLAYER, radius * 2, radius * 2, 1);
    }
}
