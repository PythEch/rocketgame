package com.rocketfool.rocketgame.entity.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.entity.SolidObject;
import com.rocketfool.rocketgame.util.TextureManager;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by pythech on 03/03/16.
 */
public class Player extends Playable {
    public static float IMPULSE = 100;
    public static float ROTATE_IMPULSE = 75;

    //region Constructor
    public Player(float x, float y, World world) {
        currentImpulse = 0;
        impulse = IMPULSE;
        rotateImpulse = ROTATE_IMPULSE;


        texture = TextureManager.PLAYER_TEXTURE;
        // increase the quality of image
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        body = createBody(x, y, world);
    }

    private Body createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Gdx.graphics.getWidth() / 2f * toMeter, Gdx.graphics.getHeight() / 2f * toMeter);

        Body body = world.createBody(bodyDef);

        body.setTransform(0, 0, -90 * MathUtils.degreesToRadians);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(texture.getWidth() / 2f * toMeter, texture.getHeight() / 2f * toMeter);

        // Define properties of object here
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 1.0f; // TODO: Calculate a reasonable density
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        rectangle.dispose();

        return body;
    }
    //endregion


    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
