package com.rocketfool.rocketgame.model.entity;

/**
 * A base entity class which represents common functionality of the game objects
 */
public abstract class GameObject {
    public GameObject() { }

    public abstract void update(float deltaTime);
}
