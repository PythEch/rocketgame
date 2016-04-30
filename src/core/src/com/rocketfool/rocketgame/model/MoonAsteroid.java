package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by pythech on 29/04/16.
 */
public class MoonAsteroid extends SolidObject {
    private Planet moon;
    private float distance;
    private float angle;
    private float radius;


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

    @Override
    public void update(float deltaTime) {
        angle -= 0.001f;
        Vector2 newPos = moon.getBody().getPosition().add(new Vector2(0, distance).rotateRad(angle));

        body.setTransform(newPos, angle);
        //body.setTransform(0,0,0);
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public float getRadius(){
        return radius;
    }
}
