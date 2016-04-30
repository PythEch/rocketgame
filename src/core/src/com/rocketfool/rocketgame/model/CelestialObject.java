package com.rocketfool.rocketgame.model;

/**
 * Parent class for all planets, moons, and stars.
 */
public abstract class CelestialObject extends SolidObject {
    //region Fields
    protected float mass;
    protected float radius;
    protected boolean orbitPreset = false;
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

    public boolean isOrbitPreset() {return orbitPreset;}

    public void setOrbitPreset(boolean orbitPreset) {this.orbitPreset = orbitPreset;}
    //endregion
}
