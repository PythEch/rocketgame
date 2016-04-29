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
    /** When the target reaches the trigger, it is activated.*/
    private SolidObject target;
    /** Position triggers can be made to follow host objects.*/
    private SolidObject host;
    private float xOffset;
    private float yOffset;
    private boolean isTriggeredBefore;
    private int trigCount;
    //endregion

    //region Constructors
    public PositionTrigger(float x, float y, float radius, SolidObject target) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.target = target;
        isTriggeredBefore = false;
        this.host = null;
        trigCount = 0;
    }

    public PositionTrigger(SolidObject host, float xOffset, float yOffset, float radius, SolidObject target) {
        this.host = host;
        this.radius = radius;
        this.isTriggeredBefore = false;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.target = target;
        trigCount = 0;
        followHost();
    }
    //endregion

    //region Methods

    public void followHost(){
        if (host != null) {
            this.x = host.getBody().getPosition().x + xOffset;
            this.y = host.getBody().getPosition().y + yOffset;
        }
    }

    public final boolean isTriggered() {
        Vector2 pos = target.getBody().getPosition();

        if (pos.dst(x, y) <= radius) {
            isTriggeredBefore = true;
            trigCount++;
            return true;
        } else {
            return false;
        }

    }

    public final boolean isTriggeredBefore() {return isTriggeredBefore;}

    public float getX(){return  x;}

    public float getY() {return y;}

    public float getRadius() {return radius;}

    public int getTrigCount() {return trigCount;}

    //endregion
}
