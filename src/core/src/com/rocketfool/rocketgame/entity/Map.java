package com.rocketfool.rocketgame.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.util.TextureManager;

/**
 * Created by pythech on 07/03/16.
 */
public class Map extends Entity {
    //region Fields
    private int width;
    private int height;
    private Array<Planet> planets;
    //endregion

    //region Constructor
    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        planets = new Array<Planet>();

        texture = TextureManager.MAP_TEXTURE;
        // this makes background repeatable
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }
    //endregion

    //region Methods
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
    //endregion
}
