package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Class to create instances of all levels. Also, it performs most of the calculations.
 */
public abstract class Level {
    protected static /*final*/ float G = 6.67408e-11f;

    protected World world;
    protected Playable playable;
    protected Map map;
    protected Array<Trigger> triggers;
    protected Array<Waypoint> waypoints;
    protected Array<SolidObject> solidObjects;
    protected float timePassed;
    protected int score;

    enum State {
        RUNNING, PAUSED, GAME_OVER
    }

    protected State state;

    public Level() {
        this.world = new World(new Vector2(0, 0), true);
        this.triggers = new Array<Trigger>();
        this.waypoints = new Array<Waypoint>();
        this.solidObjects = new Array<SolidObject>();
        this.timePassed = 0;

        this.score = 0;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void saveGame() {} //TODO: Implement

    public void update(float deltaTime) {
        //Calculate physical quantities and game components
        updateSolidObjects(deltaTime);
        updateGravity(deltaTime);
        updateTriggers(deltaTime);

        timePassed += deltaTime;
        world.step(1 / 60f, 6, 2);
    }

    private void updateSolidObjects(float deltaTime) {
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
        for (SolidObject solidObject : solidObjects) {
            if (!(solidObject instanceof Planet))
                continue;

            Planet planet = (Planet) solidObject;

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
            float forceScalar = G * spaceship.getMass() * planet.getMass() / directionVector.len2(); //**

            // So now we have the value of the force and the direction
            // We have to get a vector with given direction and value
            Vector2 forceVector = directionVector.setLength(forceScalar);

            // apply this force to spaceship
            spaceship.applyForceToCenter(forceVector, true);
        }
    }

    private void updateWaypoints(float deltaTime) {} //TODO: implement

    private void updateParticles(float deltaTime) {} //TODO: implement

    public World getWorld() {
        return world;
    }

    public Playable getPlayable() {
        return playable;
    }

    public Map getMap() {
        return map;
    }

    public float getTimePassed() {
        return timePassed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
