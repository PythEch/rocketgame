package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Class to create instances of all levels. Also, it performs most of the calculations.
 */
public class Level {
    protected static final float G = 6.67408f * 1e-20f;
    ;

    protected World world;
    protected Playable playable;
    protected Map map;
    protected Array<Trigger> triggers;
    protected Array<Waypoint> waypoints;
    protected Array<SolidObject> solidObjects;
    protected Array<GameObject> gameObjects;
    protected float timePassed;
    protected float currentGravForce;
    protected int score;
    protected State state;

    public enum ObjectType {
        PLANET, OBSTACLE, PLAYABLE
    }

    public Level() {
        this.state = State.RUNNING;

        this.world = new World(new Vector2(0, 0), true);
        this.triggers = new Array<Trigger>();
        this.waypoints = new Array<Waypoint>();
        this.solidObjects = new Array<SolidObject>();
        this.gameObjects = new Array<GameObject>();
        this.timePassed = 0;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                ObjectType typeA = (ObjectType) contact.getFixtureA().getUserData();
                ObjectType typeB = (ObjectType) contact.getFixtureB().getUserData();

                if ((typeA == ObjectType.PLANET && typeB == ObjectType.PLAYABLE) ||
                        (typeB == ObjectType.PLANET && typeA == ObjectType.PLAYABLE)) {
                    planetCollision(contact);
                }
                else if ((typeA == ObjectType.OBSTACLE && typeB == ObjectType.PLAYABLE) ||
                        (typeB == ObjectType.OBSTACLE && typeA == ObjectType.PLAYABLE)) {
                    obstacleCollision(contact);
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        this.score = 0;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        if (state == State.GAME_OVER) {
            System.out.println("gg, game over");
        }
    }

    public void saveGame() {
    } //TODO: Implement

    public void update(float deltaTime) {
        //Calculate physical quantities and game components

        if (state == State.RUNNING) {
            updateSolidObjects(deltaTime);
            updateGravity(deltaTime);
            updateTriggers(deltaTime);
            updateVisualObjects(deltaTime);
            updateWaypoints(deltaTime);

            timePassed += deltaTime;
            world.step(1 / 60f, 6, 2);
        }
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

            // Get the directionVector by subtracting the middle points of two objects
            Vector2 directionVector = planet.getBody().getPosition().sub(spaceship.getPosition());

            // uses F = G * M * m / r^2
            // We also use directionVector as it's also capable of calculating the distance
            // between the two objects.
            // Another interesting thing is LibGDX also provieds len2() method which is
            // basically len^2 so we can get r^2 this way with less code
            // it's also faster because normally distance calculation involves an Math.sqrt()
            // while len2() doesn't have to do so, so we don't have two Math.pow(Math.sqrt(distance), 2)
            // which is unnecessary work.

            float forceScalar = G * spaceship.getMass() * planet.getMass() / directionVector.len2();
            currentGravForce = forceScalar;

            // So now we have the value of the force and the direction
            // We have to get a vector with given direction and value
            Vector2 forceVector = directionVector.setLength(forceScalar);

            // apply this force to spaceship
            spaceship.applyForceToCenter(forceVector, true);
        }
    }

    private void updateWaypoints(float deltaTime) {

        //remove waypoints form screen to meet the endgame condition
        for (int i = 0; i < waypoints.size; i++) {

            if (playable.getBody().getPosition().dst(waypoints.get(i).getX(), waypoints.get(i).getY()) <= 10) {
                waypoints.get(i).setOnScreen(false);
            }
        }
    }

    private void updateParticles(float deltaTime) {
    } //TODO: implement

    private void updateVisualObjects(float deltaTime) {
        for (GameObject go : gameObjects) {
            go.update(deltaTime);
        }
    }

    public float getCurrentGravForce() {
        return currentGravForce;
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

    public float getTimePassed() {
        return timePassed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * This method is needed for analysis and debugging, but we may remove it if necessary.
     */
    public Vector2 getPlanetLocation(int no) {
        if (com.rocketfool.rocketgame.util.Constants.DEBUG) {
            return this.solidObjects.get(no).getBody().getPosition();
        } else {
            return null;
        }
    }

    public Array<Planet> getPlanets() {
        Array<Planet> planets = new Array<Planet>();
        for (SolidObject solidObject : solidObjects) {
            if (solidObject instanceof Planet) {
                planets.add((Planet) solidObject);
            }
        }
        return planets;
    }

    private void planetCollision(Contact contact) {
        System.out.println("planet collision");
        setState(State.GAME_OVER);
    }

    private void obstacleCollision(Contact contact) {
        System.out.println("obstacle collision");
        setState(State.GAME_OVER);
    }

    enum State {
        RUNNING, PAUSED, GAME_OVER
    }
}