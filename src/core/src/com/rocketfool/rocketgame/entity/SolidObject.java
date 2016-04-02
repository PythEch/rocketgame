package com.rocketfool.rocketgame.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class SolidObject extends VisualObject {
    //region Fields
    /** A Box2D body which will be used to simulate the physics object in {@link #update(float)} */
    protected Body body;
    //endregion

    //region Getters & Setters
    public Body getBody() {
        return body;
    }
    //endregion
}
