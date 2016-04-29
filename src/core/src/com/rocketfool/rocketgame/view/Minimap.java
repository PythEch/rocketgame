package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.rocketfool.rocketgame.model.Level;
import com.rocketfool.rocketgame.model.Planet;
import com.rocketfool.rocketgame.model.TrajectorySimulator;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by pythech on 27/04/16.
 * TODO: reevaluate scales and draw trajectory smoother
 */
public class Minimap {
    private float originX;
    private float originY;
    private float side;
    private float radius;
    private Level level;
    private OrthographicCamera camera;
    private TrajectorySimulator trajectorySimulator;
    private ShapeRenderer shapeRenderer;
    private boolean enabled;

    public Minimap(float originX, float originY, float side, Level level, OrthographicCamera camera, TrajectorySimulator trajectorySimulator) {
        this.originX = originX;
        this.originY = originY;
        this.side = side;
        this.radius = (float)(side * Math.sqrt(2) / 2);
        this.level = level;
        this.camera = camera;
        this.trajectorySimulator = trajectorySimulator;
        this.enabled = true;
        if (DEBUG)
            shapeRenderer = new ShapeRenderer();
    }

    public void draw(SpriteBatch batch) {
        float playableScale = 0.25f;

        Vector2 playablePos = level.getPlayable().getBody().getPosition();

        drawAt(batch, AssetManager.MINIMAP_PLAYER, playablePos.x, playablePos.y, playableScale);

        for (Planet planet : level.getPlanets()) {
            Vector2 planetPos = planet.getBody().getPosition();
            float playableArea = level.getPlayable().getWidth() * level.getPlayable().getHeight();
            float planetArea = (float) (Math.PI * planet.getRadius() * planet.getRadius());
            float planetScale = planetArea / (float) Math.pow(playableArea, 1.5f) * playableScale;

            drawAt(batch, AssetManager.MINIMAP_PLANET, planetPos.x, planetPos.y, planetScale);
        }

        for (int i = 0; i < trajectorySimulator.getEstimationPath().size; i += 10) {
            Vector2 point = trajectorySimulator.getEstimationPath().get(i);
            drawAt(batch, AssetManager.GHOST, point.x, point.y, 0.1f);
        }

        if (DEBUG)
            debugDraw(batch);
    }

    private void drawAt(SpriteBatch batch, Texture texture, float x, float y, float scale) {
        x = side * (x * toPixel / level.getMap().getWidth());
        y = side * (y * toPixel / level.getMap().getHeight());

        if (new Vector2(x, y).dst(side / 2, side / 2) > radius) {
            return;
        }

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

    private void debugDraw(SpriteBatch batch) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(originX, originY, side, side);
        shapeRenderer.circle(originX + side / 2, originY + side / 2, radius);
        shapeRenderer.end();
        batch.begin();
    }

    public boolean isEnabled() {return enabled;}

    public void setEnabled(boolean enabled){this.enabled = enabled;}
}
