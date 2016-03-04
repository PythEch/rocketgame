package com.rocketfool.rocketgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.utils.Math;

import static com.rocketfool.rocketgame.utils.Constants.PPM;

/**
 * Created by pythech on 03/03/16.
 */
public class Player {
    private static final float IMPULSE = 45;
    private static final float ROTATE_IMPULSE = 45;

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

        spaceship.setTransform(0, 0, -90 * MathUtils.degreesToRadians);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(image.getWidth() / 2f / PPM, image.getHeight() / 2f / PPM);

        // Define properties of object here
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 1.0f; // TODO: Calculate a reasonable density
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
            float angle = -spaceship.getAngle();

            float horizontalImpulse = Math.offsetX(angle, IMPULSE * dt);
            float verticalImpulse = Math.offsetY(angle, IMPULSE * dt);

            float bottomCenterX = spaceship.getPosition().x - Math.offsetX(angle, image.getHeight() / 2f / PPM);
            float bottomCenterY = spaceship.getPosition().y - Math.offsetY(angle, image.getHeight() / 2f / PPM);

            if (RocketGame.DEBUG)
                RocketGame.getInstance().drawDebugBox(bottomCenterX, bottomCenterY, 4 / PPM, 4 / PPM);

            spaceship.applyLinearImpulse(horizontalImpulse, verticalImpulse, bottomCenterX, bottomCenterY, false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // Do nothing for now
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                image,
                getX() - image.getWidth() / 2f,
                getY() - image.getHeight() / 2f,
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
     * @return Returns the pixel x-coordinate of the player's center within the world.
     */
    public float getX() {
        return spaceship.getPosition().x * PPM;
    }

    /**
     * @return Returns the pixel y-coordinate of the player's center within the world.
     */
    public float getY() {
        return spaceship.getPosition().y * PPM;
    }
}
