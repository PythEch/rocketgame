package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Represents the ortbiting toxic asteroid around the moon on level 2
 */
public class MoonAsteroid extends SolidObject {
    //region Constants
    public static final float DELTA_ANGLE = -0.001f;
    //endregion

    //region Fields
    private Planet moon;
    private float distance;
    private float angle;
    private float radius;
    //endregion

    //region Constructor
    public MoonAsteroid(Planet moon, float distance, float radius, World world) {
        this.moon = moon;
        this.distance = distance;
        this.angle = 0;
        this.body = createBody(moon, radius, world);
    }

    private Body createBody(Planet moon, float radius, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(moon.getBody().getPosition().x, moon.getBody().getPosition().y);

        Body body = world.createBody(bodyDef);
        this.radius = radius;
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 10000f;

        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData(Level.ObjectType.PLANET);

        circle.dispose();

        return body;
    }
    //endregion

    //region Methods

    /**
     * Rotates the asteroid around the moon
     */
    @Override
    public void update(float deltaTime) {
        angle += DELTA_ANGLE;
        Vector2 newPos = moon.getBody().getPosition().add(new Vector2(0, distance).rotateRad(angle));

        body.setTransform(newPos, angle);
    }
    //endregion

    //region Getters & Setters
    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getRadius() {
        return radius;
    }

    public Planet getMoon() {
        return moon;
    }

    public float getDistance() {
        return distance;
    }

    public float getAngle() {
        return angle;
    }
    //endregion
}
