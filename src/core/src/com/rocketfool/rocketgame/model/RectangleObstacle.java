package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Class for all objects with physical properties and a rectangular shape. Eg. Unplayed satellites.
 */
public class RectangleObstacle extends SolidObject {
    public RectangleObstacle(float x, float y, int width, int height, int speed, float angle) {
        this.body = createBody(x, y, width, height, speed, angle);
    }

    private Body createBody(float x, float y, int width, int height, int speed, float angle) {
        return null;
    }

    @Override
    public void update(float deltaTime) {

    }
}
