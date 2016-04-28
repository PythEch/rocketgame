package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.physics.box2d.*;

/**
 * A Planet which has fixed position with a huge mass
 * TODO: Add motion
 */
public class Planet extends CelestialObject {
    //region Fields
    private Star star;
    boolean circles;
    //endregion

    //region Constructor
    public Planet(float x, float y, float mass, float radius, Star star, World world) {
        super(mass, radius);
        this.star = star;
        this.body = createBody(x, y, mass, radius, world);
        circles = false;
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
        fixtureDef.friction = 10000f;

        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData(Level.ObjectType.PLANET);

        circle.dispose();

        return body;
    }
    //endregion

    //region Methods
    @Override
    public void update(float dt) {
    }
    //endregion

    public void setCircles(boolean circles) {
        this.circles = circles;
    }

    public boolean getCircles(){ return this.circles; }
}
