package com.rocketfool.rocketgame.model.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.view.AssetManager;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by pythech on 03/03/16.
 */
public class Player extends Playable {
    private static final float IMPULSE = 100;
    private static final float ROTATE_IMPULSE = 75;

    private static final float WIDTH = 112;
    private static final float HEIGHT = 75;

    //region Constructor
    public Player(float x, float y, World world) {
        super(WIDTH, HEIGHT);
        impulse = IMPULSE;
        rotateImpulse = ROTATE_IMPULSE;

        createBody(x, y, world);
    }

    private void createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Gdx.graphics.getWidth() / 2f * toMeter, Gdx.graphics.getHeight() / 2f * toMeter);

        Body body = world.createBody(bodyDef);

        body.setTransform(0, 0, -90 * MathUtils.degreesToRadians);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(width / 2f * toMeter, height / 2f * toMeter);

        // Define properties of object here
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 1.0f; // TODO: Calculate a reasonable density
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        rectangle.dispose();

        this.body = body;
    }
    //endregion


    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
