package com.rocketfool.rocketgame.model;

/**
 * Parent class for all planets, moons, and stars.
 */
public abstract class CelestialObject extends SolidObject {
    protected float mass;
    protected float radius;

    public CelestialObject(float mass, float radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public float getMass() {
        return mass;
    }

    public float getRadius() {
        return radius;
    }
}
