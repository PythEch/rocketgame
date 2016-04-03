package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by pythech on 02/04/16.
 */
public class Obstacle extends SolidObject {
    public Obstacle(float x, float y, int width, int height, int speed, float angle) {
        this.body = createBody(x, y, width, height, speed, angle);
    }

    private Body createBody(float x, float y, int width, int height, int speed, float angle) {
        return null;
    }

    @Override
    public void update(float deltaTime) {

    }
}
