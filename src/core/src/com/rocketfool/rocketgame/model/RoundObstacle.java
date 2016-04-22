package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Class for all objects with physical properties and a circular shape. Eg. asteroids.
 */
public class RoundObstacle extends SolidObject {
    public RoundObstacle(float x, float y, int radius, int speed, float angle) {
        this.body = createBody(x, y, radius, speed, angle);
    }

    private Body createBody(float x, float y, int radius, int speed, float angle) {
       /* BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        //TODO: world isnt valid as variable
        Body body = world.createBody(bodyDef);

        //initial design for the round obstancle
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0.0f;

        body.createFixture(fixtureDef);

        circle.dispose();
        return body;
        */
        return null;
    }

    @Override
    public void update(float deltaTime) {

    }
}
