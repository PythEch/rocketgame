package com.rocketfool.rocketgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.rocketfool.rocketgame.utils.Constants.PPM;

/**
 * Created by pythech on 07/03/16.
 */
public class Map extends GameObject {
    private static final float G = 6.67408e-11f;

    private int width;
    private int height;
    private Array<Planet> planets;
    private Player player;

    public Map(int width, int height, Player player) {
        this.width = width;
        this.height = height;
        this.player = player;
        planets = new Array<Planet>();

        texture = new Texture("Backgrounds/darkPurple.png");
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat); // makes background repeatable
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    @Override
    public void update(float dt) {
        for (Planet planet : planets) {
            Vector2 directionVector = planet.getBody().getPosition().sub(player.getBody().getPosition());
            float forceScalar = player.getBody().getMass() * planet.getMass() / (float)Math.pow(directionVector.len(), 2);
            Vector2 forceVector = directionVector.setLength(forceScalar);
            player.getBody().applyForceToCenter(forceVector, true);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                texture,
                0,
                0,
                0,
                0,
                width * 100,
                height * 100
        );
    }
}
