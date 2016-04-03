package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class SolidObject extends GameObject {
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
