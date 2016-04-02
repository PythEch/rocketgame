package com.rocketfool.rocketgame.entity.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.entity.SolidObject;

/**
 * Created by pythech on 07/03/16.
 */
public class Planet extends SolidObject {
    //region Constants
    private static final float G = 6.67408e-11f;
    //endregion

    //region Fields
    private float mass;
    private float radius;
    //endregion

    //region Constructor
    public Planet(int x, int y, float mass, float radius, World world) {
        this.mass = mass;
        this.radius = radius;
        this.body = createBody(x, y, mass, radius, world);
    }

    private Body createBody(float x, float y, float mass, float radius, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

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
    public void update(float dt) { }

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
