package com.rocketfool.rocketgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by pythech on 07/03/16.
 */
public class Planet extends Movable {
    private float mass;
    private float radius;

    private Body body;

    public Planet(int x, int y, float mass, float radius) {
        super(x, y);
        this.mass = mass;
        this.radius = radius;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        body = RocketGame.getInstance().world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;

        body.createFixture(fixtureDef);

        circle.dispose();
    }

    public float getMass() {
        return mass;
    }

    public float getRadius() {
        return radius;
    }

    public float getGConstant() {
        return mass / radius * radius;
    }

    public float getGravitationalForce() {
        return mass * getGConstant();
    }

    public Body getBody() {
        return body;
    }
}
