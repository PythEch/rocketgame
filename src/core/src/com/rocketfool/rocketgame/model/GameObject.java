package com.rocketfool.rocketgame.model;

/**
 * A base entity class which represents common functionality of the game objects
 * All GameObjects have to be updatable per frame.
 */
public abstract class GameObject {
    //region Constructor
    public GameObject() {
    }
    //endregion

    //region Methods
    public abstract void update(float deltaTime);
    //endregion
}
