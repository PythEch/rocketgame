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
    private boolean isTriggeredBefore;

    public PositionTrigger(float x, float y, float radius, SolidObject target) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.target = target;
        isTriggeredBefore = false;
    }

    public final boolean isTriggered() {
        Vector2 pos = target.getBody().getPosition();

        if(pos.dst(x, y) <= radius) {
            isTriggeredBefore = true;
            return true;
        }
        else {
            return false;
        }

    }

    public final boolean isTriggeredBefore() {
        return isTriggeredBefore;
    }
}
