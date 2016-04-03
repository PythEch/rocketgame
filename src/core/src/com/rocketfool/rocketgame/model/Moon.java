package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by pythech on 07/03/16.
 */
public class Moon extends CelestialObject {
    //region Fields

    //endregion

    //region Constructor
    public Moon(int x, int y, float mass, float radius, World world) {
        super(mass, radius);
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
    //endregion

    //region Getters & Setters
    //endregion
}
