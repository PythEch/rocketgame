package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Omer on 29/04/2016.
 */
public class ForceDiagram extends GameObject {
    public static final float FRAME_RATE = 1f / 60;
    private World world;
    private Level level;
    private Array<Planet> planets;
    private Array<Vector2> vectors;
    private Playable playable;
    private Vector2 impulseVector;
    private Vector2 resultant;

    public ForceDiagram(Level level) {
        this.level = level;

        planets = new Array<Planet>();
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
        playable = new Playable(
                level.getPlayable().getBody().getPosition().x,
                level.getPlayable().getBody().getPosition().y,
                level.getPlayable().getWidth(),
                level.getPlayable().getHeight(),
                level.getPlayable().getBody().getMass() - level.getPlayable().getFuelLeft(),
                level.getPlayable().getDeltaAngularImpulse(),
                level.getPlayable().getDeltaImpulse(),
                level.getPlayable().getMaxImpulse(),
                level.getPlayable().getFuelLeft(),
                world
        );
    }

    @Override
    public void update(float deltaTime) {
        doGravity();
        doThrust(FRAME_RATE);
        doResultant(FRAME_RATE);

        playable.update(FRAME_RATE);
        world.step(FRAME_RATE, 8, 3);
    }

    private void doGravity() {
        for (int i = 0; i < planets.size; i++) {
            Vector2 vector = new Vector2();
            vectors.add(vector);
        }
        for (int i = 0; i < planets.size; i++) {
            Body spaceship = playable.getBody();

            // Get the directionVector by substracting the middle points of two objects
            Vector2 directionVector = planets.get(i).getBody().getPosition().sub(spaceship.getPosition());

            // uses F = G * M * m / r^2
            // We also use directionVector as it's also capable of calculating the distance
            // between the two objects.
            // Another interesting thing is LibGDX also provieds len2() method which is
            // basically len^2 so we can get r^2 this way with less code
            // it's also faster because normally distance calculation involves an Math.sqrt()
            // while len2() doesn't have to do so, so we don't have two Math.pow(Math.sqrt(distance), 2)
            // which is unnecessary work.
            float forceScalar = level.G * spaceship.getMass() * planets.get(i).getMass() / directionVector.len2(); //**

            // So now we have the value of the force and the direction
            // We have to get a vector with given direction and value
            Vector2 forceVector = directionVector.setLength(forceScalar);
            vectors.get(i).set(forceVector);
        }
    }

    private void doThrust(float deltaTime) {
        Vector2 impulseVector = new Vector2(0, playable.getCurrentImpulse() * deltaTime).rotateRad(playable.getBody().getAngle());
    }

    private void doResultant(float deltaTime) {
        Vector2 resultant = impulseVector;
        for (Vector2 vector: vectors) {
             resultant = resultant.add(vector);
        }
    }

}
