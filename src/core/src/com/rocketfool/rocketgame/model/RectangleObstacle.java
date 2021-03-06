package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Class for all objects with physical properties and a rectangular shape. Eg. Unplayed satellites.
 */
public class RectangleObstacle extends SolidObject {
    //region Fields
    private float width;
    private float height;
    //endregion

    //region Constructor
    public RectangleObstacle(float x, float y, float width, float height, Vector2 speed, World world) {
        this.body = createBody(x, y, width, height, world);
        this.width = width;
        this.height = height;
        body.setLinearVelocity(speed);
        this.width = width;
        this.height = height;
    }

    private Body createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData(Level.ObjectType.OBSTACLE);

        rectangle.dispose();

        return body;
    }
    //endregion

    //region Methods
    @Override
    public void update(float deltaTime) {
    }
    //endregion

    //region Getters & Setters
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    //endregion
}
