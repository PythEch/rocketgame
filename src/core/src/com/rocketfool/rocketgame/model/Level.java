package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.rocketfool.rocketgame.util.Constants.DEBUG;

/**
 * Class to create instances of all levels. Also, it performs most of the calculations.
 */
public class Level {
    //region Constants
    public static final float G = 6.67408f * 1e-20f;
    //endregion

    //region Fields
    protected World world;
    protected Playable playable;
    protected Map map;
    protected Array<Trigger> triggers;
    protected Array<Waypoint> waypoints;
    protected Array<Planet> planets;
    protected Array<GameObject> gameObjects;
    protected float timePassed;
    protected float currentGravForce;
    protected int score;
    protected State state;
    //endregion

    //region Nested Types
    public enum ObjectType {
        PLANET, OBSTACLE, PLAYABLE
    }

    public enum State {
        RUNNING, PAUSED, GAME_OVER
    }
    //endregion

    //region Constructor
    public Level() {
        this.state = State.RUNNING;

        // Create a Box2D world with no gravity
        this.world = new World(new Vector2(0, 0), true);

        this.triggers = new Array<Trigger>();
        this.waypoints = new Array<Waypoint>();
        this.planets = new Array<Planet>();
        this.gameObjects = new Array<GameObject>();
        this.timePassed = 0;
        this.score = 0;

        // Register collisions
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                // User data stores the type of the object (for now)
                ObjectType typeA = (ObjectType) contact.getFixtureA().getUserData();
                ObjectType typeB = (ObjectType) contact.getFixtureB().getUserData();

                // Check if playable crashes with a planet or an obstacle
                if ((typeA == ObjectType.PLANET && typeB == ObjectType.PLAYABLE) ||
                        (typeB == ObjectType.PLANET && typeA == ObjectType.PLAYABLE)) {
                    planetCollision(contact);
                } else if ((typeA == ObjectType.OBSTACLE && typeB == ObjectType.PLAYABLE) ||
                        (typeB == ObjectType.OBSTACLE && typeA == ObjectType.PLAYABLE)) {
                    obstacleCollision(contact);
                }
            }

            //region Methods just to override
            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
            //endregion
        });
    }
    //endregion

    //region Methods

    /**
     * Update models per frame
     */
    public void update(float deltaTime) {
        if (state == State.RUNNING) {
            playable.update(deltaTime);
            updateGravity(deltaTime);
            updateTriggers(deltaTime);
            updateVisualObjects(deltaTime);
            updateWaypoints(deltaTime);

            timePassed += deltaTime;
            // A world step simulates the Box2D world
            world.step( 1/60f , 8, 3); //New values suggested by Box2D user manual. **
        }
    }

    /**
     * Fires triggers that have their conditions fullfilled
     */
    private void updateTriggers(float deltaTime) {
        for (Trigger trigger : triggers) {
            if (trigger.isTriggered()) {
                trigger.triggerPerformed();
            }
        }
    }

    /**
     * Calculates the gravity forces and applies them to the spacecraft
     */
    private void updateGravity(float deltaTime) {
        for (Planet planet : planets) {
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

    /*
     * Removes a waypoint from the screen when the playable approaches it.
     */
    private void updateWaypoints(float deltaTime) {
        for (Waypoint waypoint : waypoints) {
            if (playable.getBody().getPosition().dst(waypoint.getX(), waypoint.getY()) <= 10) {
                waypoint.setOnScreen(false);
            }
        }
    }

    /**
     * This is used to update objects like background visuals
     */
    private void updateVisualObjects(float deltaTime) {
        for (GameObject go : gameObjects) {
            go.update(deltaTime);
        }
    }

    private void planetCollision(Contact contact) {
        System.out.println("planet collision");
        if (!DEBUG)
            setState(State.GAME_OVER);
    }

    private void obstacleCollision(Contact contact) {
        System.out.println("obstacle collision");
        if (!DEBUG)
            setState(State.GAME_OVER);
    }

    /**
     * This method checks if a point in orbit around a planet is passed by a playable and returns the time it took.
     * It needs to be run at all updates, but should respond when it is called.
     */
    public static float OrbitPeriod(Playable craft, Planet p, boolean firstRun) {
        final int MARGIN = 3; //Provides a small error margin when locating positions.
        float period;
        long stopTime;
        long startTime;
        float px = p.getBody().getPosition().x;
        float py = p.getBody().getPosition().y;
        float radius = p.getRadius();
        float cx = craft.getBody().getPosition().x;
        float cy = craft.getBody().getPosition().x;
        //ToDo: remove unneeded declerations later

        boolean pointN;
        boolean pointE;
        boolean pointS;
        boolean pointW;

        boolean firstPoint;
        boolean oppositePoint;

        /*    Method Plan //ToDo: implement
           1. Wait for next point to be passed. There, make its boolean true. Get startTime from System.
           2. Wait for the opposite point to be passed. Then make its boolean true. Make the first point false.
           3. Wait for the first point again. Then make it true.
           4. If any both points are now true, get stopTime from System. Return the now-known period. Reset the method fields.
              (Using the method as a boolean, if the period is larger than 0, the planet has been circled)
              Note: maybe this could just be a subclass?
         */

        //Example passage notification:
        if ((cx < px + MARGIN) && (cx > px - MARGIN) && (cy > py))
            pointN = true;
        else if ((cx < px + MARGIN) && (cx > px - MARGIN) && (cy < py))
            pointS = true;
        else if ((cy < py + MARGIN) && (cy > py - MARGIN) && (cx > px))
            pointE = true;
        else if ((cy < py + MARGIN) && (cy > py - MARGIN) && (cx < px))
            pointW = true;

        return 0f;
    }

    public void saveGame() {
        //TODO: Implement
    }

    private void updateParticles(float deltaTime) {
        //TODO: implement
    }
    //endregion

    //region Getters & Setters
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        if (state == State.GAME_OVER) {
            System.out.println("gg, game over");
        }
    }

    public Array<Planet> getPlanets() {
        return planets;
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

    //endregion
}