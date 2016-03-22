package com.rocketfool.rocketgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by pythech on 07/03/16.
 */
public class Planet extends GameObject {
    private float mass;
    private float radius;

    public Planet(int x, int y, float mass, float radius) {
        super(x, y);
        this.mass = mass;
        this.radius = radius;
        this.body = createBody(x, y);
    }

    private Body createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        Body body = RocketGame.getInstance().world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0.0f;

        body.createFixture(fixtureDef);

        circle.dispose();

        return body;
    }

    public float getMass() {
        return mass;
    }

    public float getRadius() {
        return radius;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void update(float dt) { }

    @Override
    public void draw(SpriteBatch batch) { }
}
