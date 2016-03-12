package com.rocketfool.rocketgame;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.rocketfool.rocketgame.utils.Constants.PPM;

/**
 * Created by pythech on 07/03/16.
 */
public class Map {
    private int width;
    private int height;
    private Array<Planet> planets;
    private Player player;

    public Map(int width, int height, Player player) {
        this.width = width;
        this.height = height;
        this.player = player;
        planets = new Array<Planet>();
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

    public void update(float dt) {
        for (Planet planet : planets) {
            Vector2 direction = planet.getBody().getPosition().sub(player.getSpaceship().getPosition());
            Vector2 force = direction.setLength(planet.getGravitationalForce());
            player.getSpaceship().applyForceToCenter(force, true);
        }
    }
}
