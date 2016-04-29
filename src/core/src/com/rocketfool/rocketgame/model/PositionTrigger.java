package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Triggers an event if the playable object enters a circular, predetermined region.
 */
public abstract class PositionTrigger implements Trigger {
    //region Fields
    private float x;
    private float y;
    private float radius;
    private SolidObject target;
    private float xOffset;
    private float yOffset;
    private boolean isTriggeredBefore;
    //endregion

    //region Constructors
    public PositionTrigger(float x, float y, float radius, SolidObject target) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.target = target;
        isTriggeredBefore = false;
    }

    public PositionTrigger(SolidObject target, float radius, float xOffset, float yOffset) {
        this.target = target;
        this.radius = radius;
        this.isTriggeredBefore = false;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        followTarget();
    }
    //endregion

    //region Methods

    public void followTarget(){
        if (target != null) {
            this.x = target.getBody().getPosition().x + xOffset;
            this.y = target.getBody().getPosition().y + yOffset;
        }
    }

    public final boolean isTriggered() {
        Vector2 pos = target.getBody().getPosition();

        if (pos.dst(x, y) <= radius) {
            isTriggeredBefore = true;
            return true;
        } else {
            return false;
        }

    }

    public final boolean isTriggeredBefore() {
        return isTriggeredBefore;
    }

    public float getX(){return  x; }

    public float getY() {return y; }

    public float getRadius() { return radius; }

    //endregion
}
