package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Class for all objects with physical properties and a circular shape. Eg. asteroids.
 */
public class RoundObstacle extends SolidObject {
    public RoundObstacle(float x, float y, int radius, Vector2 speed, World world) {
        this.body = createBody(x, y, radius, world);
        body.setLinearVelocity(speed);
    }

    private Body createBody(float x, float y, int radius, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData(Level.ObjectType.OBSTACLE);

        circle.dispose();

        return body;
    }

    @Override
    public void update(float deltaTime) {

    }
}
