package com.rocketfool.rocketgame.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class VisualObject extends GameObject {
    /** A texture to draw using {@link #draw(SpriteBatch)} method */
    protected Texture texture;

    @Override
    public void update(float deltaTime) {

    }

    public Texture getTexture() {
        return texture;
    }
}
