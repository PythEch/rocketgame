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
    public static final float PLAYABLE_SCALE = 0.75f;
    public static final float GHOST_SCALE = 0.1f;
    public static final float PLANET_SCALE = 4e-4f;

    private float originX;
    private float originY;
    private float width;
    private float height;
    private float radius;
    private Level level;
    private OrthographicCamera camera;
    private TrajectorySimulator trajectorySimulator;
    private ShapeRenderer shapeRenderer;
    private Vector2 center;
    private Vector2 origin;
    private boolean enabled;

    public Minimap(float originX, float originY, float radius, Level level, OrthographicCamera camera, TrajectorySimulator trajectorySimulator) {
        this.originX = originX;
        this.originY = originY;
        this.radius = radius;
        this.level = level;
        this.camera = camera;
        this.trajectorySimulator = trajectorySimulator;
        this.enabled = true;

        float realRadius = (float)Math.sqrt(Math.pow(level.getMap().getWidth(), 2) + Math.pow(level.getMap().getHeight(), 2)) / 2f;

        this.width = radius / realRadius * level.getMap().getWidth();
        this.height = radius / realRadius * level.getMap().getHeight();

        center = new Vector2(originX + radius / (float)Math.sqrt(2), originY + radius / (float)Math.sqrt(2));
        origin = new Vector2(center.x - width / 2f, center.y - height / 2f);

        if (DEBUG)
            shapeRenderer = new ShapeRenderer();
    }

    public void draw(SpriteBatch batch) {
        Vector2 playablePos = level.getPlayable().getBody().getPosition();

        drawAt(batch, AssetManager.MINIMAP_PLAYER, playablePos.x, playablePos.y, PLAYABLE_SCALE);

        for (Planet planet : level.getPlanets()) {
            Vector2 planetPos = planet.getBody().getPosition();
            float planetArea = (float) (Math.PI * planet.getRadius() * planet.getRadius());
            float planetScale = (float) (Math.sqrt(planetArea) * PLANET_SCALE);

            drawAt(batch, AssetManager.MINIMAP_PLANET, planetPos.x, planetPos.y, planetScale);
        }

        for (int i = 0; i < trajectorySimulator.getEstimationPath().size; i += 10) {
            Vector2 point = trajectorySimulator.getEstimationPath().get(i);
            drawAt(batch, AssetManager.GHOST, point.x, point.y, GHOST_SCALE);
        }

        if (DEBUG)
            debugDraw(batch);
    }

    private void drawAt(SpriteBatch batch, Texture texture, float x, float y, float scale) {
        x = width * (x * toPixel / level.getMap().getWidth());
        y = height * (y * toPixel / level.getMap().getHeight());

        if (center.dst(x,y) > radius) {
            //sSystem.out.println("nope");
            return;
        }

        batch.draw(
                texture,
                camera.position.x + (-camera.viewportWidth / 2f + x + origin.x - texture.getWidth() * scale / 2f) * camera.zoom,
                camera.position.y + (-camera.viewportHeight / 2f + y + origin.y - texture.getHeight() * scale / 2f) * camera.zoom,
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
        shapeRenderer.rect(origin.x, origin.y, width, height);
        shapeRenderer.circle(center.x, center.y, radius);

        shapeRenderer.end();
        batch.begin();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
