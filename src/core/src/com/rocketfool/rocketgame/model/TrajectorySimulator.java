package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.io.*;

/**
 * Created by pythech on 22/04/16.
 */
public class TrajectorySimulator extends GameObject {
    private World world;
    private Level level;
    private Array<Vector2> currentEstimationPath;
    private Array<Vector2> lastEstimationPath;
    private Array<Planet> planets;
    private Playable playable;
    private int times = 0;

    public TrajectorySimulator(Level level) {
        this.level = level;

        planets = new Array<Planet>();
        currentEstimationPath = new Array<Vector2>();
        lastEstimationPath = currentEstimationPath;

        world = new World(new Vector2(0, 0), true);

        for (Planet planet : level.getPlanets()) {
            planets.add(new Planet(
                    planet.getBody().getPosition().x,
                    planet.getBody().getPosition().y,
                    planet.getMass(),
                    planet.getRadius(),
                    null,
                    world
            ));
        }

        createWorld();

    }

    private void createWorld() {
        currentEstimationPath.clear();

        if (playable != null)
            world.destroyBody(playable.getBody());

        world.clearForces();

        playable = new Playable(
                level.getPlayable().getBody().getPosition().x,
                level.getPlayable().getBody().getPosition().y,
                level.getPlayable().getWidth(),
                level.getPlayable().getHeight(),
                level.getPlayable().getBody().getMass(),
                level.getPlayable().getDeltaAngularImpulse(),
                level.getPlayable().getDeltaThrust(),
                10000,
                1000000,
                world
        );

        Body myBody = playable.getBody();
        Body other = level.getPlayable().getBody();

        playable.setCurrentThrust(level.getPlayable().getCurrentThrust());
        myBody.setAngularVelocity(other.getAngularVelocity());
        myBody.setAngularDamping(other.getAngularDamping());
        myBody.setLinearVelocity(other.getLinearVelocity().cpy());
        myBody.setTransform(other.getPosition().cpy(), other.getAngle());
        myBody.getTransform().setOrientation(other.getTransform().getOrientation().cpy());
        myBody.getTransform().setRotation(other.getTransform().getRotation());
    }


    @Override
    public void update(float deltaTime) {
        if (++times == 20) {
            times = 0;
            lastEstimationPath = new Array<Vector2>(currentEstimationPath);
            createWorld();
        }

        for (int i = 1; i <= 60; i++) {
            doGravity();
            playable.update(deltaTime);
            world.step(1 / 60f, 6, 2);

            if (i % 10 == 0)
                currentEstimationPath.add(playable.getBody().getPosition().cpy());
        }
        //createWorld();
    }

    public Array<Vector2> getEstimationPath() {
        return lastEstimationPath;
    }

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
}
