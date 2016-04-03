package com.rocketfool.rocketgame.model.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.model.entity.Map;
import com.rocketfool.rocketgame.model.entity.Planet;
import com.rocketfool.rocketgame.model.entity.Playable;
import com.rocketfool.rocketgame.model.level.trigger.FuelDepletionTrigger;
import com.rocketfool.rocketgame.model.level.trigger.OutOfMapTrigger;
import com.rocketfool.rocketgame.model.level.trigger.PositionTrigger;
import com.rocketfool.rocketgame.model.level.trigger.Trigger;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class Level {
    protected static final float G = 6.67408e-11f;

    protected World world;
    protected Playable playable;
    protected Map map;
    protected Array<Trigger> triggers;
    protected Array<Planet> planets;

    public Level() {
        this.world = new World(new Vector2(0, 0), true);
        this.triggers = new Array<Trigger>();
        this.planets = new Array<Planet>();
    }

    protected void addTriggers() {
        triggers.add(new OutOfMapTrigger(map, playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("OUT OF MAP!");
            }
        });

        triggers.add(new FuelDepletionTrigger(playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("NO FUEL!");
            }
        });
    }

    protected void gameOver(String reason) {
        // TODO: do something
    }

    public void update(float deltaTime) {
        updatePlayable(deltaTime);
        updateGravity(deltaTime);
        updateTriggers(deltaTime);

        world.step(1 / 60f, 6, 2);
    }

    private void updatePlayable(float deltaTime) {
        playable.update(deltaTime);
    }

    private void updateTriggers(float deltaTime) {
        for (Trigger trigger : triggers) {
            if (trigger.isTriggered()) {
                trigger.triggerPerformed();
            }
        }
    }

    private void updateGravity(float deltaTime) {
        for (Planet planet : planets) {
            Body spaceship = playable.getBody();

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

    public World getWorld() {
        return world;
    }

    public Playable getPlayable() {
        return playable;
    }

    public Map getMap() {
        return map;
    }

    public Array<Trigger> getTriggers() {
        return triggers;
    }

    public Array<Planet> getPlanets() {
        return planets;
    }
}
