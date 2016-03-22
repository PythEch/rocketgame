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

    public Map(int width, int height) {
        this.width = width * 100;
        this.height = height * 100;
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
            planet.update(dt);
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
                width,
                height
        );
    }
}
