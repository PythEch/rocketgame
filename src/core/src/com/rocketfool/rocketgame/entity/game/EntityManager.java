package com.rocketfool.rocketgame.entity.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.Drawable;
import com.rocketfool.rocketgame.Updatable;

/**
 * Created by pythech on 02/04/16.
 */
public class EntityManager implements Updatable, Drawable {
    private Array<Planet> planets;
    private Player player;
    private Map map;

    public EntityManager(Player player, Map map) {
        this.planets = new Array<Planet>();
        this.player = player;
        this.map = map;
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    @Override
    public void update(float delta) {
        player.update(delta);
        updateGravity();
    }

    @Override
    public void draw(SpriteBatch batch) {
        map.draw(batch);
        player.draw(batch);
        for (Planet planet : planets) {
            planet.draw(batch);
        }
    }

    private void updateGravity() {
        for (Planet planet : planets) {
            Body spaceship = player.getBody();

            // Get the directionVector by substracting the middle points of two objects
            Vector2 directionVector = planet.getBody().getPosition().sub(spaceship.getPosition());

            // uses F = G * M * m / r^2
            // We also use directionVector as it's also capable of calculating the distance
            // between the two objects.
            // Another interesting thing is LibGDX also provieds len2() method which is
            // basically len^2 so we can get r^2 this way with less code
            // it's also faster because normally distance calculation involves an Math.sqrt()
            // while len2() doesn't have to do so, so we don't have two Math.pow(Math.sqrt(distance), 2)
            // which is unnecessary work.
            float forceScalar = spaceship.getMass() * planet.getMass() / directionVector.len2();

            // So now we have the value of the force and the direction
            // We have to get a vector with given direction and value
            Vector2 forceVector = directionVector.setLength(forceScalar);

            // apply this force to spaceship
            spaceship.applyForceToCenter(forceVector, true);
        }
    }
}
