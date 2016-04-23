package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Class for all objects with physical properties and a rectangular shape. Eg. Unplayed satellites.
 */
public class RectangleObstacle extends SolidObject {
    public RectangleObstacle(float x, float y, float width, float height, Vector2 speed, World world) {
        this.body = createBody(x, y, width, height, world);
        body.setLinearVelocity(speed);
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

        body.createFixture(fixtureDef);

        rectangle.dispose();

        return body;
    }

    @Override
    public void update(float deltaTime) {

    }

    public void draw(ShapeRenderer shapeR) {
        shapeR.setColor(1, 1, 1, 1);
        shapeR.begin(ShapeRenderer.ShapeType.Line);

        for(int i=0, j=shapex.length-1; i<shapex.length; j=i++) {
            shapeR.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }
        shapeR.end();
    }
}
