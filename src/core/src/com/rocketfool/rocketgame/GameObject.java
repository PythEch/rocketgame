package com.rocketfool.rocketgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by pythech on 07/03/16.
 */
public abstract class GameObject {
    protected Body body;
    protected Texture texture;

    public GameObject(float x, float y, float z) { }

    public GameObject(float x, float y) {
        this(x, y, 0);
    }

    public GameObject() {
        this(0, 0, 0);
    }

    public abstract void draw(SpriteBatch batch);

    public abstract void update(float dt);

    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }
}
