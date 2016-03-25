package com.rocketfool.rocketgame.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * A base entity class which represents common functionality of the game objects
 */
public abstract class Entity {
    public Entity() {}

    //region Fields
    /** A Box2D body which will be used to simulate the physics object in {@link #update(float)} */
    protected Body body;
    /** A texture to draw using {@link #draw(SpriteBatch)} method */
    protected Texture texture;
    //endregion

    //region Methods
    /**
     * Used to draw graphics of an entity
     * @param batch Used to draw objects, textures etc. on screen
     */
    public abstract void draw(SpriteBatch batch);

    /**
     * Used to calculate state, physics, etc. (anything other than drawing goes here)
     * @param dt Stands for DeltaTime which is the time passed between two sequential calls of update
     */
    public abstract void update(float dt);
    //endregion

    //region Getters & Setters
    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }
    //endregion
}
