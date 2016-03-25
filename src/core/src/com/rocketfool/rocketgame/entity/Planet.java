package com.rocketfool.rocketgame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.screen.GameScreen;

/**
 * Created by pythech on 07/03/16.
 */
public class Planet extends Entity {
    //region Constants
    private static final float G = 6.67408e-11f;
    //endregion

    //region Fields
    private float mass;
    private float radius;
    //endregion

    //region Constructor
    public Planet(int x, int y, float mass, float radius) {
        this.mass = mass;
        this.radius = radius;
        this.body = createBody(x, y);
    }

    private Body createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        Body body = GameScreen.getInstance().getWorld().createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0.0f;

        body.createFixture(fixtureDef);

        circle.dispose();

        return body;
    }
    //endregion

    //region Methods
    @Override
    public void update(float dt) {
        Body spaceship = GameScreen.getInstance().getPlayer().getBody();

        // Get the directionVector by substracting the middle points of two objects
        Vector2 directionVector = this.body.getPosition().sub( spaceship.getPosition() );

        // uses F = G * M * m / r^2
        // We also use directionVector as it's also capable of calculating the distance
        // between the two objects.
        // Another interesting thing is LibGDX also provieds len2() method which is
        // basically len^2 so we can get r^2 this way with less code
        // it's also faster because normally distance calculation involves an Math.sqrt()
        // while len2() doesn't have to do so, so we don't have two Math.pow(Math.sqrt(distance), 2)
        // which is unnecessary work.
        float forceScalar = spaceship.getMass() * this.mass / directionVector.len2();

        // So now we have the value of the force and the direction
        // We have to get a vector with given direction and value
        Vector2 forceVector = directionVector.setLength(forceScalar);

        // apply this force to spaceship
        spaceship.applyForceToCenter(forceVector, true);

    }

    @Override
    public void draw(SpriteBatch batch) { }
    //endregion

    //region Getters & Setters
    public float getMass() {
        return mass;
    }

    public float getRadius() {
        return radius;
    }
    //endregion
}
