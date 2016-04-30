package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Triggers an event if the playable object enters a circular, predetermined region.
 */
public class PositionTrigger extends Trigger {
    //region Fields
    private float x;
    private float y;
    private float radius;
    /**
     * When the target reaches the trigger, it is activated.
     */
    private SolidObject target;
    /**
     * Position triggers can be made to move in unison with designated host objects.
     */
    private SolidObject host;
    private boolean reverse;
    //endregion

    //region Constructors
    public PositionTrigger(boolean runOnce, SolidObject host, float x, float y, float radius, SolidObject target, boolean reverse) {
        super(runOnce);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.target = target;
        this.host = host;
        this.reverse = reverse;
    }

    public PositionTrigger(boolean runOnce, float x, float y, float radius, SolidObject target, boolean reverse) {
        this(runOnce, null, x, y, radius, target, reverse);
    }

    public PositionTrigger(boolean runOnce, SolidObject host, float x, float y, float radius, SolidObject target) {
        this(runOnce, host, x, y, radius, target, false);
    }

    public PositionTrigger(boolean runOnce, float x, float y, float radius, SolidObject target) {
        this(runOnce, null, x, y, radius, target, false);
    }

    public PositionTrigger(SolidObject host, float x, float y, float radius, SolidObject target, boolean reverse) {
        this(true, host, x, y, radius, target, reverse);
    }

    public PositionTrigger(float x, float y, float radius, SolidObject target, boolean reverse) {
        this(true, null, x, y, radius, target, reverse);
    }

    public PositionTrigger(SolidObject host, float x, float y, float radius, SolidObject target) {
        this(true, host, x, y, radius, target, false);
    }

    public PositionTrigger(float x, float y, float radius, SolidObject target) {
        this(true, null, x, y, radius, target, false);
    }

    //endregion

    //region Methods
    @Override
    protected boolean isTriggeredInternal() {
        Vector2 pos = getPosition();

        // ^ means XOR operator
        //
        // Logic table of XOR
        //  ________________
        // | A | B | Output |
        // | 1 | 1 | 0      |
        // | 1 | 0 | 1      |
        // | 0 | 1 | 1      |
        // | 0 | 0 | 0      |
        //  ----------------
        //
        // Which means it produces true only when inputs differ
        // So this effectively,
        // when reverse is false, produces true when in its inside the circle
        // when reverse is true, produces true when outside the circle

        return pos.dst(target.getBody().getPosition()) <= radius ^ reverse;
    }

    @Override
    public void triggerPerformed() {

    }

    public Vector2 getPosition() {
        if (host == null) {
            return new Vector2(x, y);
        }
        return new Vector2(x + host.getBody().getPosition().x, y + host.getBody().getPosition().y);
    }

    public float getRadius() {
        return radius;
    }

    //endregion
}
