package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.physics.box2d.Body;
import com.rocketfool.rocketgame.external.RocketGame;

/**
 * Any game object with physical properties, using Box2D.
 */
public abstract class SolidObject extends GameObject {
    //region Fields
    /**
     * A Box2D body which will be used to simulate the physics object in {@link #update(float)}
     */
    protected Body body;
    protected boolean orbitPreset = false;
    //endregion

    //region Getters & Setters
    public Body getBody() {
        return body;
    }

    public boolean isOrbitPreset() {return orbitPreset;}

    public void setOrbitPreset(boolean orbitPreset) {this.orbitPreset = orbitPreset;}
    //endregion
}
