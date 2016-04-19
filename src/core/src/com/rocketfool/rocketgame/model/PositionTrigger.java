package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Triggers an event if the playable object enters a circular, predetermined region.
 */
public abstract class PositionTrigger implements Trigger {
    private float x;
    private float y;
    private float radius;
    private SolidObject target;

    public PositionTrigger(float x, float y, float radius, SolidObject target) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.target = target;
    }

    public final boolean isTriggered() {
        Vector2 pos = target.getBody().getPosition();

        return pos.dst(x, y) <= radius;
    }
}
