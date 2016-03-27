package com.rocketfool.rocketgame.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.screen.GameScreen;
import com.rocketfool.rocketgame.util.TextureManager;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by pythech on 03/03/16.
 */
public class Player extends Entity {
    //region Constants
    private static final float IMPULSE = 65;
    private static final float ROTATE_IMPULSE = 100;
    //endregion

    //region Fields
    private float currentImpulse;
    //endregion

    //region Constructor
    public Player(float x, float y) {
        texture = TextureManager.PLAYER_TEXTURE;
        // increase the quality of image
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        body = createBody(x, y);
    }

    private Body createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Gdx.graphics.getWidth() / 2f * toMeter, Gdx.graphics.getHeight() / 2f * toMeter);

        Body body = GameScreen.getInstance().getWorld().createBody(bodyDef);

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

    //region Methods
    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.applyAngularImpulse(ROTATE_IMPULSE * dt, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.applyAngularImpulse(-ROTATE_IMPULSE * dt, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currentImpulse += dt * IMPULSE;
            currentImpulse = Math.max(currentImpulse, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentImpulse -= dt * IMPULSE;
            currentImpulse = Math.max(currentImpulse, 0);
        }

        if (DEBUG) {
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                currentImpulse = 0;
                body.setAngularVelocity(0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                body.setLinearVelocity(0, 0);
            }
        }

        move(dt);
    }

    private void move(float dt) {
        float angle = body.getAngle();

        Vector2 bottomVector = new Vector2(0, -texture.getHeight() / 2f * toMeter).rotateRad(angle);
        Vector2 bottomPosition = bottomVector.add(body.getPosition());

        Vector2 impulseVector = new Vector2(0, dt * currentImpulse).rotateRad(body.getAngle());

        if (DEBUG)
            //GameScreen.getInstance().drawDebugBox(bottomPosition.x, bottomPosition.y, 4 * toMeter, 4 * toMeter);

        body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, false);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                texture,
                this.getBody().getPosition().x * toPixel - texture.getWidth() / 2f,
                this.getBody().getPosition().y * toPixel - texture.getHeight() / 2f,
                texture.getWidth() / 2f,
                texture.getHeight() / 2f,
                texture.getWidth(),
                texture.getHeight(),
                1,
                1,
                body.getAngle() * MathUtils.radiansToDegrees,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false
        );
    }
    //endregion

    //region Getters & Setters
    public float getCurrentImpulse() {
        return currentImpulse;
    }
    //endregion
}
