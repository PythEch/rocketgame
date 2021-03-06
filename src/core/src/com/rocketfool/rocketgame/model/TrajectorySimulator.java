package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.util.GameUtils;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Simulates the Box2D in advance in order to predict where the player is going.
 * Very precise especially when additional controls (i.e keyboard keys) are not used.
 */
public class TrajectorySimulator extends GameObject {
    //region Constants
    /**
     * Number of steps that will be calculated in advance
     */
    public static final int SIMULATION_STEPS = 3600;

    /**
     * A hand tweaked multiplier to make the intervals between trajectory dots evenly as possible
     */
    public static final float SKIP_MULTIPLIER = 20;

    /**
     * The minimum threshold in our skip algorithm.
     */
    public static final int MIN_SKIP = 2;

    /**
     * The maximum velocity is used to scale the interval of the dots
     */
    public static final float MAX_VELOCITY = 120;

    /**
     * Minimum threshold to calculate a trajectory
     */
    public static final int IGNORE_THRESHOLD = 15;

    public static boolean enabled = true;
    //endregion

    //region Fields
    /**
     * Our replica of the Box2D world of the Level we are simulating in
     */
    private World world;
    private Level level;
    /**
     * Used to store the points where the playable has traveled in our simulator
     */
    private Array<Vector2> currentEstimationPath;
    /**
     * Used to copy planets to our own World
     */
    private Array<Planet> planets;
    /**
     * A replica of the playable of the current Level
     */
    private Playable playable;
    /**
     * Used to indiciate if the spacecraft has crashed in the simulation
     */
    private boolean collided = false;
    /**
     * Used to give the point of the crash in case it happens
     */
    private Vector2 collisionPoint;
    //endregion

    //region Constructor
    public TrajectorySimulator(Level level) {
        this.level = level;

        planets = new Array<Planet>();
        currentEstimationPath = new Array<Vector2>();

        // Make a replica world
        world = new World(new Vector2(0, 0), true);

        // Register collisions
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (contact.getFixtureA().getUserData() == Level.ObjectType.PLAYABLE) {
                    collisionPoint = contact.getFixtureA().getBody().getPosition();
                } else if (contact.getFixtureB().getUserData() == Level.ObjectType.PLAYABLE) {
                    collisionPoint = contact.getFixtureB().getBody().getPosition();
                }

                collided = true;
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

        // Copy all planets to our own world
        for (Planet planet : level.getPlanets()) {
            Planet newPlanet = new Planet(
                    planet.getBody().getPosition().x,
                    planet.getBody().getPosition().y,
                    planet.getMass(),
                    planet.getRadius(),
                    planet.getPrimary(),
                    world,
                    planet.getPlanetType()
            );
            planets.add(newPlanet);
        }

        // Copy the playable to our own world
        playable = new Playable(
                level.getPlayable().getBody().getPosition().x,
                level.getPlayable().getBody().getPosition().y,
                level.getPlayable().getWidth(),
                level.getPlayable().getHeight(),
                level.getPlayable().getBody().getMass() - level.getPlayable().getFuelLeft(),
                level.getPlayable().getDeltaTorque(),
                level.getPlayable().getDeltaThrust(),
                level.getPlayable().getMaxThrust(),
                level.getPlayable().getFuelLeft(),
                world
        );

        resetSimulation();
    }
    //endregion

    //region Methods

    /**
     * Resets the world in order to update the changes in user behaviour (e.g. user turns the spacecraft, decreases thrust)
     */
    private void resetSimulation() {
        // Remove all points in the estimation
        currentEstimationPath.clear();

        // Clear the world
        world.clearForces();

        // Copy the properties of the real playable to the one in the simulated environment
        Body simulatedPlayable = playable.getBody();
        Body realPlayable = level.getPlayable().getBody();

        playable.setCurrentThrust(level.getPlayable().getCurrentThrust());
        playable.setFuelLeft(level.getPlayable().getFuelLeft());
        playable.setSASEnabled(level.getPlayable().getSASEnabled());

        if (level.getPlayable().getSASEnabled())
            simulatedPlayable.setAngularVelocity(0);
        else
            simulatedPlayable.setAngularVelocity(realPlayable.getAngularVelocity());

        simulatedPlayable.setLinearVelocity(realPlayable.getLinearVelocity());
        simulatedPlayable.setTransform(realPlayable.getPosition(), realPlayable.getAngle());
        simulatedPlayable.getTransform().setOrientation(realPlayable.getTransform().getOrientation());
        simulatedPlayable.getTransform().setRotation(realPlayable.getTransform().getRotation());

        GameUtils.changeMass(simulatedPlayable, realPlayable.getMass());
    }

    /**
     * Simulates the world in every frame
     */
    @Override
    public void update(float deltaTime) {
        collided = false;

        // skip if no trajectory is necessary
        if (level.getPlayable().getBody().getLinearVelocity().len() + level.getPlayable().getCurrentThrust() < IGNORE_THRESHOLD) {
            currentEstimationPath.clear();
            return;
        }

        // Calculate a reasonable skipCount so that the spacings between the trajectory dots will be evenly as possible
        int skipCount = (int) ((1 - (level.getPlayable().getBody().getLinearVelocity().len() / MAX_VELOCITY)) * SKIP_MULTIPLIER);
        skipCount = Math.max(MIN_SKIP, skipCount);

        // Reset our simulation in case of a user input
        resetSimulation();

        // Predict the next N steps of the playable by simulating the world
        for (int i = 1; i <= SIMULATION_STEPS; i++) {
            if (collided)
                break;

            // Simulate the world
            doGravity();
            updatePlanets();
            playable.update(FRAME_RATE);
            world.step(FRAME_RATE, 8, 3);

            // Skip a certain N amounts of points as to not clutter the screen
            if (i % skipCount == 0)
                currentEstimationPath.add(playable.getBody().getPosition().cpy());
        }
    }

    /**
     * Update the positions of the planets
     */
    private void updatePlanets() {
        for (int i = 0; i < planets.size; i++) {
            Planet myPlanet = planets.get(i);
            Planet otherPlanet = level.getPlanets().get(i);

            myPlanet.getBody().setTransform(otherPlanet.getBody().getPosition(), 0);
        }
    }

    /**
     * Calculate the gravity
     */
    private void doGravity() {
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
            float forceScalar = level.G * spaceship.getMass() * planet.getMass() / directionVector.len2(); //**

            // So now we have the value of the force and the direction
            // We have to get a vector with given direction and value
            Vector2 forceVector = directionVector.setLength(forceScalar);

            // apply this force to spaceship
            spaceship.applyForceToCenter(forceVector, true);
        }
    }
    //endregion

    //region Getters & Setters
    public Array<Vector2> getEstimationPath() {
        return currentEstimationPath;
    }

    public Vector2 getCollisionPoint() {
        return collisionPoint;
    }

    public boolean isCollided() {
        return collided;
    }
    //endregion
}
