package com.rocketfool.rocketgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.rocketfool.rocketgame.utils.Constants.PPM;

/**
 * Created by pythech on 03/03/16.
 */
public class Player {
    private static final float IMPULSE = 45;
    private static final float ROTATE_IMPULSE = 35;

    private Body spaceship;
    private Texture image;

    public Player(Texture image) {
        // increase the quality of image
        image.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        this.image = image;

        // create physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Gdx.graphics.getWidth() / 2f / PPM, Gdx.graphics.getHeight() / 2f / PPM);

        spaceship = RocketGame.getInstance().world.createBody(bodyDef);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(image.getWidth() / 2f / PPM, image.getHeight() / 2f / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;

        Fixture fixture = spaceship.createFixture(fixtureDef);

        rectangle.dispose();
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            spaceship.applyAngularImpulse(ROTATE_IMPULSE * dt, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            spaceship.applyAngularImpulse(-ROTATE_IMPULSE * dt, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            float x = MathUtils.sin(-spaceship.getAngle()) * dt * IMPULSE;
            float y = MathUtils.cos(-spaceship.getAngle()) * dt * IMPULSE;
            spaceship.applyLinearImpulse(new Vector2(x, y), spaceship.getPosition(), false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            float x = MathUtils.sin(-spaceship.getAngle()) * dt * IMPULSE;
            float y = MathUtils.cos(-spaceship.getAngle()) * dt * IMPULSE;
            spaceship.applyLinearImpulse(new Vector2(x, -y), spaceship.getPosition(), false);
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                image,
                getCenterX(),
                getCenterY(),
                image.getWidth() / 2f,
                image.getHeight() / 2f,
                image.getWidth(),
                image.getHeight(),
                1,
                1,
                spaceship.getAngle() * MathUtils.radiansToDegrees,
                0,
                0,
                image.getWidth(),
                image.getHeight(),
                false,
                false
        );
    }

    /**
     * @return Returns the absolute pixel x-coordinate of the player's within the world.
     */
    public float getX() {
        return spaceship.getPosition().x * PPM;
    }

    /**
     * @return Returns the absolute pixel y-coordinate of the player's within the world.
     */
    public float getY() {
        return spaceship.getPosition().y * PPM;
    }

    /**
     * @return Returns the pixel x-coordinate of the player's center within the world.
     */
    public float getCenterX() {
        return getX() - image.getWidth() / 2f;
    }

    /**
     * @return Returns the pixel y-coordinate of the player's center within the world.
     */
    public float getCenterY() {
        return getY() - image.getHeight() / 2f;
    }
}
