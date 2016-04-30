package com.rocketfool.rocketgame.model;

/**
 * Parent class for all planets, moons, and stars.
 */
public abstract class CelestialObject extends SolidObject {
    //region Fields
    protected float mass;
    protected float radius;
    //endregion

    //region Constructor
    public CelestialObject(float mass, float radius) {
        this.mass = mass;
        this.radius = radius;
    }
    //endregion

    //region Getters & Setters
    public float getMass() {
        return mass;
    }

    public float getRadius() {
        return radius;
    }
    //endregion
}
