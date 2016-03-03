package com.rocketfool.rocketgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;

/**
 * Created by pythech on 03/03/16.
 */
public class Player {
    private static final int ACCELERATION = 3;
    private static final int ROTATE_DEGREE = 2;

    private Polygon spaceship;
    private Texture image;

    private float velocity_x = 0;
    private float velocity_y = 0;

    public Player(Texture image) {
        // increase the quality of image
        image.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        this.image = image;

        // Parameters for Polygon is a bit weird
        // Parameters:
        //     vertices - an array where every even element represents the horizontal part of a point,
        //     and the following element representing the vertical part

        // (0,H)                (W,H)
        //    .------------------.
        //    |                  |
        //    |                  |
        //    .------------------.
        // (0,0)                (W,0)
        spaceship = new Polygon(new float[] {
                0,
                0,
                0,
                image.getWidth(),
                image.getWidth(),
                image.getHeight(),
                0,
                image.getHeight()
        });

        spaceship.setOrigin(image.getWidth() / 2.0f, image.getHeight() / 2.0f);
    }

    public void move() {
        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity_x += MathUtils.sinDeg(-spaceship.getRotation()) * ACCELERATION * dt;
            velocity_y += MathUtils.cosDeg(spaceship.getRotation()) * ACCELERATION * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity_x -= MathUtils.sinDeg(-spaceship.getRotation()) * ACCELERATION * dt;
            velocity_y -= MathUtils.cosDeg(spaceship.getRotation()) * ACCELERATION * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            spaceship.rotate(ROTATE_DEGREE);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            spaceship.rotate(-ROTATE_DEGREE);
        }

        spaceship.translate(velocity_x, velocity_y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                image,
                spaceship.getX(),
                spaceship.getY(),
                spaceship.getOriginX(),
                spaceship.getOriginY(),
                image.getWidth(),
                image.getHeight(),
                1,
                1,
                spaceship.getRotation(),
                0,
                0,
                image.getWidth(),
                image.getHeight(),
                false,
                false
        );
    }
}
