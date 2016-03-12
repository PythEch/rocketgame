package com.rocketfool.rocketgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.rocketfool.rocketgame.utils.Constants.PPM;

/**
 * Created by pythech on 03/03/16.
 */

public class Player {
    private static final float IMPULSE = 100;
    private static final float ROTATE_IMPULSE = 45;

    private int currentImpulse;

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
            currentImpulse += dt * IMPULSE;
            currentImpulse = Math.max(currentImpulse, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentImpulse -= dt * IMPULSE;
            currentImpulse = Math.max(currentImpulse, 0);
        }

        move(dt);
    }

    private void move(float dt) {
        float angle = spaceship.getAngle();

        Vector2 bottomVector = new Vector2(0, -image.getHeight() / 2f / PPM).rotateRad(angle);
        Vector2 bottomPosition = bottomVector.add(spaceship.getPosition());

        Vector2 impulseVector = new Vector2(0 ,dt * currentImpulse).rotateRad(spaceship.getAngle());

        if (RocketGame.DEBUG)
            RocketGame.getInstance().drawDebugBox(bottomPosition.x, bottomPosition.y, 4 / PPM, 4 / PPM);

        spaceship.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, false);
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

    public Body getSpaceship() {
        return spaceship;
    }
}
